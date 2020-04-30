package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.UpkeepRecordDao;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.system.domain.UserDO;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@EnableTransactionManagement
@Service
public class UpkeepRecordServiceImpl implements UpkeepRecordService {
	@Autowired
	private UpkeepRecordDao upkeepRecordDao;
	@Autowired
	private UserService userService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UpkeepProjectService upkeepProjectService;
	@Autowired
	private UpkeepPlanProjectService upkeepPlanProjectService;
	@Autowired
	private SparePartService sparePartService;
	@Autowired
	private UpkeepPlanPartService upkeepPlanPartService;
	@Autowired
	private UpkeepPlanService upkeepPlanSrvice;
	@Autowired
	private UpkeepRecordPartService upkeepRecordPartService;
	@Autowired
	private UpkeepRecordProjectService upkeepRecordProjectService;
	@Autowired
	private UpkeepCheckService  upkeepCheckService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;




	@Override
	public UpkeepRecordDO get(Long id){
		return upkeepRecordDao.get(id);
	}
	
	@Override
	public List<UpkeepRecordDO> list(Map<String, Object> map){
		return upkeepRecordDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return upkeepRecordDao.count(map);
	}
	
	@Override
	public int save(UpkeepRecordDO upkeepRecord){
		return upkeepRecordDao.save(upkeepRecord);
	}
	
	@Override
	public int update(UpkeepRecordDO upkeepRecord){
		return upkeepRecordDao.update(upkeepRecord);
	}
	
	@Override
	public int remove(Long id){
		return upkeepRecordDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return upkeepRecordDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> recordListForDevice(int pageno, int pagesize, Long deviceId) {
		Map<String,Object> userMap = this.userService.getUserMap();
		Map<String,Object> deviceMap = this.deviceService.getDeviceMap();
		Map<String,Object> results = Maps.newHashMap();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceId",deviceId);
		List<UpkeepRecordDO> records = upkeepRecordDao.list(params);
		Long total = (long)upkeepRecordDao.count(params);
		List<Map<String,Object>> datas = new ArrayList<>();
		if(records!=null && records.size()>0){
			for (UpkeepRecordDO record:records){
				Map map = new HashMap();
				map.put("id", record.getId());
				map.put("workOrderno", record.getWorkOrderno() == null ? "" : record.getWorkOrderno());
				map.put("name", record.getName() == null ? "" : record.getName());
				map.put("deviceName", record.getDeviceId() == null ? "" : deviceMap.get(record.getDeviceId().toString()));
				map.put("engineer", record.getEngineerId() == null ? "" : userMap.get(record.getEngineerId().toString()));
				map.put("startTime", record.getStartTime() == null ? "" : record.getStartTime());
				map.put("endTime", record.getEndTime() == null ? "" : record.getEndTime());
				map.put("gmtTime", record.getCreateTime() == null ? "" : record.getCreateTime());
				map.put("status", record.getStatus() == null ? "" : record.getStatus());
				datas.add(map);
			}
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(datas);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total.intValue());
			dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
			results.put("data",dsRet);
		}
		return results;
	}

	@Override
	public Map<String, Object> getRecordDetail(Long id) {
		return null;
	}

	@Override
	public UpkeepRecordDO getByPlanId(Long id) {
		UpkeepRecordDO record = this.upkeepRecordDao.getByPlanId(id);
		return record;
	}

	@Override
	public Map<String, Object> saveRecord(UpkeepRecordDO record, String projectIds, String partIdArray) {
		Map<String,Object> results = Maps.newHashMap();
		record.setWorkOrderno(DateFormatUtil.getWorkOrderno());
		int code = upkeepRecordDao.save(record);
		if(code>0){
			//关联项目
			if(projectIds!=null && !projectIds.equals("")){
				String[] projectArray = projectIds.split(",");
				for(int i = 0;i < projectArray.length;i++){
					Long  projectId = Long.valueOf(projectArray[i]);
					UpkeepProjectDO project = this.upkeepProjectService.get(projectId);
					UpkeepPlanProjectDO planProject = new UpkeepPlanProjectDO();
					planProject.setPlanId(record.getId());
					planProject.setProjectId(project.getId());
					this.upkeepPlanProjectService.save(planProject);
				}
			}
			//关联备品备件
			JSONArray jsonArray = new JSONArray(partIdArray);
			for(int j = 0; j < jsonArray.length(); j ++) {
				JSONObject jsonObject = jsonArray.getJSONObject(j);
				Long partId = jsonObject.getLong("partId");
				int amount = jsonObject.getInt("amount");
				SparePartDO sparePart = this.sparePartService.get(partId);
				UpkeepPlanPartDO planPart = new UpkeepPlanPartDO();
				planPart.setAmount(amount);
				planPart.setPartId(sparePart.getId());
				planPart.setPlanId(record.getId());
				this.upkeepPlanPartService.save(planPart);
			}
		}
		return results;
	}

	@Override
	public Map<String, Object> recordListForUser(int pageno, int pagesize, Long userId) {
		Map<String,Object> userMap = this.userService.getUserMap();
		Map<String,Object> deviceMap = this.deviceService.getDeviceMap();
		Map<String,Object> results = Maps.newHashMap();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId",userId);
		List<UpkeepRecordDO> records = upkeepRecordDao.list(params);
		Long total = (long)upkeepRecordDao.count(params);
		List<Map<String,Object>> datas = new ArrayList<>();
		if(records!=null && records.size()>0){
			for (UpkeepRecordDO record:records){
				Map map = new HashMap();
				map.put("id", record.getId());
				map.put("workOrderno", record.getWorkOrderno() == null ? "" : record.getWorkOrderno());
				map.put("name", record.getName() == null ? "" : record.getName());
				map.put("deviceName", record.getDeviceId() == null ? "" : deviceMap.get(record.getDeviceId().toString()));
				map.put("engineer", record.getEngineerId() == null ? "" : userMap.get(record.getEngineerId().toString()));
				map.put("startTime", record.getStartTime() == null ? "" : record.getStartTime());
				map.put("endTime", record.getEndTime() == null ? "" : record.getEndTime());
				map.put("gmtTime", record.getCreateTime() == null ? "" : record.getCreateTime());
				map.put("status", record.getStatus() == null ? "" : record.getStatus());
				datas.add(map);
			}
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(datas);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total.intValue());
			dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
			results.put("data",dsRet);
		}
		return results;
	}

	@Override
	public Map<String, Object> handleRecord(Long id, String content, String projectArray, String partArray) {
		return null;
	}

	@Override
	public Map<String, Object> checkRecord(HttpServletRequest request, UpkeepCheckDO check, Long id) {
		String userToken = request.getHeader("Authorization");
		///根据key获取缓存中的用户名
		//String username = redisTemplate.opsForValue().get(Constant.REDIS_USER_TOKEN_PREFIX+userToken);
		UserDO userDO = ShiroUtils.getUser();
		Map<String,Object> results = Maps.newHashMap();
		//创建验收记录
		check.setCheckTime(new Date());
		check.setUserId(userDO.getUserId());
		check.setRecordId(id);
		this.upkeepCheckService.save(check);
		//更新保养记录和保养计划状态
		UpkeepRecordDO record = this.upkeepRecordDao.get(id);
		record.setStatus(3L);
		record.setEndTime(new Date());
		this.upkeepRecordDao.update(record);
		UpkeepPlanDO plan = upkeepPlanSrvice.get(record.getPlanId());
		plan.setEndTime(new Date());
		plan.setStatus(2L);
		this.upkeepPlanSrvice.update(plan);
		return results;
	}

	@Override
	public List<Map<String, Object>> listRecords(Map<String, Object> map) {
		return upkeepRecordDao.listRecords(map);
	}

	@Override
	public List<Map<String, Object>> newOfListRecords(Map<String, Object> map) {
		return upkeepRecordDao.newOfListRecords(map);
	}

	@Override
	public List<Map<String, Object>> oneRecordDetail(Map<String, Object> map) {
		return upkeepRecordDao.oneRecordDetail(map);
	}

	@Override
	public List<Map<String, Object>> getMsgByDeviceId(Map<String, Object> map) {
		return upkeepRecordDao.getMsgByDeviceId(map);
	}

	@Override
	public Map<String, Object> countListRecords(Map<String, Object> map) {
		return upkeepRecordDao.countListRecords(map);
	}

	@Override
	public int countOfWaitingDo(Map<String, Object> map) {
		return upkeepRecordDao.countOfWaitingDo(map);
	}


	@Override
	public R closePaseOfRecord(Long[] recordIds,String closeResen) {
		Map<String,Object> result = Maps.newHashMap();
		Map<String,Object> query = Maps.newHashMap();
		query.put("ids",recordIds);
		query.put("result", ConstantForDevice.WAITING_DEAL);//待处理
		List<Map<String, Object>> mapList = upkeepRecordDao.idsOfCanChange(query);
		if(mapList.size()>0){
			Long updateIds[]=new Long[mapList.size()];

			for(int i =0;i<mapList.size();i++){
				Map<String,Object> map =mapList.get(i);
				updateIds[i]=Long.parseLong(map.get("id").toString());
				UpkeepRecordDO   recordDO= new UpkeepRecordDO();

				recordDO.setId(Long.parseLong(map.get("id").toString()));
				recordDO.setResult(ConstantForDevice.CLOSE);
				recordDO.setClosureReason(closeResen);
				upkeepRecordDao.update(recordDO);
			}
			result.put("seccessUpdate",updateIds);
			return R.ok(result);
		}
		//"所传id其数据不允许关闭！请检查状态！"
		return R.error(messageSourceHandler.getMessage("apis.upkeep.noAllowClose",null));
	}

	@Override
	public R getOneDetailOfRecord(Map<String,Object> maps){
		Map<String, Object> query = Maps.newHashMap();
		Map<String, Object> results = Maps.newHashMap();

		UpkeepRecordDO recordDO = upkeepRecordDao.get(Long.parseLong(maps.get("id").toString()));
		List<Map<String, Object>> oneRecordDetailReco ;
		if(recordDO!=null){
			if(Objects.nonNull(recordDO.getPlanId())){
				oneRecordDetailReco = this.oneRecordDetail(maps);
			}else{
				oneRecordDetailReco = this.oneRecordDetaiOfNoPlan(maps);
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}

		List<Map<String, Object>> oneRecordDetailPro = upkeepRecordProjectService.projectDetailByRecordID(maps);
		List<Map<String, Object>> oneRecordDetailspart = upkeepRecordPartService.spartByRecordIdAgain(maps);

		if(oneRecordDetailReco.size() > 0 ){
			query.remove("id");
			query.put("record", oneRecordDetailReco);
			query.put("project", oneRecordDetailPro);
			query.put("part", oneRecordDetailspart);
			results.put("data", query);
			return R.ok(results);
		} else {
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}

	}


	@Override
	public R doingRecorder(Long recordId, Date dateNow) {
		Map<String, Object> query = Maps.newHashMap();
		query.put("id", recordId);
		//判断工单时间是否已经过期，待评价状态下也可执行
		UpkeepRecordDO recordDO = upkeepRecordDao.get(recordId);

		if (recordDO != null) {

			if (Objects.equals(ConstantForDevice.WAITING_DEAL, recordDO.getResult()) && (dateNow.before(recordDO.getEndTime()))) {
				//返回信息，+将状态给为待评价
				UpkeepRecordDO recordDoChagne = new UpkeepRecordDO();
				recordDoChagne.setResult(ConstantForDevice.WAITING_CHECK);
				recordDoChagne.setId(recordId);
				upkeepRecordDao.update(recordDoChagne);
				R rr = this.getOneDetailOfRecord(query);
				return rr;

			} else if (Objects.equals(ConstantForDevice.WAITING_CHECK, recordDO.getResult())) {//待评价 状态下也可执行
				//返回信息  (不用更新状态)
				R r = this.getOneDetailOfRecord(query);
				return r;
			}
			//"此单已验收/已过期/暂存状态，不允许执行！"
			return R.error(messageSourceHandler.getMessage("apis.upkeep.noAllowExecute",null));
		}
		return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
	}


	@Override
	public R saveRecorderOfNoPlan(UpkeepRecordDO upkeepRecordDO, String proList, String partList, int sign) {
		Map<String, Object> result = Maps.newHashMap();
		if (sign == 0) {

			String prefix = DateFormatUtil.getWorkOrderno(ConstantForDevice.SBBY, new Date());
			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
			params.put("maxNo", prefix);
			params.put("offset", 0);
			params.put("limit", 1);
			List<UpkeepRecordDO> list = upkeepRecordDao.list(params);
			String suffix = null;
			if (list.size() > 0) {
				suffix = list.get(0).getWorkOrderno();
			}
			upkeepRecordDO.setWorkOrderno(DateFormatUtil.getWorkOrderno(prefix, suffix));


			upkeepRecordDao.save(upkeepRecordDO);

			if (!Objects.isNull(proList)) {
				List<UpkeepRecordProjectDO> inbodyNewDOS = JSON.parseArray(proList, UpkeepRecordProjectDO.class);

				for (UpkeepRecordProjectDO proDo : inbodyNewDOS) {

					proDo.setRecordId(upkeepRecordDO.getId());

					upkeepRecordProjectService.save(proDo);
				}
			}

			if (!"".equals(partList)) {
				//[{"partId":3,"spartAmount":5,"spartSum":1000},{"partId":3,"spartAmount":5,"spartSum":1000}]                                                spart_sum
//[{"partId":3,"spartAmount":5,"spartUnit":"个","spartPrice":20,"spartSum":1000,"remark":"备注"},{"partId":3,"spartAmount":5,"spartUnit":1,"spartPrice":20,"spartSum":1000,"remark":"备注"}]

				List<UpkeepRecordPartDO> inbodyNewDOS = JSON.parseArray(partList, UpkeepRecordPartDO.class);

				for (UpkeepRecordPartDO partDO : inbodyNewDOS) {

					partDO.setRecordId(upkeepRecordDO.getId());

//					partDO.setSpartSum(0.0);

					upkeepRecordPartService.save(partDO);
				}
			}
			//将工单id保存至check验收表中，状态result 初始为133
			UpkeepCheckDO upkeepCheckDO = new UpkeepCheckDO();
			upkeepCheckDO.setResult(ConstantForDevice.NO_EVALUATED);
			upkeepCheckDO.setRecordId(upkeepRecordDO.getId());

			upkeepCheckService.save(upkeepCheckDO);

			result.put("recordId",upkeepRecordDO.getId());
			return R.ok(result);

		} else {
			Long recordID=upkeepRecordDO.getId();
			//修改
			upkeepRecordDao.update(upkeepRecordDO);

			if (!Objects.isNull(proList)) {
				List<UpkeepRecordProjectDO> inbodyNewDOS = JSON.parseArray(proList, UpkeepRecordProjectDO.class);

				upkeepRecordProjectService.removeByRecordId(recordID);

				for (UpkeepRecordProjectDO proDo : inbodyNewDOS) {

					proDo.setRecordId(recordID);

					upkeepRecordProjectService.save(proDo);
				}
			}
			if (!"".equals(partList)) {
				List<UpkeepRecordPartDO> inbodyNewDOS = JSON.parseArray(partList, UpkeepRecordPartDO.class);

				upkeepRecordPartService.removeByRecordId(recordID);

				for (UpkeepRecordPartDO partDO : inbodyNewDOS) {
					partDO.setRecordId(recordID);
//					partDO.setSpartSum(0.0);
					upkeepRecordPartService.save(partDO);
				}
			}
			return R.ok();
		}
	}

	@Override
	public List<Map<String, Object>> oneRecordDetaiOfNoPlan(Map<String, Object> map) {
		return upkeepRecordDao.oneRecordDetaiOfNoPlan(map);
	}

	@Override
	public int countOfMsgByDeviceId(Map<String, Object> map) {
		return upkeepRecordDao.countOfMsgByDeviceId(map);
	}

	@Override
	public R deletOfRecords(Long[] recordIds) {

		Map<String, Object> query = Maps.newHashMap();
			query.put("ids",recordIds);
			query.put("result", Constant.TS);
			//验证所传id是否都是出于暂存状态
		List<Map<String, Object>> mapList = upkeepRecordDao.idsOfCanDelet(query);
		if(mapList.size()==recordIds.length){

			 upkeepRecordDao.batchRemove(recordIds);
				upkeepRecordProjectService.batchRemoveByRecordId(recordIds);
				upkeepRecordPartService.batchRemoveByRecordId(recordIds);
				return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
		}
	}

	@Override
	public List<Map<String, Object>> upkeepDeatailOfBoard(Map<String, Object> map) {
		return upkeepRecordDao.upkeepDeatailOfBoard(map);
	}

	@Override
	public int countOfupkeepOfBoard(Map<String, Object> map) {
		return upkeepRecordDao.countOfupkeepOfBoard(map);
	}


}
