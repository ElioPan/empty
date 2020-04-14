package com.ev.report.service.impl;

import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.report.dao.OutsourcedManagementAccountingReportDao;
import com.ev.report.service.OutsourcedManagementAccountingReportService;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.ContractBillItemVO;
import com.ev.report.vo.ContractPayItemVO;
import com.ev.report.vo.StockInItemVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OutsourcedManagementAccountingReportServiceImpl implements OutsourcedManagementAccountingReportService {

    @Autowired
    private OutsourcedManagementAccountingReportDao reportDao;
    @Autowired
    private WarehouseAccountingReportService stockService;

    @Override
    public List<Map<String, Object>> debtDueList(Map<String, Object> params) {
        return reportDao.debtDueList(params);
    }

    @Override
    public List<Map<String, Object>> balanceList(Map<String, Object> params) {
        return reportDao.balanceList(params);
    }

    @Override
    public List<Map<String, Object>> outsourcedContractList(Map<String, Object> params) {
        return reportDao.outsourcedContractList(params);
    }

    @Override
    public List<Map<String, Object>> inOutReconciliationList(Map<String, Object> params) {
        return reportDao.inOutReconciliationList(params);
    }

    @Override
    public List<ContractPayItemVO> contractPayItem(Map<String, Object> params) {
        return reportDao.contractPayItem(params);
    }

    @Override
    public List<ContractBillItemVO> billItem(Map<String, Object> params) {
        return reportDao.billItem(params);
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult(String supplierName, Long materielType, Long deptId, Long userId, int showTotalInt, int showItemInt, String startTime, String endTime) {

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

        // 获取委外合同列表
        List<Map<String, Object>> outsourcedContractList = this.outsourcedContractList(params);
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
            // 入库表
            List<StockInItemVO> stockInItemVOS = stockService.stockInItem(params);
            params.put("sourceIds", contractIds);
            // 合同收款条件表
            List<ContractPayItemVO> contractPayItemVOS = this.contractPayItem(params);

            // 入库数量
            Map<Long, BigDecimal> stockCountGroup = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockInItemVO::getSourceId
                            , StockInItemVO::getCount
                            , BigDecimal::add));
            // 委外数量
            Map<Long, BigDecimal> outsourceCountGroup = outsourcedContractList
                    .stream()
                    .collect(Collectors.toMap(k -> Long.parseLong(k.get("id").toString()), v -> MathUtils.getBigDecimal(v.get("count").toString())));
            // 未入库数量
            Map<Long, BigDecimal> unStockCountGroup = Maps.newHashMap();
            for (Long id : outsourceCountGroup.keySet()) {
                unStockCountGroup.put(id, outsourceCountGroup.get(id).subtract(stockCountGroup.getOrDefault(id, BigDecimal.ZERO)));
            }

            // 加工费用表
            List<ContractBillItemVO> contractBillItemVO = this.billItem(params);
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
                map.put("unInCount", unStockCountGroup.getOrDefault(itemId, BigDecimal.ZERO));
                map.put("billCount", billCountGroup.getOrDefault(itemId, 0.0d));
                map.put("billAmount", billAmountGroup.getOrDefault(itemId, 0.0d));

                // 总计
                if (showTotal) {
                    String sourceCode = map.get("contractCode").toString();
                    Long sourceId = Long.parseLong(map.get("outsourcingContractId").toString());
                    if (copyContractIds.contains(sourceId)) {
                        totalMap = Maps.newHashMap();
                        totalMap.put("outsourcingContractId", sourceId);
                        totalMap.put("contractCode", sourceCode + ConstantForReport.TOTAL_SUFFIX);
                        totalMap.put("contractDate", map.getOrDefault("contractDate", ""));
                        totalMap.put("deptName", map.getOrDefault("deptName", ""));
                        totalMap.put("supplierName", map.getOrDefault("supplierName", ""));
                        Double count = totalSalesCountGroup.getOrDefault(sourceCode, 0.0d);
                        totalMap.put("count", count);
                        BigDecimal inCount = totalStockCountGroup.getOrDefault(sourceCode, BigDecimal.ZERO);
                        totalMap.put("inCount", inCount);
                        totalMap.put("unInCount", BigDecimal.valueOf(count).subtract(inCount));
                        totalMap.put("inAmount", totalStockAmountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        totalMap.put("billCount", totalBillCountGroup.getOrDefault(sourceCode, 0.0d));
                        totalMap.put("billAmount", totalBillAmountGroup.getOrDefault(sourceCode, 0.0d));
                        totalMap.put("totalPaidAmount", totalPaidAmountGroup.getOrDefault(sourceId, 0.0d));
                        totalMap.put("totalUnPaidAmount", totalUnPaidAmountGroup.getOrDefault(sourceId, 0.0d));

                        totalMap.put("sortNo", 1);
                        totalMap.put("sign", ConstantForReport.COLOUR_END);
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

            Map<String, Object> totalResult = Maps.newHashMap();
            Double count = totalSalesCountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d);
            totalResult.put("count", count);
            totalResult.put("taxAmount", totalSalesAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            BigDecimal inCount = totalStockCountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalResult.put("inCount", inCount);
            totalResult.put("unInCount", BigDecimal.valueOf(count).subtract(inCount));
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
            return Pair.of(collect, totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult(int showTotalInt, int showItemInt, Long supplierId, Long deptId, Long userId, String endTime) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        List<Map<String, Object>> debtDueLists = this.debtDueList(params);
        debtDueLists = debtDueLists
                .stream()
                .filter(e -> Integer.parseInt(e.get("expiryDays").toString()) > 0)
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
                    map.put("sign", ConstantForReport.COLOUR_END);
                    // 排序号
                    map.put("sortNo", 1);
                    map.put("supplierName", supplierNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
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
            return Pair.of(collect, totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult(Long supplierId, int showTotalInt, int showItemInt, Long deptId, Long userId, String endTime) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        List<Map<String, Object>> balanceLists = this.balanceList(params);
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
                    map.put("sign", ConstantForReport.COLOUR_END);
                    // 排序号
                    map.put("sortNo", 1);
                    map.put("supplierName", supplierNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
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
            return Pair.of(collect, totalResult);
        }
        return null;
    }
}
