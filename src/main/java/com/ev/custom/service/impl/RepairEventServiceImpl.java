package com.ev.custom.service.impl;


import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import com.ev.custom.service.*;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.RepairEventDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.domain.TaskEmployeeDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.DeptService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;


@Service
public class RepairEventServiceImpl implements RepairEventService {
	@Autowired
	private RepairEventDao repairEventDao;
	@Autowired
	private RepairEventPartService repairEventPartService;
	@Autowired
	private TaskEmployeeService taskEmployeeService;
	@Autowired
    private ContentAssocService contentAssocService;
	@Autowired
    private RepairCheckService repairCheckService;
	@Autowired
    private RepairRecordService repairRecordService;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskMainService taskMainService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public RepairEventDO get(Long id){
		return repairEventDao.get(id);
	}
	
	@Override
	public List<RepairEventDO> list(Map<String, Object> map){
		return repairEventDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return repairEventDao.count(map);
	}
	
	@Override
	public int save(RepairEventDO repairEvent){
		return repairEventDao.save(repairEvent);
	}
	
	@Override
	public int update(RepairEventDO repairEvent){
		return repairEventDao.update(repairEvent);
	}

	
	@Override
	public List<Map<String, Object>> listForMap(Map<String,Object>params) {
		return this.repairEventDao.listForMap(params);
	}
	
	@Override
	public int countForMap(Map<String, Object> params) {
		return this.repairEventDao.countForMap(params);
	}
	
	@Override
	public Map<String, Object> getDetail(Long eventId) {
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(5);
		repairInfo(eventId, results);
		Map<String,Object> params = Maps.newHashMapWithExpectedSize(3);
		//获取维修事件维修记录列表
		params.put("userId",ShiroUtils.getUserId());
		params.put("eventId",eventId);
		List<Map<String, Object>> records = repairRecordService.listForMap(params);
		results.put("records", records);
		// 获取维修所用工时及成本信息
		Map<String, Object> cost = repairRecordService.countForMap(params);
		results.put("cost", cost);
		return results;
	}
	
	
	private void repairInfo(Long eventId, Map<String, Object> results) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		//获取维修记录与维修报修事件的信息
		Map<String, Object>event=repairEventDao.getDetail(eventId);
		results.put("repairEvent",event);
		
		//获取发起报修事件时的照片
		params.put("assocId",eventId );
		params.put("assocType", ConstantForDevice.REPAIR_EVENT_IMAGE);
		List<ContentAssocDO> contentAssocDO = contentAssocService.list(params);
		results.put("repairEventImage", contentAssocDO);
		
		//获取维修事件抄送人
		params.clear();
		params.put("taskId",eventId);
		params.put("assocType", ConstantForDevice.REPAIR_EVENT_CC_PERSON);
		params.put("replyId",0);
		List<TaskEmployeeDO> ccPerson = taskEmployeeService.list(params);
		JSONArray ccIdList = new JSONArray();
		UserDO userDO;
		for(TaskEmployeeDO map:ccPerson){
			JSONObject object = new JSONObject();
			userDO = userService.get(map.getEmployeeId());
			object.put("id",map.getEmployeeId());
			object.put("name",userDO.getName());
			ccIdList.add(object);
		}
		results.put("ccIdList", ccIdList);
	}
	
	@Override
	public Map<String, Object> getRecordDetailByEventId(Long eventId) {
		Map<String,Object> results = Maps.newHashMapWithExpectedSize(1);
    	this.repairInfo(eventId, results);
    	List<Map<String, Object>> records = this.getRecordInfoByEventId(eventId);
		if (records.size()>0) {
			Map<String, Object> map = records.get(0);
			if (Objects.equals(Long.parseLong(map.get("statusId").toString()), Constant.TS)) {
				Long recordId = Long.parseLong(map.get("id").toString());
				Map<String, Object> recordDetail = this.repairRecordService.recordDetail(recordId);
				if (ShiroUtils.isUser(Long.parseLong(map.get("heldPersonId").toString()))) {
					results.put("recordDetail", recordDetail);
				}
			}
		}
		return results;
	}
	
	@Override
	public List<Map<String, Object>> getRecordInfoByEventId(Long eventId) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("eventId",eventId);
		params.put("offset", 0);
		params.put("limit", 1);
		return repairRecordService.listForMap(params);
	}
	
	@Override
	public Map<String, Object> getCountBacklog(Long userId, Long deptId) {
		String idPath = null;
		Long status = ConstantForDevice.WAITING_DEAL;
        if (Objects.nonNull(deptId)) {
        	idPath = deptService.get(deptId).getIdPath();
		}
        Map<String,Object> results = Maps.newHashMapWithExpectedSize(2);
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
        // 创建人可以知晓该事项 待验收或待处理的数量
//        params.put("createBy",userId);
        // 创建人部门可以知晓该事项 待验收与或待处理的数量
        params.put("deptId",idPath);
        params.put("status",status);
        // 获取当前用户创建的待验收或待处理的数量
//        int createUserCount = this.countForMap(params);
//        results.put("createerWaitingCount", createUserCount);
//        params.remove("createBy");
        // 若传入为待处理 则获取当前用户待处理的任务数量
		{
			params.put("engineerId",userId);
			int waitingStatusCount = this.countForMap(params);
			results.put("helderWaitingCount", waitingStatusCount);
		}
        // 若传入为待验收 则获取当前用户待验收的任务数量
        if (Objects.equals(status, ConstantForDevice.WAITING_CHECK)) {
        	params.put("createBy",userId);
        	int waitingStatusCount = this.countForMap(params);
        	results.put("helderCheckCount", waitingStatusCount);
		}
		return results;
	}
	
	@Override
	public List<Map<String, Object>> getRepairCheckInfo(Long eventId) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
    	params.put("eventId",eventId);
    	params.put("offset", 0);
		params.put("limit", 1);
		return repairCheckService.listForMap(params);
	}
	
	@Override
	public void saveEventSatellite(String[] taglocationappearanceImage, Long[] carbonCopyRecipients, Long eventId) {
		// 将抄送人与事件id一起保存至cus_task_employee表中
		if (ArrayUtils.isNotEmpty(carbonCopyRecipients)) {
			this.saveEvengts(eventId,carbonCopyRecipients);
		}
		// 将图片地址和事件ID一起保存至content_assoc
		contentAssocService.saveList(eventId, taglocationappearanceImage, ConstantForDevice.REPAIR_EVENT_IMAGE);
	}
	
	@Override
	public void saveEvengts(Long eventId, Long[] ids) {
        //保存抄送人信息
        for(Long id:ids){
        	// 维修事件抄送人	Constant.REPAIR_EVENT_CC_PERSON
        	TaskEmployeeDO taskEmployeeDO = new TaskEmployeeDO(eventId,id, ConstantForDevice.REPAIR_EVENT_CC_PERSON,0L);
        	taskEmployeeService.save(taskEmployeeDO);
		}
    }
	
	@Override
	public R saveRepairInfo(RepairEventDO event, String[] taglocationappearanceImage, Long[] carbonCopyRecipients) throws IOException, ParseException {
		// 若事件为新添事件则执行保存操作
    	Long eventId = event.getId();
    	if (eventId==null) {
    		//1.将事件状态保存为暂存Constant.TS
    		Map<String, Object> map = this.addRepairEvent(event);
    		eventId = event.getId();
    		if (eventId!=null) {
    			this.saveEventSatellite(taglocationappearanceImage, carbonCopyRecipients, eventId);
    			if (map.containsKey("id")) {
    				// 发送消息
					Long repairId = Long.parseLong(map.get("id").toString());
					JSONObject contentDetail = new JSONObject();
					contentDetail.put("id",repairId);
					contentDetail.put("url","/eqRepair/repairDetail?id="+repairId);
					List<Long> toUsers = new ArrayList<>(Arrays.asList(carbonCopyRecipients));
					String content = "单号为“"+event.getWorkOrderno()+"”的维修单据抄送了您，请及时关注！";
					noticeService.saveAndSendSocket("@我的维修单", content, repairId, contentDetail.toString(),5L, ShiroUtils.getUserId(),toUsers);
    				return R.ok(map);
    			}
    		}
    	}
    	
    	// 若有修改操作则执行修改逻辑
    	// 将事件修改
        if ( this.update(event)>0) {
        	Long [] eventIds = {eventId};
        	// 删除事件附属信息增加新的附属信息
        	this.removeAndSaveEventSatellite(taglocationappearanceImage, carbonCopyRecipients, eventId, eventIds);
			 // 发送消息
			JSONObject contentDetail = new JSONObject();
			contentDetail.put("id",eventId);
			contentDetail.put("url","/eqRepair/repairDetail?id="+eventId);
			List<Long> toUsers = new ArrayList<>(Arrays.asList(carbonCopyRecipients));
			String content = "单号为“"+event.getWorkOrderno()+"”的维修单据抄送了您，请及时关注！";
			noticeService.saveAndSendSocket("@我的维修单", content, eventId, contentDetail.toString(),5L, ShiroUtils.getUserId(),toUsers);
        	return R.ok();
		}
		return R.error();
	}
	
	@Override
	public Map<String,Object> addRepairEvent(RepairEventDO event) {
		Map<String,Object> result = Maps.newHashMap();
		String maxNo = DateFormatUtil.getWorkOrderno(ConstantForDevice.GZBX);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
		param.put("maxNo", maxNo);
		param.put("offset", 0);
		param.put("limit", 1);
		List<RepairEventDO> list = this.list(param);
		String taskNo = null;
		if (list.size()>0) {
			taskNo = list.get(0).getWorkOrderno();
		}
		event.setWorkOrderno(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
		//如果填入设备使用情况则更改设备使用状态
//		if (event.getUsage()!=null) {
//			DeviceDO deviceDO=new DeviceDO();
//			deviceDO.setUsingStatus(event.getUsage());
//			this.deviceService.update(deviceDO);
//		}
		 this.repairEventDao.save(event);
		 result.put("id", event.getId());
		 
		return result;
	}
	
	@Override
	public R saveProactiveRepairInfo(RepairEventDO event, RepairRecordDO record,
			String[] taglocationappearanceEventImage, Long[] carbonCopyRecipients, String partIdArray,
			String[] taglocationappearanceRecordImage) throws IOException, ParseException {
		Map<String,Object> result = Maps.newHashMapWithExpectedSize(1);
		Long eventId = event.getId();
		// 若为新增事件则为保存逻辑
    	if (eventId==null) {
			// 将事件保存 
			Map<String, Object> map = this.addRepairEvent(event);
			eventId = event.getId();
			result.put("id", eventId);
			if (eventId!=null) {
				this.repairRecordService.addRepairRecord(eventId,record,partIdArray,taglocationappearanceRecordImage);
				this.saveEventSatellite(taglocationappearanceEventImage, carbonCopyRecipients, eventId);
				// 将图片地址和事件ID一起保存至content_assoc
				if (map.containsKey("id")) {
					Long repairId = Long.parseLong(map.get("id").toString());
					JSONObject contentDetail = new JSONObject();
					contentDetail.put("id",repairId);
					contentDetail.put("url","/eqRepair/repairDetail?id="+repairId);
					List<Long> toUsers = new ArrayList<>(Arrays.asList(carbonCopyRecipients));
					String content = "单号为“"+event.getWorkOrderno()+"”的维修单据抄送了您，请及时关注！";
					noticeService.saveAndSendSocket("@我的维修单", content, repairId, contentDetail.toString(),5L, ShiroUtils.getUserId(),toUsers);
					return R.ok(result);
				}
			}
		}
    	// 若为暂存状态则执行修改操作
    	if (this.update(event)>0) {
    		Long [] eventIds = {eventId};
    		// 删除事件附属信息增加新的附属信息
    		this.removeAndSaveEventSatellite(taglocationappearanceEventImage, carbonCopyRecipients, eventId, eventIds);
    		// 删除记录附属信息增加新的附属信息
    		List<Map<String, Object>> records = this.getRecordInfoByEventId(eventId);
    		if (records.size()>0) {
    			Long recordId = Long.parseLong(records.get(0).get("id").toString());
				record.setId(recordId);
    			this.repairRecordService.update(record);
    			this.removeAndSaveRecordSatellite(event, record, partIdArray, taglocationappearanceRecordImage, recordId);
			}
			JSONObject contentDetail = new JSONObject();
			contentDetail.put("id",eventId);
			contentDetail.put("url","/eqRepair/repairDetail?id="+eventId);
			List<Long> toUsers = new ArrayList<>(Arrays.asList(carbonCopyRecipients));
			String content = "单号为“"+event.getWorkOrderno()+"”的维修单据抄送了您，请及时关注！";
			noticeService.saveAndSendSocket("@我的维修单", content, eventId, contentDetail.toString(),5L, ShiroUtils.getUserId(),toUsers);
    		return R.ok();
		}
    	
		return R.error();
	}
	
	@Override
	public R saveRepairInfo(Long eventId, String partIdArray, String[] taglocationappearanceImage,
			RepairRecordDO record, RepairEventDO repairEventDO) {
		// 将维修记录状态更改为待验收Constant.WAITINGCHECK
    	Long recordId = record.getId();
    	if (recordId==null) {
    		Map<String,Object> results = this.repairRecordService.addRepairRecord(eventId,record,partIdArray,taglocationappearanceImage);
    		return R.ok(results);
		}
    	RepairRecordDO repairRecord = repairRecordService.get(recordId);
    	if(this.nonTS(repairRecord.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("repair.status.nonTs",null));
    	}
    	// 若为暂存设备维修记录则执行更新操作
    	if (repairRecordService.update(record)>0) {
    		// 删除记录附属信息增加新的附属信息
    		this.removeAndSaveRecordSatellite(repairEventDO, record, partIdArray, taglocationappearanceImage, recordId);
    		return R.ok();
		}
    	
    	return R.error();
	}
	
	@Override
	public void removeRecordSatellite(Long recordId) {
		 Long[] recordIds = {recordId};
		// 将完工图片删除
		contentAssocService.removeByAssocIdAndType(recordIds, ConstantForDevice.REPAIR_EVENT_RECORD_IMAGE);
		// 将备件信息删除
		repairEventPartService.remove(recordId);
	}
	
	@Override
	public void removeAndSaveEventSatellite(String[] taglocationappearanceImage, Long[] carbonCopyRecipients,
			Long eventId, Long[] eventIds) {
		// 删除事件附属信息
		Long [] assocTypes = {ConstantForDevice.REPAIR_EVENT_CC_PERSON};
		taskMainService.removeSatellite(eventIds,assocTypes, ConstantForDevice.REPAIR_EVENT_IMAGE);
		// 保存事件附属信息
		this.saveEventSatellite(taglocationappearanceImage, carbonCopyRecipients, eventId);
	}
	
	@Override
	public void removeAndSaveRecordSatellite(RepairEventDO event, RepairRecordDO record, String partIdArray,
			String[] taglocationappearanceRecordImage, Long recordId) {
		// 再删除记录的附属信息再执行添加
		this.removeRecordSatellite(recordId);
		// 再将备件及图片信息保存 并修改设备的使用状态
		repairRecordService.saveRecordDetail(record, partIdArray, taglocationappearanceRecordImage, event);
	}
	
	@Override
	public int remove(Long id){
		repairCheckService.remove(id);
		repairEventPartService.remove(id);
		repairRecordService.remove(id);
		return repairEventDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		repairCheckService.batchRemove(ids);
		repairEventPartService.batchRemove(ids);
		repairRecordService.batchRemove(ids);
		return repairEventDao.batchRemove(ids);
	}
	
	@Override
	public void removeSatellite(Long[] ids, Long[] assocTypes, String imageType) {
		this.taskMainService.removeSatellite(ids, assocTypes, imageType);
	}
	
	@Override
	public boolean nonWaitingDeal(Long status) {
		return this.taskMainService.nonWaitingDeal(status);
	}
    
	@Override
	public boolean nonTS(Long status) {
		return this.taskMainService.nonTS(status);
	}
	
	@Override
	public boolean nonWaitingCheck(Long status) {
		return this.taskMainService.nonWaitingCheck(status);
	}
	
	@Override
	public Map<String, Object> params(Map<String, Object> params) {
//		if (params.get("checkType")!=null) {
//			Long userId=params.get("userId")==null?null:Long.parseLong(params.get("userId").toString());
//			if(Long.parseInt(params.get("checkType").toString())==1){
//				params.put("createBy",userId);//我发起的
//			}else{
//				params.put("manage",userId);//我办理的
//			}
//		}
		return params;
	}
	
	@Override
	public R saveRepairRecord(Long eventId, String partIdArray, String[] taglocationappearanceImage,
			RepairRecordDO record, RepairEventDO repairEventDO) {
		Map<String,Object>params = Maps.newHashMapWithExpectedSize(3);
    	params.put("taskId",eventId);
		params.put("assocType", ConstantForDevice.REPAIR_EVENT_HELD_PERSON);
		params.put("replyId",0);
		List<TaskEmployeeDO> heldPerson = taskEmployeeService.list(params);
		if (heldPerson.size()>0) {
			if (!ShiroUtils.isUser(heldPerson.get(0).getEmployeeId())) {
				return R.error(messageSourceHandler.getMessage("repair.nonHeldPerson.repair",null));
			}
			return this.saveRepairInfo(eventId, partIdArray, taglocationappearanceImage, record, repairEventDO);
		}
    	if (!ShiroUtils.isUser(repairEventDO.getEngineerId())) {
			return R.error(messageSourceHandler.getMessage("repair.nonHeldPerson.repair",null));
		}
    	return this.saveRepairInfo(eventId, partIdArray, taglocationappearanceImage, record, repairEventDO);
	}

}
