package com.ev.apis.controller.mes;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.ProcessReportCheckDO;
import com.ev.mes.service.DispatchItemService;
import com.ev.mes.service.ProcessReportCheckItemService;
import com.ev.mes.service.ProcessReportCheckService;
import com.ev.mes.service.ProcessReportService;
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
 * Created by Kuzi on 2019-12-3.
 * @author
 */
@Api(value="/",tags="工序报工 + + 工序检验")
@RestController
public class MesReportAndCheckController {
        @Autowired
        private DispatchItemService dispatchItemService;
        @Autowired
        private ProcessReportService processReportService;
        @Autowired
        private ProcessReportCheckService processReportCheckService;
        @Autowired
        private ProcessReportCheckItemService processReportCheckItemService;
        @Autowired
        private MessageSourceHandler messageSourceHandler;


    @EvApiByToken(value = "/apis/mes/processReport/headOfReport", method = RequestMethod.POST, apiTitle = "报工(按钮/扫描工单号的工单信息)")
    @ApiOperation("报工(按钮/扫描工单号的工单信息)")
    public R itemOfReportHead(@ApiParam(value = "工单id", required = true) @RequestParam(value = "dispatchId", required = true) Long dispatchId) {

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("dispatchId", dispatchId);
        Map<String, Object> stringObjectMap = dispatchItemService.rollBackOfDetail(params);
        //前端取字段processName ，code, dispatchId
        params.clear();
        if(Objects.nonNull(stringObjectMap)){
            params.put("data",stringObjectMap);
        }
        return R.ok(params);
    }


//    @EvApiByToken(value = "/apis/mes/processReport/saveAddAndChange", method = RequestMethod.POST, apiTitle = "暂存/修改暂存 报工单")
//    @ApiOperation("暂存/修改暂存 报工单")
//    @Transactional(rollbackFor = Exception.class)
//    public R saveAddAndChange(@ApiParam(value = "明细数据[{\n" +
//            "\"id\":\"报工主键（修改时传且必传）\",\n" +
//            "\"dispatchItemId\":\"工单明细主键 取dispatchId\",\n" +
//            "\"createBy\":\"汇报人\",\n" +
//            "\"supplierId\":\"供应商 \",\n" +
//            "\"completionCount\":\" 完工数量\",\n" +
//            "\"conformityCount\":\" 合格数量\",\n" +
//            "\"reworkCount\":\" 返工数量\",\n" +
//            "\"scrapCount\":\"报废数量\"\n" +
//            "}]", required = true) @RequestParam(value = "reportDetail", required = true) String  reportDetail) {
//
//        return processReportService.saveAndchangeSbumit(reportDetail, 0);
//    }

    @EvApiByToken(value = "/apis/mes/processReport/submitReport", method = RequestMethod.POST, apiTitle = "提交报工-（1.新增后直接提交)")
    @ApiOperation("提交报工-（1.新增后直接提交)")
    @Transactional(rollbackFor = Exception.class)
    public R submint(@ApiParam(value = "明细数据[{\n" +
            "\"id\":\"报工主键（修改时传且必传）\",\n" +
            "\"dispatchItemId\":\"工单明细主键 取dispatchId\",\n" +
            "\"createBy\":\"汇报人\",\n" +
            "\"supplierId\":\"供应商 \",\n" +
            "\"completionCount\":\" 完工数量\",\n" +
            "\"conformityCount\":\" 合格数量\",\n" +
            "\"reworkCount\":\" 返工数量\",\n" +
            "\"scrapCount\":\"报废数量\"\n" +
            "}]", required = true) @RequestParam(value = "reportDetail") String  reportDetail) {

        return processReportService.saveAndchangeSbumit(reportDetail, 1);
    }

    @EvApiByToken(value = "/apis/mes/processReport/detailOfReport", method = RequestMethod.POST, apiTitle = "报工详情")
    @ApiOperation("报工详情")
    public R reportOfdetail(@ApiParam(value = "报工id", required = true) @RequestParam(value = "id") Long id)  {

        Map<String, Object> params = Maps.newHashMap();
        params.put("id",id);
        Map<String, Object> stringObjectMap = processReportService.reportDetailById(params);
        params.clear();
        if(Objects.nonNull(stringObjectMap)){
            params.put("data",stringObjectMap);
        }
        return R.ok(params);
    }


    @EvApiByToken(value = "/apis/mes/processReport/listOfReport", method = RequestMethod.POST, apiTitle = "报工列表/待检报工单列表/工序检验（工控首页）")
    @ApiOperation("报工列表/待检报工单列表/工序检验（工控首页）")
    public R processReportOfList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                                 @ApiParam(value = "操作工id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
                                 @ApiParam(value = "设备id") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
                                 @ApiParam(value = "工序名id") @RequestParam(value = "processId", defaultValue = "", required = false) Long  processId,
                                 @ApiParam(value = "产品(物料)id") @RequestParam(value = "materialId", defaultValue = "", required = false) Long  materialId,
                                 @ApiParam(value = "报工单号code") @RequestParam(value = "code", defaultValue = "", required = false) String  code,
                                 @ApiParam(value = "供应商id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long  supplierId,

                                 @ApiParam(value = "操作工名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
                                 @ApiParam(value = "设备名字") @RequestParam(value = "deviceName", defaultValue = "", required = false) String deviceName,
                                 @ApiParam(value = "工序名字") @RequestParam(value = "processName", defaultValue = "", required = false) String processName,
                                 @ApiParam(value = "产品名") @RequestParam(value = "materielName", defaultValue = "", required = false) String materialName,
                                 @ApiParam(value = "供应商名字") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
                                 @ApiParam(value = "报工时间") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime,

                                 @ApiParam(value = "开始日期") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                                 @ApiParam(value = "截止日期") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                                 @ApiParam(value = "是否检验（1是检验）") @RequestParam(value = "isExamine", defaultValue = "", required = false) Integer isExamine,
                                 @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                                 @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {

        Map<String, Object> params = Maps.newHashMap();

        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = processReportService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("processId", processId);
        params.put("materialId", materialId);
        params.put("code", code);
        params.put("supplierId", supplierId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("isExamine", isExamine);

        params.put("operatorName", operatorName);
        params.put("deviceName", deviceName);
        params.put("processName", processName);
        params.put("materialName", materialName);
        params.put("supplierName", supplierName);
        params.put("createTime", createTime);

        List<Map<String, Object>> list = processReportService.listForMap(params);
        int count = processReportService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        BigDecimal allCompletionCount=BigDecimal.ZERO;
        BigDecimal allConformityCount=BigDecimal.ZERO;
        BigDecimal allReworkCount=BigDecimal.ZERO;
        BigDecimal allScrapCount=BigDecimal.ZERO;
        if (!(list.isEmpty())) {
            for(Map<String, Object> map :list){
                allCompletionCount=allCompletionCount.add(new BigDecimal(map.getOrDefault("completionCount","0").toString()));
                allConformityCount=allConformityCount.add(new BigDecimal(map.getOrDefault("conformityCount","0").toString()));
                allReworkCount=allReworkCount.add(new BigDecimal(map.getOrDefault("reworkCount","0").toString()));
                allScrapCount=allScrapCount.add(new BigDecimal(map.getOrDefault("scrapCount","0").toString()));
            }
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",count);
            dsRet.put("totalPages",(count + pagesize - 1) / pagesize);
            dsRet.put("allCompletionCount",allCompletionCount);
            dsRet.put("allConformityCount",allConformityCount);
            dsRet.put("allReworkCount",allReworkCount);
            dsRet.put("allScrapCount",allScrapCount);
            dsRet.put("datas",list);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/mes/processReport/reworkRepair", method = RequestMethod.POST, apiTitle = "返工返修--报工单")
    @ApiOperation("返工返修")
    @Transactional(rollbackFor = Exception.class)
    public R reworkRepairOfReport(@ApiParam(value = "报工单id", required = true) @RequestParam(value = "id") Long id,
                                  @ApiParam(value = "报工返工返修传0，报检返工返修传1", required = true) @RequestParam(value = "sign") Integer sign)  {

        return processReportService.reworkRepair(id,sign);
    }



    @EvApiByToken(value = "/apis/mes/processReport/deletOfReport", method = RequestMethod.POST, apiTitle = "删除报工")
    @ApiOperation("删除报工")
    @Transactional(rollbackFor = Exception.class)
    public R removeOfReport(@ApiParam(value = "报工id", required = true) @RequestParam(value = "ids") Long[] ids)  {

        //只允许删除暂存状态下的报工单
        return processReportService.deletOfReports(ids);
    }


/**
 * ======================================================工 序 检 验=======================================================================
 */


    /**
     * 根据  报工单id返回报检头部信息及检验项目
     */
    @EvApiByToken(value = "/apis/mes/reportCheck/buttonOfCheck", method = RequestMethod.POST, apiTitle = "报检")
    @ApiOperation("报检")
    public R checkDetailOfButton(@ApiParam(value = "报工单id", required = true) @RequestParam(value = "reportId", required = true) Long reportId) {


        Map<String,Object> pramy= new HashMap<>();
        Map<String,Object>  query= new HashMap<>();
        query.put("processReportId",new Long[]{reportId});
        int rows = processReportCheckService.countDeleAboutCheck(query);
        if(rows!=0){
            return R.error(messageSourceHandler.getMessage("apis.mes.report.wetherCheck",null));
        }
        pramy.put("reportId",reportId);
        Map<String, Object> headDetail = processReportCheckService.checkHeadDetail(pramy);

        pramy.put("type", ConstantForMES.GXJH_GYLX);
        List<Map<String, Object>> badyDetail = processReportCheckService.checkBadyDetail(pramy);
        pramy.clear();
        if(Objects.nonNull(headDetail)){
            pramy.put("HeadDetail",headDetail);
            pramy.put("BadyDetail",badyDetail);
        }
        return R.ok(pramy);
    }



    @EvApiByToken(value = "/apis/mes/reportCheck/submitAddOrChange", method = RequestMethod.POST, apiTitle = "提交报检单（新增后直接提交）")
    @ApiOperation("提交报检单（新增后直接提交）")
    @Transactional(rollbackFor = Exception.class)
    public R submitOfCheck(ProcessReportCheckDO processReportCheckDO,
                                @ApiParam(value = "检验明细[{\n" +
                                        "\"rejectsReason\":\"不良原因\",\n" +
                                        "\"rejectsCount\":\"不良数量\",\n" +
                                        "\"dispose\":\"处理方式\",\n" +
                                        "\"projectId\":\"检验项目id\",\n" +
                                        "\"actualValue\":\"实际检验值\",\n" +
                                        "\"createTime\":\"datetime\", \n" +
                                        "\"normn\":\"目标检验值\", \n" +
                                        "\"unit\":\"单位（String）\", \n" +
                                        "\"whetherCheck\":\"是否必检\", \n" +
                                        "\"result\":\"result\"\n" +
                                        "}]", required = true) @RequestParam(value = "bodyDetail") String bodyDetail) {
        return processReportCheckService.saveAndChangeAndSbumit(processReportCheckDO, bodyDetail,null, 1);
    }

    @EvApiByToken(value = "/apis/mes/reportCheck/listOfReportCheck", method = RequestMethod.POST, apiTitle = "检验单列表")
    @ApiOperation("工序检验单列表")
    public R reportCheckOfList(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                               @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                               @ApiParam(value = "操作工id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
                               @ApiParam(value = "设备id") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
                               @ApiParam(value = "工序名id") @RequestParam(value = "processId", defaultValue = "", required = false) Long  processId,
                               @ApiParam(value = "产品(物料)id") @RequestParam(value = "materialId", defaultValue = "", required = false) Long  materialId,
                               @ApiParam(value = "检验单号code") @RequestParam(value = "code", defaultValue = "", required = false) String  code,
                               @ApiParam(value = "供应商id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long  supplierId,

                               @ApiParam(value = "操作工名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
                               @ApiParam(value = "设备名字") @RequestParam(value = "deviceName", defaultValue = "", required = false) String deviceName,
                               @ApiParam(value = "工序名字") @RequestParam(value = "processName", defaultValue = "", required = false) String processName,
                               @ApiParam(value = "产品名") @RequestParam(value = "materialName", defaultValue = "", required = false) String materialName,
                               @ApiParam(value = "供应商名字"   ) @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
                               @ApiParam(value = "检验员"   ) @RequestParam(value = "createName", defaultValue = "", required = false) String createName,

                               @ApiParam(value = "开始日期") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                               @ApiParam(value = "截止日期") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                                @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
                               @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {

        Map<String, Object> params = Maps.newHashMap();

        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps =  processReportCheckService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("processId", processId);
        params.put("materialId", materialId);
        params.put("code", code);
        params.put("supplierId", supplierId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("operatorName", operatorName);
        params.put("deviceName", deviceName);
        params.put("processName", processName);
        params.put("materialName", materialName);
        params.put("supplierName", supplierName);
        params.put("createName", createName);

        List<Map<String, Object>> list = processReportCheckService.listForMap(params);
        int count = processReportCheckService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        if (!(list.isEmpty())) {

            BigDecimal allCheckCount=BigDecimal.ZERO;
            BigDecimal allConformityCount=BigDecimal.ZERO;
            BigDecimal allReworkCount=BigDecimal.ZERO;
            BigDecimal allRcrapCount=BigDecimal.ZERO;
            for(Map<String, Object> map:list){
                allCheckCount=allCheckCount.add(new BigDecimal(map.getOrDefault("checkCount","0").toString()));
                allConformityCount=allConformityCount.add(new BigDecimal(map.getOrDefault("conformityCount","0").toString()));
                allReworkCount=allReworkCount.add(new BigDecimal(map.getOrDefault("reworkCount","0").toString()));
                allRcrapCount=allRcrapCount.add(new BigDecimal(map.getOrDefault("scrapCount","0").toString()));
             }

            Map<String,Object>  dsRet= new HashMap<>(10);
            dsRet.put("datas",list);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",count);
            dsRet.put("totalPages",(count + pagesize - 1) / pagesize);
            dsRet.put("allCheckCount",allCheckCount);
            dsRet.put("allConformityCount",allConformityCount);
            dsRet.put("allReworkCount",allReworkCount);
            dsRet.put("allRcrapCount",allRcrapCount);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }

    /**
     *   选中检验列表中某行，查看子表信息
     */
    @EvApiByToken(value = "/apis/mes/reportCheck/detailOfCheckItem", method = RequestMethod.POST, apiTitle = "检验明细")
    @ApiOperation("检验明细")
    @Transactional(rollbackFor = Exception.class)
    public R reportCheckItemDetail(@ApiParam(value = "检验单id", required = true) @RequestParam(value = "reportCheckId") Long reportCheckId) {

        Map<String,Object> result=new HashMap<>();
        List<Map<String, Object>> itemList = processReportCheckItemService.detailByCheckId(reportCheckId);
        if(!(itemList.isEmpty())){

            result.put("data",itemList);
        }
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/mes/reportCheck/batchRemoveCheck", method = RequestMethod.POST, apiTitle = "删除检验单")
    @ApiOperation("删除/批量删除检验单")
    @Transactional(rollbackFor = Exception.class)
    public R removeReportCheck(@ApiParam(value = "检验单id", required = true) @RequestParam(value = "reportCheckId") Long[] reportCheckId) {
            // 有返工返修的工序计划禁止删除
        return processReportCheckService.batchRemoveCheckReport(reportCheckId);
    }




}
