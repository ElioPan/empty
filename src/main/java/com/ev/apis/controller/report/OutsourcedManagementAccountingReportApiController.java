package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.OutsourcedManagementAccountingReportService;
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
 * 委外管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-27 09:51:41
 */
@Api(value = "/", tags = "委外管理报表分析API")
@RestController
public class OutsourcedManagementAccountingReportApiController {
    @Autowired
    private OutsourcedManagementAccountingReportService reportService;

    @EvApiByToken(value = "/apis/outsourcedManagement/tracking", method = RequestMethod.POST, apiTitle = "委外全程跟踪")
    @ApiOperation("委外全程跟踪")
    public R tracking(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "委外员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("materielType", materielType);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForMES.OK_AUDITED);
        Map<String, Object> results = Maps.newHashMap();

        // 获取委外合同列表
        List<Map<String, Object>> outsourcedContractList = reportService.outsourcedContractList(params);
        if (outsourcedContractList.size() > 0) {
            List<String> supplierIds = outsourcedContractList
                    .stream()
                    .map(e -> e.get("supplierId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = supplierIds.size();
            // 将供应商分页
            List<String> supplierIdsPage = PageUtils.startPage(supplierIds, pageno, pagesize);
            // 获取分页后的供应商检验单
            List<Map<String, Object>> badPurchaseList = outsourcedContractList
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

    @EvApiByToken(value = "/apis/outsourcedManagement/debtDue", method = RequestMethod.POST, apiTitle = "委外到期债务(供应商小计)")
    @ApiOperation("委外到期债务(供应商小计)")
    public R debtDue(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
//            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

//        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> debtDueLists = reportService.debtDueList(params);
        if (debtDueLists.size() > 0) {
            // 获取所有的供应商
            List<String> supplierIds = debtDueLists
                    .stream()
                    .map(e -> e.get("supplierId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = supplierIds.size();
            // 将供应商分页
            List<String> supplierIdsPage = PageUtils.startPage(supplierIds, pageno, pagesize);

            // 获取分页下的部门的所有产品检验单
            List<Map<String, Object>> debtDueList = debtDueLists
                    .stream()
                    .filter(e -> supplierIdsPage.contains(e.get("supplierId").toString()))
                    .collect(Collectors.toList());

            // 应付
            Map<String, Double> payableAmountMap = debtDueList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payableAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> paidAmountMap = debtDueList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("paidAmount").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unpaidAmountMap = debtDueList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unpaidAmount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = debtDueList
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
                map.put("totalPayableAmount", payableAmountMap.get(s));
                map.put("totalPaidAmount", paidAmountMap.get(s));
                map.put("totalUnpaidAmount", unpaidAmountMap.get(s));
                data.add(map);
            }
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));

            Double totalPayableAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payableAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalPaidAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("paidAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnpaidAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unpaidAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("totalPayableAmount",totalPayableAmount);
            results.put("totalPaidAmount",totalPaidAmount);
            results.put("totalUnpaidAmount",totalUnpaidAmount);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/debtDue/item", method = RequestMethod.POST, apiTitle = "委外到期债务(详细)")
    @ApiOperation("委外到期债务(详细)")
    public R debtDueItem(
            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "委外员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> debtDueLists = reportService.debtDueList(params);
        if (debtDueLists.size() > 0) {
            results.put("data", debtDueLists);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/summary", method = RequestMethod.POST, apiTitle = "委外汇总统计(小计)")
    @ApiOperation("委外汇总统计(小计)")
    public R summary(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:0供应商，1部门，2委外员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForMES.OK_AUDITED);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> summaryLists = reportService.summaryList(params);
        if (summaryLists.size() > 0) {
            String typePrefix = "";
            switch (type) {
                case 0:
                    typePrefix = "client";
                    break;
                case 1:
                    typePrefix = "dept";
                    break;
                case 2:
                    typePrefix = "user";
                    break;
                default:
                    break;
            }
            final String finalTypeNameForMap = typePrefix + "Name";
            final String finalTypeIdForMap = typePrefix + "Id";

            List<String> typeIds = summaryLists
                    .stream()
                    .map(e -> e.get(finalTypeIdForMap).toString())
                    .filter(e->!"".equals(e))
                    .distinct()
                    .collect(Collectors.toList());

            int total = typeIds.size();
            // 将数据分页
            List<String> typeIdsPage = PageUtils.startPage(typeIds, pageno, pagesize);

            // 获取分页下的所有小计

            List<Map<String, Object>> summaryList = summaryLists
                    .stream()
                    .filter(e -> typeIdsPage.contains(e.get(finalTypeIdForMap).toString()))
                    .collect(Collectors.toList());

            Map<String, Double> countMap = summaryList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get(finalTypeIdForMap).toString()
                            , v -> Double.parseDouble(v.get("count").toString())
                            , Double::sum));

            Map<String, Double> amountMap = summaryList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get(finalTypeIdForMap).toString()
                            , v -> Double.parseDouble(v.get("taxAmount").toString())
                            , Double::sum));

            Map<String, String> typeNameMap = summaryList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get(finalTypeIdForMap).toString()
                            , v -> v.get(finalTypeNameForMap).toString()
                            , (v1, v2) -> v1));
            List<Map<String, Object>> data = Lists.newArrayList();
            Map<String, Object> map;
            for (String s : typeNameMap.keySet()) {
                map = Maps.newHashMap();
                map.put(finalTypeIdForMap, s);
                map.put(finalTypeNameForMap, typeNameMap.get(s) + "小计");
                map.put("count", countMap.get(s));
                map.put("amount", amountMap.get(s));
                data.add(map);
            }

            results.put("data", new DsResultResponse(pageno, pagesize, total, data));

            Double totalCount = summaryLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("count").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalAmount = summaryLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("taxAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("totalCount",totalCount);
            results.put("totalAmount",totalAmount);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/summary/item", method = RequestMethod.POST, apiTitle = "委外汇总统计(详细)")
    @ApiOperation("委外汇总统计(详细)")
    public R summaryItem(
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:0供应商，1部门，2委外员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        switch (type){
            case 0:
                if (supplierId == null) {
                    return R.ok();
                }
                break;
            case 1:
                if (deptId == null) {
                    return R.ok();
                }
                break;
            case 2:
                if (userId == null) {
                    return R.ok();
                }
                break;
            default:
                return R.ok();
        }

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForMES.OK_AUDITED);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> summaryLists = reportService.summaryList(params);
        if (summaryLists.size() > 0) {
            results.put("data", summaryLists);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/outsourcedManagement/balance", method = RequestMethod.POST, apiTitle = "委外合同余额(供应商小计)")
    @ApiOperation("委外合同余额(供应商小计)")
    public R balance(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
//            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,

            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "委外员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

//        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
        if (balanceLists.size() > 0) {
            // 获取所有的供应商
            List<String> supplierIds = balanceLists
                    .stream()
                    .map(e -> e.get("supplierId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = supplierIds.size();
            // 将生产部门分页
            List<String> supplierIdsPage = PageUtils.startPage(supplierIds, pageno, pagesize);

            // 获取分页下的部门的所有产品检验单
            List<Map<String, Object>> balanceList = balanceLists
                    .stream()
                    .filter(e -> supplierIdsPage.contains(e.get("supplierId").toString()))
                    .collect(Collectors.toList());

            // 应付
            Map<String, Double> payableAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payableAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> paidAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("paidAmount").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unpaidAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unpaidAmount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = balanceList
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
                map.put("totalPayableAmount", payableAmountMap.get(s));
                map.put("totalPaidAmount", paidAmountMap.get(s));
                map.put("totalUnpaidAmount", unpaidAmountMap.get(s));
                data.add(map);
            }

            results.put("data", new DsResultResponse(pageno, pagesize, total, data));

            Double totalPayableAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payableAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalPaidAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("paidAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnpaidAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unpaidAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("totalPayableAmount",totalPayableAmount);
            results.put("totalPaidAmount",totalPaidAmount);
            results.put("totalUnpaidAmount",totalUnpaidAmount);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/balance/item", method = RequestMethod.POST, apiTitle = "委外合同余额(详细)")
    @ApiOperation("委外合同余额(详细)")
    public R balanceItem(
            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "委外员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);

        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
        if (balanceLists.size() > 0) {
            results.put("data", balanceLists);
        }
        return R.ok(results);
    }


}
