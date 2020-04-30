package com.ev.mes.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.mes.dao.ProcessReportCheckDao;
import com.ev.mes.domain.ProcessReportCheckDO;
import com.ev.mes.domain.ProcessReportCheckItemDO;
import com.ev.mes.service.ProcessReportCheckItemService;
import com.ev.mes.service.ProcessReportCheckService;
import com.ev.mes.service.ReworkRepairMiddleService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProcessReportCheckServiceImpl implements ProcessReportCheckService {
	@Autowired
	private ProcessReportCheckDao processReportCheckDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private ProcessReportCheckItemService processReportCheckItemService;
	@Autowired
	private ReworkRepairMiddleService reworkRepairMiddleService;
	
	@Override
	public ProcessReportCheckDO get(Long id){
		return processReportCheckDao.get(id);
	}
	
	@Override
	public List<ProcessReportCheckDO> list(Map<String, Object> map){
		return processReportCheckDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return processReportCheckDao.count(map);
	}
	
	@Override
	public int save(ProcessReportCheckDO processReportCheck){
		return processReportCheckDao.save(processReportCheck);
	}
	
	@Override
	public int update(ProcessReportCheckDO processReportCheck){
		return processReportCheckDao.update(processReportCheck);
	}
	
	@Override
	public int remove(Long id){
		return processReportCheckDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return processReportCheckDao.batchRemove(ids);
	}

	@Override
	public Map<String, Object> checkHeadDetail(Map<String, Object> map) {
		return processReportCheckDao.checkHeadDetail(map);
	}

	@Override
	public List<Map<String, Object>> checkBadyDetail(Map<String, Object> map) {
		return  processReportCheckDao.checkBadyDetail(map);
	}

	@Override
	public R saveAndChangeAndSbumit(ProcessReportCheckDO processReportCheckDO, String bodyDetail, Long[] itemIds, int sign) {

		if (Objects.nonNull(processReportCheckDO)) {
//			List<ProcessReportCheckDO> prCDO = JSON.parseArray(headDetail, ProcessReportCheckDO.class);
//			ProcessReportCheckDO pRCDo = prCDO.get(0);
			if(sign==1){
				processReportCheckDO.setStatus(ConstantForMES.MES_APPLY_APPROED);
			}else{
				processReportCheckDO.setStatus(Constant.TS);//暂存
			}
			if (Objects.isNull(processReportCheckDO.getId())) {
				//保存新增
				//<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
				String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.REPORT_CHECK_GXJY, new Date());
				Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
				params.put("maxNo", prefix);
				params.put("offset", 0);
				params.put("limit", 1);
				List<ProcessReportCheckDO> list = processReportCheckDao.list(params);
				String suffix = null;
				if (list.size() > 0) {
					suffix = list.get(0).getCode();
				}
				processReportCheckDO.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
				processReportCheckDO.setCreateBy(ShiroUtils.getUserId());//报检人、检验人
				processReportCheckDao.save(processReportCheckDO);

				if (Objects.nonNull(bodyDetail)) {

					List<ProcessReportCheckItemDO> pRCIDO = JSON.parseArray(bodyDetail, ProcessReportCheckItemDO.class);
					for (ProcessReportCheckItemDO prcIDo : pRCIDO) {
							//保存新增
							prcIDo.setReportCheckId(processReportCheckDO.getId());
							processReportCheckItemService.save(prcIDo);
					}
				}

				return R.ok();
			} else {
				//更新
				processReportCheckDao.update(processReportCheckDO);

				if (Objects.nonNull(itemIds)&&itemIds.length > 0) {
					processReportCheckItemService.batchRemove(itemIds);
				}
				if (Objects.nonNull(bodyDetail)) {
					List<ProcessReportCheckItemDO> pRCIDO = JSON.parseArray(bodyDetail, ProcessReportCheckItemDO.class);
					for (ProcessReportCheckItemDO prcIDo : pRCIDO) {
						if (Objects.nonNull(prcIDo.getId())) {
							//更新
							processReportCheckItemService.update(prcIDo);
						} else {
							//保存新增
							prcIDo.setReportCheckId(processReportCheckDO.getId());
							processReportCheckItemService.save(prcIDo);
						}
					}
				}

				return R.ok();
			}

		} else {
			//"所传主表数据为空！"
			return R.error(messageSourceHandler.getMessage("common.massge.dateOfNon",null));
		}
	}

	@Override
	public R batchRemoveCheckReport(Long[] reportCheckId){

		if(reportCheckId.length>0){
			Map<String,Object>  map= new HashMap<String,Object>();

			map.put("type",ConstantForMES.REWORK_CHECK_TYPE);
			map.put("sourceId",reportCheckId);
			int rows = reworkRepairMiddleService.canDelReportAboutMiddle(map);

			if (rows!=0){
				return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
			}else{
				processReportCheckDao.batchRemove(reportCheckId);
				processReportCheckItemService.removeByReportId(reportCheckId);
				return R.ok();
			}
		}else{
			//"所传参数为空！"
			return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon",null));
		}
	}

	@Override
	public  R getDetailOfCheck(Long reportCheckId){
		if(Objects.nonNull(processReportCheckDao.get(reportCheckId))){
				//获取主子表信息
			Map<String, Object> headDetailOfCheck = processReportCheckDao.getdetailOfCheck(reportCheckId);
			List<Map<String, Object>> bodyDetailOfCheck = processReportCheckItemService.detailByCheckId(reportCheckId);
			Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);

			if(Objects.nonNull(headDetailOfCheck)){
				results.put("headDetailOfCheck",headDetailOfCheck);
				results.put("bodyDetailOfCheck",bodyDetailOfCheck);
			}
			return R.ok(results);

		}else{
			//"所传参数下数据不存在！"
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return processReportCheckDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return processReportCheckDao.countForMap(map);
	}

	@Override
	public int countDeleAboutCheck(Map<String, Object> map) {
		return processReportCheckDao.countDeleAboutCheck(map);
	}

}
