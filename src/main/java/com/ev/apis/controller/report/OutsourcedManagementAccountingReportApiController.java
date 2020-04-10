package com.ev.apis.controller.report;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.OutsourcedManagementAccountingReportService;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.ContractBillItemVO;
import com.ev.report.vo.ContractPayItemVO;
import com.ev.report.vo.StockInItemVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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
    @Autowired
    private WarehouseAccountingReportService stockService;


    @EvApiByToken(value = "/apis/outsourcedManagement/tracking", method = RequestMethod.POST, apiTitle = "委外全程跟踪")
    @ApiOperation("委外全程跟踪")
    public R tracking(
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

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
            List<Object> itemIds = outsourcedContractList
                    .stream()
                    .map(e -> e.get("id"))
                    .collect(Collectors.toList());

            List<Long> contractIds = outsourcedContractList
                    .stream()
                    .map(e -> Long.parseLong(e.get("outsourcingContractId").toString()))
                    .distinct()
                    .collect(Collectors.toList());

            params.clear();
            params.put("sourceIds", itemIds);
            params.put("sourceType", ConstantForGYL.XSHT);
            params.put("auditSign", ConstantForMES.OK_AUDITED);
            // 出库表
            List<StockInItemVO> stockInItemVOS = stockService.stockInItem(params);
            params.put("sourceIds", contractIds);
            // 合同收款条件表
            List<ContractPayItemVO> contractPayItemVOS = reportService.contractPayItem(params);

            Map<Long, BigDecimal> stockCountGroup = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockInItemVO::getSourceId
                            , StockInItemVO::getCount
                            , BigDecimal::add));
            Map<Long, BigDecimal> stockAmountGroup = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockInItemVO::getSourceId
                            , StockInItemVO::getAmount
                            , BigDecimal::add));

            // 加工费用表
            List<ContractBillItemVO> contractBillItemVO = reportService.billItem(params);
            Map<Long, Double> billCountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceId
                            , Collectors.summingDouble(ContractBillItemVO::getCount)));
            Map<Long, Double> billAmountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceId
                            , Collectors.summingDouble(ContractBillItemVO::getAmount)));

            // 加工费用合计项
            Map<String, Double> totalBillCountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceCode
                            , Collectors.summingDouble(ContractBillItemVO::getCount)));
            Map<String, Double> totalBillAmountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceCode
                            , Collectors.summingDouble(ContractBillItemVO::getAmount)));

            // 委外入库合计项
            Map<String, BigDecimal> totalStockCountGroup = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockInItemVO::getSourceCode
                            , StockInItemVO::getAmount
                            , BigDecimal::add));
            Map<String, BigDecimal> totalStockAmountGroup = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockInItemVO::getSourceCode
                            , StockInItemVO::getAmount
                            , BigDecimal::add));

            // 委外数量 委外金额
            Map<String, Double> totalSalesCountGroup = outsourcedContractList
                    .stream()
                    .collect(Collectors.groupingBy(e -> e.get("contractCode").toString()
                            , Collectors.summingDouble(e -> Double.parseDouble(e.get("count").toString()))));
            Map<String, Double> totalSalesAmountGroup = outsourcedContractList
                    .stream()
                    .collect(Collectors.groupingBy(e -> e.get("contractCode").toString()
                            , Collectors.summingDouble(e -> Double.parseDouble(e.get("taxAmount").toString()))));
            // 已收款金额 未收金额
            Map<Long, Double> totalPaidAmountGroup = contractPayItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(ContractPayItemVO::getSourceId
                            , Collectors.summingDouble(ContractPayItemVO::getReceivedAmount)));
            Map<Long, Double> totalUnPaidAmountGroup = contractPayItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(ContractPayItemVO::getSourceId
                            , Collectors.summingDouble(ContractPayItemVO::getUnReceivedAmount)));

            List<Map<String, Object>> showList = Lists.newArrayList();

            Map<String, Object> totalMap;
            List<Long> copyContractIds = new ArrayList<>(contractIds);

            for (Map<String, Object> map : outsourcedContractList) {
                long itemId = Long.parseLong(map.get("id").toString());
                map.put("inCount", stockCountGroup.getOrDefault(itemId, BigDecimal.ZERO));
                map.put("inAmount", stockAmountGroup.getOrDefault(itemId, BigDecimal.ZERO));
                map.put("billCount", billCountGroup.getOrDefault(itemId, 0.0d));
                map.put("billAmount", billAmountGroup.getOrDefault(itemId, 0.0d));

                // 总计
                if (showTotal) {
                    String sourceCode = map.get("contractCode").toString();
                    Long sourceId = Long.parseLong(map.get("outsourcingContractId").toString());
                    if (copyContractIds.contains(sourceId)) {
                        totalMap = Maps.newHashMap();
                        totalMap.put("outsourcingContractId", sourceId);
                        totalMap.put("contractCode", sourceCode + "小计");
                        totalMap.put("contractDate", map.getOrDefault("contractDate", ""));
                        totalMap.put("deptName", map.getOrDefault("deptName", ""));
                        totalMap.put("supplierName", map.getOrDefault("supplierName", ""));

                        totalMap.put("count", totalSalesCountGroup.getOrDefault(sourceCode, 0.0d));
                        totalMap.put("taxAmount", totalSalesAmountGroup.getOrDefault(sourceCode, 0.0d));
                        totalMap.put("inCount", totalStockCountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        totalMap.put("inAmount", totalStockAmountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        totalMap.put("billCount", totalBillCountGroup.getOrDefault(sourceCode, 0.0d));
                        totalMap.put("billAmount", totalBillAmountGroup.getOrDefault(sourceCode, 0.0d));
                        totalMap.put("totalPaidAmount", totalPaidAmountGroup.getOrDefault(sourceId, 0.0d));
                        totalMap.put("totalUnPaidAmount", totalUnPaidAmountGroup.getOrDefault(sourceId, 0.0d));

                        totalMap.put("sortNo", 1);
                        totalMap.put("sign", "end");
                        showList.add(totalMap);
                        copyContractIds.remove(sourceId);
                    }
                }
            }
            if (showItem) {
                showList.addAll(outsourcedContractList);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("outsourcingContractId").toString())))
                    .collect(Collectors.toList());
            results.put("data", collect);
            Map<String, Object> totalResult = Maps.newHashMap();
            totalResult.put("count", totalSalesCountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("taxAmount", totalSalesAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("inCount", totalStockCountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            totalResult.put("inAmount", totalStockAmountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            totalResult.put("billCount", totalBillCountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("billAmount", totalBillAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("totalPaidAmount", totalPaidAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("totalUnPaidAmount", totalUnPaidAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            results.put("total", totalResult);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/debtDue", method = RequestMethod.POST, apiTitle = "委外到期债务")
    @ApiOperation("委外到期债务")
    public R debtDue(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> debtDueLists = reportService.debtDueList(params);
        debtDueLists = debtDueLists
                .stream()
                .filter(e->Integer.parseInt(e.get("expiryDays").toString())>0)
                .collect(Collectors.toList());

        if (debtDueLists.size() > 0) {

            List<Map<String, Object>> showList = Lists.newArrayList();

            // 应付
            Map<String, Double> payableAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payableAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> paidAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("paidAmount").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unpaidAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unpaidAmount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = debtDueLists
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
                    map.put("payableAmount", payableAmountMap.get(s));
                    map.put("paidAmount", paidAmountMap.get(s));
                    map.put("unpaidAmount", unpaidAmountMap.get(s));
                    showList.add(map);
                }
            }
            if (showItem) {
                showList.addAll(debtDueLists);
            }
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("supplierId").toString())))
                    .collect(Collectors.toList());
            results.put("data", collect);

            // 合计项
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
            Map<String, Object> totalResult = Maps.newHashMap();
            totalResult.put("totalPayableAmount", totalPayableAmount);
            totalResult.put("totalPaidAmount", totalPaidAmount);
            totalResult.put("totalUnpaidAmount", totalUnpaidAmount);
            results.put("total", totalResult);
        }
        return R.ok(results);
    }

//    @EvApiByToken(value = "/apis/outsourcedManagement/debtDue/item", method = RequestMethod.POST, apiTitle = "委外到期债务(详细)")
//    @ApiOperation("委外到期债务(详细)")
//    public R debtDueItem(
//            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
//            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
//            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("supplierId", supplierId);
//        params.put("deptId", deptId);
//        params.put("userId", userId);
//        params.put("endTime", endTime);
//
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> debtDueLists = reportService.debtDueList(params);
//        if (debtDueLists.size() > 0) {
//            results.put("data", debtDueLists);
//        }
//        return R.ok(results);
//    }

    @EvApiByToken(value = "/apis/outsourcedManagement/balance", method = RequestMethod.POST, apiTitle = "委外合同余额")
    @ApiOperation("委外合同余额")
    public R balance(
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
        if (balanceLists.size() > 0) {

            List<Map<String, Object>> showList = Lists.newArrayList();

            // 应付
            Map<String, Double> payableAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payableAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> paidAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("paidAmount").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unpaidAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unpaidAmount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = balanceLists
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
                    map.put("payableAmount", payableAmountMap.get(s));
                    map.put("paidAmount", paidAmountMap.get(s));
                    map.put("unpaidAmount", unpaidAmountMap.get(s));
                    showList.add(map);
                }
            }

            if (showItem) {
                showList.addAll(balanceLists);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("supplierId").toString())))
                    .collect(Collectors.toList());
            results.put("data", collect);

            // 合计项
            Double totalReceivableAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payableAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("paidAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnpaidAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unpaidAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Map<String, Object> totalResult = Maps.newHashMap();
            totalResult.put("totalPayableAmount", totalReceivableAmount);
            totalResult.put("totalPaidAmount", totalReceivedAmount);
            totalResult.put("totalUnpaidAmount", totalUnpaidAmount);
            results.put("total", totalResult);

        }
        return R.ok(results);
    }

//    @EvApiByToken(value = "/apis/outsourcedManagement/balance/item", method = RequestMethod.POST, apiTitle = "委外合同余额(详细)")
//    @ApiOperation("委外合同余额(详细)")
//    public R balanceItem(
//            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
//            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
//            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("supplierId", supplierId);
//        params.put("deptId", deptId);
//        params.put("userId", userId);
//        params.put("endTime", endTime);
//
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
//        if (balanceLists.size() > 0) {
//            results.put("data", balanceLists);
//        }
//        return R.ok(results);
//    }

    @EvApiByToken(value = "/apis/outsourcedManagement/inOutReconciliation", method = RequestMethod.POST, apiTitle = "委外收发对账")
    @ApiOperation("委外收发对账")
    public R inOutReconciliation(
//            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
//            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
//            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
//        params.put("offset", (pageno - 1) * pagesize);
//        params.put("limit", pagesize);
        params.put("supplierId", supplierId);
//        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);

        params.put("outboundType", ConstantForGYL.WWCK);
        params.put("auditSign", ConstantForMES.OK_AUDITED);
        Map<String, Object> results = Maps.newHashMap();

        // 获取委外合同列表
        List<Map<String, Object>> inOutReconciliationList = reportService.inOutReconciliationList(params);
        if (inOutReconciliationList.size() > 0) {
//            List<String> supplierIds = inOutReconciliationList
//                    .stream()
//                    .map(e -> e.get("supplierId").toString())
//                    .distinct()
//                    .collect(Collectors.toList());
//            int total = supplierIds.size();
//            // 将供应商分页
//            List<String> supplierIdsPage = PageUtils.startPage(supplierIds, pageno, pagesize);
//            // 获取分页后的供应商检验单
//            List<Map<String, Object>> badPurchaseList = inOutReconciliationList
//                    .stream()
//                    .filter(e -> supplierIdsPage.contains(e.get("supplierId").toString()))
//                    .collect(Collectors.toList());

            results.put("data", inOutReconciliationList);
        }
        return R.ok(results);
    }
}
