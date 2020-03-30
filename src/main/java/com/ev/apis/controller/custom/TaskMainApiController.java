package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.domain.TaskEmployeeDO;
import com.ev.custom.domain.TaskMainDO;
import com.ev.custom.domain.TaskReplyDO;
import com.ev.custom.service.*;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.DeptService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(value = "/",tags = "任务管理API")
@RestController
public class TaskMainApiController {
    @Autowired
    private TaskMainService taskMainService;
    
    @Autowired
	private TaskReplyService taskReplyService;
    
    @Autowired
    private DeptService deptService;
    
    @Autowired
    private TaskEmployeeService taskEmployeeService;

	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Autowired
	private NoticeService noticeService;
    

    @EvApiByToken(value = "/apis/taskMain/list",method = RequestMethod.POST,apiTitle = "获取任务列表信息")
    @ApiOperation("获取任务列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "任务主题") @RequestParam(value = "title",defaultValue = "",required = false)  String title,
                  @ApiParam(value = "发起人") @RequestParam(value = "createBy",defaultValue = "",required = false)  String createBy,
//                  @ApiParam(value = "发起部门") @RequestParam(value = "deptId",defaultValue = "",required = false)  String deptId,
                  @ApiParam(value = "处理状态（待处理：56；待验收：57；已验收：58）") @RequestParam(value = "status",defaultValue = "",required = false)  String status,
                  @ApiParam(value = "处理状态（多标签）（待处理：56；待验收：57；已验收：58）") @RequestParam(value = "singleStatus",defaultValue = "",required = false)  Integer singleStatus,
//                  @ApiParam(value = "当前用户ID",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)  Long userId,
                  @ApiParam(value = "责任人") @RequestParam(value = "heldPerson",defaultValue = "",required = false)  String heldPerson,
                  @ApiParam(value = "发起开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                  @ApiParam(value = "发起结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,

                  @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                  @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
                  ){
        Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.taskMainService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }

//        UserDO userDO = ShiroUtils.getUserDataPermission();
        Long userId = ShiroUtils.getUserId();
        String idPath = null;
//        if (userDO != null) {
//            userId = userDO.getUserId();
//            Long deptIdParam = userDO.getDeptId();
//            params.put("deptId",deptIdParam);
//            params.put("deptDatas",userDO.getDeptDatas());
//            // 本部门及其下属部门
//            if (Objects.equals(userDO.getDataPermission(),Constant.SUBORDINATE_DEPT_DATA)) {
//                DeptDO deptDO = deptService.get(deptIdParam == null ? 0L : deptIdParam);
//                idPath = deptDO==null?null:deptDO.getIdPath();
//                params.put("idPath",idPath);
//            }
//        }

        params.put("title",title);
        params.put("createBy",createBy);
        params.put("idPath",idPath);
        params.put("status",status);
        params.put("heldPerson",heldPerson);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);


        
        // singleStatus 匹配不同字段
        if (Objects.equals(singleStatus, Constant.WAITING_CHECK)) {
        	params.put("singleStatus", singleStatus);
        	params.put("createId", userId);
        }
        if (Objects.equals(singleStatus, Constant.WAITING_DEAL)) {
        	 params.put("singleStatus", singleStatus);
			params.put("heldPersonId", userId);
		}
        if (Objects.equals(singleStatus, Constant.ALREADY_CHECK)) {
        	params.put("userId", userId);
        	params.put("singleStatusAready", singleStatus);
		}
        if (Objects.isNull(singleStatus)) {
        	params.put("userId", userId);
        	params.put("ccperson", ","+userId+",");
		}
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.taskMainService.listForMap(params);
        int total = this.taskMainService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/taskMain/detail",method = RequestMethod.POST,apiTitle = "获取任务详细信息")
    @ApiOperation("获取任务详细信息")
    public R detail(@ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return  R.ok(taskMainService.detail(id));
    }
    
    @EvApiByToken(value = "/apis/taskMain/detailDealReplyInfo",method = RequestMethod.POST,apiTitle = "获取处理记录详细信息")
    @ApiOperation("获取处理记录详细信息")
    public R detailDealReplyInfo(@ApiParam(value = "处理记录ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return  R.ok(taskReplyService.detail(id));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/save",method = RequestMethod.POST,apiTitle = "保存任务单信息")
    @ApiOperation("提交任务单信息")
    public R save(@ApiParam(value = "任务单信息",required = true) TaskMainDO taskMain,
                  @ApiParam(value = "抄送人") @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList,
                  @ApiParam(value = "责任人",required = true) @RequestParam(value = "heldPerson",defaultValue = "") Long heldPerson,
                  @ApiParam(value = "验收人",required = true) @RequestParam(value = "checkPerson",defaultValue = "") Long checkPerson,
                  @ApiParam(value = "关联单号") @RequestParam(value = "linkOrderNo",defaultValue = "",required = false) String linkOrderNo,
                  @ApiParam(value = "关联单据类型") @RequestParam(value = "linkOrderType",defaultValue = "",required = false) Integer linkOrderType,
                  @ApiParam(value = "关联单据阶段类型") @RequestParam(value = "linkStageType",defaultValue = "",required = false) Integer linkStageType,
                  @ApiParam(value = "上传图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage){
        	taskMain.setStatus(Constant.WAITING_DEAL);
        	return taskMainService.saveTaskInfo(taskMain, ccList, heldPerson, checkPerson, linkOrderNo, linkOrderType, linkStageType,
					taglocationappearanceImage);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/saveTS",method = RequestMethod.POST,apiTitle = "暂存任务单信息")
    @ApiOperation("暂存任务单信息")
    public R saveTS(@ApiParam(value = "任务单信息",required = true) TaskMainDO taskMain,
                  @ApiParam(value = "抄送人") @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList,
                  @ApiParam(value = "责任人",required = true) @RequestParam(value = "heldPerson",defaultValue = "") Long heldPerson,
                  @ApiParam(value = "验收人",required = true) @RequestParam(value = "checkPerson",defaultValue = "") Long checkPerson,
                  @ApiParam(value = "关联单号") @RequestParam(value = "linkOrderNo",defaultValue = "",required = false) String linkOrderNo,
                  @ApiParam(value = "关联单据类型") @RequestParam(value = "linkOrderType",defaultValue = "",required = false) Integer linkOrderType,
                  @ApiParam(value = "关联单据阶段类型") @RequestParam(value = "linkStageType",defaultValue = "",required = false) Integer linkStageType,
                  @ApiParam(value = "上传图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage){
         	// 状态设置为暂存
         	taskMain.setStatus(Constant.TS);
         	return taskMainService.saveTaskInfo(taskMain, ccList, heldPerson, checkPerson, linkOrderNo, linkOrderType, linkStageType,
					taglocationappearanceImage);
     }

//    @EvApiByToken(value = "/apis/taskMain/update",method = RequestMethod.POST,apiTitle = "编辑任务单信息")
//    @ApiOperation("编辑任务单信息")
//    public R update(@ApiParam(value = "任务单信息",required = true) TaskMainDO taskMain,
//                    @ApiParam(value = "抄送人") @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList,
//                    @ApiParam(value = "责任人",required = true) @RequestParam(value = "heldPerson",defaultValue = "") Long heldPerson,
//                    @ApiParam(value = "验收人",required = true) @RequestParam(value = "checkPerson",defaultValue = "") Long checkPerson,
//                    @ApiParam(value = "添加图片服务器路径") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage,
//                    @ApiParam(value = "删除图片ID列表") @RequestParam(value = "deletetag_appearanceImage",defaultValue = "",required = false) String[] deletetagAppearanceImage){
//        try {
//            taskMainService.edit(taskMain, ccList, heldPerson, checkPerson, taglocationappearanceImage, deletetagAppearanceImage);
//            return R.ok();
//        }catch (Exception ex){
//            return R.error(ex.getMessage());
//        }
//    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/dealSave",method = RequestMethod.POST,apiTitle = "提交处理任务")
    @ApiOperation("提交处理任务")
    public R dealSave(@ApiParam(value = "处理信息",required = true) TaskReplyDO taskReplyDO
//                     , @ApiParam(value = "抄送人",required = false) @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList
                     ){
        	TaskMainDO taskMainDO = taskMainService.get(taskReplyDO.getTaskid());
        	if (taskMainService.nonWaitingDeal(taskMainDO.getStatus())) {
				return R.error(messageSourceHandler.getMessage("task.status.nonWaitingDeal",null));
			}
        	if (!taskMainService.isDealBy(taskMainDO.getId())) {
				return R.error(messageSourceHandler.getMessage("task.nonHeldPerson.task",null));
			}
        	// 设置回复状态为处理
        	Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        	taskReplyDO.setStatus(Constant.REPLY_DEAL);
			taskMainService.dealSave(taskReplyDO/* ,ccList */);
			result.put("id", taskReplyDO.getId());
			if (Objects.isNull(taskReplyDO.getId())) {
				return R.error();
			}
            return R.ok(result);
    }
    
    @EvApiByToken(value = "/apis/taskMain/dealDetail",method = RequestMethod.POST,apiTitle = "处理详情")
    @ApiOperation("处理详情(调用暂存编辑时使用)")
    public R dealDetail(@ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "")Long id){
        try {
        	Map<String,Object> result = Maps.newHashMapWithExpectedSize(1);
        	List<Map<String, Object>> dealList = taskMainService.getTaskReplyInfo(id,Constant.REPLY_DEAL);
    		if (dealList.size()>0) {
    			Map<String, Object> map = dealList.get(0);
    			if (Objects.equals(Integer.parseInt(map.get("statusId").toString()), Constant.TS)) {
    				if (taskMainService.isDealBy(id)) {
    					result.put("dealInfo", map);
					}
				}
			}
            return R.ok(result);
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/dealSaveTS",method = RequestMethod.POST,apiTitle = "暂存处理")
    @ApiOperation("暂存处理")
    public R dealSaveTS(@ApiParam(value = "处理信息",required = true) TaskReplyDO taskReplyDO
//                     , @ApiParam(value = "抄送人",required = false) @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList
                     ){
        	TaskMainDO taskMainDO = taskMainService.get(taskReplyDO.getTaskid());
        	if (taskMainService.nonWaitingDeal(taskMainDO.getStatus())) {
				return R.error(messageSourceHandler.getMessage("task.status.nonWaitingDeal",null));
			}
        	if (!taskMainService.isDealBy(taskMainDO.getId())) {
				return R.error(messageSourceHandler.getMessage("task.nonHeldPerson.task",null));
			}
        	Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        	taskReplyDO.setStatus(Constant.TS);
			taskMainService.dealSave(taskReplyDO/* ,ccList */);
			result.put("id", taskReplyDO.getId());
			if (Objects.isNull(taskReplyDO.getId())) {
				return R.error();
			}
            return R.ok(result);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/replySave",method = RequestMethod.POST,apiTitle = "回复任务单")
    @ApiOperation("回复任务单")
    public R replySave(@ApiParam(value = "回复信息",required = true) TaskReplyDO taskReplyDO,
                      @ApiParam(value = "抄送人") @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList) throws IOException, ParseException {
    	TaskMainDO taskMainDO = taskMainService.get(taskReplyDO.getTaskid());
    	if (taskMainService.isAlreadyCheck(taskMainDO.getStatus())) {
			return R.error("任务已验收不能回复");
		}

		int replySave = taskMainService.replySave(taskReplyDO, ccList);
    	JSONObject contentDetail = new JSONObject();
		contentDetail.put("id",taskMainDO.getId());
		contentDetail.put("url","/task/taskDetail?id="+taskMainDO.getId());
		List<Long> toUsers = new ArrayList<>();
		toUsers.add(taskMainDO.getCreateBy());
		String content = "原因分析："+taskReplyDO.getReason()+"\r\n解决方案："+taskReplyDO.getSolution();
		noticeService.saveAndSendSocket("任务回复信息", content, taskMainDO.getId(), contentDetail.toString(),2L, ShiroUtils.getUserId(),toUsers);
		if (replySave > 0) {
			return R.ok();
		}
		return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/checkSave",method = RequestMethod.POST,apiTitle = "提交验收")
    @ApiOperation("提交验收")
    public R checkSave(@ApiParam(value = "验收信息",required = true) TaskReplyDO taskReplyDO
//                       ,@ApiParam(value = "抄送人",required = false) @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList
                       ){
        	TaskMainDO taskMainDO = taskMainService.get(taskReplyDO.getTaskid());
        	if (taskMainService.nonWaitingCheck(taskMainDO.getStatus())) {
				return R.error(messageSourceHandler.getMessage("task.status.nonWaitingCheck",null));
    		}
        	if (!ShiroUtils.isUser(taskMainDO.getCreateBy())) {
				return R.error(messageSourceHandler.getMessage("task.nonCreateUser",null));
    		}
        	Long id = taskReplyDO.getTaskid();
    		List<Map<String, Object>> dealList = taskMainService.getTaskReplyInfo(id, Constant.REPLY_DEAL);
        	if (dealList.size()>0) {
				taskReplyDO.setDealId(Integer.parseInt(dealList.get(0).get("id").toString()));
				taskMainService.checkSave(taskReplyDO/* ,ccList */);
				return R.ok();
			}
        	return R.error();
    }
    
    @EvApiByToken(value = "/apis/taskMain/checkDetail",method = RequestMethod.POST,apiTitle = "验收详情")
    @ApiOperation("验收详情(调用暂存编辑时使用)")
    public R checkDetail(@ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "")Long id){
        try {
        	Map<String,Object> result = Maps.newHashMapWithExpectedSize(1);
    		List<Map<String, Object>> checkList = taskMainService.getTaskReplyInfo(id,Constant.REPLY_CHECK);
    		if (checkList.size()>0) {
    			Map<String, Object> check = checkList.get(0);
    			if (Objects.equals(Integer.parseInt(check.get("statusId").toString()), Constant.TS)) {
    				result.put("checkInfo", checkList.get(0));
    			}
			}
            return R.ok(result);
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/checkSaveTS",method = RequestMethod.POST,apiTitle = "暂存验收")
    @ApiOperation("暂存验收")
    public R checkSaveTS(@ApiParam(value = "验收信息",required = true) TaskReplyDO taskReplyDO
//                       ,@ApiParam(value = "抄送人",required = false) @RequestParam(value = "ccList",defaultValue = "",required = false) Long[] ccList
                       ){
        	TaskMainDO taskMainDO = taskMainService.get(taskReplyDO.getTaskid());
        	if (taskMainService.nonWaitingCheck(taskMainDO.getStatus())) {
				return R.error(messageSourceHandler.getMessage("task.status.nonWaitingCheck",null));
    		}
        	if (!ShiroUtils.isUser(taskMainDO.getCreateBy())) {
				return R.error(messageSourceHandler.getMessage("task.nonCreateUser",null));
    		}
        	taskReplyDO.setStatus(Constant.TS);
        	Long id = taskReplyDO.getTaskid();
    		List<Map<String, Object>> dealList = taskMainService.getTaskReplyInfo(id, Constant.REPLY_DEAL);
        	if (dealList.size()>0) {
				taskReplyDO.setDealId(Integer.parseInt(dealList.get(0).get("id").toString()));
				taskMainService.checkSave(taskReplyDO/* ,ccList */);
				return R.ok();
			}
            return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/remove",method = RequestMethod.POST,apiTitle = "删除任务单")
    @ApiOperation("删除任务单")
    public R remove(@ApiParam(value = "任务单主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
    	TaskMainDO taskMainDO = taskMainService.get(id);
    	if (taskMainService.nonTS(taskMainDO.getStatus())) {
			return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
		}
    	if (!ShiroUtils.isUser(taskMainDO.getCreateBy())) {
			return R.error(messageSourceHandler.getMessage("task.nonCreateUser",null));
		}
    	if(taskMainService.remove(id)>0){
			Integer[] assocTypes = { Constant.HELD_PERSON, Constant.CC_PERSON, Constant.CHECK_PERSON };
			Long[] ids = { id };
    		taskMainService.removeSatellite(ids, assocTypes, Constant.TASK_APPEARANCE_IMAGE);
            return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除任务单")
    @ApiOperation("批量删除任务单")
    public R remove(@ApiParam(value = "任务单主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        for (Long id : ids) {
        	TaskMainDO taskMainDO = taskMainService.get(id);
        	if (taskMainService.nonTS(taskMainService.get(id).getStatus())) {
				return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
        	}
        	if (!ShiroUtils.isUser(taskMainDO.getCreateBy())) {
				return R.error(messageSourceHandler.getMessage("task.nonCreateUser",null));
    		}
		}
    	int batchRemove = taskMainService.batchRemove(ids);
    	if (batchRemove==ids.length) {
			Integer[] assocTypes = { Constant.HELD_PERSON, Constant.CC_PERSON, Constant.CHECK_PERSON };
    		taskMainService.removeSatellite(ids, assocTypes, Constant.TASK_APPEARANCE_IMAGE);
    		return R.ok();
		}
    	return R.error();
    }
    
    @EvApiByToken(value = "/apis/taskMain/backlog",method = RequestMethod.POST,apiTitle = "获取待办事项数量")
    @ApiOperation("获取待办事项数量")
    public R countBacklog(
    		
//                  @ApiParam(value = "用户Id",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)  Long userId,
//                  @ApiParam(value = "用户部门Id",required = false) @RequestParam(value = "deptId",defaultValue = "",required = false)  Long deptId
                 /* @ApiParam(value = "处理状态", required = false) @RequestParam(value = "status",defaultValue = "",required = false)  Integer status*/){
        Long userId = ShiroUtils.getUserId();
//    	String idPath = null;
//        if (Objects.nonNull(deptId)) {
//        	idPath = deptService.get(deptId).getIdPath();
//		}
        Map<String,Object> results = Maps.newHashMapWithExpectedSize(2);
        taskMainService.getUserWaitingCount(userId, Constant.WAITING_CHECK, null, results);
        taskMainService.getUserWaitingCount(userId, Constant.WAITING_DEAL, null, results);
        
        return  R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/taskMain/reventTurnToSend", method = RequestMethod.POST)
    @ApiOperation("将任务转派")
    public R reventTurnToSend(@ApiParam(value = "任务Id", required = true) @RequestParam(value = "taskId", defaultValue = "")Long taskId,
    		@ApiParam(value = "新的负责Id", required = true) @RequestParam(value = "userId", defaultValue = "")Long userId) {
    	TaskMainDO taskMainDO = taskMainService.get(taskId);
    	if (taskMainService.nonWaitingDeal(taskMainDO.getStatus())) {
			return R.error(messageSourceHandler.getMessage("task.status.nonWaitingDeal.turnToSend",null));
		}
    	if (!taskMainService.isDealBy(taskMainDO.getId())) {
			return R.error(messageSourceHandler.getMessage("task.nonHeldPerson.turnToSend",null));
		}
    	List<Map<String, Object>> dealList = taskMainService.getTaskReplyInfo(taskId, Constant.REPLY_DEAL);
    	if (dealList.size()>0) {
    		if (Objects.equals(dealList.get(0).get("statusId").toString(), Constant.TS.toString())) {
				return R.error(messageSourceHandler.getMessage("task.status.isTs.turnToSend",null));
    		}
		}
    	// Constant.HELD_PERSON 为责任人
    	TaskEmployeeDO taskEmployeeDO = new TaskEmployeeDO(taskId,userId,Constant.HELD_PERSON,0L);
    	if(taskEmployeeService.save(taskEmployeeDO)>0) {
    		return R.ok();
    	}
    	return R.error();
    }
    

}
