package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.WorkingProcedureDetailDO;
import com.ev.mes.service.DispatchItemService;
import com.ev.mes.service.DispatchWorkingHungService;
import com.ev.mes.service.WorkingProcedureDetailService;
import com.ev.system.domain.DeptDO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Kuzi on 2019-11-28.
 * @author
 */
@Api(value = "/", tags = "工单 + + 开工挂起")
@RestController
public class MesDispatchApiController {

    @Autowired
    private WorkingProcedureDetailService workingProcedureDetailService;
    @Autowired
    private DispatchItemService dispatchItemService;
    @Autowired
    private DispatchWorkingHungService dispatchWorkingHungService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private DeptService deptService;


    //派工列表
    @EvApiByToken(value = "/apis/mes/dispatch/disptchOfList", method = RequestMethod.POST, apiTitle = "派工计划 列表")
    @ApiOperation("派工计划 列表")
    public R dispatchOfList(
                            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                             @ApiParam(value = "当前登录人部门id") @RequestParam(value = "deptId", required = false) Long deptId) {
        String idPath=null;
        if (null != deptId) {
            DeptDO deptDO = deptService.get(deptId);
            idPath = Objects.isNull(deptDO) ? null : deptDO.getIdPath();
        }

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("idPath",idPath);

        List<Map<String, Object>> list = dispatchItemService.listForMapOfPlan(params);
        int count = dispatchItemService.countForListMapOfPlan(params);

        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);

        if (!list.isEmpty()) {
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(list);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(count);
            dsRet.setTotalPages( (count + pagesize - 1) / pagesize);
            results.put("data", dsRet);
        }

        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/dispatch/sendButton", method = RequestMethod.POST, apiTitle = "派工(按钮)")
    @ApiOperation("派工(按钮)")
    public R sendOfButton(@ApiParam(value = "工序计划子表主键", required = true) @RequestParam(value = "planItemId") Long planItemId) {

         WorkingProcedureDetailDO workingProcedureDetailDO=workingProcedureDetailService.get(planItemId);
        if (workingProcedureDetailDO != null) {
            if(Objects.equals(workingProcedureDetailDO.getAlreadyCount(),workingProcedureDetailDO.getPlanCount())){
                //计划生产数量已全部派工完毕
                return R.error(messageSourceHandler.getMessage("apis.mes.dispatch.countOver",null));
            }else{
                Map<String, Object> result = dispatchItemService.sendOfButton(planItemId);
                return R.ok(result);
            }
        }
        //"此id下数据不存在！"
        return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
    }


    /**
     *派工成功后回写已派数量
     **需要前端控制派工数量应小于等于待派工数量!!!
    */
    @EvApiByToken(value = "/apis/mes/dispatch/submitDispatchDetail", method = RequestMethod.POST, apiTitle = "提交派工")
    @ApiOperation("提交派工")
    @Transactional(rollbackFor = Exception.class)
    public R submitOfDispatch(@ApiParam(value = "工序计划子表主键", required = true) @RequestParam(value = "planItemId") Long planItemId,
                              @ApiParam(value = "派工明细：需要前端控制派工数量应小于等于待派工数量[{\n" +
                                      "\"operator\":\"操作工/业务员id\",\n" +
                                      "\"supplierId\":\"委外供应商id\",\n" +
                                      "\"deviceId\":\"设备id(委外不传)\",\n" +
                                      "\"planCount\":\"生产数量\",\n" +
                                      "\"startTime\":\"计划开工时间\",\n" +
                                      "\"endTime\":\"计划完工时间\"\n" +
                                      "},\n" +
                                      "{\n" +
                                      "\"operator\":\"操作工/业务员id\",\n" +
                                      "\"supplierId\":\"委外供应商id\",\n" +
                                      "\"deviceId\":\"设备id(委外不传)\",\n" +
                                      "\"planCount\":\"生产数量\",\n" +
                                      "\"startTime\":\"计划开工时间\",\n" +
                                      "\"endTime\":\"计划完工时间\"\n" +
                                      "}]\n", required = true) @RequestParam(value = "dispatchDetal") String dispatchDetal) {

        //需要前端控制派工数量应小于等于待派工数量
        return dispatchItemService.disposeSbmint(planItemId, dispatchDetal);
    }

    @EvApiByToken(value = "/apis/mes/dispatch/detailOfdisptch", method = RequestMethod.POST, apiTitle = "派工记录//等待工单（工控）")
    @ApiOperation("派工记录//等待工单（工控）")
    public R dispatchOfItems(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                             @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                             @ApiParam(value = "派单人") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
                             @ApiParam(value = "派单人名字") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
                             @ApiParam(value = "操作工") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
                             @ApiParam(value = "工单状态") @RequestParam(value = "status", required = false) Integer[] status,
                             @ApiParam(value = "派工时间") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime,
                             @ApiParam(value = "操作工名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
                             @ApiParam(value = "设备") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
                             @ApiParam(value = "设备名字") @RequestParam(value = "deviceName", defaultValue = "", required = false) String deviceName,
                             @ApiParam(value = "工序id") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
                             @ApiParam(value = "工序名字") @RequestParam(value = "processName", defaultValue = "", required = false) String processName,
                             @ApiParam(value = "产品id") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
                             @ApiParam(value = "产品名") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                             @ApiParam(value = "派工单号") @RequestParam(value = "code", defaultValue = "", required = false) String code,
                             @ApiParam(value = "批号") @RequestParam(value = "batchNo", defaultValue = "", required = false) String batchNo,
                             @ApiParam(value = "供应商名字") @RequestParam(value = "suplierName", defaultValue = "", required = false) String suplierName,
                             @ApiParam(value = "供应商id") @RequestParam(value = "suplier", defaultValue = "", required = false) Long suplier,
                             @ApiParam(value = "(计划开工时间)开始日期") @RequestParam(value = "startTimes", defaultValue = "", required = false) String startTimes,
                             @ApiParam(value = "(计划完工时间)截止日期") @RequestParam(value = "endTimes", defaultValue = "", required = false) String endTimes,
                             @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                             @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order ) {

        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps =dispatchItemService.listForMapOfDispatch(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("createBy", createBy);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("processId", processId);
        params.put("materielId", materielId);
        params.put("code", code);
        params.put("batchNo", batchNo);
        params.put("supplier", suplier);
        params.put("startTimes", startTimes);
        params.put("endTimes", endTimes);

        params.put("deviceName", deviceName);
        params.put("createByName", createByName);
        params.put("operatorName", operatorName);
        params.put("processName", processName);
        params.put("materielName", materielName);
        params.put("supplierName", suplierName);
        params.put("status",status);
        params.put("createTime", createTime);

        List<Map<String, Object>> list = dispatchItemService.listForMapOfDispatch(params);
        int count = dispatchItemService.countForMapOfDispatch(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        BigDecimal allPlanCount=BigDecimal.ZERO;
        BigDecimal allCompletionCount=BigDecimal.ZERO;
        if (!list.isEmpty()) {
            for(Map<String, Object> map:list){
                 allPlanCount=allPlanCount.add(new BigDecimal(map.getOrDefault("planCount","0").toString()));
                 allCompletionCount=allCompletionCount.add(new BigDecimal(map.getOrDefault("completionCount","0").toString()));
            }
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("datas",list);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",count);
            dsRet.put("totalPages",(count + pagesize - 1) / pagesize);
            dsRet.put("allPlanCount",allPlanCount);
            dsRet.put("allCompletionCount",allCompletionCount);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    /**
     * 允许删除时回写 已派工数量
     */
    @EvApiByToken(value = "/apis/mes/dispatch/batchRemove", method = RequestMethod.POST, apiTitle = "删除派工记录")
    @ApiOperation("删除派工记录")
    @Transactional(rollbackFor = Exception.class)
    public R deletAll(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId") Long[] dispatchId) {

        return dispatchItemService.deletByIds(dispatchId);
    }


    /**
     * 派工退回按钮
     */
    @EvApiByToken(value = "/apis/mes/dispatch/buttonOfRollBack", method = RequestMethod.POST, apiTitle = "派工退回按钮")
    @ApiOperation("派工退回按钮")
    @Transactional(rollbackFor = Exception.class)
    public R rollBackDetails(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId") Long dispatchId) {

        Map<String, Object> paramy = Maps.newHashMapWithExpectedSize(2);

        paramy.put("dispatchId", dispatchId);
        Map<String, Object> stringObjectMap = dispatchItemService.rollBackOfDetail(paramy);
        paramy.clear();
        if (stringObjectMap!=null) {
            paramy.put("data", stringObjectMap);
        }
        return R.ok(paramy);
    }


    /**
     * 派工退回保存     允许退回后将 退回数量在  工序计划-->已派工数量   做减法 反写  同时将工单计划数量做减法
     */
    @EvApiByToken(value = "/apis/mes/dispatch/saveRollBackCount", method = RequestMethod.POST, apiTitle = "派工退回保存")
    @ApiOperation("派工退回保存")
    @Transactional(rollbackFor = Exception.class)
    public R saveBackCount(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId") Long dispatchId,
                           @ApiParam(value = "派工退回数量", required = true) @RequestParam(value = "rollBackCount") String rollBackCount) {
        return dispatchItemService.saveRollBackOfCount(dispatchId, rollBackCount);
    }


    @EvApiByToken(value = "/apis/mes/dispatch/closeOfDispatch", method = RequestMethod.POST, apiTitle = "完工关闭")
    @ApiOperation("完工关闭")
    @Transactional(rollbackFor = Exception.class)
    public R closeOfDispatchs(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId") Long[] dispatchId) {

        //状态改为  ConstantForMES.CLOSE_CASE//234结案
        return dispatchItemService.closeDispatch(dispatchId);
    }


    @EvApiByToken(value = "/apis/mes/dispatch/workStart", method = RequestMethod.POST, apiTitle = "开工")
    @ApiOperation("开工")
    @Transactional(rollbackFor = Exception.class)
    public R workStartDispatch(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId") Long dispatchId)  {
        //验证工序计划是否下达状态 +工单是否结案状态 +
        return dispatchItemService.workStartDispatchItem(dispatchId);
    }


    @EvApiByToken(value = "/apis/mes/dispatch/workHangUp", method = RequestMethod.POST, apiTitle = "挂起")
    @ApiOperation("挂起")
    @Transactional(rollbackFor = Exception.class)
    public R workHangUpDispastch(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId") Long dispatchId,
                                 @ApiParam(value = "挂起原因") @RequestParam(value = "pendingReason",defaultValue = "") String  pendingReason)  {
        return dispatchItemService.hangUpDispatchItem(dispatchId,pendingReason);
    }


    @EvApiByToken(value = "/apis/mes/dispatch/listOfWorkHung", method = RequestMethod.POST, apiTitle = "开工/挂起 记录")
    @ApiOperation("开工/挂起 记录")
    public R workHungOfList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                             @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                             @ApiParam(value = "操作工id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
                            @ApiParam(value = "操作工名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
                            @ApiParam(value = "设备id") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
                            @ApiParam(value = "设备名字") @RequestParam(value = "deviceName", defaultValue = "", required = false) String deviceName,
                            @ApiParam(value = "工序名") @RequestParam(value = "processName", defaultValue = "", required = false) String  processName,
                             @ApiParam(value = "派工单号") @RequestParam(value = "code", defaultValue = "", required = false) String code,
                             @ApiParam(value = "开始日期") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                             @ApiParam(value = "截止日期") @RequestParam(value = "endTimes", defaultValue = "", required = false) String endTime,
                             @ApiParam(value = "开工记录传1/挂起记录传0", required = true) @RequestParam(value = "sign") Integer sign,
                            @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                            @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps =  dispatchWorkingHungService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("processName", processName);
        params.put("code", code);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("sign", sign);
        params.put("operatorName", operatorName);
        params.put("deviceName", deviceName);

        List<Map<String, Object>> list = dispatchWorkingHungService.listForMap(params);
        int count = dispatchWorkingHungService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        BigDecimal totalManHour= BigDecimal.ZERO;
        if (!list.isEmpty()) {
            for(Map<String, Object> map:list){
                totalManHour=totalManHour.add(new BigDecimal(map.getOrDefault("manHour","0").toString()));
            }
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("datas",list);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",count);
            dsRet.put("totalPages",(count + pagesize - 1) / pagesize);
            dsRet.put("totalManHour",totalManHour);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/dispatch/deletOfRecord", method = RequestMethod.POST, apiTitle = "删除开工/挂起记录")
    @ApiOperation("删除开工/挂起记录")
    @Transactional(rollbackFor = Exception.class)
    public R removeOfRecord(@ApiParam(value = "记录id", required = true) @RequestParam(value = "ids") Long[] ids)  {

        //暂定工单结案的记录禁止删除！
        return dispatchWorkingHungService.deletOfHungOrWorking(ids);
    }

    @EvApiByToken(value = "/apis/mes/dispatch/getOneStartWorking", method = RequestMethod.POST, apiTitle = "工控--在制工单信息")
    @ApiOperation("工控--在制工单信息（含工艺要求）")
    @Transactional(rollbackFor = Exception.class)
    public R getOneDisStartWorking(){
        //显示一条当前登录人且开工状态的工单明细   +并返回工艺要求信息
        return dispatchItemService.oneStartWorkDetail();
    }







}
