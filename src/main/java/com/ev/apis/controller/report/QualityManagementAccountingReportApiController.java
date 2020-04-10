package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.report.service.QualityManagementAccountingReportService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 质量管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-23 09:51:41
 */
@Api(value = "/", tags = "质量管理报表分析API")
@RestController
public class QualityManagementAccountingReportApiController {
    @Autowired
    private QualityManagementAccountingReportService reportService;

    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badPurchase", method = RequestMethod.POST, apiTitle = "采购不良分析")
    @ApiOperation("采购不良分析")
    public R badPurchase(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("supplierId", supplierId);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("status", ConstantForMES.OK_AUDITED);
        params.put("inspectionType", ConstantForMES.LLJY);
        Map<String, Object> results = Maps.newHashMap();

        // 获取所有来料检验单
        List<Map<String, Object>> badPurchaseLists = reportService.badPurchaseList(params);
        if (badPurchaseLists.size() > 0) {
            List<Map<String, Object>> showList = Lists.newArrayList();

            Map<String, Double> unqualifiedCountMap = badPurchaseLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unqualifiedCount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = badPurchaseLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> v.get("supplierName").toString()
                            , (v1, v2) -> v1));
            if (showTotal) {
                Map<String, Object> map;
                for (String s : supplierNameMap.keySet()) {
                    map = Maps.newHashMap();
                    map.put("supplierId", s);
                    // 标记颜色
                    map.put("sign", "end");
                    // 排序号
                    map.put("sortNo", 1);
                    map.put("supplierName", supplierNameMap.get(s) + "小计");
                    map.put("unqualifiedCount", unqualifiedCountMap.get(s));
                    showList.add(map);
                }
            }

            if (showItem) {
                showList.addAll(badPurchaseLists);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("clientId").toString())))
                    .collect(Collectors.toList());
            results.put("data",collect);

        }
        return R.ok(results);
    }

//    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badPurchase/item", method = RequestMethod.POST, apiTitle = "采购不良分析(详细)")
//    @ApiOperation("采购不良分析(详细)")
//    public R badPurchaseItem(
//            @ApiParam(value = "供应商", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
//            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
//            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("supplierId", supplierId);
//        params.put("materielId", materielId);
//        params.put("startTime", startTime);
//        params.put("endTime", endTime);
//
//        params.put("status", ConstantForMES.OK_AUDITED);
//        params.put("inspectionType", ConstantForMES.LLJY);
//        Map<String, Object> results = Maps.newHashMap();
//
//        // 获取所有来料检验单
//        List<Map<String, Object>> badPurchaseLists = reportService.badPurchaseList(params);
//        if (badPurchaseLists.size() > 0) {
//            results.put("data", badPurchaseLists);
//        }
//        return R.ok(results);
//    }

    @EvApiByToken(value = "/apis/qualityManagement/badProcess", method = RequestMethod.POST, apiTitle = "工序不良分析")
    @ApiOperation("工序不良分析")
    public R badProcessForDept(
            @ApiParam(value = "显示部门小计", required = true) @RequestParam(value = "showDeptTotal", defaultValue = "1") int showDeptTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "显示工序小计", required = true) @RequestParam(value = "showProcessTotal", defaultValue = "1") int showProcessTotalInt,

            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "工序ID") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showDeptTotal = showDeptTotalInt == 1;
        boolean showProcessTotal = showProcessTotalInt == 1;

        params.put("deptId", deptId);
        params.put("processId", processId);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("isGroup", 0);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> badProcessLists = reportService.badProcessList(params);
        if (badProcessLists.size() > 0) {

            List<Map<String, Object>> showList = Lists.newArrayList();

            int max = Integer.parseInt(
                    badProcessLists
                            .stream()
                            .max(Comparator.comparing(e->Integer.parseInt(e.getOrDefault("processId",0).toString())))
                            .get()
                            .get("processId")
                            .toString()
            );
            // 最大一个工序号+1
            max = max + 1;

            Map<String, Double> unqualifiedCountMap = badProcessLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> Double.parseDouble(v.get("rejectsCount").toString())
                            , Double::sum));

            Map<String, String> deptNameMap = badProcessLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> v.get("deptName").toString()
                            , (v1, v2) -> v1));

            Map<String, String> processNameMap = badProcessLists
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("processId").toString()
                            , v -> v.get("processName").toString()
                            , (v1, v2) -> v1));


            if (showDeptTotal) {
                Map<String, Object> map;
                for (String s : deptNameMap.keySet()) {
                    map = Maps.newHashMap();
                    map.put("deptId", s);
                    map.put("deptName", deptNameMap.get(s) + "小计");
                    // 最后一层小计赋end
                    map.put("sign","end");
                    map.put("sortNo", max);
                    map.put("rejectsCount", unqualifiedCountMap.get(s));
                    map.put("processId", max);
                    showList.add(map);
                }
            }

            if (showProcessTotal) {
                Map<String, Map<String, Double>> priceGroupByDeptAndUser = badProcessLists
                        .stream()
                        .collect(
                                Collectors.groupingBy(e->e.getOrDefault("deptId",0).toString()
                                        , Collectors.groupingBy(e->e.getOrDefault("processId",0).toString()
                                                , Collectors.summingDouble(e->Double.parseDouble(e.getOrDefault("rejectsCount",0.0d).toString())))));
                Map<String, Double> groupByDept;
                Map<String, Object> totalMap;
                for (String deptIdParam : priceGroupByDeptAndUser.keySet()) {
                    groupByDept = priceGroupByDeptAndUser.get(deptIdParam);
                    for (String processIdParam : groupByDept.keySet()) {
                        totalMap = Maps.newHashMap();
                        Double rejectsCount = groupByDept.getOrDefault(processIdParam,0.0d);
                        // 部门总计标end 工序总计标1 详情标0
                        totalMap.put("sign",1);
                        totalMap.put("sortNo",processIdParam);
                        totalMap.put("deptId",deptIdParam);
                        totalMap.put("processName", processNameMap.get(processIdParam)+"小计");
                        totalMap.put("processId",processIdParam);
                        totalMap.put("rejectsCount",rejectsCount);
                        showList.add(totalMap);
                    }
                }
            }

            if (showItem) {
                showList.addAll(badProcessLists);
            }

            // 总合计
            double total = badProcessLists
                    .stream()
                    .map(e->Double.parseDouble(e.getOrDefault("rejectsCount"
                            ,0.0d).toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);

            // 排序
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("processId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("deptId").toString())))
                    .collect(Collectors.toList());

            results.put("data",collect);
            results.put("total",total);

        }
        return R.ok(results);
    }

//    @EvApiByToken(value = "/apis/qualityManagement/badProcess", method = RequestMethod.POST, apiTitle = "工序不良分析(工序小计)")
//    @ApiOperation("工序不良分析(工序小计)")
//    public R badProcess(
//            @ApiParam(value = "生产部门", required = true) @RequestParam(value = "deptId", defaultValue = "") Long deptId,
//            @ApiParam(value = "工序ID") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
//            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
//            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
//            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("deptId", deptId);
//        params.put("processId", processId);
//        params.put("operator", operator);
//        params.put("deviceId", deviceId);
//        params.put("startTime", startTime);
//        params.put("endTime", endTime);
//
//        params.put("isGroup", 0);
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> badProcessLists = reportService.badProcessList(params);
//        if (badProcessLists.size() > 0) {
//
//            Map<String, Double> unqualifiedCountMap = badProcessLists
//                    .stream()
//                    .collect(Collectors.toMap(
//                            k -> k.get("processId").toString()
//                            , v -> Double.parseDouble(v.get("rejectsCount").toString())
//                            , Double::sum));
//
//            Map<String, String> processNameMap = badProcessLists
//                    .stream()
//                    .collect(Collectors.toMap(
//                            k -> k.get("processId").toString()
//                            , v -> v.get("processName").toString()
//                            , (v1, v2) -> v1));
//
//            List<Map<String, Object>> data = Lists.newArrayList();
//            Map<String, Object> map;
//            for (String s : processNameMap.keySet()) {
//                map = Maps.newHashMap();
//                map.put("processId", s);
//                map.put("processName", processNameMap.get(s) + "小计");
//                map.put("count", unqualifiedCountMap.get(s));
//                data.add(map);
//            }
//
//            results.put("data", data);
//        }
//        return R.ok(results);
//    }

//    @EvApiByToken(value = "/apis/qualityManagement/badProcess/item", method = RequestMethod.POST, apiTitle = "工序不良分析(详细列表)")
//    @ApiOperation("工序不良分析(详细列表)")
//    public R badProcessItem(
//            @ApiParam(value = "生产部门", required = true) @RequestParam(value = "deptId", defaultValue = "") Long deptId,
//            @ApiParam(value = "工序ID", required = true) @RequestParam(value = "processId", defaultValue = "") Long processId,
//            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
//            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
//            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("deptId", deptId);
//        params.put("processId", processId);
//        params.put("operator", operator);
//        params.put("deviceId", deviceId);
//        params.put("startTime", startTime);
//        params.put("endTime", endTime);
//
//        params.put("isGroup", 1);
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> badProcessLists = reportService.badProcessList(params);
//        if (badProcessLists.size() > 0) {
//            results.put("data", badProcessLists);
//        }
//        return R.ok(results);
//    }

    @EvApiByToken(value = "/apis/qualityManagement/badProductForDept", method = RequestMethod.POST, apiTitle = "产品不良分析（部门小计）")
    @ApiOperation("产品不良分析（部门小计）")
    public R badProductForDept(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "产品ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("deptId", deptId);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("status", ConstantForMES.OK_AUDITED);
        params.put("inspectionType", ConstantForMES.CPJY);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> badProductLists = reportService.badPurchaseList(params);

        if (badProductLists.size() > 0) {
            // 获取所有的生产部门
            List<String> deptIds = badProductLists
                    .stream()
                    .map(e -> e.get("deptId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = deptIds.size();
            // 将生产部门分页
            List<String> deptIdsPage = PageUtils.startPage(deptIds, pageno, pagesize);

            // 获取分页下的部门的所有产品检验单
            List<Map<String, Object>> badProductList = badProductLists
                    .stream()
                    .filter(e -> deptIdsPage.contains(e.get("deptId").toString()))
                    .collect(Collectors.toList());

            Map<String, Double> unqualifiedCountMap = badProductList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> Double.parseDouble(v.get("unqualifiedCount").toString())
                            , Double::sum));

            Map<String, String> deptNameMap = badProductList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> v.get("deptName").toString()
                            , (v1, v2) -> v1));

            List<Map<String, Object>> data = Lists.newArrayList();
            Map<String, Object> map;
            for (String s : deptNameMap.keySet()) {
                map = Maps.newHashMap();
                map.put("deptId", s);
                map.put("deptName", deptNameMap.get(s) + "小计");
                map.put("count", unqualifiedCountMap.get(s));
                data.add(map);
            }

            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badProduct", method = RequestMethod.POST, apiTitle = "产品不良分析(产品小计)")
    @ApiOperation("产品不良分析(产品小计)")
    public R badProduct(
            @ApiParam(value = "部门Id", required = true) @RequestParam(value = "deptId", defaultValue = "") Long deptId,
            @ApiParam(value = "产品ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime

    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("deptId", deptId);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("status", ConstantForMES.OK_AUDITED);
        params.put("inspectionType", ConstantForMES.CPJY);
        Map<String, Object> results = Maps.newHashMap();

        // 获取所有产品检验单
        List<Map<String, Object>> badProductLists = reportService.badPurchaseList(params);
        if (badProductLists.size() > 0) {

            Map<String, Double> unqualifiedCountMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("materielId").toString()
                            , v -> Double.parseDouble(v.get("unqualifiedCount").toString())
                            , Double::sum));

            Map<String, String> materielNameMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("materielId").toString()
                            , v -> v.get("materielName").toString()
                            , (v1, v2) -> v1));

            List<Map<String, Object>> data = Lists.newArrayList();
            Map<String, Object> map;
            for (String s : materielNameMap.keySet()) {
                map = Maps.newHashMap();
                map.put("materielId", s);
                map.put("materielName", materielNameMap.get(s) + "小计");
                map.put("count", unqualifiedCountMap.get(s));
                data.add(map);
            }
            results.put("data", data);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badProduct/item", method = RequestMethod.POST, apiTitle = "产品不良分析(详细列表)")
    @ApiOperation("产品不良分析(详细列表)")
    public R badProductItem(
            @ApiParam(value = "部门Id", required = true) @RequestParam(value = "deptId", defaultValue = "") Long deptId,
            @ApiParam(value = "产品ID", required = true) @RequestParam(value = "materielId", defaultValue = "") Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime

    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("deptId", deptId);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("groupBatch",1);
        params.put("status", ConstantForMES.OK_AUDITED);
        params.put("inspectionType", ConstantForMES.CPJY);
        Map<String, Object> results = Maps.newHashMap();

        // 获取所有产品检验单
        List<Map<String, Object>> badProductLists = reportService.badPurchaseList(params);
        if (badProductLists.size() > 0) {
            results.put("data", badProductLists);
        }
        return R.ok(results);
    }

    /**
     *  采购入库物料且有批次管理
     */
    @EvApiByToken(value = "/apis/qualityManagement/qualityTraceability", method = RequestMethod.POST, apiTitle = "质量追溯分析")
    @ApiOperation("质量追溯分析")
    public R qualityTraceability(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批号", required = true) @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();


        params.put("batch", batch);
        params.put("deviceId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForGYL.OK_AUDITED);

        params.put("storageType", ConstantForGYL.PURCHASE_INSTOCK);
        params.put("inspectionType", ConstantForMES.LLJY);
        params.put("outboundType", ConstantForGYL.LYCK);
        int total = reportService.qualityTraceabilityCount(params);

        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        // 采购入库&来料检验&生产领料&生产计划
        List<Map<String, Object>> data = reportService.qualityTraceabilityList(params);
        Map<String, Object> results = Maps.newHashMap();
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }


}
