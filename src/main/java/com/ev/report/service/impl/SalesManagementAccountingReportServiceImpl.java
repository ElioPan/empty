package com.ev.report.service.impl;

import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.report.dao.SalesManagementAccountingReportDao;
import com.ev.report.service.SalesManagementAccountingReportService;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.ContractBillItemVO;
import com.ev.report.vo.ContractPayItemVO;
import com.ev.report.vo.StockOutItemVO;
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
public class SalesManagementAccountingReportServiceImpl implements SalesManagementAccountingReportService {

    @Autowired
    private SalesManagementAccountingReportDao reportDao;
    @Autowired
    private WarehouseAccountingReportService stockService;


    @Override
    public List<Map<String, Object>> salesContractList(Map<String, Object> params) {
        return reportDao.salesContractList(params);
    }

    @Override
    public List<Map<String, Object>> debtDueList(Map<String, Object> params) {
        return reportDao.debtDueList(params);
    }

    @Override
    public List<Map<String, Object>> summaryList(Map<String, Object> params) {
        return reportDao.summaryList(params);
    }

    @Override
    public List<Map<String, Object>> balanceList(Map<String, Object> params) {
        return reportDao.balanceList(params);
    }

    @Override
    public List<ContractBillItemVO> stockBillItem(Map<String, Object> params) {
        return reportDao.stockBillItem(params);
    }

    @Override
    public List<ContractPayItemVO> contractPayItem(Map<String, Object> params) {
        return reportDao.contractPayItem(params);
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult(Long clientId, String contractCode, Long contractId, Long deptId, Long userId, int showItemInt, int showTotalInt, String startTime, String endTime) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;
        params.put("clientId", clientId);
        params.put("contractCode", contractCode);
        params.put("contractId", contractId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForMES.OK_AUDITED);

        // 获取销售合同列表
        List<Map<String, Object>> salesContractList = this.salesContractList(params);
        if (salesContractList.size() > 0) {
            List<Object> itemIds = salesContractList
                    .stream()
                    .map(e -> e.get("id"))
                    .collect(Collectors.toList());

            List<Long> contractIds = salesContractList
                    .stream()
                    .map(e -> Long.parseLong(e.get("salesContractId").toString()))
                    .distinct()
                    .collect(Collectors.toList());

            params.clear();
            params.put("sourceIds", itemIds);
            params.put("sourceType", ConstantForGYL.XSHT);
            params.put("auditSign", ConstantForMES.OK_AUDITED);
            // 出库表
            List<StockOutItemVO> stockOutItemVOS = stockService.stockOutItem(params);
            params.put("sourceIds", contractIds);
            // 合同收款条件表
            List<ContractPayItemVO> contractPayItemVOS = this.contractPayItem(params);

            // 出库数量
            Map<Long, BigDecimal> stockCountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockOutItemVO::getSourceId
                            ,StockOutItemVO::getCount
                            ,BigDecimal::add)
                            );
            // 销售数量
            Map<Long, BigDecimal> salesCountGroup = salesContractList
                    .stream()
                    .collect(Collectors.toMap(k -> Long.parseLong(k.get("id").toString())
                            , v -> MathUtils.getBigDecimal(v.get("count"))));
            // 未出库数量
            Map<Long, BigDecimal> unStockCountGroup = Maps.newHashMap();
            for (Long id : salesCountGroup.keySet()) {
                unStockCountGroup.put(id, salesCountGroup.get(id).subtract(stockCountGroup.getOrDefault(id, BigDecimal.ZERO)));
            }

            // 销售发票表
            List<ContractBillItemVO> contractBillItemVO = this.stockBillItem(params);
            Map<Long, BigDecimal> billCountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.toMap(ContractBillItemVO::getSourceId
                            , ContractBillItemVO::getCount,BigDecimal::add));
            Map<Long, BigDecimal> billAmountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.toMap(ContractBillItemVO::getSourceId
                            , ContractBillItemVO::getAmount,BigDecimal::add));

            // 销售发票合计项
            Map<String, BigDecimal> totalBillCountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.toMap(ContractBillItemVO::getSourceCode
                            , ContractBillItemVO::getCount,BigDecimal::add));
            Map<String, BigDecimal> totalBillAmountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.toMap(ContractBillItemVO::getSourceCode
                            , ContractBillItemVO::getAmount,BigDecimal::add));

            // 销售出库合计项
            Map<String, BigDecimal> totalStockCountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockOutItemVO::getSourceCode
                            , StockOutItemVO::getCount,BigDecimal::add));
            Map<String, BigDecimal> totalStockAmountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(StockOutItemVO::getSourceCode
                            , StockOutItemVO::getAmount,BigDecimal::add));

            // 销售数量 销售金额
            Map<String, BigDecimal> totalSalesCountGroup = salesContractList
                    .stream()
                    .collect(Collectors.toMap(e -> e.get("contractCode").toString()
                            , e -> MathUtils.getBigDecimal(e.get("count")),BigDecimal::add));
            Map<String, BigDecimal> totalSalesAmountGroup = salesContractList
                    .stream()
                    .collect(Collectors.toMap(e -> e.get("contractCode").toString()
                            , e -> MathUtils.getBigDecimal(e.get("taxAmount")),BigDecimal::add));

            // 已收款金额 未收金额
            Map<Long, BigDecimal> totalReceivedAmountGroup = contractPayItemVOS
                    .stream()
                    .collect(Collectors.toMap(ContractPayItemVO::getSourceId
                            , ContractPayItemVO::getReceivedAmount,BigDecimal::add));
            Map<Long, BigDecimal> totalUnReceivedAmountGroup = contractPayItemVOS
                    .stream()
                    .collect(Collectors.toMap(ContractPayItemVO::getSourceId
                            , ContractPayItemVO::getUnReceivedAmount,BigDecimal::add));

            List<Map<String, Object>> showList = Lists.newArrayList();

            Map<String, Object> totalMap;
            List<Long> copyContractIds = new ArrayList<>(contractIds);

            for (Map<String, Object> map : salesContractList) {
                long itemId = Long.parseLong(map.get("id").toString());
                map.put("outCount", stockCountGroup.getOrDefault(itemId, BigDecimal.ZERO));
                map.put("unOutCount", unStockCountGroup.getOrDefault(itemId, BigDecimal.ZERO));
                map.put("billCount", billCountGroup.getOrDefault(itemId, BigDecimal.ZERO));
                map.put("billAmount", billAmountGroup.getOrDefault(itemId, BigDecimal.ZERO));

                // 总计
                if (showTotal) {
                    String sourceCode = map.get("contractCode").toString();
                    Long sourceId = Long.parseLong(map.get("salesContractId").toString());
                    if (copyContractIds.contains(sourceId)) {
                        totalMap = Maps.newHashMap();
                        totalMap.put("salesContractId", sourceId);
                        totalMap.put("contractCode", sourceCode + ConstantForReport.TOTAL_SUFFIX);
                        totalMap.put("contractDate", map.getOrDefault("contractDate", ""));
                        totalMap.put("deptName", map.getOrDefault("deptName", ""));
                        totalMap.put("clientName", map.getOrDefault("clientName", ""));
                        totalMap.put("userName", map.getOrDefault("userName", ""));
                        BigDecimal count = totalSalesCountGroup.getOrDefault(sourceCode, BigDecimal.ZERO);
                        totalMap.put("count", count);
                        totalMap.put("taxAmount", totalSalesAmountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        BigDecimal outCount = totalStockCountGroup.getOrDefault(sourceCode, BigDecimal.ZERO);
                        totalMap.put("outCount", outCount);
                        totalMap.put("unOutCount", count.subtract(outCount));
                        totalMap.put("outAmount", totalStockAmountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        totalMap.put("billCount", totalBillCountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        totalMap.put("billAmount", totalBillAmountGroup.getOrDefault(sourceCode, BigDecimal.ZERO));
                        totalMap.put("totalReceivedAmount", totalReceivedAmountGroup.getOrDefault(sourceId, BigDecimal.ZERO));
                        totalMap.put("totalUnReceivedAmount", totalUnReceivedAmountGroup.getOrDefault(sourceId, BigDecimal.ZERO));

                        totalMap.put("sortNo", 1);
                        totalMap.put("sign", ConstantForReport.COLOUR_END);
                        showList.add(totalMap);
                        copyContractIds.remove(sourceId);
                    }
                }
            }
            if (showItem) {
                showList.addAll(salesContractList);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("salesContractId").toString())))
                    .collect(Collectors.toList());

            Map<String, Object> totalResult = Maps.newHashMap();
            BigDecimal count = totalSalesCountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalResult.put("count", count);
            totalResult.put("taxAmount", totalSalesAmountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            BigDecimal outCount = totalStockCountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalResult.put("outCount", outCount);
            totalResult.put("unOutCount", count .subtract(outCount) );
            totalResult.put("outAmount", totalStockAmountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            totalResult.put("billCount", totalBillCountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            totalResult.put("billAmount", totalBillAmountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            totalResult.put("totalReceivedAmount", totalReceivedAmountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            totalResult.put("totalUnReceivedAmount", totalUnReceivedAmountGroup
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO));
            return Pair.of(collect,totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult(Long clientId, int showTotalInt, int showItemInt, Long deptId, Long userId, String endTime) {

        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("clientId", clientId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        List<Map<String, Object>> debtDueLists = this.debtDueList(params);
        debtDueLists = debtDueLists
                .stream()
                .filter(e->Integer.parseInt(e.get("expiryDays").toString())>0)
                .collect(Collectors.toList());

        if (debtDueLists.size() > 0) {

            List<Map<String, Object>> showList = Lists.newArrayList();

            // 应收
            Map<String, BigDecimal> receivableAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("receivableAmount"))
                            , BigDecimal::add));
            // 已收
            Map<String, BigDecimal> receivedAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("receivedAmount"))
                            , BigDecimal::add));

            // 未收
            Map<String, BigDecimal> unreceivedAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("unreceivedAmount"))
                            , BigDecimal::add));

            Map<String, String> clientNameMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> v.get("clientName").toString()
                            , (v1, v2) -> v1));

            if (showTotal) {
                Map<String, Object> map;
                for (String s : clientNameMap.keySet()) {
                    map = Maps.newHashMap();
                    map.put("clientId", s);
                    // 标记颜色
                    map.put("sign",ConstantForReport.COLOUR_END);
                    // 排序号
                    map.put("sortNo",1);
                    map.put("clientName", clientNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("receivableAmount", receivableAmountMap.get(s));
                    map.put("receivedAmount", receivedAmountMap.get(s));
                    map.put("unreceivedAmount", unreceivedAmountMap.get(s));
                    showList.add(map);
                }
            }
            if (showItem) {
                showList.addAll(debtDueLists);
            }
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("clientId").toString())))
                    .collect(Collectors.toList());

            // 合计项
            BigDecimal totalReceivableAmount = debtDueLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("receivableAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalReceivedAmount = debtDueLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("receivedAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalUnreceivedAmount = debtDueLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("unreceivedAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            Map<String,Object> totalResult = Maps.newHashMap();
            totalResult.put("totalReceivableAmount",totalReceivableAmount);
            totalResult.put("totalReceivedAmount",totalReceivedAmount);
            totalResult.put("totalUnreceivedAmount",totalUnreceivedAmount);
            return Pair.of(collect,totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> summaryResult(int showTotalInt, int showItemInt, String clientName, Long clientId, Long deptId, Long userId, String startTime, String endTime, Integer type) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;
        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("clientId", clientId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForMES.OK_AUDITED);
        List<Map<String, Object>> summaryLists = this.summaryList(params);
        if (summaryLists.size() > 0) {
            List<Map<String, Object>> showList = Lists.newArrayList();

            String typePrefix = "";
            switch (type) {
                case 1:
                    typePrefix = "client";
                    break;
                case 2:
                    typePrefix = "dept";
                    break;
                case 3:
                    typePrefix = "user";
                    break;
                default:
                    break;
            }
            final String finalTypeNameForMap = typePrefix + "Name";
            final String finalTypeIdForMap = typePrefix + "Id";

            Map<String, BigDecimal> countMap = summaryLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get(finalTypeIdForMap).toString()
                            , v -> MathUtils.getBigDecimal(v.get("count").toString())
                            , BigDecimal::add));

            Map<String, BigDecimal> amountMap = summaryLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get(finalTypeIdForMap).toString()
                            , v -> MathUtils.getBigDecimal(v.get("taxAmount").toString())
                            , BigDecimal::add));

            Map<String, String> typeNameMap = summaryLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get(finalTypeIdForMap).toString()
                            , v -> v.get(finalTypeNameForMap).toString()
                            , (v1, v2) -> v1));
            if (showTotal) {
                Map<String, Object> map;
                for (String s : typeNameMap.keySet()) {
                    map = Maps.newHashMap();
                    map.put(finalTypeIdForMap, s);
                    map.put(finalTypeNameForMap, typeNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("count", countMap.get(s));
                    map.put("taxAmount", amountMap.get(s));
                    map.put("sortNo",1);
                    map.put("sign",ConstantForReport.COLOUR_END);
                    showList.add(map);
                }
            }
            if (showItem) {
                showList.addAll(summaryLists);
            }
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get(finalTypeIdForMap).toString())))
                    .collect(Collectors.toList());

            Map<String,Object> totalResult = Maps.newHashMap();
            BigDecimal totalCount = summaryLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("count").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalAmount = summaryLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("taxAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalResult.put("totalCount",totalCount);
            totalResult.put("totalAmount",totalAmount);
            return Pair.of(collect,totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult(String clientName, Long clientId, int showTotalInt, int showItemInt, Long deptId, Long userId, String endTime) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;
        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("clientId", clientId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        List<Map<String, Object>> balanceLists = this.balanceList(params);
        if (balanceLists.size() > 0) {

            List<Map<String, Object>> showList = Lists.newArrayList();

            // 应收
            Map<String, BigDecimal> receivableAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("receivableAmount").toString())
                            , BigDecimal::add));
            // 已收
            Map<String, BigDecimal> receivedAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("receivedAmount").toString())
                            , BigDecimal::add));

            // 未收
            Map<String, BigDecimal> unreceivedAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("unreceivedAmount").toString())
                            , BigDecimal::add));

            Map<String, String> clientNameMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> v.get("clientName").toString()
                            , (v1, v2) -> v1));

            if (showTotal) {
                Map<String, Object> map;
                for (String s : clientNameMap.keySet()) {
                    map = Maps.newHashMap();
                    map.put("clientId", s);
                    // 标记颜色
                    map.put("sign",ConstantForReport.COLOUR_END);
                    // 排序号
                    map.put("sortNo",1);
                    map.put("clientName", clientNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("receivableAmount", receivableAmountMap.get(s));
                    map.put("receivedAmount", receivedAmountMap.get(s));
                    map.put("unreceivedAmount", unreceivedAmountMap.get(s));
                    showList.add(map);
                }
            }

            if (showItem) {
                showList.addAll(balanceLists);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("clientId").toString())))
                    .collect(Collectors.toList());

            // 合计项
            BigDecimal totalReceivableAmount = balanceLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("receivableAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalReceivedAmount = balanceLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("receivedAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalUnreceivedAmount = balanceLists
                    .stream()
                    .map(v -> MathUtils.getBigDecimal(v.get("unreceivedAmount").toString()))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            Map<String,Object> totalResult = Maps.newHashMap();
            totalResult.put("totalReceivableAmount",totalReceivableAmount);
            totalResult.put("totalReceivedAmount",totalReceivedAmount);
            totalResult.put("totalUnreceivedAmount",totalUnreceivedAmount);
            return Pair.of(collect,totalResult);
        }
        return null;
    }
}
