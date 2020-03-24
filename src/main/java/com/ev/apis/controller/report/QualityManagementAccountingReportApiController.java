package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
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

    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badPurchase", method = RequestMethod.POST, apiTitle = "采购不良分析(合计)")
    @ApiOperation("采购不良分析(合计)")
    public R badPurchase(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

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
            List<String> supplierIds = badPurchaseLists
                    .stream()
                    .map(e -> e.get("supplierId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = supplierIds.size();
            // 将供应商分页
            List<String> supplierIdsPage = PageUtils.startPage(supplierIds, pageno, pagesize);
            // 获取分页后的供应商检验单
            List<Map<String, Object>> badPurchaseList = badPurchaseLists
                    .stream()
                    .filter(e -> supplierIdsPage.contains(e.get("supplierId").toString()))
                    .collect(Collectors.toList());

            Map<String, Double> unqualifiedCountMap = badPurchaseList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unqualifiedCount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = badPurchaseList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> v.get("supplierName").toString()
                            , (v1, v2) -> v1));

            List<Map<String, Object>> data = Lists.newArrayList();
            Map<String, Object> map;
            for (String s : supplierNameMap.keySet()) {
                map = Maps.newHashMap();
                map.put("supplierId", s);
                map.put("supplierName", supplierNameMap.get(s) + "小计");
                map.put("count", unqualifiedCountMap.get(s));
                data.add(map);
            }

            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badPurchase/item", method = RequestMethod.POST, apiTitle = "采购不良分析(详细)")
    @ApiOperation("采购不良分析(详细)")
    public R badPurchaseItem(
            @ApiParam(value = "供应商", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime

    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

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
            results.put("data", badPurchaseLists);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/qualityManagement/badProcessForDept", method = RequestMethod.POST, apiTitle = "工序不良分析(部门小计)")
    @ApiOperation("工序不良分析(部门小计)")
    public R badProcessForDept(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "工序ID") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

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
            // 获取所有的生产部门
            List<String> deptIds = badProcessLists
                    .stream()
                    .map(e -> e.get("deptId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = deptIds.size();
            // 将生产部门分页
            List<String> deptIdsPage = PageUtils.startPage(deptIds, pageno, pagesize);

            // 获取分页下的部门的所有产品检验单
            List<Map<String, Object>> badProductList = badProcessLists
                    .stream()
                    .filter(e -> deptIdsPage.contains(e.get("deptId").toString()))
                    .collect(Collectors.toList());

            Map<String, Double> unqualifiedCountMap = badProductList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> Double.parseDouble(v.get("rejectsCount").toString())
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

    @EvApiByToken(value = "/apis/qualityManagement/badProcess", method = RequestMethod.POST, apiTitle = "工序不良分析(工序小计)")
    @ApiOperation("工序不良分析(工序小计)")
    public R badProcess(
            @ApiParam(value = "生产部门", required = true) @RequestParam(value = "deptId", defaultValue = "") Long deptId,
            @ApiParam(value = "工序ID") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

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

            Map<String, Double> unqualifiedCountMap = badProcessLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("processId").toString()
                            , v -> Double.parseDouble(v.get("rejectsCount").toString())
                            , Double::sum));

            Map<String, String> processNameMap = badProcessLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("processId").toString()
                            , v -> v.get("processName").toString()
                            , (v1, v2) -> v1));

            List<Map<String, Object>> data = Lists.newArrayList();
            Map<String, Object> map;
            for (String s : processNameMap.keySet()) {
                map = Maps.newHashMap();
                map.put("processId", s);
                map.put("processName", processNameMap.get(s) + "小计");
                map.put("count", unqualifiedCountMap.get(s));
                data.add(map);
            }

            results.put("data", data);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/qualityManagement/badProcess/item", method = RequestMethod.POST, apiTitle = "工序不良分析(详细列表)")
    @ApiOperation("工序不良分析(详细列表)")
    public R badProcessItem(
            @ApiParam(value = "生产部门", required = true) @RequestParam(value = "deptId", defaultValue = "") Long deptId,
            @ApiParam(value = "工序ID", required = true) @RequestParam(value = "processId", defaultValue = "") Long processId,
            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("deptId", deptId);
        params.put("processId", processId);
        params.put("operator", operator);
        params.put("deviceId", deviceId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("isGroup", 1);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> badProcessLists = reportService.badProcessList(params);
        if (badProcessLists.size() > 0) {
            results.put("data", badProcessLists);
        }
        return R.ok(results);
    }

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

    @EvApiByToken(value = "/apis/qualityManagement/qualityTraceability", method = RequestMethod.POST, apiTitle = "质量追溯分析")
    @ApiOperation("质量追溯分析")
    public R qualityTraceability(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批号") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("batch", batch);
        params.put("deviceId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = reportService.qualityTraceabilityList(params);
        int total = reportService.qualityTraceabilityCount(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }


}