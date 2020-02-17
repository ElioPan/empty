package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.ev.custom.dao.TaskMainDao;


@Service
public class TaskMainServiceImpl implements TaskMainService {
	@Autowired
	private TaskMainDao taskMainDao;

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private ReportTaskService reportTaskService;
	
	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private TaskEmployeeService taskEmployeeService;

	@Autowired
	private ContentAssocService contentAssocService;

	@Autowired
	private TaskReplyService taskReplyService;

	@Override
	public TaskMainDO get(Long id){
		return taskMainDao.get(id);
	}
	
	@Override
	public List<TaskMainDO> list(Map<String, Object> map){
		return taskMainDao.list(map);
	}
	
	@Override
	public List<Map<String,Object>> listForMap(Map<String, Object> map) {
		return taskMainDao.listForMap(map);
	}
	
	@Override
	public Map<String, Object> detail(Long id) {
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(10);
		Map<String,Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("id",id);
		Map<String,Object> taskMain = listForMap(params).get(0);
		results.put("taskMain", taskMain);
		//获取责任人和抄送人
		params.clear();
		params.put("taskId",id);
		params.put("assocType","held_person");
		params.put("replyId",0);
		List<Map<String,Object>> heldPerson = taskEmployeeService.listForMap(params);
		if(heldPerson.size()>0){
			results.put("heldPerson", heldPerson.get(0));
		}
		params.clear();
		params.put("taskId",id);
		params.put("assocType","cc_person");
		params.put("replyId",0);
		List<Map<String,Object>> ccPerson = taskEmployeeService.listForMap(params);
		JSONArray ccIdList = new JSONArray();
		for(Map<String,Object> map:ccPerson){
			JSONObject object = new JSONObject();
			object.put("id",map.get("employeeId").toString());
			object.put("name",map.get("userName").toString());
			ccIdList.add(object);
		}
		results.put("ccIdList", ccIdList);
		//获取图片
		params.clear();
		params.put("assocId",id);
		params.put("assocType",Constant.TASK_APPEARANCE_IMAGE);
		List<ContentAssocDO> contentAssocDOS = contentAssocService.list(params);
		results.put("initFileList", contentAssocDOS);
		// 获取回复信息
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(2);
		param.put("taskId", id);
		// 回复	Constant.REPLY
		param.put("replyType", Constant.REPLY);
		List<Map<String,Object>> replyList = taskReplyService.listForMap(param);
		results.put("replyList",replyList);
		// 获取处理记录
		param.clear();
		param.put("taskId", id);
		//Constant.REPLY_DEAL	处理	reply_deal
		param.put("replyType", Constant.REPLY_DEAL);
		List<Map<String, Object>> dealList = taskReplyService.listForMap(param);
		results.put("dealList", dealList);
		
		return results;
	}
	
	@Override
	public List<Map<String, Object>> getTaskReplyInfo(Long id, Integer status) {
		Map<String,Object> params = Maps.newHashMapWithExpectedSize(4);
		if (Objects.equals(Constant.REPLY_CHECK, status)) {
			params.put("createBy", ShiroUtils.getUserId());
		}
		params.put("taskId", id);
		params.put("replyType", status);
		params.put("offset", 0);
		params.put("limit", 1);
		return taskReplyService.listForMap(params);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return taskMainDao.count(map);
	}
	
	@Override
	public int countForMap(Map<String, Object> map) {
		return taskMainDao.countForMap(map);
	}
	
	@Override
	public int countBackLog(Map<String, Object> params) {
		return taskMainDao.countBackLog(params);
	}

	@Override
	public List<Map<String, Object>> countWeekBackLog(Map<String, Object> params) {
		return taskMainDao.countWeekBackLog(params);
	}
	
	@Override
	public void getUserWaitingCount(Long userId, Integer status, String idPath, Map<String, Object> results) {
		 Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
		// 创建人可以知晓该事项 待验收或待处理的数量
//        params.put("createBy",userId);
        // 创建人部门可以知晓该事项 待验收与或待处理的数量
        params.put("idPath",idPath);
        params.put("status",status);
        Map<String, Object> findDates = DatesUtil.findDates();
        params.putAll(findDates);
        // 获取当前用户创建的待验收或待处理的数量
//        int createUserCount = this.countBackLog(params);
//        List<Map<String,Object>>createUserWeekCount = this.countWeekBackLog(params);
//        results.put("createTaskCount", createUserCount);
//        results.put("createUserWeekCount", createUserWeekCount);
//        params.remove("createBy");
        // 若传入为待处理 则获取当前用户待处理的任务数量
        if (Objects.equals(status, Constant.WAITING_DEAL)) {
        	params.put("heldPerson", userId);
        	int createUserCount = this.countBackLog(params);
        	List<Map<String,Object>>createUserWeekCount = this.countWeekBackLog(params);
        	results.put("waitingDealCount", createUserCount);
            results.put("waitingDealWeekListCount", createUserWeekCount);
		}
        // 若传入为待验收 则获取当前用户待验收的任务数量
        if (Objects.equals(status, Constant.WAITING_CHECK)) {
        	params.put("createBy",userId);
        	int createUserCount = this.countBackLog(params);
        	List<Map<String,Object>>createUserWeekCount = this.countWeekBackLog(params);
        	results.put("waitingCheckCount", createUserCount);
            results.put("waitingCheckWeekListCount", createUserWeekCount);
		}
//        int waitingStatusCount = this.countBackLog(params);
//        List<Map<String,Object>>waitingStatusWeekListCount = this.countWeekBackLog(params);
//        if (Objects.equals(status, Constant.WAITING_DEAL)) {
//        	results.put("waitingDealCount", waitingStatusCount);
//        	results.put("waitingDealWeekListCount", waitingStatusWeekListCount);
//		}
//        if (Objects.equals(status, Constant.WAITING_CHECK)) {
//        	results.put("waitingCheckCount", waitingStatusCount);
//        	results.put("waitingCheckWeekListCount", waitingStatusWeekListCount);
//		}
	}
	
	@Override
	public int save(TaskMainDO taskMain){
		//获取编号
		DictionaryDO dic = dictionaryService.get(taskMain.getTaskType());
		String maxNo = DateFormatUtil.getWorkOrderno(dic.getValue());
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<TaskMainDO> list = this.list(param);
		String taskNo = null;
		if (list.size()>0) {
			 taskNo = list.get(0).getTaskNo();
		}
		taskMain.setTaskNo(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
		return taskMainDao.save(taskMain);
	}
	
	@Override
	public void add(Long taskMainId, Long[] ccList, Long heldPerson, Long checkPerson,
			String[] taglocationappearanceImage) {
		//抄送人Constant.CC_PERSON
		for (Long aLong : ccList) {
			TaskEmployeeDO taskEmployeeDO = new TaskEmployeeDO(taskMainId, aLong, Constant.CC_PERSON, 0L);
			taskEmployeeService.save(taskEmployeeDO);
		}
		//责任人 Constant.HELD_PERSON
		TaskEmployeeDO heldEmployeeDO = new TaskEmployeeDO(taskMainId,heldPerson,Constant.HELD_PERSON,0L);
		taskEmployeeService.save(heldEmployeeDO);
		//验收人 Constant.CHECK_PERSON
		TaskEmployeeDO checkEmployeeDO = new TaskEmployeeDO(taskMainId,checkPerson, Constant.CHECK_PERSON,0L);
		taskEmployeeService.save(checkEmployeeDO);
		//保存图片信息
		contentAssocService.saveList(taskMainId,taglocationappearanceImage,Constant.TASK_APPEARANCE_IMAGE);
	}
	
	
	
	@Override
	public R saveTaskInfo(TaskMainDO taskMain, Long[] ccList, Long heldPerson, Long checkPerson, String linkOrderNo,
			Integer linkOrderType, Integer linkStageType, String[] taglocationappearanceImage) {
		Long taskId = taskMain.getId();
		if (taskId==null) {
			Map<String,Object> result = Maps.newHashMapWithExpectedSize(1);
			int saveTaskMain = this.save(taskMain);
			taskId = taskMain.getId();
			if (saveTaskMain==0) {
				return R.error();
			}
			this.add(taskId,ccList,heldPerson,checkPerson,taglocationappearanceImage);
			if (linkOrderNo!=null&&linkOrderType!=null&&linkStageType!=null) {
				//保存关联单号信息
				ReportTaskDO reportTask = new ReportTaskDO(taskId,linkOrderNo,linkOrderType,linkStageType);
				reportTaskService.save(reportTask);
			}
			result.put("taskId",taskId);
			return R.ok(result);
		}
		if(this.update(taskMain)>0) {
			Long[] taskIds = { taskId };
			// Constant.CC_PERSON 抄送人 cc_person
			// Constant.HELD_PERSON 责任人 held_person
			// Constant.CHECK_PERSON 验收人 check_person
			
			Integer[] assocTypes = { Constant.HELD_PERSON, Constant.CC_PERSON, Constant.CHECK_PERSON };
			this.removeSatellite(taskIds, assocTypes, Constant.TASK_APPEARANCE_IMAGE);
		
			this.add(taskId,ccList,heldPerson,checkPerson,taglocationappearanceImage);
			return R.ok();
		}
		return R.error();
	}
	
	@Override
	public void dealSave(TaskReplyDO taskReplyDO/* , Long[] ccList */) {
		//Constant.REPLY_DEAL	处理	reply_deal
		taskReplyDO.setReplyType(Constant.REPLY_DEAL);
		int count = saveReplyDO(taskReplyDO);
		
		// 新原型任务处理的时候需要填写抄送人
		/*
		 * DictionaryDO ccDic = dictionaryService.getByValue("cc_person","task_role");
		 * for(int i=0;i<ccList.length;i++){ TaskEmployeeDO taskEmployeeDO = new
		 * TaskEmployeeDO(taskReplyDO.getTaskid(),ccList[i],ccDic.getId(),taskReplyDO.
		 * getId()); taskEmployeeService.save(taskEmployeeDO); }
		 */
		/*
		 * DictionaryDO heldDic =
		 * dictionaryService.getByValue("check_person","task_role"); TaskEmployeeDO
		 * taskEmployeeDO = new
		 * TaskEmployeeDO(taskReplyDO.getTaskid(),checkPerson,heldDic.getId(),
		 * taskReplyDO.getId()); taskEmployeeService.save(taskEmployeeDO);
		 */
		//若处理不为暂存状态修改任务单状态
		if (taskReplyDO.getStatus().equals(Constant.TS)) {
			return;
		}
		if (count>0) {
			TaskMainDO taskMainDO = get(taskReplyDO.getTaskid());
			// 将任务状态改为待处理
			taskMainDO.setStatus(Constant.WAITING_CHECK);
			//修改任务单状态
			update(taskMainDO);
		}
	}
	
	@Override
	public void checkSave(TaskReplyDO taskReplyDO/* , Long[] ccList */) {
		// Constant.REPLY_CHECK	验收	reply_check
		taskReplyDO.setReplyType(Constant.REPLY_CHECK);
		int count = saveReplyDO(taskReplyDO);
		/*
		 * //Constant.CC_PERSON 抄送人 cc_person for(int i=0;i<ccList.length;i++){ TaskEmployeeDO
		 * taskEmployeeDO = new
		 * TaskEmployeeDO(taskReplyDO.getTaskid(),ccList[i],Constant.CC_PERSON,taskReplyDO.getId());
		 * taskEmployeeService.save(taskEmployeeDO); }
		 */
		//若处理不为暂存状态修改任务单状态
		if (taskReplyDO.getStatus().equals(Constant.TS)) {
			return;
		}
		if (count>0) {
			Integer status = taskReplyDO.getStatus();
			// 修改任务处理记录的状态
			TaskReplyDO dealReply = taskReplyService.get(taskReplyDO.getDealId());
			dealReply.setStatus(status);
			taskReplyService.update(dealReply);
			// 修改任务单据里的状态
			TaskMainDO taskMainDO = get(taskReplyDO.getTaskid());	
			if (Objects.equals(Constant.RESULT_PASS, taskReplyDO.getStatus())) {
				// 将任务状态改为已验收
				taskMainDO.setStatus(Constant.ALREADY_CHECK);
			}
			if (Objects.equals(Constant.RESULT_UNPASS, taskReplyDO.getStatus())) {
				// 将任务状态改为待处理
				taskMainDO.setStatus(Constant.WAITING_DEAL);
			}
			update(taskMainDO);
		}
	}

	@Override
	public int replySave(TaskReplyDO taskReplyDO, Long[] ccList){
		//Constant.REPLY	回复	reply
		taskReplyDO.setReplyType(Constant.REPLY);
		
		int save = taskReplyService.save(taskReplyDO);
		//Constant.CC_PERSON	抄送人	cc_person
		if (save>0) {
			for (Long aLong : ccList) {
				TaskEmployeeDO taskEmployeeDO = new TaskEmployeeDO(taskReplyDO.getTaskid(), aLong, Constant.CC_PERSON, taskReplyDO.getId());
				taskEmployeeService.save(taskEmployeeDO);
			}
		}
		return save;
	}
	
	private int saveReplyDO(TaskReplyDO taskReplyDO) {
		int count = 0;
		Long replyId = taskReplyDO.getId();
		if (replyId==null) {
			count = taskReplyService.save(taskReplyDO);
		}
		if (replyId!=null) {
			count = taskReplyService.update(taskReplyDO);
		}
		return count;
	}
	
	@Override
	public int update(TaskMainDO taskMain){
		return taskMainDao.update(taskMain);
	}
	
	@Override
	public void edit(TaskMainDO taskMain, Long[] ccList,Long heldPerson, Long checkPerson, String[] taglocationappearanceImage,String[] deleteTagAppearanceImage) {
		update(taskMain);
		Long taskMainId = taskMain.getId();
		contentAssocService.saveList(taskMainId,taglocationappearanceImage,Constant.TASK_APPEARANCE_IMAGE);
		contentAssocService.deleteList(deleteTagAppearanceImage);
		Map<String,Object> params = Maps.newHashMapWithExpectedSize(2);
		params.put("assocType",Constant.HELD_PERSON);
		params.put("taskId",taskMainId);
		TaskEmployeeDO taskEmployeeDO = taskEmployeeService.list(params).get(0);
		taskEmployeeDO.setEmployeeId(heldPerson);
		taskEmployeeService.update(taskEmployeeDO);
		
		params.put("assocType", Constant.CHECK_PERSON);
		TaskEmployeeDO checkTaskEmployeeDO = taskEmployeeService.list(params).get(0);
		checkTaskEmployeeDO.setEmployeeId(checkPerson);
		taskEmployeeService.update(checkTaskEmployeeDO);
	}
	
	@Override
	public int remove(Long id){
		return taskMainDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return taskMainDao.batchRemove(ids);
	}

	@Override
	public void removeSatellite(Long[] ids,Integer[] assocTypes,String imageType) {
		if (ArrayUtils.isNotEmpty(ids)) {
			// 将抄送人删除
			Map<String, Object> map =Maps.newHashMapWithExpectedSize(2);
			map.put("taskId", ids);
			map.put("assocTypes", assocTypes);
			taskEmployeeService.removeByAssocIdAndTaskId(map);
			// 将图片删除
			contentAssocService.removeByAssocIdAndType(ids,imageType);
		}
	}
	
	@Override
	public boolean nonWaitingDeal(Integer status) {
		return !(Objects.equals(status, Constant.WAITING_DEAL));
	}
	
	@Override
	public boolean nonTS(Integer status) {
		return !(status==null||Objects.equals(status,Constant.TS));
	}
	
	@Override
	public boolean nonWaitingCheck(Integer status) {
		return !(Objects.equals(status,Constant.WAITING_CHECK));
	}
	
	@Override
	public boolean isAlreadyCheck(Integer status) {
		return Objects.equals(status,Constant.ALREADY_CHECK);
	}
	
	@Override
	public boolean isDealBy(Long id) {
    	Map<String,Object>params = Maps.newHashMapWithExpectedSize(3);
    	params.put("taskId",id);
		params.put("assocType",Constant.HELD_PERSON);
		params.put("replyId",0);
		List<TaskEmployeeDO> heldPerson = taskEmployeeService.list(params);
		if(heldPerson.size()>0){
			return Objects.equals(heldPerson.get(0).getEmployeeId(), ShiroUtils.getUserId());
		}
		return false;
    }
	
}
