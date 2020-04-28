package com.ev.custom.service.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.PatrolRecordDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.DeviceDO;
import com.ev.custom.domain.PatrolCheckDO;
import com.ev.custom.domain.PatrolDetailDO;
import com.ev.custom.domain.PatrolPlanDO;
import com.ev.custom.domain.PatrolProjectDO;
import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.DeviceService;
import com.ev.custom.service.PatrolCheckService;
import com.ev.custom.service.PatrolDetailService;
import com.ev.custom.service.PatrolPlanDetailService;
import com.ev.custom.service.PatrolPlanService;
import com.ev.custom.service.PatrolProjectService;
import com.ev.custom.service.PatrolRecordService;
import com.ev.system.service.DeptService;
import com.google.common.collect.Maps;



@Service
public class PatrolRecordServiceImpl implements PatrolRecordService {
	@Autowired
	private PatrolRecordDao patrolRecordDao;
	@Autowired
	private PatrolDetailService patrolDetailService;
	@Autowired
	private PatrolCheckService patrolCheckService;
	@Autowired
	private PatrolPlanService patrolPlanService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private PatrolProjectService patrolProjectService;
	@Autowired
	private ContentAssocService contentAssocService;
	@Autowired
	private PatrolPlanDetailService patrolPlanDetailService;
	@Autowired
	private DeptService deptService;
	
	@Override
	public PatrolRecordDO get(Long id){
		return patrolRecordDao.get(id);
	}
	
	@Override
	public List<PatrolRecordDO> list(Map<String, Object> map){
		return patrolRecordDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return patrolRecordDao.count(map);
	}
	
	@Override
	public int save(PatrolRecordDO patrolRecord){
		return patrolRecordDao.save(patrolRecord);
	}
	
	@Override
	public int update(PatrolRecordDO patrolRecord){
		return patrolRecordDao.update(patrolRecord);
	}
	
	@Override
	public int remove(Long id){
		return patrolRecordDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return patrolRecordDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> recordDetail(Long id) {
		Map<String,Object> results = Maps.newHashMap();
		Map<String,Object> record=this.patrolRecordDao.detail(id);
		if(record!=null){
			results.put("record",record);
			//获取签到图片
			Map<String, Object> param = Maps.newHashMapWithExpectedSize(2);
			param.put("assocId",id);
			param.put("assocType",Constant.PATROL_RECORD_SIGNIN_IMAGE);
			List<ContentAssocDO> contentAssocDO = contentAssocService.list(param);
			results.put("signInImage", contentAssocDO);
			//获取完成图片
			param.clear();
			param.put("assocId",id);
			param.put("assocType",Constant.PATROL_RECORD_APPEARANCE_IMAGE);
			List<ContentAssocDO> contentAssocDOS = contentAssocService.list(param);
			results.put("initFileList", contentAssocDOS);
			//获取任务中的巡检项目详情
			param.clear();
			param.put("recordId",id);
			param.put("sort", "id");
			param.put("order", "ASC");
			List<PatrolDetailDO>  detailList = this.patrolDetailService.list(param);
			if(detailList!=null && detailList.size()>0){
//			JSONArray array = new JSONArray();
			List<Map<String, Object>>array =new ArrayList<>();
			for(PatrolDetailDO detail: detailList){
				Map<String, Object>object=Maps.newHashMapWithExpectedSize(8);
//				JSONObject object = new JSONObject();
				DeviceDO device = this.deviceService.get(detail.getDeviceId());
				if (device==null) {
					return results;
				}
				PatrolProjectDO project =  this.patrolProjectService.get(detail.getProjectId());
				object.put("id", detail.getId());
				object.put("deviceId", device.getId());
				object.put("projectId", project.getId());
				object.put("serialNo",device.getSerialno());
				object.put("deviceName",device.getName());
				object.put("projectName",project.getName());
				object.put("function",project.getFunction());
				object.put("result",detail.getResult());
				object.put("content",detail.getContent());
				object.put("offTime",detail.getOffTime());
				array.add(object);
			}
			results.put("details",array);
			// 获取巡检的验收详情
			param.clear();
			param.put("recordId", id);
			param.put("limit", 1);
			param.put("offset", 0);
			List<PatrolCheckDO> list = patrolCheckService.list(param);
			if (list.size()>0) {
				results.put("checkDetail", list.get(0));
			}
			
		}
			
		}
		return results;
	}
	
	/**
	 * 单独创建巡检任务单，并不关联任何计划单
	 */
	@Override
	public Map<String, Object> addRecord(PatrolRecordDO record, String detailArray, String[] signInImage, String[] taglocationappearanceImage) {
		Long recordId = record.getId();
		int code = 0;
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(1);
		if (Objects.isNull(recordId)) {
			record.setWorkOrderno(DateFormatUtil.getWorkOrderno());
			record.setStartTime(new Date());
			code = patrolRecordDao.save(record);
			recordId = record.getId();
			results.put("id",record.getId());
		}
		if (Objects.nonNull(recordId)) {
			record.setStartTime(new Date());
			code = patrolRecordDao.update(record);
		}
		
		if(code>0){
			//关联巡检明细项
			JSONArray jsonArray = JSON.parseArray(detailArray);
			for(int j = 0; j < jsonArray.size(); j ++) {
				PatrolDetailDO detail = new PatrolDetailDO();
				JSONObject jsonObject = jsonArray.getJSONObject(j);
				Long deviceId = Long.parseLong(jsonObject.getOrDefault("deviceId",0L).toString());
				Long projectId = Long.parseLong(jsonObject.getOrDefault("projectId",0L).toString());
				//检查项目结果现未加入数据字典(1为正常,2为异常)
				int result = Integer.parseInt( jsonObject.getOrDefault("result",0).toString());
				String detailContent=jsonObject.getOrDefault("detailContent","").toString();
//				String offTime = jsonObject.optString("offTime");
//				if(StringUtils.isEmpty(offTime)) {
//					detail.setOffTime(null);
//				}
//				if (StringUtils.isNoneEmpty(offTime)) {
//					detail.setOffTime(DateFormatUtil.getDateByParttern(offTime));
//				}
				detail.setContent(detailContent);
				detail.setDeviceId(deviceId);
				detail.setProjectId(projectId);
				detail.setRecordId(recordId);
				detail.setResult(result);
				this.patrolDetailService.save(detail);
				
			}
			//图片上传
			// 完成后的照片上传
			contentAssocService.saveList(recordId, taglocationappearanceImage, Constant.PATROL_RECORD_APPEARANCE_IMAGE);
			// 拍照签到的照片上传
			contentAssocService.saveList(recordId, signInImage, Constant.PATROL_RECORD_SIGNIN_IMAGE);
			
		}
		return results;
	}
	/**
	 * 通过计划单的信息来创建出一个任务单
	 */
	@Override
	@Transactional
	public Map<String, Object> addRecordByPlan(Long planId,Date beginTime,Date endTime) {
		//通过计划id获取计划详情，详情里的信息再保存如计划任务中，通过定时功能来动态调用该方法
		Map<String,Object> results = Maps.newHashMap();
		PatrolPlanDO patrolPlanDO = patrolPlanService.get( planId);
		PatrolRecordDO record = new PatrolRecordDO();
//		record.setInformId(inFormId);
		record.setName(patrolPlanDO.getName());
		record.setEngineerId(patrolPlanDO.getEngineerId());
		record.setCellphone(patrolPlanDO.getCellphone());
		//巡检开启时间暂时为该任务创建时间
		record.setStartTime(beginTime);
		//通过计划的频次间隔计算出任务的应结束时间
//		Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//        calendar.setTime(date);
//		long currentTime = calendar.getTimeInMillis();
//		currentTime+=patrolPlanDO.getFrequency()*60*60*1000;
		record.setEndTime(endTime);
		//设置任务状态为待处理
		record.setStatus(Constant.WAITING_DEAL);
		record.setPlanId(patrolPlanDO.getId());

		String prefix = DateFormatUtil.getWorkOrderno(Constant.XJJL,beginTime);
		Map<String,Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("maxNo", prefix);
		params.put("offset", 0);
		params.put("limit", 1);
		List<PatrolRecordDO> list = this.list(params);
		String  suffix= null;
		if (list.size()>0) {
			suffix = list.get(0).getWorkOrderno();
		}
		record.setWorkOrderno(DateFormatUtil.getWorkOrderno(prefix, suffix));

		int code = patrolRecordDao.save(record);
		Long recordId = record.getId();
		results.put("id", recordId);
		//获取计划中设备检查详情
		if (code>0) {
			Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
			param.put("linkPatrolPlanId",planId);
			List<Map<String,Object>>patrolPlanDetail=this.patrolPlanDetailService.listForMap(param);
			for (Map<String, Object> map : patrolPlanDetail) {
				String linkProjectId = map.get("linkProjectId").toString();
				Long deviceId=Long.parseLong(map.get("deviceId").toString());
				linkProjectId=linkProjectId.substring(linkProjectId.indexOf("[")+1, linkProjectId.indexOf("]"));
				String[] split = linkProjectId.split(",");
				for (String s : split) {
					PatrolDetailDO detail = new PatrolDetailDO();
					detail.setProjectId(Long.parseLong(s));
					detail.setRecordId(recordId);
					detail.setDeviceId(deviceId);
					this.patrolDetailService.save(detail);
				}
			}
		}
		return results;
	}

	@Override
	public Map<String, Object> handleRecord(PatrolRecordDO record, String content,String detailArray,String[] taglocationappearanceImage,String[] signInImage) {
		Map<String,Object> results = Maps.newHashMap();
		Long recordId = record.getId();
		//更新巡检明细结果
		this.patrolRecordDao.update(record);
		List<PatrolDetailDO> parseArray = JSON.parseArray(detailArray, PatrolDetailDO.class);
//		JSONArray jsonArray = new JSONArray(detailArray);
		for (PatrolDetailDO patrolDetailDO : parseArray) {
			this.patrolDetailService.update(patrolDetailDO);
		}
		//图片上传
		// 完成后的照片上传
		contentAssocService.saveList(recordId, taglocationappearanceImage, Constant.PATROL_RECORD_APPEARANCE_IMAGE);
		
		// 拍照签到的照片上传
		contentAssocService.saveList(recordId, signInImage, Constant.PATROL_RECORD_SIGNIN_IMAGE);

		return results;
	}

//	@Override
	public Map<String, Object> checkRecord(PatrolCheckDO check, PatrolRecordDO record) {
		Map<String,Object> results = Maps.newHashMap();
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("recordId", record.getId());
		List<PatrolCheckDO> list = patrolCheckService.list(param);
		if (list.size()==0) {
			//创建验收记录
			check.setUserId(ShiroUtils.getUserId());
			check.setRecordId(record.getId());
			this.patrolCheckService.save(check);
		}
		if (list.size()>0) {
			check.setId(list.get(0).getId());
			this.patrolCheckService.update(check);
		}
		//更新巡检记录和巡检计划状态
		//设置任务单状态为已验收
		if (!Objects.equals(Constant.TS, check.getResult())) {
			record.setStatus(Constant.ALREADY_CHECK);
			record.setResult(check.getResult());
			record.setEndTime(new Date());
			this.patrolRecordDao.update(record);
			//通过该任务单计划ID查询所有兄弟任务单,查看任务状态单是否都为已验收，全部验收并且计划结束时间已过则计划最终完成
//			Map<String,Object> param = Maps.newHashMapWithExpectedSize(2);
//			param.put("planId",record.getPlanId());
//			param.put("status",Constant.ALREADY_CHECK);
//			List<PatrolRecordDO>patrolRecords=this.patrolRecordDao.list(param);
//			if (patrolRecords.size()==0) {
//				//获取该任务单对应的计划单
//				PatrolPlanDO plan = this.patrolPlanService.get(record.getPlanId());
//				//判断现在时间是否大于计划结束时间
//				if (plan.getEndTime().before(new Date())) {
//					//设置计划单状态131	已完成	state_stop_over
//					plan.setStatus(131);
//					this.patrolPlanService.update(plan);
//				}
//			}
		}
		return results;
	}

    @Override
    public List<Map<String, Object>> recordList(Map<String, Object> params) {
        return patrolRecordDao.listForMap(params);
    }

//	@Override
	public PatrolRecordDO getByPlanId(Long id) {
		return this.patrolRecordDao.getByPlanId(id);
	}

	@Override
	public List<Map<String, Object>> listApi(Map<String, Object>params) {
			List<Map<String,Object>>results =new ArrayList<>();
			List<Map<String,Object>> datas = patrolDetailService.getRecordByDevice(params);
			for (Map<String, Object> map : datas) {
				Long recordId = Long.parseLong(map.get("recordId").toString());
				params.put("recordId", recordId);
				List<Map<String, Object>> project = patrolDetailService.listForMap(params);
				if (project.size()>0) {
					map.put("project", project);
					results.add(map);
				}
			}
			return results;
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return this.patrolRecordDao.countForMap(params);
	}
	@Override
	public Map<String, Object> params(Map<String, Object> params) {
		if (params.get("checkType")!=null) {
			Long userId=params.get("userId")==null?null:Long.parseLong(params.get("userId").toString());
			if(Integer.parseInt(params.get("checkType").toString())==1){
				params.put("createBy",userId);//我发起的
			}else{
				params.put("manage",userId);//我办理的
			}
		}
		return params;
	}

	@Override
	public Map<String, Object> recordCheckDetail(Long id) {
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("recordId", id);
		param.put("limit", 1);
		param.put("offset", 0);
		List<PatrolCheckDO> list = patrolCheckService.list(param);
		if (list.size()>0) {
			result.put("checkDetail", list.get(0));
		}
		return result;
	}

	@Override
	public int closeRecord(Long id, String closeReason) {
		if (this.get(id).getEndTime().after(new Date())) {
			return -1;
		}
		PatrolRecordDO recordDO = new PatrolRecordDO();
		recordDO.setId(id);
		recordDO.setCompleteTime(new Date());
		recordDO.setCloseReason(closeReason);
		recordDO.setStatus(Constant.CLOSE);
		return this.update(recordDO);
	}
	
	@Override
	public int closeRecord(Long[] ids, String closeReason) {
		for (Long id : ids) {
			if (this.get(id).getEndTime().after(new Date())) {
				return -1;
			}
		}
		if (ids.length==0) {
			return -2;
		}
		Map<Object, Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("closeReason", closeReason);
		param.put("completeTime", new Date());
		param.put("status", Constant.CLOSE);
		param.put("ids", ids);
		return patrolRecordDao.batchUpdate(param);
	}
	
	@Override
	public void removeImage(Long id) {
		Long[] ids = { id };
		String[] types = { Constant.PATROL_RECORD_APPEARANCE_IMAGE,Constant.PATROL_RECORD_SIGNIN_IMAGE };
		contentAssocService.removeByAssocIdsAndTypes(ids, types);
	}
	
	@Override
	public Map<String, Object> saveRecord(PatrolRecordDO record, String detailArray, String[] signInImage,
			String[] taglocationappearanceImage) {
		Long recordId = record.getId();
		if (Objects.nonNull(recordId)) {
			patrolDetailService.remove(recordId);
			this.removeImage(recordId);
		}
		return this.addRecord(record, detailArray, signInImage, taglocationappearanceImage);
	}
	
	@Override
	public Map<String, Object> saveHandleRecord(String content, String detailArray, String[] signInImage,
			String[] taglocationappearanceImage, PatrolRecordDO record) {
		// 设置完成时间为当前修改时间
		record.setCompleteTime(new Date());
		// 设置任务结果为传入结果
		record.setContent(content);
		// 删除图片
		this.removeImage(record.getId());
		return this.handleRecord(record, content, detailArray, taglocationappearanceImage,
				signInImage);
	}
	
	@Override
	public Map<String, Object> getBacklog(Long userId, Long deptId) {
		String idPath = null;
		Long status = Constant.WAITING_DEAL;
        if (Objects.nonNull(deptId)) {
        	idPath = deptService.get(deptId).getIdPath();
		}
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
        // 创建人部门可以知晓该事项 待验收与或待处理的数量
        params.put("deptId",idPath);
        params.put("singleStatus",status);
        // 若传入为待处理 则获取当前用户待处理的任务数量
        params.put("now", new Date());
        params.put("user",userId);
        int waitingStatusCount = this.countForMap(params);
        Map<String,Object> results = Maps.newHashMapWithExpectedSize(1);
        results.put("helderWaitingCount", waitingStatusCount);
		return results;
	}
}
