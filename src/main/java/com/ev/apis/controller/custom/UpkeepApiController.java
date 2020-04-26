package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.domain.UpkeepCheckDO;
import com.ev.custom.domain.UpkeepPlanDO;
import com.ev.custom.domain.UpkeepProjectDO;
import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.custom.service.*;
import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.DeptDO;
import com.ev.framework.il8n.MessageSourceHandler;
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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yunian
 * @date 2018/6/17
 */
@Api(value = "/",tags = "设备保养管理API")
@RestController
public class UpkeepApiController {
    @Autowired
    private UpkeepRecordService upkeepRecordService;
    @Autowired
    private UpkeepPlanService upkeepPlanService;
    @Autowired
    private UpkeepProjectService upkeepProjectService;
    @Autowired
    private UpkeepRecordProjectService upkeepRecordProjectService;
    @Autowired
    private UpkeepRecordPartService upkeepRecordPartService;
    @Autowired
    private UpkeepCheckService  upkeepCheckService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @EvApiByToken(value = "/apis/upkeepPlan/startUsing", method = RequestMethod.POST)
    @ApiOperation("启用——设备保养计划")
    @Transactional(rollbackFor = Exception.class)
    public R startUsing(@ApiParam(value = "保养计划id", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return upkeepPlanService.disposeStartUsing(ids);
    }

    @EvApiByToken(value = "/apis/upkeepPlan/forbidden", method = RequestMethod.POST)
    @ApiOperation("禁用——设备保养计划")
    @Transactional(rollbackFor = Exception.class)
    public R forbidden(@ApiParam(value = "保养计划id", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return upkeepPlanService.disposeForbidden(ids);
    }




        @EvApiByToken(value = "/apis/upkeepPlan/newPlanAndChangePlan", method = RequestMethod.POST)
    @ApiOperation("设备保养计划——暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R addNewPlanAndChangePlan(UpkeepPlanDO planDo,
                                     @ApiParam(value = "保养项目ID数组,id用','分隔，如4,5", required = true) @RequestParam(value = "projectIds", defaultValue = "") String projectIds,
                                     @ApiParam(value = "备品备件ID数组，如{\"dataList\":[{\"partId\":3,\"amount\":5,\"spartUnit\":\"个\",\"spartPrice\":20,\"spartSum\":1000,\"remark\":\"备注\"}]}", required = true) @RequestParam(value = "partIdArray", defaultValue = "") String partIdArray) {

        if (Objects.isNull(planDo.getId())) {
            Map<String, Object> results ;
            //保养计划保存后是暂存：146暂存
            planDo.setStatus(Constant.TS);
            results = this.upkeepPlanService.savePlan(planDo, projectIds, partIdArray);
            return R.ok(results);
        } else {
            //修改暂存
            //验证是否能够修改
            UpkeepPlanDO upkeepPlanDO = upkeepPlanService.get(planDo.getId());
            if (upkeepPlanDO != null) {
                if (Objects.equals(Constant.TS, upkeepPlanDO.getStatus())) { //146计划暂存
                    upkeepPlanService.savePlanChangeAndSbmit( planDo,  projectIds,  partIdArray, 0);
                    return  R.ok();
                }
                //"保养计划已启动，不能修改！"
                return R.error(messageSourceHandler.getMessage("common.plan.noAllowChange",null));
            }
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }

    }

    @EvApiByToken(value = "/apis/upkeepPlan/sbmitOfNewAndChange", method = RequestMethod.POST)
    @ApiOperation("提交保养计划（2.新增后直接提交；1.未修改/修改后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R sbmitOfNewAndChange(UpkeepPlanDO planDo,
                                     @ApiParam(value = "保养项目ID数组,id用','分隔，如4,5", required = true) @RequestParam(value = "projectIds", defaultValue = "") String projectIds,
                                     @ApiParam(value = "备品备件ID数组，如{\"dataList\":[{\"partId\":3,\"amount\":5,\"spartUnit\":\"个\",\"spartPrice\":20,\"spartSum\":1000,\"remark\":\"备注\"}]}", required = true) @RequestParam(value = "partIdArray", defaultValue = "") String partIdArray) {

        if (Objects.isNull(planDo.getId())) {
            //保养计划提交后就是启用：129启用
            planDo.setStatus(Constant.STATE_START);
            Map<String, Object> results = this.upkeepPlanService.savePlan(planDo, projectIds, partIdArray);
            return R.ok(results);
        } else {
            //修改后提交
            //验证是否能够修改
            UpkeepPlanDO upkeepPlanDO = upkeepPlanService.get(planDo.getId());
            if (upkeepPlanDO != null) {
                if (Objects.equals(Constant.TS, upkeepPlanDO.getStatus())) {
                    upkeepPlanService.savePlanChangeAndSbmit( planDo,  projectIds,  partIdArray, 1);
                    return  R.ok();
                }
                //"保养计划已提交/已结束，不能重复提交！"
                return R.error(messageSourceHandler.getMessage("common.upkeep.submitNotAllow",null));
            }
            //"所传入的ID数据不存在！"
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }


    @EvApiByToken(value = "/apis/upkeepPlan/getOnePlanDetail", method = RequestMethod.POST)
    @ApiOperation("获取保养计划详情")
    public R onePlanDetail(@ApiParam(value = "保养计划id", required = true) @RequestParam(value = "planId", defaultValue = "") Long planId) {

        Map<String, Object> results = Maps.newHashMap();

        Map<String, Object> query = Maps.newHashMap();

        UpkeepPlanDO upkeepPlanDO = upkeepPlanService.get(planId);

        if(upkeepPlanDO!=null){
            //根据id获取plan头信息
            Map<String, Object> planMsg = upkeepPlanService.getPlanMsgById(planId);
            //获取项目信息
            List<Map<String, Object>> proMsg = upkeepPlanService.getProMsgById(planId);
            //获取备件信息
            List<Map<String, Object>> partMsg = upkeepPlanService.getPartMsgById(planId);

            if(planMsg.size()>0){
                query.put("planMsg",planMsg);
                query.put("proMsg",proMsg);
                query.put("partMsg",partMsg);
                results.put("data",query);
                return R.ok(results);
            }else{
                results.put("data","");
                return R.ok(results);
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }


    //1.提供功能：模糊查询当前用户保养计划列表（未执行排序靠前）

    @EvApiByToken(value = "/apis/upkeepPlan/planListForUser", method = RequestMethod.POST)
    @ApiOperation("获取保养计划列表")
    public R planListForUser(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                             @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                             @ApiParam(value = "负责人ID") @RequestParam(value = "engineerId", defaultValue = "", required = false) Long engineerId,
                             @ApiParam(value = "计划名称") @RequestParam(value = "name", defaultValue = "", required = false) String name,
                             @ApiParam(value = "计划开始时间") @RequestParam(value = "start_time", defaultValue = "", required = false) String startTime,
                             @ApiParam(value = "计划结束时间") @RequestParam(value = "end_time", defaultValue = "", required = false) String endTime,
                             @ApiParam(value = "部门id") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
                             @ApiParam(value = "计划状态") @RequestParam(value = "status", defaultValue = "", required = false) Long status,
                             @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                             @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {
        Map<String, Object> query = new HashMap<>();
        String idPath=null;
        if (null != deptId) {
            DeptDO deptDO = deptService.get(deptId);
            idPath = Objects.isNull(deptDO) ? null : deptDO.getIdPath();
        }

        Long lodwnUserId = ShiroUtils.getUserId();

        if (StringUtils.isNoneEmpty(sort)) {
            query.put("offset", 0);
            query.put("limit", 1);
            List<Map<String, Object>> maps = upkeepPlanService.getPlanListByUser(query);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                query.put("sort", sort);
                query.put("order", StringUtils.isOrder(order));
            }
        }

        query. put("idPath",idPath);
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);
        query.put("name", name);
        query.put("startTime", startTime);
        query.put("endTime", endTime);
        query.put("status", status);
        query.put("deletStatus", null);
        query.put("engineerId",engineerId);//engineerId 负责人

        //保养计划 暂存态只有创建人才可以看到。
        query.put("lodwnUserId", lodwnUserId);

        int count = upkeepPlanService.countOfList(query);

        List<Map<String, Object>> planLists = upkeepPlanService.getPlanListByUser(query);

        Map<String, Object> resultList = new HashMap<>();

        if ( planLists.size() > 0) {
            DsResultResponse resultPonse = new DsResultResponse() {{
                setDatas(planLists);
                setPageno(pageno);
                setPagesize(pagesize);
                setTotalRows(count);
                setTotalPages( ((count + pagesize - 1) / pagesize));
            }};
            resultList.put("data", resultPonse);
            return R.ok(resultList);
        } else {
            resultList.put("data", "");
            return R.ok(resultList);
        }
    }

    //2.提供功能：保养计划“删除”状态（将delet_status状态更新为1删除，同时将status 计划状态改为 已完成131）

    @EvApiByToken(value = "/apis/upkeepPlan/deletUpPlan", method = RequestMethod.POST)
    @ApiOperation("删除保养计划")
    public R deletPlan(@ApiParam(value = "保养计划ID", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id) {

        //1.查询出是否已定时发出工单，已发出的不允许删除，只能设置停止（已完成）
        Map<String, Object> query = Maps.newHashMap();
        query.put("planId", id);
        UpkeepPlanDO upkeepPlanDo = new UpkeepPlanDO() ;
        upkeepPlanDo.setDeletStatus(1L);
        upkeepPlanDo.setId(id);
        upkeepPlanDo.setStatus(Constant.STATE_STOP_OVER);//CSTATE_STOP_OVER   计划结束/完成

        Long lodwnUserId = ShiroUtils.getUserId();
        int counts = upkeepRecordService.count(query);
        UpkeepPlanDO upkeepPlanDO = upkeepPlanService.get(id);

        if(upkeepPlanDO!=null){
            if(Objects.equals(lodwnUserId,upkeepPlanDO.getCreateBy())&&counts==0){
                //不存在工单允许删除 做删除标记
                upkeepPlanService.update(upkeepPlanDo);
                return R.ok();
            }
            //单据已关联/当前登录人非创建人，不允许删除
            return R.error(messageSourceHandler.getMessage("common.plan.allowRemove",null));
        }
        return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
    }

    /**
     *  //2.提供功能：保养计划“删除”状态（将delet_status状态更新为1删除，同时将status 计划状态改为 已完成131）
     */
    @EvApiByToken(value = "/apis/upkeepPlan/deletLargeUpPlan", method = RequestMethod.POST)
    @ApiOperation("批量删除保养计划")
    @Transactional(rollbackFor = Exception.class)
    public R deletLargePlan(@ApiParam(value = "保养计划ID", required = true) @RequestParam(value = "id", required = true) Long[] id) {

        //1.查询出是否已定时发出工单，已发出的不允许删除，非创建人禁止删除

        Map<String, Object> query = Maps.newHashMap();

        if(id.length>0){
            for (int i=0;i<id.length;i++) {
                query.clear();
                query.put("planId", id[i]);

                Long lodwnUserId = ShiroUtils.getUserId();
                UpkeepPlanDO upkeepPlanDO = upkeepPlanService.get(id[i]);
                int counts = upkeepRecordService.count(query);
                Long crater=upkeepPlanDO.getCreateBy();
                if (!(Objects.equals(lodwnUserId,crater))||counts!=0) {
                    //单据已关联/当前登录人非创建人，不允许删除
                    return R.error(messageSourceHandler.getMessage("common.plan.allowRemove",null));
                }
            }
            Map<String,Object>  map= new HashMap<>();
            map.put("ids",id);
            upkeepPlanService.deletOfPlan(map);
            return R.ok();

        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.dateIsNon",null));
        }
    }


    //4.提供功能：添加 保养项目+方法的接口

    @EvApiByToken(value = "/apis/upkeepPlan/addProjects", method = RequestMethod.POST)
    @ApiOperation("新增设备保养项目和方法")
    @Transactional
    public R addProjectMotheed(UpkeepProjectDO upkeepProjectDO) {
        return upkeepProjectService.addProject(upkeepProjectDO);
    }

    /*
       5.提供功能： 模糊查询保养项目方法列表
     */
    @EvApiByToken(value = "/apis/upkeepPlan/findProjects", method = RequestMethod.POST)
    @ApiOperation("保养项目和方法列表")
    public R findProList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "设备类型id") @RequestParam(value = "device_tepy", defaultValue = "", required = false) Integer deviceTepy,
                         @ApiParam(value = "保养项目名字") @RequestParam(value = "name", defaultValue = "", required = false) String name,
                         @ApiParam(value = "保养方法") @RequestParam(value = "function", defaultValue = "", required = false) String function) {

        Map<String, Object> query = Maps.newHashMap();
        Map<String, Object> results = Maps.newHashMap();
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);
        query.put("deviceTepy", deviceTepy);
        query.put("name", name);
        query.put("functions", function);

        int count = upkeepProjectService.countOfList(query);
        List<Map<String, Object>> listPros = upkeepProjectService.findListsPros(query);
        DsResultResponse resultPonse = new DsResultResponse();
        if (listPros.size() > 0) {

            resultPonse.setDatas(listPros);
            resultPonse.setPageno(pageno);
            resultPonse.setPagesize(pagesize);
            resultPonse. setTotalRows(count);
            resultPonse.setTotalPages(((count + pagesize - 1) / pagesize));
            results.put("data", resultPonse);
            return R.ok(results);
        } else {
            results.put("data",resultPonse);
            return R.ok(results);
        }
    }

    @EvApiByToken(value = "/apis/upkeepPlan/removeProject", method = RequestMethod.POST)
    @ApiOperation("删除保养项目")
    @Transactional(rollbackFor = Exception.class)
    public R removeProject(@ApiParam(value = "保养项目ID", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        return upkeepProjectService.delete(ids);
    }

//======================================================================================================================================

    //提供功能：填写保养工单明细
    @EvApiByToken(value = "/apis/upkeepPlan/recordOfNoPlan", method = RequestMethod.POST)
    @ApiOperation("无计划保养工单——暂存/修改暂存")
    @Transactional(rollbackFor = Exception.class)
    public R recordOfNoPlan(UpkeepRecordDO upkeepRecordDO,
                            @ApiParam(value = "保养单下所有保养项目，如[{\"projectId\":3,\"manhour\":5,\"upkeepCost\":522,\"manHourCost\":100,\"result\":1(1正常，0异常),\"remark\":\"备注\"}]", required = true) @RequestParam(value = "projectArray", defaultValue = "") String projectArray,
                            @ApiParam(value = "所有备件明细，如[{\"partId\":3,\"spartAmount\":5,\"spartUnit\":\"个\",\"spartPrice\":20,\"spartSum\":1000,\"remark\":\"备注\"}]", required = true) @RequestParam(value = "partArray", defaultValue = "") String partArray) {

        if (Objects.isNull(upkeepRecordDO.getId())) {
            //新增暂存

                upkeepRecordDO.setResult(Constant.TS);
                R r = upkeepRecordService.saveRecorderOfNoPlan(upkeepRecordDO, projectArray, partArray,0);

                return r;
        } else {

            //修改暂存
            UpkeepRecordDO recordDO = upkeepRecordService.get(upkeepRecordDO.getId());
            if (recordDO != null) {
                if (Objects.equals(Constant.TS, recordDO.getResult())) {

                        R r = upkeepRecordService.saveRecorderOfNoPlan(upkeepRecordDO, projectArray, partArray,1);

                        return r;
                }
                //"工单状态非暂存不允许更改！"
                return R.ok(messageSourceHandler.getMessage("common.dailyReport.savChange",null));
            }
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));

        }
    }

    //提供功能：填写保养工单明细
    @EvApiByToken(value = "/apis/upkeepPlan/sumitNoPlanOfRecord", method = RequestMethod.POST)
    @ApiOperation("无计划保养工单——提交（新增直接提交；暂存态提交）")
    @Transactional(rollbackFor = Exception.class)
    public R sumitNoPlanOfRecord(UpkeepRecordDO upkeepRecordDO,
                            @ApiParam(value = "保养单下所有保养项目，如[{\"projectId\":3,\"manhour\":5,\"upkeepCost\":522,\"manHourCost\":100,\"result\":1(1正常，0异常),\"remark\":\"备注\"}]", required = true) @RequestParam(value = "projectArray", defaultValue = "") String projectArray,
                            @ApiParam(value = "所有备件明细，如[{\"partId\":3,\"spartAmount\":5,\"spartUnit\":\"个\",\"spartPrice\":20,\"spartSum\":1000,\"remark\":\"备注\"}]", required = true) @RequestParam(value = "partArray", defaultValue = "") String partArray) {

        if (Objects.isNull(upkeepRecordDO.getId())) {
            //新增+提交

                upkeepRecordDO.setResult(Constant.WAITING_CHECK);//待验收
            return upkeepRecordService.saveRecorderOfNoPlan(upkeepRecordDO, projectArray, partArray,0);
        } else {

            //修改暂存+提交
            UpkeepRecordDO recordDO = upkeepRecordService.get(upkeepRecordDO.getId());
            if (recordDO != null) {
                if (Objects.equals(Constant.TS, recordDO.getResult())) {

                        upkeepRecordDO.setResult(Constant.WAITING_CHECK);//待验收
                    return upkeepRecordService.saveRecorderOfNoPlan(upkeepRecordDO, projectArray, partArray,1);
                }
                //请勿重复提交
                return R.ok(messageSourceHandler.getMessage("common.dailyReport.submit",null));
            }
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId",null));
        }
    }


        /**
         * 1.根据前端传的当前登录人的ID去查找启动的计划ids，
         * 2.根据ids和当前时间 去查cus_notes 中是否有匹配的“消息”，
         * 3.如果有，根据计划id生成保养工单
         * 4.没有略过。
         * 5.以上做完后根据提供的查询条件提供upkeep_record 列表 ，未执行排前
         */
    @EvApiByToken(value = "/apis/upkeepPlan/addValidRecords", method = RequestMethod.POST)
    @ApiOperation("保养执行—'待处理+待验收+所有的'列表查询")
    public R addValidRecord(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "保养开始时间") @RequestParam(value = "start_time", defaultValue = "", required = false) String start_time,
                            @ApiParam(value = "保养结束时间") @RequestParam(value = "end_time", defaultValue = "", required = false) String end_time,
                            @ApiParam(value = "保养负责人Id") @RequestParam(value = "engineer_id", defaultValue = "", required = false) Long engineerId,
                            @ApiParam(value = "部门id") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
                            @ApiParam(value = "保养状态") @RequestParam(value = "resultId", defaultValue = "", required = false) Long resultId,
                            @ApiParam(value = "处理状态（多标签）（待处理：56；待验收：57）") @RequestParam(value = "singleStatus",defaultValue = "",required = false)  Integer singleStatus,
                            @ApiParam(value = "设备名称/编码（模糊）") @RequestParam(value = "deviceName", defaultValue = "", required = false) String deviceName,
                            @ApiParam(value = "验收结果字段") @RequestParam(value = "recordResultId",defaultValue = "",required = false)  Long recordResultId,
                            @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                            @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {

        Map<String, Object> query = new HashMap<>();
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> total = new HashMap<>();
            Long manId=ShiroUtils.getUserId();
        //2.查询record工单列表
        String idPath = null;
        if (null != deptId) {
            DeptDO deptDO = deptService.get(deptId);
            idPath = Objects.isNull(deptDO)?null:deptDO.getIdPath();
        }
        if (StringUtils.isNoneEmpty(sort)) {
            query.put("offset", 0);
            query.put("limit", 1);
            List<Map<String, Object>> maps = upkeepRecordService.newOfListRecords(query);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                query.put("sort", sort);
                query.put("order", StringUtils.isOrder(order));
            }
        }
        query.put("idPath", idPath);
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);
        query.put("startTime", start_time);
        query.put("endTime", end_time);
        query.put("deviceName", deviceName);
        int count = 0;

        //如果singleStatus什么都不传，查询出包含暂存状态的所有单据
        if(Objects.isNull(singleStatus)){
            query.put("engineerId", engineerId);
            query.put("manId", manId);
            query. put("resultId", resultId);
        }
        //待处理
        if (Objects.nonNull(singleStatus) && Objects.equals(singleStatus, Constant.WAITING_DEAL)) {
            query.put("singleStatuss", singleStatus);
            //工单责任人当前登录者
            query.put("manIds", manId);
            //待处理中显示过期数据
            query.put("overTimeResult", 999);
        }
        //待验收   则返回保养计划创建人和
        if (Objects.nonNull(singleStatus) &&Objects.equals(singleStatus, Constant.WAITING_CHECK)) {
            query.put("singleStatus", singleStatus);
            //工单和计划创建人为当前登录者
            query.put("manIdss", manId);
        }
        //统计行数+总金额+总工时
        Map<String, Object> countListRecords = upkeepRecordService.countListRecords(query);

        String totalManHour = null;
        String totalCost = null;
        String totalManhourCost=null;
        String totalUpkeepCost=null;
        if (countListRecords.size() > 0) {
            count+= Integer.parseInt(countListRecords.get("count").toString());
            totalManHour = countListRecords.getOrDefault("totalManHour","0").toString();
            totalUpkeepCost = countListRecords.getOrDefault("totalUpkeepCost","0").toString();
            totalCost = countListRecords.getOrDefault("totalCost","0").toString();
            totalManhourCost=countListRecords.getOrDefault("totalManhourCost","0").toString();
        }
        //查询明细
        List<Map<String, Object>> recordLists = upkeepRecordService.newOfListRecords(query);

        if (recordLists.size() > 0) {
            total.put("totalManHour", totalManHour);
            total.put("totalCost", totalCost);
            total.put("totalManhourCost", totalManhourCost);
            total.put("totalUpkeepCost", totalUpkeepCost);
            total.put("datas", recordLists);
            total.put("pageno", pageno);
            total.put("totalRows", count);
            total.put("totalPages", (count + pagesize - 1) / pagesize);
            total.put("pagesize", pagesize);
            total.put("keyword", null);
            total.put("searchKey", null);

            results.put("data", total);
            return R.ok(results);
        } else {
            results.put("data", total);
            return R.ok(results);
        }
    }


    @EvApiByToken(value = "/apis/upkeepPlan/closeRecordOfPase", method = RequestMethod.POST)
    @ApiOperation("关闭/批量关闭 过期保养工单")
    @Transactional(rollbackFor = Exception.class)
    public R closeRecordOfPase(@ApiParam(value = "保养工单IDS", required = true) @RequestParam(value = "recordIds", defaultValue = "") Long[] recordIds,
                               @ApiParam(value = "关闭原因", required = true) @RequestParam(value = "closureReason", defaultValue = "") String closureReason) {
        return upkeepRecordService.closePaseOfRecord(recordIds,closureReason);
    }


    @EvApiByToken(value = "/apis/upkeepPlan/oneRecordDetail", method = RequestMethod.POST)
    @ApiOperation("保养单详情")
    public R recordOneDetail(@ApiParam(value = "保养单id", required = true) @RequestParam(value = "recordId") Long recordId) {
        Map<String, Object> query = Maps.newHashMap();
        query.put("id", recordId);

        //计算备件总费用

//        try{
//            int count = upkeepRecordPartService.dealWithSpareDetails(query);
//        }catch(Exception e){
//            e.getMessage();
//        }
        return  upkeepRecordService.getOneDetailOfRecord(query);
    }

    //提供功能：填写保养工单明细
    @EvApiByToken(value = "/apis/upkeepPlan/saveRecordDetail", method = RequestMethod.POST)
    @ApiOperation("暂存/修改暂存——保养工单")
    @Transactional(rollbackFor = Exception.class)
    public R saveRecordDetails(@ApiParam(value = "保养单id", required = true) @RequestParam(value = "record_id", defaultValue = "") Long id,
                               @ApiParam(value = "停机时长", required = true) @RequestParam(value = "down_hour", defaultValue = "") double downHour,
                               @ApiParam(value = "设备使用状况id", required = true) @RequestParam(value = "status", defaultValue = "") int status,
                               @ApiParam(value = "本次保养总工时：所有项目保养时间总和", required = true) @RequestParam(value = "man_hour", defaultValue = "" ) double manHour,
                               @ApiParam(value = "保养总结") @RequestParam(value = "content", defaultValue = "", required = false) String content,
                               @ApiParam(value = "工时费合计") @RequestParam(value = "manHourCost", defaultValue = "", required = false) String manHourCost,
                               @ApiParam(value = "材料费合计") @RequestParam(value = "cost", defaultValue = "", required = false) String cost,
                               @ApiParam(value = "保养单下所有保养项目，如{\"dataList\":[{\"projectId\":3,\"manhour\":5,\"manHourCost\":100,\"result\":1(1正常，0异常),\"remark\":\"备注\"} ]}\n", required = true) @RequestParam(value = "projectArray", defaultValue = "" ) String projectArray,
                               @ApiParam(value = "所有备件明细，如{\"dataList\":[{\"partId\":15,\"spart_amount\":5,\"spartPrice\":10.5,\"spart_sum\":1000,\"remark\":\"备注\"} ]}", required = true) @RequestParam(value = "partArray", defaultValue = "" ) String partArray) throws ParseException {

        //**判断工单若为57待验收 或者58已验收 则不允许修改。
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dd = df.format(new Date());
        UpkeepRecordDO upkeepRecordDo = upkeepRecordService.get(id);
        //"工单id无数据！"
        if(Objects.isNull(upkeepRecordDo)){ return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));}
        //允许超期的工单暂存保养信息
//        if (Objects.equals(Constant.WAITING_DEAL, upkeepRecordDo.getResult()) && (dateNow.before(upkeepRecordDo.getEndTime()))){
        if (Objects.equals(Constant.WAITING_DEAL, upkeepRecordDo.getResult())){
            //允许修改
            UpkeepRecordDO upkeepRecordDO = new UpkeepRecordDO() ;
            upkeepRecordDO. setId(id);
            upkeepRecordDO. setDownHour(downHour);
            upkeepRecordDO. setStatus(status);
            upkeepRecordDO.setManHour(manHour);
            upkeepRecordDO.setContent(content);
            upkeepRecordDO.setResult(Constant.WAITING_DEAL);
            upkeepRecordDO.setCost(new BigDecimal(cost));
            upkeepRecordDO.setManHourCost(new BigDecimal(manHourCost));

            upkeepRecordPartService.updateRecorPartDetail(projectArray,partArray,upkeepRecordDO);
            return R.ok();
        } else {
            //"保养单非待处理状态或已过期，无法暂存！！"
            return R.error(messageSourceHandler.getMessage("apis.upkeep.noAllowTS",null));
        }
    }


    //提供功能：填写保养工单明细
    @EvApiByToken(value = "/apis/upkeepPlan/sbumitRecordDetail", method = RequestMethod.POST)
    @ApiOperation("提交— 保养工单")
    @Transactional(rollbackFor = Exception.class)
    public R sbumitRecordDetails(@ApiParam(value = "保养单id", required = true) @RequestParam(value = "record_id", defaultValue = "") Long id,
                               @ApiParam(value = "停机时长", required = true) @RequestParam(value = "down_hour", defaultValue = "") double downHour,
                               @ApiParam(value = "设备使用状况id", required = true) @RequestParam(value = "status", defaultValue = "") int status,
                               @ApiParam(value = "本次保养总工时：所有项目保养时间总和", required = true) @RequestParam(value = "man_hour", defaultValue = "") double manHour,
                               @ApiParam(value = "保养总结") @RequestParam(value = "content", defaultValue = "", required = false) String content,
                                 @ApiParam(value = "工时费合计", required = true) @RequestParam(value = "manHourCost", defaultValue = "") String manHourCost,
                                 @ApiParam(value = "材料费合计", required = true) @RequestParam(value = "cost", defaultValue = "") String cost,
                                 @ApiParam(value = "保养单下所有保养项目，如{\"dataList\":[{\"projectId\":3,\"manhour\":5,\"manHourCost\":100,\"result\":1(1正常，0异常),\"remark\":\"备注\"} ]}", required = true) @RequestParam(value = "projectArray", defaultValue = "", required = true) String projectArray,
                               @ApiParam(value = "所有备件明细，如{\"dataList\":[{\"partId\":3,\"spart_amount\":5,\"spartPrice\":10.5,\"spart_sum\":1000,\"remark\":\"备注\"} ]}", required = true) @RequestParam(value = "partArray", defaultValue = "") String partArray) throws ParseException {

        //**判断工单若为57待验收 或者58已验收 则不允许修改。
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dd = df.format(new Date());
        Date dateNow = df.parse(dd);
        UpkeepRecordDO upkeepRecordDo = upkeepRecordService.get(id);

        if(Objects.isNull(upkeepRecordDo)){ return R.error("请传正确工单id！");}
        //允许超期的工单提交保养信息
//        if (Objects.equals(Constant.WAITING_DEAL, upkeepRecordDo.getResult()) && (dateNow.before(upkeepRecordDo.getEndTime()))){
        if (Objects.equals(Constant.WAITING_DEAL, upkeepRecordDo.getResult())){

                //允许修改
            UpkeepRecordDO upkeepRecordDO = new UpkeepRecordDO() ;
            upkeepRecordDO. setId(id);
            upkeepRecordDO. setDownHour(downHour);
            upkeepRecordDO. setStatus(status);
            upkeepRecordDO.setManHour(manHour);
            upkeepRecordDO.setContent(content);
            upkeepRecordDO.setResult(Constant.WAITING_CHECK);
            upkeepRecordDO.setCost(new BigDecimal(cost));
            upkeepRecordDO.setManHourCost(new BigDecimal(manHourCost));

            upkeepRecordPartService.updateRecorPartDetail(projectArray,partArray,upkeepRecordDO);
            return R.ok();
        } else {
            //"保养单非待处理状态或已过期，无法提交！！"
            return R.error(messageSourceHandler.getMessage("apis.upkeep.noAllowSubmit",null));
        }
    }


    @EvApiByToken(value = "/apis/upkeepRecord/checkRecord", method = RequestMethod.POST)
    @ApiOperation("验收保养任务单--暂存/修改暂存")
    public R checkRecord(UpkeepCheckDO upkeepCheckDO,
                         @ApiParam(value = "保养工单ID", required = true) @RequestParam(value = "recordId", defaultValue = "") Long id) {

        //工单已单一旦验收（无论通过与否）则不允许更改,且工单结束。(115通过，116不通过，133待评价)
        Map<String, Object> detailByRecordId = upkeepCheckService.getDetailByRecordId(id);
        //评价状态
        if(detailByRecordId!=null){
            int satus= Integer.parseInt(detailByRecordId.get("result").toString());
            if(Objects.equals(Constant.NO_EVALUATED,satus)){//113待评价

                upkeepCheckDO.setUserId(ShiroUtils.getUserId());//当前登录人的id作为评价单的验收人
                upkeepCheckDO.setRecordId(id);
                upkeepCheckDO.setResultRemark(upkeepCheckDO.getResult());
                upkeepCheckDO.setResult(Constant.NO_EVALUATED);

                upkeepCheckService.updateByRecordId(upkeepCheckDO);
                return R.ok();

            }else{
                //"保养单已验收，不允许再更改!"
                return R.error(messageSourceHandler.getMessage("apis.upkeep.noAllowChange",null));
            }
        }
        //"所传id期无数据，请检查正确性!"
        return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
    }

    @EvApiByToken(value = "/apis/upkeepRecord/sumitOfCheck", method = RequestMethod.POST)
    @ApiOperation("提交验收评价")
    public R sumitOfCheck(UpkeepCheckDO upkeepCheckDO,
                         @ApiParam(value = "保养工单ID", required = true) @RequestParam(value = "recordId", defaultValue = "") Long id) {

        //工单已单一旦验收（无论通过与否）则不允许更改,且工单结束。(115通过，116不通过，133待评价)
        Map<String, Object> detailByRecordId = upkeepCheckService.getDetailByRecordId(id);
        //评价状态
        if(detailByRecordId!=null){
            int satus= Integer.parseInt(detailByRecordId.get("result").toString());
            if(Objects.equals(Constant.NO_EVALUATED,satus)){//113待评价

                upkeepCheckDO.setUserId(ShiroUtils.getUserId());//当前登录人的id作为评价单的验收人
                upkeepCheckDO.setRecordId(id);
                upkeepCheckDO.setResultRemark(Constant.APPLY_APPROED);

                int counts= upkeepCheckService.updateByRecordId(upkeepCheckDO);
                if(counts>0){
                    //更新保养单转态为 58已验收
                    UpkeepRecordDO upkeepRecordDO = new UpkeepRecordDO() ;
                    upkeepRecordDO.setId(id);
                    upkeepRecordDO.setResult(Constant.ALREADY_CHECK);
                    upkeepRecordService.update(upkeepRecordDO);
                    return R.ok();
                }
                //"验收失败，请检查参数是否正确！"
                    return R.error(messageSourceHandler.getMessage("apis.upkeep.failtCheck",null));
            }else{
                //"保养单已验收，不允许再更改!"
                return R.error(messageSourceHandler.getMessage("apis.upkeep.noAllowChange",null));
            }
        }
        //"所传id期无数据，请检查正确性!"
        return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
    }

    /*
     *9.提供功能：获取保养评价详情
     */
    @EvApiByToken(value = "/apis/upkeepRecord/getCheckDetail", method = RequestMethod.POST)
    @ApiOperation("获取保养单评价详情")
    public R checkDetailByRecordId(@ApiParam(value = "保养工单ID", required = true) @RequestParam(value = "recordId", defaultValue = "") Long recordId) {

        Map<String, Object> detailByRecordId = upkeepCheckService.getDetailByRecordId(recordId);
        Map<String, Object> result = new HashMap<>();
        if (detailByRecordId.size()>0) {
            result.put("data", detailByRecordId);
            return R.ok(result);
        } else {
            result.put("data", "");
            return R.ok(result);
        }
    }


    //10.提供功能：根据当前设备查询保养
    @EvApiByToken(value = "/apis/upkeepRecord/getProByDeviceID", method = RequestMethod.POST)
    @ApiOperation("根据当前设备查询保养记录")
    public R getProjectByDeviceId(@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "" ) Long deviceId,
                                  @ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                                  @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                                  @ApiParam(value = "工单创建时间—开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                                  @ApiParam(value = "工单创建时间--结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime) {

        Map<String, Object> query = Maps.newHashMap();

        //根据设备id查找recordid,
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);
        query.put("deviceId", deviceId);
        query.put("endTime", endTime);
        query.put("startTime", startTime);

        List<Map<String, Object>> msgByDeviceId = upkeepRecordService.getMsgByDeviceId(query);
        int count = upkeepRecordService.countOfMsgByDeviceId(query);

        Map<String, Object> resultList = Maps.newHashMap();
        DsResultResponse resultPonse = new DsResultResponse();
        if ( msgByDeviceId.size() > 0) {
            resultPonse.setDatas(msgByDeviceId);
            resultPonse.setPageno(pageno);
            resultPonse.setPagesize(pagesize);
            resultPonse.setTotalRows(count);
            resultPonse.setTotalPages(((count + pagesize - 1) / pagesize));
        }
        resultList.put("data", resultPonse);
        return R.ok(resultList);
    }


    @EvApiByToken(value = "/apis/upkeepRecord/deletProByDeviceId", method = RequestMethod.POST)
    @ApiOperation("删除设备保养工单")
    @Transactional(rollbackFor =Exception.class)
    public R deletOneRecordById(@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "", required = false) Long id,
            @ApiParam(value = "保养记录id", required = true) @RequestParam(value = "recordId", defaultValue = "") Long recordId) {


        int removeRows = upkeepRecordService.remove(recordId);
        if(removeRows>0){

            upkeepRecordProjectService.removeByRecordId(recordId);
            upkeepRecordPartService.removeByRecordId(recordId);

            return R.ok();
        }else{
            return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
        }
    }

    /**
     *验证处于暂存态的允许删除，自动生成的工单不允许删除。
     */
    @EvApiByToken(value = "/apis/upkeepRecord/deletLargeRecord", method = RequestMethod.POST)
    @ApiOperation("批量删除保养工单")
    @Transactional(rollbackFor = Exception.class)
    public R deletLargRecords(@ApiParam(value = "保养记录ids:long[]", required = true) @RequestParam(value = "recordId", defaultValue = "" ) Long[] recordIds) {

        return upkeepRecordService.deletOfRecords(recordIds);

    }

    @EvApiByToken(value = "/apis/upkeepPlan/watingOfUpkeepAmount", method = RequestMethod.POST)
    @ApiOperation("保养工单待处理数量")
    public R watingOfUpkeepAmount(@ApiParam(value = "保养负责人Id" ) @RequestParam(value = "engineer_id", defaultValue = "", required = false) Long engineerId,
                                  @ApiParam(value = "部门id" ) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId){

        Map<String, Object> query = new HashMap<>();
        Map<String, Object> results = Maps.newHashMap();

        String idPath = null;
        if (Objects.nonNull(deptId)) {
            DeptDO deptDO = deptService.get(deptId);
            idPath = Objects.nonNull(deptDO)?deptDO.getIdPath():null;
        }
        query.put("engineerId", engineerId);
        query.put("result", Constant.WAITING_DEAL);//工单状态{56待处理，57待验收，58已验收}

        int waitingOfRecode = upkeepRecordService.countOfWaitingDo(query);
        results.put("data",waitingOfRecode);
        return R.ok(results);
    }


    /*
       ===============================================保养看板接口=============================================
     */

    @EvApiByToken(value = "/apis/upkeepRecorder/upkeepOfBoard", method = RequestMethod.POST,apiTitle = "保养看板")
    @ApiOperation("保养看板")
    public R watingOfUpkeepAmount(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                                  @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize){

        Map<String, Object> query = Maps.newHashMap();
        //根据设备id查找recordid,
        query.put("offset", (pageno - 1) * pagesize);
        query.put("limit", pagesize);

        List<Map<String, Object>> list = upkeepRecordService.upkeepDeatailOfBoard(query);
        int count= upkeepRecordService.countOfupkeepOfBoard(query);

        Map<String, Object> resultList = Maps.newHashMap();
        DsResultResponse resultPonse = new DsResultResponse();
        if ( list.size() > 0) {
            resultPonse.setDatas(list);
            resultPonse.setPageno(pageno);
            resultPonse.setPagesize(pagesize);
            resultPonse.setTotalRows(count);
            resultPonse.setTotalPages(((count + pagesize - 1) / pagesize));
        }
        resultList.put("data", resultPonse);
        return R.ok(resultList);

    }



}









