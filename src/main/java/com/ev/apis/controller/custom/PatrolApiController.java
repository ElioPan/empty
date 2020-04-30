package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.framework.utils.DatesUtil;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author gumingjie
 * @date 2019/9/04
 */
@Api(value = "/",tags = "巡检管理API")
@RestController
public class PatrolApiController {
    @Autowired
    private PatrolRecordService patrolRecordService;
    @Autowired
    private PatrolProjectService patrolProjectService;
    @Autowired
    private PatrolPlanService patrolPlanService;
    @Autowired
    private PatrolProjectDetailService patrolProjectDetailService;
    @Autowired
    private PatrolDetailService patrolDetailService;
    @Autowired
    private PatrolPlanDetailService patrolPlanDetailService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolProject/addProject",method = RequestMethod.POST)
    @ApiOperation("新增巡检标准信息")
    public R addProject(PatrolProjectDO project){
        int code = patrolProjectService.save(project);
        if(code>0){
            return R.ok();
        }else if(code == -1){
            return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
        }else if(code == -2){
            return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
        }
        return R.error();
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolProject/updateProject",method = RequestMethod.POST)
    @ApiOperation("修改巡检标准信息")
    public R updateProject(PatrolProjectDO project){
        if(project.getId()!=null){
            int code = patrolProjectService.update(project);
            if(code>0){
                return R.ok();
            }
            if(code == -1){
                return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
            }
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolProject/removeProject",method = RequestMethod.POST)
    @ApiOperation("删除巡检标准信息")
    public R removeProject(@ApiParam(value = "标准ID") @RequestParam(value = "projectId",defaultValue = "")  Long projectId){
        return patrolProjectService.removeProject(projectId);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolProject/batchRemoveProject",method = RequestMethod.POST)
    @ApiOperation("批量删除巡检标准信息")
    public R batchRemoveProject(@ApiParam(value = "标准ID数组") @RequestParam(value = "projectIds",defaultValue = "")  Long[] projectId){
        return patrolProjectService.batchRemoveProject(projectId);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolProject/addProjectDetail",method = RequestMethod.POST)
    @ApiOperation("新增巡检标准明细信息")
    public R addProjectDetail(PatrolProjectDetailDO detail,
                              @ApiParam(value = "标准ID") @RequestParam(value = "projectId",defaultValue = "")  Long projectId){
        return R.ok(this.patrolProjectDetailService.addProjectDetail(projectId,detail));
    }

    @EvApiByToken(value = "/apis/patrolProject/list",method = RequestMethod.POST)
    @ApiOperation("获取所有巡点检标准信息列表")
    public R projectList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "模糊查询") @RequestParam(value = "fuzzySearch",defaultValue = "",required = false) String fuzzySearch){
        return R.ok(this.patrolProjectService.listApi(pageno,pagesize,fuzzySearch));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolPlan/addPlan",method = RequestMethod.POST)
    @ApiOperation("提交巡点检计划信息")
    public R addPlan(PatrolPlanDO plan,
                     @ApiParam(value = "巡检明细数组，如[{\"deviceId\":3,\"projectId\":[4,5]},{\"deviceId\":4,\"projectId\":[7,8]}]") @RequestParam(value = "detailArray",defaultValue = "",required = false)  String detailArray){
    	// 创建新的计划设置计划为启动状态 Constant.STATE_START 
		plan.setStatus(ConstantForDevice.STATE_START);
		Map<String, Object> result = this.patrolPlanService.addPlan(plan, detailArray);
		if (result.size()>0) {
			return R.ok();
		}
        return R.error(messageSourceHandler.getMessage("patrol.empty.error",null));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolPlan/addPlanTS",method = RequestMethod.POST)
    @ApiOperation("暂存巡点检计划信息")
    public R addPlanTS(PatrolPlanDO plan,
                     @ApiParam(value = "巡检明细数组，如[{\"deviceId\":3,\"projectId\":[4,5]},{\"deviceId\":4,\"projectId\":[7,8]}]") @RequestParam(value = "detailArray",defaultValue = "",required = false)  String detailArray){
    	// 创建新的计划设置计划为暂存状态 146 
    	plan.setStatus(Constant.TS);
    	Map<String, Object> result = this.patrolPlanService.addPlan(plan, detailArray);
		if (result.size()>0) {
			return R.ok();
		}
        return R.error(messageSourceHandler.getMessage("patrol.empty.error",null));
    }


    @EvApiByToken(value = "/apis/patrolPlan/startUsing", method = RequestMethod.POST)
    @ApiOperation("启用——巡点检计划")
    @Transactional(rollbackFor = Exception.class)
    public R startUsing(@ApiParam(value = "计划id", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return patrolPlanService.disposeStartUsing(ids);
    }

    @EvApiByToken(value = "/apis/patrolPlan/forbidden", method = RequestMethod.POST)
    @ApiOperation("禁用——巡点检计划")
    @Transactional(rollbackFor = Exception.class)
    public R forbidden(@ApiParam(value = "计划id", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return patrolPlanService.disposeForbidden(ids);
    }


    @EvApiByToken(value = "/apis/patrolPlan/planList",method = RequestMethod.POST)
    @ApiOperation("获取用户的巡点检计划列表")
    public R planList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                      @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                      @ApiParam(value = "巡检人ID") @RequestParam(value = "engineerId",defaultValue = "",required = false)  Long engineerId,
                      @ApiParam(value = "计划状态(129启用；131已完成；不传默认查询所有)") @RequestParam(value = "status",defaultValue = "0")  int status,
                      @ApiParam(value = "计划名称") @RequestParam(value = "planName",defaultValue = "",required = false)String planName,
                      @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                      @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
                      @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                      @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
//                      @ApiParam(value = "类型（我发起的事件1；我办理的事件2；）",required = false) @RequestParam(value = "checkType",defaultValue = "",required = false) Integer checkType,
//                      @ApiParam(value = "用户部门ID",required = false) @RequestParam(value = "deptId",defaultValue = "",required = false) Long deptId,
//                      @ApiParam(value = "用户ID",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)Long userId
                      ){
    	Long userId = ShiroUtils.getUserId();
		Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.patrolPlanService.planList(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
		params.put("engineerId", engineerId);
		params.put("planName", StringUtils.sqlLike(planName));
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("status", status);
//    	if (deptId!=null&&deptService.get(deptId)!=null) {
//    		String idPath = deptService.get(deptId).getIdPath();
//    		params.put("deptId",idPath);
//		}
        params.put("userId",userId);
		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMap();
		List<Map<String, Object>> data = this.patrolPlanService.planList(params);
		int total = this.patrolPlanService.countForMap(params);
		if ( data.size() > 0) {
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(data);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			results.put("data", dsRet);
		}
		return R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/addRecord",method = RequestMethod.POST)
    @ApiOperation("提交设备巡点检记录信息")
    public R addRecord(PatrolRecordDO record,
//	            @ApiParam(value = "巡检结果",required = true) @RequestParam(value = "content",defaultValue = "")  String content,
	            @ApiParam(value = "巡检明细数组，如[{\"deviceId\":3,\"projectId\":4,\"result\":1,\"detailContent\":\"电路正常\"}]",required = true) @RequestParam(value = "detailArray",defaultValue = "")  String detailArray,
	            @ApiParam(value = "上传签到图片") @RequestParam(value = "signInImage",defaultValue = "",required = false) String[] signInImage,
	            @ApiParam(value = "上传结果图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage
            ){
//    	if (!checkSubmit(record.getId())) {
//			return R.error("用户错误");
//		}
        //将任务设置为已完成
        record.setStatus(ConstantForDevice.STATE_STOP_OVER);
        JSONArray jsonArray = JSON.parseArray(detailArray);
        for(int j = 0; j < jsonArray.size(); j ++) {
        	JSONObject jsonObject = jsonArray.getJSONObject(j);
        	int result = Integer.parseInt(jsonObject.getOrDefault("result",0).toString());
        	if (result==0) {
                return R.error(messageSourceHandler.getMessage("patrol.empty.result",null));
			}
        }
        return R.ok( this.patrolRecordService.saveRecord(record, detailArray, signInImage, taglocationappearanceImage));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/addRecordTS",method = RequestMethod.POST)
    @ApiOperation("暂存设备巡点检记录信息")
    public R addRecordTS(PatrolRecordDO record,
//	            @ApiParam(value = "巡检结果",required = true) @RequestParam(value = "content",defaultValue = "")  String content,
	            @ApiParam(value = "巡检明细数组，如[{\"deviceId\":3,\"projectId\":4,\"result\":1,\"detailContent\":\"电路正常\"}]",required = true) @RequestParam(value = "detailArray",defaultValue = "")  String detailArray,
	            @ApiParam(value = "上传签到图片") @RequestParam(value = "signInImage",defaultValue = "",required = false) String[] signInImage,
	            @ApiParam(value = "上传结果图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage
            ){
//    	if (!checkSubmit(record.getId())) {
//			return R.error("用户错误");
//		}
        //将任务设置为暂存
        record.setStatus(Constant.TS);
        return R.ok( this.patrolRecordService.saveRecord(record, detailArray, signInImage, taglocationappearanceImage));
    }
    
//    @EvApiByToken(value = "/apis/patrolPlan/getNoticeListByPatrolPlan",method = RequestMethod.POST)
//    @ApiOperation("查看当前时间段内的所派发的关于巡检任务的通知单列表")
//    public R getNoticeListByPatrolPlan(
//    		@ApiParam(value = "用户部门ID",required = false) @RequestParam(value = "deptId",defaultValue = "",required = false) Long deptId,
//            @ApiParam(value = "用户ID") @RequestParam(value = "userId",defaultValue = "",required = false)Long userId,
//            @ApiParam(value = "类型（我发起的事件1；我办理的事件2；）") @RequestParam(value = "checkType",defaultValue = "",required = false) Integer checkType){
//        Map<String,Object> results = Maps.newHashMap();
//        List<Map<String,Object>>data=this.patrolPlanService.getNoticeList(deptId,userId,checkType);
//        if (data.size()==0) {
//			return R.ok("当前时间段内并无通知");
//		}
//        results.put("data",data);
//        return R.ok(results);
//    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/addRecordByPlan",method = RequestMethod.POST)
    @ApiOperation("计划生成工单（通过通知单id与计划id自动生成一张巡检记录信息）")
    public R addRecordByPlan(@ApiParam(value = "通知单Id",required = true) @RequestParam(value = "inFormId",defaultValue = "") Long inFormId,
    		@ApiParam(value = "计划单Id",required = true) @RequestParam(value = "patrolPlanId",defaultValue = "") Long patrolPlanId
                 ){
    	Map<String,Object> param = Maps.newHashMapWithExpectedSize(2);
    	param.put("informId", inFormId);
    	param.put("planId", patrolPlanId);
    	Map<String,Object> results = Maps.newHashMap();
    	List<PatrolRecordDO> list = patrolRecordService.list(param);
    	if (list.size()>0) {
    		PatrolRecordDO patrolRecordDO = list.get(0);
    		results.put("id", patrolRecordDO.getId());
			return R.ok(results);
		}
		results = this.patrolRecordService.addRecordByPlan(patrolPlanId,new Date(),new Date());
        return R.ok(results);
    }
    
    @EvApiByToken(value = "/apis/patrolRecord/recordList",method = RequestMethod.POST)
    @ApiOperation("获取用户的巡点检记录列表")
    public R recordList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                      @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                      @ApiParam(value = "巡检人") @RequestParam(value = "engineerId",defaultValue = "",required = false)  Long engineerId,
                      @ApiParam(value = "任务状态(56待处理；不传默认所有)") @RequestParam(value = "status",defaultValue = "",required = false)  Integer status,
                      @ApiParam(value = "任务状态（多标签）（待处理：56）") @RequestParam(value = "singleStatus",defaultValue = "",required = false)  Integer singleStatus,
                      @ApiParam(value = "是否显示暂存(1为不显示)") @RequestParam(value = "isShowTs",defaultValue = "",required = false)  Integer isShowTs,
                      @ApiParam(value = "巡检单号") @RequestParam(value = "workOrderno",defaultValue = "",required = false)  String workOrderno,
//                      @ApiParam(value = "类型（我发起的事件1；我办理的事件2；）",required = false) @RequestParam(value = "checkType",defaultValue = "",required = false) Integer checkType,
//                      @ApiParam(value = "用户部门ID",required = false) @RequestParam(value = "deptId",defaultValue = "",required = false) Long deptId,
//                      @ApiParam(value = "用户ID",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)Long userId
                        @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                        @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order
    ){
    	Long userId = ShiroUtils.getUserId();
        Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.patrolRecordService.recordList(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("engineerId",engineerId);
        params.put("status",status);
        params.put("singleStatus",singleStatus);
        params.put("workOrderno", workOrderno);
//        params.put("checkType",checkType);
//    	if (deptId!=null&&deptService.get(deptId)!=null) {
//    		String idPath = deptService.get(deptId).getIdPath();
//    		params.put("deptId",idPath);
//		}
        if (Objects.isNull(singleStatus)) {
        	params.put("userId",userId);
		}
        if (Objects.nonNull(singleStatus)) {
        	params.put("user",userId);
		}
        params.put("isShowTs",isShowTs);
        params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		int total = patrolRecordService.countForMap(params);
//        int noticeCount=0;
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String, Object>> datas = this.patrolRecordService.recordList(params);
       /*
        List<Map<String,Object>> noticeList = patrolPlanService.getNoticeList(deptId,userId,checkType);
        if (noticeList.size()>0) {
        	if (status==0||status==Constant.WAITIN_GDEAL) {
        		noticeCount = patrolPlanService.getNoticeListCount(params);
        		for (Map<String, Object> map : noticeList) {
        			map.put("reportStatusName", "待处理");
        			map.put("reportStatusId", Constant.WAITINGDEAL);
        			datas.add(map);
        		}
        	}
        	Collections.sort(datas,new Comparator<Map<String,Object>>() {
        		@Override
        		public int compare(Map<String,Object>arg0,Map<String,Object>arg1) {
        			return arg1.get("sortTime").toString().compareTo(arg0.get("sortTime").toString()) ;
        		}
        	});
		}
		*/
        if(datas!=null && datas.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(datas);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return R.ok(results);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/closeRecord",method = RequestMethod.POST)
    @ApiOperation("关闭巡检任务单")
    public R closeRecord(@ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id,
                          @ApiParam(value = "关闭原因",required = true) @RequestParam(value = "closeReason",defaultValue = "")  String closeReason
                       ){
    	int closeRecord = patrolRecordService.closeRecord(id,closeReason);
    	if (closeRecord>0) {
    		return R.ok();
		}
    	if (closeRecord==-1) {
            return R.error(messageSourceHandler.getMessage("patrol.time.isOut",null));
		}
    	return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/batchCloseRecord",method = RequestMethod.POST)
    @ApiOperation("批量关闭巡检任务单")
    public R batchCloseRecord(@ApiParam(value = "任务ID数组",required = true) @RequestParam(value = "ids",defaultValue = "")  Long[] ids,
                          @ApiParam(value = "关闭原因",required = true) @RequestParam(value = "closeReason",defaultValue = "")  String closeReason
                       ){
    	int closeRecord = patrolRecordService.closeRecord(ids,closeReason);
    	if (closeRecord>0) {
    		return R.ok();
		}
    	if (closeRecord==-1) {
            return R.error(messageSourceHandler.getMessage("patrol.time.isOut",null));
		}
    	return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/handleRecord",method = RequestMethod.POST)
    @ApiOperation("填写巡检任务单")
    public R handleRecord(@ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id,
                          @ApiParam(value = "巡检结果",required = true) @RequestParam(value = "content",defaultValue = "")  String content,
                       @ApiParam(value = "巡检明细数组，如[{\"id\":24,\"result\":1,\"content\":\"电路正常\"}]",required = true) @RequestParam(value = "detailArray",defaultValue = "")  String detailArray,
                       @ApiParam(value = "上传签到图片") @RequestParam(value = "signInImage",defaultValue = "",required = false) String[] signInImage,
                       @ApiParam(value = "上传结果图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage){
        PatrolRecordDO record = this.patrolRecordService.get(id);
        if (record.getEndTime().before(new Date())) {
            return R.error(messageSourceHandler.getMessage("patrol.time.isOut.write",null));
		}
        if (record.getStartTime().after(new Date())) {
            return R.error(messageSourceHandler.getMessage("patrol.time.nonAchieve.write",null));
		}
    	if (!ShiroUtils.isUser(record.getEngineerId())) {
            return R.error(messageSourceHandler.getMessage("patrol.nonHeldPerson.patrol",null));
		}
    	List<PatrolDetailDO> parseArray = JSON.parseArray(detailArray, PatrolDetailDO.class);
    	for (PatrolDetailDO patrolDetailDO : parseArray) {
			if (Objects.isNull(patrolDetailDO.getResult())) {
                return R.error(messageSourceHandler.getMessage("patrol.empty.result",null));
			}
		}
        //设置任务状态为已完成
        record.setStatus(ConstantForDevice.STATE_STOP_OVER);
        return R.ok( this.patrolRecordService.saveHandleRecord(content, detailArray, signInImage, taglocationappearanceImage, record));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/handleRecordTS",method = RequestMethod.POST)
    @ApiOperation("暂存填写巡检任务单")
    public R handleRecordTS(@ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id,
                          @ApiParam(value = "巡检结果",required = true) @RequestParam(value = "content",defaultValue = "")  String content,
                       @ApiParam(value = "巡检明细数组，如[{\"id\":24,\"result\":1,\"content\":\"电路正常\"}]",required = true) @RequestParam(value = "detailArray",defaultValue = "")  String detailArray,
                       @ApiParam(value = "上传签到图片") @RequestParam(value = "signInImage",defaultValue = "",required = false) String[] signInImage,
                       @ApiParam(value = "上传结果图片") @RequestParam(value = "taglocationappearanceImage",defaultValue = "",required = false) String[] taglocationappearanceImage){
        PatrolRecordDO record = this.patrolRecordService.get(id);
        if (record.getEndTime().before(new Date())) {
            return R.error(messageSourceHandler.getMessage("patrol.time.isOut.write",null));
		}
        if (!ShiroUtils.isUser(record.getEngineerId())) {
            return R.error(messageSourceHandler.getMessage("patrol.nonHeldPerson.patrol",null));
		}
        //设置任务状态为暂存
        record.setStatus(ConstantForDevice.WAITING_DEAL);
        return R.ok( this.patrolRecordService.saveHandleRecord(content, detailArray, signInImage, taglocationappearanceImage, record));
    }


//    @EvApiByToken(value = "/apis/patrolRecord/checkRecord",method = RequestMethod.POST)
//    @ApiOperation("验收巡检任务单")
//    public R checkRecord(PatrolCheckDO check,
//                         @ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "",required = true)  Long id){
//        Map<String,Object> results;
//        PatrolRecordDO record = this.patrolRecordService.get(id);
//        if (Objects.equals(Constant.TS, record.getStatus())) {
//			return R.error("状态错误不允许验收");
//		}
//        results = this.patrolRecordService.checkRecord(check,record);
//        return R.ok(results);
//    }
    
//    @EvApiByToken(value = "/apis/patrolRecord/checkRecordTS",method = RequestMethod.POST)
//    @ApiOperation("暂存验收巡检任务单")
//    public R checkRecordTS(PatrolCheckDO check,
//                         @ApiParam(value = "任务ID",required = true) @RequestParam(value = "id",defaultValue = "",required = true)  Long id){
//        Map<String,Object> results = Maps.newHashMap();
//        PatrolRecordDO record = this.patrolRecordService.get(id);
//        if (Objects.equals(Constant.TS, record.getStatus())) {
//			return R.error("状态错误不允许验收");
//		}
//        // 设置验收结果为暂存
//        check.setResult(Constant.TS);
//        results = this.patrolRecordService.checkRecord(check,record);
//        return R.ok(results);
//    }

    @EvApiByToken(value = "/apis/patrolRecord/devicePatrolRecordList",method = RequestMethod.POST)
    @ApiOperation("获取当前设备巡点检记录列表")
    public R devicePatrolRecordList(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "设备ID",required = true) @RequestParam(value = "deviceId",defaultValue = "")  Long deviceId,
                  @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                  @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
                  @ApiParam(value = "巡检结果(1为正常,2为异常)") @RequestParam(value = "result",defaultValue = "",required = false)  Integer result
                  ){
        Map<String,Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(6);
        params.put("deviceId",deviceId);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("result",result);
        params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		List<Map<String,Object>> datas=this.patrolRecordService.listApi(params);
        int total = patrolDetailService.countForMap(params);
        if( datas.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(datas);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return R.ok(results);
    }
    
    @EvApiByToken(value = "/apis/patrolRecord/devicePatrolRecordListIncludeDetail",method = RequestMethod.POST)
    @ApiOperation("获取当前设备巡点检记录列表(含明细)")
    public R devicePatrolRecordListIncludeDetail(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "设备ID",required = true) @RequestParam(value = "deviceId",defaultValue = "")  Long deviceId,
                  @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
                  @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
                  @ApiParam(value = "巡检结果(1为正常,2为异常)") @RequestParam(value = "result",defaultValue = "",required = false)  Integer result
                  ){
        Map<String,Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(6);
        params.put("deviceId",deviceId);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("result",result);
        params.put("recordStatus", ConstantForDevice.STATE_STOP_OVER);
        params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		List<Map<String,Object>> datas=this.patrolDetailService.devicePatrolListForMap(params);
        int total = patrolDetailService.devicePatrolCountForMap(params);
        if( datas.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(datas);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/patrolPlan/planDetail",method = RequestMethod.POST)
    @ApiOperation("获取巡点检计划详情")
    public R planDetail(@ApiParam(value = "计划ID",required = true) @RequestParam(value = "id",defaultValue = "") Long id){
        return R.ok( this.patrolPlanService.planDetail(id));
    }

    @EvApiByToken(value = "/apis/patrolRecord/recordDetail",method = RequestMethod.POST)
    @ApiOperation("获取巡点检记录详情")
    public R recordDetail(@ApiParam(value = "记录ID",required = true) @RequestParam(value = "id",defaultValue = "") Long id){
        return R.ok(this.patrolRecordService.recordDetail(id));
    }
    
//    @EvApiByToken(value = "/apis/patrolRecord/recordCheckDetail",method = RequestMethod.POST)
//    @ApiOperation("获取巡点检记录验收详情")
//    public R recordCheckDetail(@ApiParam(value = "记录ID",required = true) @RequestParam(value = "id",defaultValue = "") Long id){
//        return R.ok(this.patrolRecordService.recordCheckDetail(id));
//    }
    
    @EvApiByToken(value = "/apis/patrolRecord/backlog",method = RequestMethod.POST,apiTitle = "获取待处理数量")
    @ApiOperation("获取待处理数量")
    public R countBacklog(
//                  @ApiParam(value = "用户Id",required = false) @RequestParam(value = "userId",defaultValue = "",required = false)  Long userId,
//                  @ApiParam(value = "用户部门Id",required = false) @RequestParam(value = "deptId",defaultValue = "",required = false)  Long deptId
                  /*@ApiParam(value = "待处理状态(56)", required = true) @RequestParam(value = "status",defaultValue = "",required = true)  Integer status*/){
        Long userId = ShiroUtils.getUserId();
        Long deptId = null;
    	return  R.ok(this.patrolRecordService.getBacklog(userId, deptId));
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolPlan/removePlan",method = RequestMethod.POST)
    @ApiOperation("删除巡检计划")
    public R removePatrolPlan(@ApiParam(value = "计划ID") @RequestParam(value = "id",defaultValue = "")  Long planId){
    	PatrolPlanDO patrolPlanDO = patrolPlanService.get(planId);
    	Date now = new Date();
    	if (now.after(DatesUtil.getDayEndTime(patrolPlanDO.getCreateTime()))) {
            return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled",null));
		}
    	if (!ShiroUtils.isUser(patrolPlanDO.getCreateBy())) {
            return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
		}
    	if (patrolPlanService.remove(planId)>0) {
    		patrolPlanDetailService.remove(planId);
    		return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolPlan/batcnRemovePlan",method = RequestMethod.POST)
    @ApiOperation("批量删除巡检计划")
    public R batcnRemovePatrolPlan(@ApiParam(value = "计划ID数组") @RequestParam(value = "ids",defaultValue = "")  Long[] planIds){
        Date now = new Date();
        for (Long planId : planIds) {
        	PatrolPlanDO patrolPlanDO = patrolPlanService.get(planId);
        	if (now.after(DatesUtil.getDayEndTime(patrolPlanService.get(planId).getCreateTime()))) {
                return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled",null));
        	}
        	if (!ShiroUtils.isUser(patrolPlanDO.getCreateBy())) {
                return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
    		}
        }
    	if (patrolPlanService.batchRemove(planIds)>0) {
        	patrolPlanDetailService.batchRemove(planIds);
    		return R.ok();
        }
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/removeRecord",method = RequestMethod.POST)
    @ApiOperation("删除巡检记录")
    public R removePatrolRecord(@ApiParam(value = "记录ID") @RequestParam(value = "id",defaultValue = "")  Long recordId){
    	PatrolRecordDO patrolRecordDO = patrolRecordService.get(recordId);
        Long status = patrolRecordDO.getStatus();
    	if (!Objects.equals(Constant.TS, status)) {
            return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
    	}
    	if (!ShiroUtils.isUser(patrolRecordDO.getCreateBy())) {
            return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
		}
    	if (Objects.equals(Constant.TS, status)) {
	    	if (patrolRecordService.remove(recordId)>0) {
	        	patrolDetailService.remove(recordId);
	    		return R.ok();
	        }
    	}
        return R.error();
    }
    
    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/patrolRecord/batchRemoveRecord",method = RequestMethod.POST)
    @ApiOperation("批量删除巡检记录")
    public R batchRemovePatrolRecord(@ApiParam(value = "记录ID数组") @RequestParam(value = "ids",defaultValue = "")  Long[] recordIds){
    	for (Long recordId : recordIds) {
    		PatrolRecordDO patrolRecordDO = patrolRecordService.get(recordId);
    		if (!Objects.equals(Constant.TS, patrolRecordDO.getStatus())) {
                return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
			}
    		if (!ShiroUtils.isUser(patrolRecordDO.getCreateBy())) {
                return R.error(messageSourceHandler.getMessage("common.nonCreateUser.delete.disabled",null));
    		}
		}
    	if (patrolRecordService.batchRemove(recordIds)>0) {
        		patrolDetailService.batchRemove(recordIds);
    		return R.ok();
        }
        return R.error();
    }
    
    /***************************************************************************************************************************/
    @EvApiByToken(value = "/apis/patrolRecord/recordListForBoard",method = RequestMethod.POST)
    @ApiOperation("获取用户的巡点检记录列表(看板)")
    public R recordListForBoard(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                      @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                      @ApiParam(value = "获取看板内待处理数量",required = true) @RequestParam(value = "statusForBoard",defaultValue = "56") Integer status
                      ){
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("statusForBoard",status);
        params.put("sortByEndTime",true);
        params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
        Map<String,Object> results = Maps.newHashMap();
    	List<Map<String,Object>> datas=this.patrolDetailService.devicePatrolListForMap(params);
        int total = patrolDetailService.devicePatrolCountForMap(params);
        if( datas.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(datas);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return R.ok(results);
    }

}
