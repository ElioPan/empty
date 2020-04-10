package com.ev.report.service.impl;

import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.report.dao.PurchaseManagementAccountingReportDao;
import com.ev.report.service.PurchaseManagementAccountingReportService;
import com.ev.report.vo.PurchaseContractVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseManagementAccountingReportServiceImpl implements PurchaseManagementAccountingReportService {

    @Autowired
    private PurchaseManagementAccountingReportDao reportDao;

    @Override
    public List<Map<String, Object>> getInCount(Map<String, Object> params) {
        return reportDao.getInCount(params);
    }

    @Override
    public List<Map<String, Object>> getInvoiceCountAndAmount(Map<String, Object> params) {
        return reportDao.getInvoiceCountAndAmount(params);
    }

    @Override
    public List<Map<String, Object>> getPayAmount(Map<String, Object> params) {
        return reportDao.getPayAmount(params);
    }

    @Override
    public List<Map<String, Object>> purchaseContractList(Map<String, Object> params) {
        return reportDao.purchaseContractList(params);
    }

    @Override
    public List<Map<String, Object>> debtDueList(Map<String, Object> params) {
        return reportDao.debtDueList(params);
    }

    @Override
    public List<PurchaseContractVO> priceAnalysisList(Map<String, Object> params) {
        return reportDao.priceAnalysisList(params);
    }

    @Override
    public List<Map<String, Object>> balanceList(Map<String, Object> params) {
        return reportDao.balanceList(params);
    }



    @Override
    public R disposeTracking(Map<String, Object> params, int pageno,int pagesize) {

        Map<String, Object> results = Maps.newHashMap();
        // 采购合同
        List<Map<String, Object>> purchaseContractList = this.purchaseContractList(params);
        if (purchaseContractList.size() > 0) {

            List<String> sourceIds = purchaseContractList
                    .stream()
                    .map(e -> e.get("id").toString())
                    .collect(Collectors.toList());
            List<String> contractIds = purchaseContractList
                    .stream()
                    .map(e -> e.get("purchaseContractId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, String> contractCode = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> v.get("contractCode").toString()
                            , (v1, v2) -> v1));

            Map<String, Object> peramy = new HashMap<>();
            peramy.put("auditSign", ConstantForGYL.OK_AUDITED);
            peramy.put("storageType", ConstantForGYL.PURCHASE_INSTOCK);
            peramy.put("sourceId", sourceIds);

            //入库（itemId）
            List<Map<String, Object>> inStockCount = this.getInCount(peramy);
            Map<String, Double> inStockCountMap = inStockCount
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("sourceId").toString()
                            , v -> Double.parseDouble(v.get("count").toString())
                            , Double::sum));
            //采购发票（itemId）
            List<Map<String, Object>> invoiceCountAndAmount = this.getInvoiceCountAndAmount(peramy);
            Map<String, Double> invoiceCountMap = invoiceCountAndAmount
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("sourceId").toString()
                            , v -> Double.parseDouble(v.get("count").toString())
                            , Double::sum));
            Map<String, Double> invoiceAmountMap = invoiceCountAndAmount
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("sourceId").toString()
                            , v -> Double.parseDouble(v.get("taxAmount").toString())
                            , Double::sum));

            for (Map<String, Object> purchaseContract : purchaseContractList) {

                BigDecimal unStoredCount = (new BigDecimal(purchaseContract.getOrDefault("count","0").toString()))
                        .subtract(new BigDecimal(String.valueOf(inStockCountMap.getOrDefault(purchaseContract.get("id"),0.0))));
                Double invoicedAmount = invoiceAmountMap.getOrDefault("id", 0.0);
                Double invoicedCount = invoiceCountMap.getOrDefault("id", 0.0);
                purchaseContract.put("inStockCount", inStockCountMap.getOrDefault(purchaseContract.get("id"), 0.0));
                purchaseContract.put("unStoredCount", unStoredCount);
                purchaseContract.put("invoicedAmount", invoicedAmount);
                purchaseContract.put("invoicedCount", invoicedCount);
            }

            //付款(contractIds)
            peramy.put("contractId", contractIds);
            List<Map<String, Object>> payAmount = this.getPayAmount(peramy);

            Map<String, Double> totailAmountPaid = payAmount
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("amountPaid").toString())
                            , Double::sum));
            Map<String, Double> totailUnpayAmount = payAmount
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("unpayAmount").toString())
                            , Double::sum));
            //小计
            Map<String, Double> totailPurchseCount = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("count").toString())
                            , Double::sum));
            Map<String, Double> totailPurchseAmount = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("taxAmount").toString())
                            , Double::sum));
            Map<String, Double> totailUnStoredCounts = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("unStoredCount").toString())
                            , Double::sum));
            Map<String, Double> totailInStoredCount = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("inStockCount").toString())
                            , Double::sum));
            Map<String, Double> totailInvoicedCounts = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("invoicedCount").toString())
                            , Double::sum));
            Map<String, Double> totailInvoicedAmounts = purchaseContractList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("purchaseContractId").toString()
                            , v -> Double.parseDouble(v.get("invoicedAmount").toString())
                            , Double::sum));
            List<Map<String, Object>> totailList = Lists.newArrayList();

            BigDecimal totailCount = BigDecimal.ZERO;
            BigDecimal totailTaxAmount = BigDecimal.ZERO;
            BigDecimal totailInStockCount = BigDecimal.ZERO;
            BigDecimal totailUnStoredCount = BigDecimal.ZERO;
            BigDecimal totailInvoicedAmount = BigDecimal.ZERO;
            BigDecimal totailInvoicedCount = BigDecimal.ZERO;
            BigDecimal totailAmountPaids = BigDecimal.ZERO;
            BigDecimal totailUnpayAmounts = BigDecimal.ZERO;

//            for (String contractId : contractIds) {
            for (int i=0;i< contractIds.size();i++) {
                Map<String, Object> totailMap = new HashMap<>();
                String contractId=contractIds.get(i);
                totailMap.put("sign", 1);
                totailMap.put("purchaseContractId", contractId);
                totailMap.put("sortNo", 1);
                totailMap.put("contractCode", "合同" + contractCode.get(contractId) + "小计");
                totailMap.put("count", totailPurchseCount.get(contractId));
                totailCount = totailCount.add(new BigDecimal(totailPurchseCount.getOrDefault("contractId",0.0).toString()));
                totailMap.put("taxAmount", totailPurchseAmount.get(contractId));
                totailTaxAmount = totailTaxAmount.add(new BigDecimal(totailPurchseAmount.getOrDefault(contractId,0.0).toString()));
                totailMap.put("inStockCount", totailInStoredCount.get(contractId));
                totailInStockCount = totailInStockCount.add(new BigDecimal(totailInStoredCount.getOrDefault(contractId,0.0).toString()));
                totailMap.put("unStoredCount", totailUnStoredCounts.get(contractId));
                totailUnStoredCount = totailUnStoredCount.add(new BigDecimal(totailUnStoredCounts.getOrDefault(contractId,0.0).toString()));
                totailMap.put("invoicedAmount", totailInvoicedCounts.get(contractId));
                totailInvoicedAmount = totailInvoicedAmount.add(new BigDecimal(totailInvoicedCounts.getOrDefault(contractId,0.0).toString()));
                totailMap.put("invoicedCount", totailInvoicedAmounts.get(contractId));
                totailInvoicedCount = totailInvoicedCount.add(new BigDecimal(totailInvoicedAmounts.getOrDefault(contractId,0.0).toString()));
                totailMap.put("amountPaid", totailAmountPaid.get(contractId));
                totailAmountPaids = totailInvoicedCount.add(new BigDecimal(totailAmountPaid.getOrDefault(contractId,0.0).toString()));
                totailMap.put("unpayAmount", totailUnpayAmount.get(contractId));
                totailUnpayAmounts = totailUnpayAmounts.add(new BigDecimal(totailUnpayAmount.getOrDefault(contractId,0.0).toString()));
                totailList.add(totailMap);
            }
            Map<String, Object> total = new HashMap<>();
            total.put("totailCount",totailCount );
            total.put("totailTaxAmount",totailTaxAmount );
            total.put("totailInStockCount",totailInStockCount );
            total.put("totailUnStoredCount",totailUnStoredCount );
            total.put("totailInvoicedAmount", totailInvoicedAmount);
            total.put("totailInvoicedCount", totailInvoicedCount);
            total.put("totailAmountPaids",totailAmountPaids );
            total.put("totailUnpayAmounts", totailUnpayAmounts);

            totailList.addAll(purchaseContractList);

            List<Map<String, Object>> collect = totailList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("purchaseContractId").toString())))
                    .collect(Collectors.toList());
            results.put("data", collect);
            results.put("total", total);
        }
        return R.ok(results);
    }







}
