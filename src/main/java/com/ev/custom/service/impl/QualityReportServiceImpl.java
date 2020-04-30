package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.custom.dao.QualityReportDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.QualityGroupDO;
import com.ev.custom.domain.QualityReportDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.QualityGroupService;
import com.ev.custom.service.QualityReportService;
import com.ev.custom.service.ReportTaskService;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;




@Service
public class QualityReportServiceImpl implements QualityReportService {
	@Autowired
	private QualityReportDao qualityReportDao;
	
	@Autowired
	private QualityGroupService qualityGroupService;
	
	@Autowired
	private ReportTaskService reportTaskService;
	
	@Autowired
	private ContentAssocService contentAssocService;
	
	@Override
	public QualityReportDO get(Long id){
		
		return qualityReportDao.get(id);
	}
	
	@Override
	public List<QualityReportDO> list(Map<String, Object> map){
		return qualityReportDao.list(map);
	}
	
	@Override
	public List<Map<String,Object>> listForMap(Map<String, Object> params) {
		return qualityReportDao.listForMap(params(params));
	}
	
	@Override
	public int count(Map<String, Object> map){
		return qualityReportDao.count(map);
	}
	
	@Override
	public int save(QualityReportDO qualityReport){
		//获取编号
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForDevice.BDBG);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<QualityReportDO> list = this.list(param);
		String taskNo = null;
		if (list.size()>0) {
			taskNo = list.get(0).getReportNo();
		}
		qualityReport.setReportNo(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
		//设置报告状态为编制中
		qualityReport.setStatus(ConstantForDevice.EDITING);
		return qualityReportDao.save(qualityReport);
	}
	
	
	@Override
	public int update(QualityReportDO qualityReport){
		return qualityReportDao.update(qualityReport);
	}
	
	@Override
	public int remove(Long id){
		return qualityReportDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return qualityReportDao.batchRemove(ids);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return qualityReportDao.countForMap(params(params));
	}
	/**
	 * 8D报告详情
	 */
	@Override
	public Map<String, Object> detail(Long id) {
		
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(10);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(2);
		param.put("id",id);
		List<Map<String,Object>> allInfo=listForMap(param);
		Map<String,Object> qualityReport = allInfo.get(0);
		results.put("qualityReport", qualityReport);
		//获取改善小组信息
		param.clear();
		param.put("reportId",id);
		List<Map<String,Object>>qualityGroup= qualityGroupService.listForMap(param);
		results.put("qualityGroup", qualityGroup);
		//获取所有的任务计划信息
		String repotNo = (String)qualityReport.get("reportNo");
		//获得临时改善计划
		param.clear();
		param.put("linkOrderNo",repotNo);
		param.put("linkStageValue","temporary_plan");
		List<Map<String,Object>>temporaryPlan = reportTaskService.listForMap(param);
		if(temporaryPlan.size()>0){
			results.put("temporaryPlan", temporaryPlan);
		}
		//获得永久改善计划
		param.clear();
		param.put("linkOrderNo",repotNo);
		param.put("linkStageValue","perpetual_plan");
		List<Map<String,Object>>perpetualPlan = reportTaskService.listForMap(param);
		if(temporaryPlan.size()>0){
			results.put("perpetualPlan", perpetualPlan);
		}
		//获得纠正措施验证
		param.clear();
		param.put("linkOrderNo",repotNo);
		param.put("linkStageValue","measures_validate");
		List<Map<String,Object>>measuresValidate = reportTaskService.listForMap(param);
		if(temporaryPlan.size()>0){
			results.put("measuresValidate", measuresValidate);
		}
		//获得预防再次发生计划
		param.clear();
		param.put("linkOrderNo",repotNo);
		param.put("linkStageValue","prevent_plan");
		List<Map<String,Object>>preventPlan = reportTaskService.listForMap(param);
		if(temporaryPlan.size()>0){
			results.put("preventPlan", preventPlan);
		}
		//获取8D报告主要原因效果确认验收附件
		param.clear();
		param.put("assocId",id);
		param.put("assocType", ConstantForDevice.BD_MAINREASON_FILE);
		List<ContentAssocDO> mainReasonList = contentAssocService.list(param);
		results.put("mainReasonList", mainReasonList);
		//获取8D报告效果确认验收附件
		param.clear();
		param.put("assocId",id);
		param.put("assocType", ConstantForDevice.BD_CHECKRESULT_FILE);
		List<ContentAssocDO> checkResultList = contentAssocService.list(param);
		results.put("checkResultList", checkResultList);
		
		return results;
	}
	/**
	 * 获得报告的单个阶段的执行计划
	 */
	@Override
	public Map<String, Object> getDetailPlan(Long id,Integer planTypeId) {
		//计划阶段页面显示的8D报告信息
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(2);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(2);
		param.put("id",id);
		List<Map<String,Object>> allInfo=listForMap(param);
		Map<String,Object> qualityReport = allInfo.get(0);
		results.put("qualityReport", qualityReport);
		//计划阶段页面显示的计划任务信息
		String repotNo = (String)qualityReport.get("reportNo");
		param.clear();
		param.put("linkOrderNo",repotNo);
		param.put("linkStageType",planTypeId);
		List<Map<String,Object>>stagePlan = reportTaskService.listForMap(param);
		if(stagePlan.size()>0){
			results.put("stagePlan", stagePlan);
		}
		
		return results;
	}

	@Override
	public Map<String,Object> add(QualityReportDO qualityReport,String groupList) {
		Map<String,Object> results=Maps.newHashMap();
		qualityReport.setCheckStatus(ConstantForDevice.WAITING_CHECK);
		save(qualityReport);
		results.put("reportId",qualityReport.getId() );
		//改善小组保存
		List<QualityGroupDO> group = JSON.parseArray(groupList, QualityGroupDO.class);
		for (QualityGroupDO obj : group) {
			if (obj.getId() != null && obj.getId() != 0) {
//				qualityGroupService.update(obj);
			} else {
				obj.setReportId(qualityReport.getId());
				
				qualityGroupService.save(obj);
			}
		}
		return results;
	}


	@Override
	public int edit(QualityReportDO qualityReport, String groupList, Long[] groupIds) {
		if (groupIds!=null&&groupIds.length>0) {
			qualityGroupService.deleteList(groupIds);
		}
		//改善小组保存
		if (StringUtils.isNotBlank(groupList)) {
			List<QualityGroupDO> group = JSON.parseArray(groupList, QualityGroupDO.class);
			for (QualityGroupDO obj : group) {
				if (obj.getId() != null && obj.getId() != 0) {
					qualityGroupService.update(obj);
				} else {
					obj.setReportId(qualityReport.getId());
					qualityGroupService.save(obj);
				}
			}
		}
		return update(qualityReport);
	}

	@Override
	public int checkSave(Long id,Long userId,String resultContent) {
		// 传入8D报告主键ID，与分析效果原因
		QualityReportDO qualityReport=new QualityReportDO();
		qualityReport.setId(id);
		qualityReport.setCheckStatus(ConstantForDevice.ALREADY_CHECK);
		qualityReport.setCheckId(userId);
		qualityReport.setResultContent(resultContent);
		//验收完设置8D报告为已完成
		qualityReport.setStatus(ConstantForDevice.COMPLETED);
//		return qualityReport;
		return update(qualityReport);
	}
	

	@Override
	public int editReason(Long id,String analyzeReason) {
		QualityReportDO qualityReport=new QualityReportDO();
		qualityReport.setAnalyzeReason(analyzeReason);
		qualityReport.setId(id);
		return update(qualityReport);
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
	public boolean checkBDTask(Long id) {
		List<Map<String, Object>> preventPlan = getTaskInfo(id);
		for (Map<String, Object> map : preventPlan) {
			if (!Objects.equals(ConstantForDevice.ALREADY_CHECK.toString(), map.get("statusId").toString())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Map<String, Object>> getTaskInfo(Long id) {
		QualityReportDO qualityReportDO = get(id);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(1);
		param.put("linkOrderNo", qualityReportDO.getReportNo());
		return reportTaskService.listForMap(param);
	}

	@Override
	public boolean isDelete(Long id) {
		return this.getTaskInfo(id).size()>0;
	}
}
