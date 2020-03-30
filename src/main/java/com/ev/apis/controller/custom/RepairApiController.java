package com.ev.apis.controller.custom;


import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.StringUtils;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author gumingjie
 * @date 2019/9/04
 */
@Api(value = "/",tags = "设备维修事件管理API")
@RestController
public class RepairApiController {
    @Autowired
    private RepairEventService repairEventService;
    @Autowired
    private RepairRecordService repairRecordService;
    @Autowired
    private RepairCheckService repairCheckService;
    @Autowired
	private TaskEmployeeService taskEmployeeService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/addRepairEvent", method = RequestMethod.POST)
    @ApiOperation("提交故障报修事件")
    public R addRepairEvent(RepairEventDO event,
                            @ApiParam(value = "上传图片") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage,
                            @ApiParam(value = "抄送人") @RequestParam(value = "carbonCopyRecipients", defaultValue = "", required = false) Long[] carbonCopyRecipients) {
    	event.setStatus(Constant.WAITING_DEAL);
    	return repairEventService.saveRepairInfo(event, taglocationappearanceImage, carbonCopyRecipients);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/addRepairEventTS", method = RequestMethod.POST)
    @ApiOperation("暂存故障报修事件")
    public R addRepairEventTS(RepairEventDO event,
                            @ApiParam(value = "上传图片") @RequestParam(value = "taglocationappearanceImage", defaultValue = "", required = false) String[] taglocationappearanceImage,
                            @ApiParam(value = "抄送人") @RequestParam(value = "carbonCopyRecipients", defaultValue = "", required = false) Long[] carbonCopyRecipients) {
    	if(repairEventService.nonTS(event.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("repair.status.nonTs",null));
    	}
    	event.setStatus(Constant.TS);
    	return repairEventService.saveRepairInfo(event, taglocationappearanceImage, carbonCopyRecipients);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/addProactiveRepair", method = RequestMethod.POST)
    @ApiOperation("提交无报修直接维修设备")
    public R addProactiveRepair(RepairEventDO event,
    							RepairRecordDO record,
                            @ApiParam(value = "上传事件图片") @RequestParam(value = "taglocationappearanceEventImage", defaultValue = "", required = false) String[] taglocationappearanceEventImage,
                            @ApiParam(value = "抄送人") @RequestParam(value = "carbonCopyRecipients", defaultValue = "", required = false) Long[] carbonCopyRecipients,
                            @ApiParam(value = "备品备件ID数组，如[{'partId':3,'amount':5,'price':2,'content':'测试备件'}]") @RequestParam(value = "partIdArray",defaultValue = "",required = false)  String partIdArray,
                            @ApiParam(value = "上传完工图片") @RequestParam(value = "taglocationappearanceRecordImage",defaultValue = "",required = false) String[] taglocationappearanceRecordImage
                           	) {
    	if(repairEventService.nonTS(event.getStatus())||repairEventService.nonTS(record.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("repair.status.nonTs",null));
    	}
    	// 将维修记录与维修事件状态都改为待验收
    	event.setStatus(Constant.WAITING_CHECK);
    	record.setStatus(Constant.WAITING_CHECK);
    	return repairEventService.saveProactiveRepairInfo(event, record, taglocationappearanceEventImage, carbonCopyRecipients,
				partIdArray, taglocationappearanceRecordImage);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/addProactiveRepairTS", method = RequestMethod.POST)
    @ApiOperation("暂存无报修直接维修设备")
    public R addProactiveRepairTS(RepairEventDO event,
    							RepairRecordDO record,
                            @ApiParam(value = "上传事件图片") @RequestParam(value = "taglocationappearanceEventImage", defaultValue = "", required = false) String[] taglocationappearanceEventImage,
                            @ApiParam(value = "抄送人") @RequestParam(value = "carbonCopyRecipients", defaultValue = "", required = false) Long[] carbonCopyRecipients,
                            @ApiParam(value = "备品备件ID数组，如[{'partId':3,'amount':5,'price':29,'content':'测试备件'}]") @RequestParam(value = "partIdArray",defaultValue = "",required = false)  String partIdArray,
                            @ApiParam(value = "上传完工图片") @RequestParam(value = "taglocationappearanceRecordImage",defaultValue = "",required = false) String[] taglocationappearanceRecordImage
                           	) {
    	if(repairEventService.nonTS(event.getStatus())||repairEventService.nonTS(record.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("repair.status.nonTs",null));
    	}
    	event.setStatus(Constant.TS);
    	record.setStatus(Constant.TS);
    	return repairEventService.saveProactiveRepairInfo(event, record, taglocationappearanceEventImage, carbonCopyRecipients,
				partIdArray, taglocationappearanceRecordImage);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairRecord/addRepairRecord",method = RequestMethod.POST)
    @ApiOperation("提交设备维修记录")
    public R addRepairRecord(@ApiParam(value = "事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "",required = false)  Long eventId,
                             @ApiParam(value = "备品备件ID数组，如[{'partId':3,'amount':5,'price':29,'content':'测试备件'}]") @RequestParam(value = "partIdArray",defaultValue = "",required = false)  String partIdArray,
                             @ApiParam(value = "上传完工图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage,
                             RepairRecordDO record){
    	RepairEventDO repairEventDO = repairEventService.get(eventId);
    	if (repairEventService.nonWaitingDeal(repairEventDO.getStatus())) {
			return R.error(messageSourceHandler.getMessage("repair.status.nonWaitingDeal",null));
		}
    	record.setStatus(Constant.WAITING_CHECK);
    	return repairEventService.saveRepairRecord(eventId, partIdArray, taglocationappearanceImage, record, repairEventDO);
    }

	
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairRecord/addRepairRecordTS",method = RequestMethod.POST)
    @ApiOperation("暂存设备维修记录")
    public R addRepairRecordTS(@ApiParam(value = "事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "",required = false)  Long eventId,
                             @ApiParam(value = "备品备件ID数组，如[{'partId':3,'amount':5,'price':29,'content':'测试备件'}]") @RequestParam(value = "partIdArray",defaultValue = "",required = false)  String partIdArray,
                             @ApiParam(value = "上传完工图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage,
                             RepairRecordDO record){
    	RepairEventDO repairEventDO = repairEventService.get(eventId);
    	if (repairEventService.nonWaitingDeal(repairEventDO.getStatus())&&repairEventService.nonTS(repairEventDO.getStatus())) {
			return R.error(messageSourceHandler.getMessage("repair.status.nonWaitingDeal",null));
		}
    	record.setStatus(Constant.TS);
    	return repairEventService.saveRepairRecord(eventId, partIdArray, taglocationappearanceImage, record, repairEventDO);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairCheck/completeCheck",method = RequestMethod.POST)
    @ApiOperation("提交维修完工验收")
    public R completeCheck(@ApiParam(value = "维修事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "") Long eventId,
                           RepairCheckDO check){
    	RepairEventDO event = this.repairEventService.get(eventId);
    	if (repairEventService.nonWaitingCheck(event.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("repair.status.nonWaitingCheck",null));
		}
    	if (!ShiroUtils.isUser(event.getCreateBy())) {
    		return R.error(messageSourceHandler.getMessage("repair.nonCreateUser",null));
		}
    	this.repairCheckService.completeCheck(event,check);
        return R.ok();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairCheck/completeCheckTS",method = RequestMethod.POST)
    @ApiOperation("暂存维修完工验收")
    public R completeCheckTS(@ApiParam(value = "维修事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "") Long eventId,
                           RepairCheckDO check){
    	RepairEventDO event = this.repairEventService.get(eventId);
    	if (repairEventService.nonWaitingCheck(event.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("repair.status.nonWaitingCheck",null));
		}
    	if (!ShiroUtils.isUser(event.getCreateBy())) {
    		return R.error(messageSourceHandler.getMessage("repair.nonCreateUser",null));
		}
    	check.setResult(Constant.TS);
        this.repairCheckService.completeCheck(event,check);
        return R.ok();
    }
    
	@EvApiByToken(value = "/apis/repairEvent/getDetail",method = RequestMethod.POST)
    @ApiOperation("获取维修事件详情")
    public R getDetail(@ApiParam(value = "维修事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "") Long eventId){
        return R.ok(this.repairEventService.getDetail(eventId));
    }
    
    @EvApiByToken(value = "/apis/repairEvent/getRecordDetail",method = RequestMethod.POST)
    @ApiOperation("获取维修记录详情")
    public R getRecordDetail(@ApiParam(value = "维修记录ID",required = true) @RequestParam(value = "recordId",defaultValue = "") Long recordId){
    	return R.ok(this.repairRecordService.recordDetail(recordId));
    }
    
    @EvApiByToken(value = "/apis/repairEvent/getRecordDetailByEventId",method = RequestMethod.POST)
    @ApiOperation("获取维修记录详情通过维修事件ID")
    public R getRecordDetailByEventId(@ApiParam(value = "维修事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "") Long eventId){
    	return R.ok(repairEventService.getRecordDetailByEventId(eventId));
    }
    
    @EvApiByToken(value = "/apis/repairCheck/checkDetail",method = RequestMethod.POST)
    @ApiOperation("维修完工验收详情")
    public R checkDeatil(@ApiParam(value = "维修事件ID",required = true) @RequestParam(value = "eventId",defaultValue = "") Long eventId
                           ){
    	Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
    	List<Map<String, Object>> checkDO = repairEventService.getRepairCheckInfo(eventId);
		if (checkDO.size()>0) {
			Map<String,Object> check = checkDO.get(0);
			if (Objects.equals(Integer.parseInt(check.get("result").toString()), Constant.TS)) {
				if (ShiroUtils.isUser(Long.parseLong(check.get("create_by").toString()))) {
					result.put("checkInfo", check);
				}
			}
		}
    	return R.ok(result);
    }
    
    @EvApiByToken(value = "/apis/repairEvent/eventList",method = RequestMethod.POST)
    @ApiOperation("故障报修事件列表")
    public R eventList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                       @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                       @ApiParam(value = "设备名称或者设备编号") @RequestParam(value = "divceName",defaultValue = "",required = false) String divceName,
                       @ApiParam(value = "开始时间(格式：2019-08-01)") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                       @ApiParam(value = "结束时间(格式：2019-08-02)") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
                       @ApiParam(value = "故障等级（紧急任务49；普通任务50；)") @RequestParam(value = "level",defaultValue = "",required = false) Integer level,
                       @ApiParam(value = "任务状态（待处理56；待验收:57；已验收58；）") @RequestParam(value = "status",defaultValue = "",required = false) Integer status,
                       @ApiParam(value = "处理状态（多标签）（待处理：56；待验收：57）") @RequestParam(value = "singleStatus",defaultValue = "",required = false)  Integer singleStatus,
                       @ApiParam(value = "维修责任人ID") @RequestParam(value = "engineerId",defaultValue = "",required = false)Long engineerId,
//                       @ApiParam(value = "类型（我发起的事件1；我办理的事件2；）",required = false) @RequestParam(value = "checkType",defaultValue = "",required = false) Integer checkType,
                       @ApiParam(value = "用户部门ID") @RequestParam(value = "deptIdQuery",defaultValue = "",required = false) Long deptIdQuery,
//                       @ApiParam(value = "用户ID",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)Long userId
                       @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                       @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
    ){
    	Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.repairEventService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
    	Long userId = ShiroUtils.getUserId();
//    	if (deptId!=null&&deptService.get(deptId)!=null) {
//    		String idPath = deptService.get(deptId).getIdPath();
//    		params.put("deptId",idPath);
//		}
    	
        params.put("divceName",divceName);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("level",level);
        params.put("status",status);
        params.put("engineerId",engineerId);
        params.put("deptIdQuery", deptIdQuery);
//        params.put("checkType",checkType);

        if (Objects.isNull(singleStatus)) {
        	params.put("userId",userId);
        	params.put("ccperson", ","+userId+",");
		}
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        // singleStatus 匹配不同字段
		params.put("singleStatus", singleStatus);

		if (Objects.equals(singleStatus, Constant.WAITING_CHECK)) {
			params.put("createBy", userId);
		}
		if (Objects.equals(singleStatus, Constant.WAITING_DEAL)) {
			params.put("engineerId", userId);
		}
		
        Map<String,Object> results = Maps.newHashMap();
    	List<Map<String,Object>> data = this.repairEventService.listForMap(params);
    	int total = this.repairEventService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
    
        return R.ok(results);
    }
    
    @EvApiByToken(value = "/apis/repairRecord/deviceRepairEventList",method = RequestMethod.POST)
    @ApiOperation("获取设备的维修记录列表")
    public R deviceRepairEventList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            				 @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
            				 @ApiParam(value = "维修事件Id") @RequestParam(value = "eventId",defaultValue = "",required = false)  Long eventId,
            				 @ApiParam(value = "设备编号") @RequestParam(value = "deviceSerialno",defaultValue = "",required = false)  String deviceSerialno,
            				 @ApiParam(value = "设备名称") @RequestParam(value = "deviceName",defaultValue = "",required = false) String deviceName,
            				 @ApiParam(value = "设备名称&&设备编号") @RequestParam(value = "deviceQuery",defaultValue = "",required = false) String deviceQuery,
            				 @ApiParam(value = "故障类型") @RequestParam(value = "type",defaultValue = "",required = false)  Integer type,
            				 @ApiParam(value = "使用情况") @RequestParam(value = "usage",defaultValue = "",required = false)  Integer usage,
            				 @ApiParam(value = "设备ID") @RequestParam(value = "deviceId",defaultValue = "",required = false)  Long deviceId,
            				 @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                             @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
                                   @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                                   @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
//                             ,@ApiParam(value = "任务单号、部门、维修人模糊查询",required = false) @RequestParam(value = "fuzzySearch",defaultValue = "",required = false)  String fuzzySearch
                             ){
    	Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.repairRecordService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
    	params.put("eventId", eventId);
    	params.put("deviceSerialno", deviceSerialno);
    	params.put("deviceName", deviceName);
    	params.put("type", type);
    	params.put("usage", usage);
    	if (Objects.isNull(deviceId)) {
    		params.put("userId", ShiroUtils.getUserId());
    	}
    	params.put("deviceQuery", deviceQuery);
        params.put("deviceId", deviceId);
        params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("offset",(pageno-1)*pagesize);
		params.put("limit",pagesize);
//		params.put("fuzzySearch", fuzzySearch);
        Map<String,Object> results = Maps.newHashMap();
    	List<Map<String,Object>> data = this.repairRecordService.listForMap(params);
    	Map<String, Object> totals = this.repairRecordService.countForMap(params);
    	int total = Integer.parseInt(totals.get("count").toString());
    	results.put("manHourTotal",totals.get("manHourTotal"));
		results.put("manHourCostTotal",totals.getOrDefault("manHourCostTotal",0));
		results.put("sparePartsCostTotal",totals.getOrDefault("sparePartsCostTotal",0));
    	results.put("costTotal",totals.get("costTotal"));
        results.put("offHourTotal",totals.get("offHourTotal"));
		return getR(pageno, pagesize, results, data, total);
	}

	@Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/remove",method = RequestMethod.POST,apiTitle = "删除维修事件")
    @ApiOperation("删除维修事件")
    public R remove(@ApiParam(value = "维修事件主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
    	RepairEventDO event = this.repairEventService.get(id);
    	if (repairEventService.nonTS(event.getStatus())) {
    		return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
		}
    	if (ShiroUtils.isUser(event.getCreateBy())) {
    		return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
		}
		Long[] ids = { id };
    	if(repairEventService.remove(id)>0){
    		Integer [] assocTypes = {Constant.REPAIR_EVENT_CC_PERSON};
    		repairEventService.removeSatellite(ids,assocTypes,Constant.REPAIR_EVENT_IMAGE);
            return R.ok();
        }
        return R.error();
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除维修事件")
    @ApiOperation("批量删除维修事件")
    public R batchRemove(@ApiParam(value = "维修事件主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
    	for (Long id : ids) {
    		RepairEventDO event = this.repairEventService.get(id);
    		if (repairEventService.nonTS(event.getStatus())) {
				return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
    		}
    		if (ShiroUtils.isUser(event.getCreateBy())) {
				return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
    		}
		}
    	int batchRemove = repairEventService.batchRemove(ids);
    	if (batchRemove==ids.length) {
    		Integer [] assocTypes = {Constant.REPAIR_EVENT_CC_PERSON};
    		repairEventService.removeSatellite(ids,assocTypes,Constant.REPAIR_EVENT_IMAGE);
    		return R.ok();
		}
    	return R.error();
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/removeRecord",method = RequestMethod.POST,apiTitle = "删除维修记录")
    @ApiOperation("删除维修记录")
    public R removeRecord(@ApiParam(value = "维修记录主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
    	RepairRecordDO record = this.repairRecordService.get(id);
    	if (repairEventService.nonTS(record.getStatus())) {
			return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
		}
    	if (ShiroUtils.isUser(record.getCreateBy())) {
			return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
		}
    	if(repairRecordService.remove(id)>0){
    		repairEventService.removeRecordSatellite(id);
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/batchRemoveRecord",method = RequestMethod.POST,apiTitle = "批量删除维修记录")
    @ApiOperation("批量删除维修记录")
    public R batchRemoveRecord(@ApiParam(value = "维修记录主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
    	for (Long id : ids) {
    		RepairRecordDO record = this.repairRecordService.get(id);
    		if (repairEventService.nonTS(record.getStatus())) {
				return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
    		}
    		if (ShiroUtils.isUser(record.getCreateBy())) {
				return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
    		}
		}
    	int batchRemove = repairRecordService.batchRemove(ids);
    	if (batchRemove==ids.length) {
    		for (Long id : ids) {
    			repairEventService.removeRecordSatellite(id);
			}
    		return R.ok();
		}
    	return R.error();
    }
    
    @EvApiByToken(value = "/apis/repairEvent/backlog",method = RequestMethod.POST,apiTitle = "获取待处理数量")
    @ApiOperation("获取待处理数量")
    public R countBacklog(
//                  @ApiParam(value = "用户Id",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)  Long userId,
//                  @ApiParam(value = "用户部门Id",required = false) @RequestParam(value = "deptId",defaultValue = "",required = false)  Long deptId
                 /* @ApiParam(value = "待处理状态(56)", required = true) @RequestParam(value = "status",defaultValue = "",required = true)  Integer status*/){
        Long userId = ShiroUtils.getUserId();
        Long deptId = null;
    	return  R.ok(repairEventService.getCountBacklog(userId, deptId));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/repairEvent/reventTurnToSend", method = RequestMethod.POST)
    @ApiOperation("将维修事件转派")
    public R reventTurnToSend(@ApiParam(value = "维修事件Id", required = true) @RequestParam(value = "eventId", defaultValue = "")Long eventId,
    		@ApiParam(value = "新的维修人Id", required = true) @RequestParam(value = "userId", defaultValue = "")Long userId) {
    	RepairEventDO repairEventDO = this.repairEventService.get(eventId);
    	if (repairEventService.nonWaitingDeal(repairEventDO.getStatus())) {
			return R.error(messageSourceHandler.getMessage("repair.status.nonWaitingDeal.turnToSend",null));
		}
    	List<Map<String,Object>> recordInfoByEventId = repairEventService.getRecordInfoByEventId(eventId);
    	if (recordInfoByEventId.size()>0) {
    		if (Objects.equals(recordInfoByEventId.get(0).get("statusId").toString(), Constant.TS.toString())) {
    			return R.error(messageSourceHandler.getMessage("repair.status.isTs.turnToSend",null));
			}
		}
		TaskEmployeeDO taskEmployeeDO = new TaskEmployeeDO(eventId,userId,Constant.REPAIR_EVENT_HELD_PERSON,0L);
		Map<String,Object>params = Maps.newHashMapWithExpectedSize(3);
    	params.put("taskId",eventId);
		params.put("assocType",Constant.REPAIR_EVENT_HELD_PERSON);
		params.put("replyId",0);
		List<TaskEmployeeDO> heldPerson = taskEmployeeService.list(params);
		if (heldPerson.size()>0) {
			if (!ShiroUtils.isUser(heldPerson.get(0).getEmployeeId())) {
				return R.error(messageSourceHandler.getMessage("repair.nonHeldPerson.turnToSend",null));
			}
			if(taskEmployeeService.save(taskEmployeeDO)>0) {
				return R.ok();
			}
		}
		if (!ShiroUtils.isUser(repairEventDO.getEngineerId())) {
			return R.error(messageSourceHandler.getMessage("repair.nonHeldPerson.turnToSend",null));
		}
		if(taskEmployeeService.save(taskEmployeeDO)>0) {
			return R.ok();
		}
    	return R.error();
    }
  /**************************************************************************************************************************************************/
    @EvApiByToken(value = "/apis/repairEvent/faultRank",method = RequestMethod.POST,apiTitle = "获取维修故障排名")
    @ApiOperation("获取维修故障排名")
    public R getFaultRank(){
    	return  R.ok(repairRecordService.getFaultRank());
    }
    
    @EvApiByToken(value = "/apis/repairEvent/repairPower",method = RequestMethod.POST,apiTitle = "获取每个设备的修复能力")
    @ApiOperation("获取每个设备的修复能力")
    public R getRepairPower(){
    	return  R.ok(repairRecordService.getRepairPower());
    }
    
    @EvApiByToken(value = "/apis/repairEvent/eventListForBoard",method = RequestMethod.POST)
    @ApiOperation("故障报修事件列表(看板用)")
    public R eventListForBoard(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			 @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
			 @ApiParam(value = "设备ID") @RequestParam(value = "deviceId",defaultValue = "",required = false)  Long deviceId,
			 @ApiParam(value = "看板用:显示待处理与待验收数据（0不显示/1显示）") @RequestParam(value = "boardStatus",defaultValue = "",required = false) Integer boardStatus) {
    	Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
    	params.put("boardStatus", boardStatus);
    	params.put("deviceId", deviceId);
    	params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
    	Map<String,Object> results = Maps.newHashMap();
    	List<Map<String,Object>> data = this.repairEventService.listForMap(params);
    	int total = this.repairEventService.countForMap(params);
		return getR(pageno, pagesize, results, data, total);
	}

	private R getR( int pageno, int pagesize, Map<String, Object> results, List<Map<String, Object>> data, int total) {
		if( data.size()>0){
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(data);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
			results.put("data",dsRet);
		}
		return R.ok(results);
	}
}
