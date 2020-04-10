package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.report.dao.PurchaseManagementAccountingReportDao;
import com.ev.report.service.PurchaseManagementAccountingReportService;
import com.ev.report.vo.PurchaseContractVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
    public R disposeTracking(Map<String, Object> params, int showItem,int showUser) {

        Map<String, Object> results = Maps.newHashMap();
        // 采购合同
        if(Objects.equals(0,showItem)&&Objects.equals(0,showUser)){
            return R.ok(results);
        }
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
                totailMap.put("sign","end");
                totailMap.put("purchaseContractId", contractId);
                totailMap.put("sortNo", 1);
                totailMap.put("contractCode", "合同" + contractCode.get(contractId) + "小计");
                totailMap.put("count", totailPurchseCount.get(contractId));
                totailCount = totailCount.add(new BigDecimal(totailPurchseCount.getOrDefault(contractId,0.0).toString()));
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

            List<Map<String, Object>> ultimatelyDate = Lists.newArrayList();
            if(Objects.equals(0,showItem)&&Objects.equals(1,showUser)){
                ultimatelyDate.addAll(totailList);
            }else if(Objects.equals(1,showItem)&&Objects.equals(0,showUser)){
                ultimatelyDate.addAll(purchaseContractList);
            }else if(Objects.equals(1,showItem)&&Objects.equals(1,showUser)){
                totailList.addAll(purchaseContractList);
                ultimatelyDate.addAll(totailList);
            }
            List<Map<String, Object>> collect = ultimatelyDate.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("purchaseContractId").toString())))
                    .collect(Collectors.toList());
            results.put("data", collect);
            results.put("total", total);
        }
        return R.ok(results);
    }


    @Override
    public R disposeDebtDue(Map<String, Object> params, int showItem,int showUser) {
        Map<String, Object> results = Maps.newHashMap();

        if(Objects.equals(0,showItem)&&Objects.equals(0,showUser)){
            return R.ok(results);
        }
        List<Map<String, Object>> debtDueLists = this.debtDueList(params);
        if (debtDueLists.size() > 0) {

            // 应付
            Map<String, Double> payAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> amountPaidMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("amountPaid").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unPayAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unPayAmount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> v.get("supplierName").toString()
                            , (v1, v2) -> v1));
//            Map<String, Double> expiryDays = debtDueLists
//                    .stream()
//                    .collect(Collectors.toMap(
//                            k -> k.get("supplierId").toString()
//                            , v -> Double.parseDouble(v.get("expiryDays").toString())
//                            , Double::sum));

            List<Map<String, Object>> totalList = Lists.newArrayList();
            Map<String, Object> totalMap;
            for (String supplierId : supplierNameMap.keySet()) {
                totalMap = Maps.newHashMap();
                totalMap.put("sign","end");
                totalMap.put("sortNo", 1);
                totalMap.put("supplierId", supplierId);
                totalMap.put("supplierName", supplierNameMap.get(supplierId) + "小计");
                totalMap.put("payAmount", payAmountMap.get(supplierId));
                totalMap.put("amountPaid", amountPaidMap.get(supplierId));
                totalMap.put("unPayAmount", unPayAmountMap.get(supplierId));
//                totalMap.put("expiryDays", expiryDays.get(supplierId));
                totalList.add(totalMap);
            }
            List<Map<String, Object>> ultimatelyDate = Lists.newArrayList();

            if(Objects.equals(0,showItem)&&Objects.equals(1,showUser)){
                ultimatelyDate.addAll(totalList);
            }else if(Objects.equals(1,showItem)&&Objects.equals(0,showUser)){
                ultimatelyDate.addAll(debtDueLists);
            }else if(Objects.equals(1,showItem)&&Objects.equals(1,showUser)){
                totalList.addAll(debtDueLists);
                ultimatelyDate.addAll(totalList);
            }
            List<Map<String, Object>> collect = ultimatelyDate.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("supplierId").toString())))
                    .collect(Collectors.toList());

            Double totalReceivableAmount = totalList
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = totalList
                    .stream()
                    .map(v -> Double.parseDouble(v.get("amountPaid").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnreceivedAmount = totalList
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unPayAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
//            Double totalExpiryDays = collect
//                    .stream()
//                    .map(v -> Double.parseDouble(v.get("expiryDays").toString()))
//                    .reduce(Double::sum)
//                    .orElse(0.0d);
            results.put("ultimatelyDate",collect);
            results.put("totalPayAmount",totalReceivableAmount);
            results.put("totalAmountPaid",totalReceivedAmount);
            results.put("totalUnPayAmount",totalUnreceivedAmount);
//            results.put("totalExpiryDays",totalExpiryDays);
        }
        return R.ok(results);
    }


    @Override
    public R disposePriceAnalysis(Map<String, Object> params,int pageno,int pagesize ){

        Map<String, Object> results = Maps.newHashMap();
        // 获取采购合同列表
        List<PurchaseContractVO> priceAnalysisLists = this.priceAnalysisList(params);
        if (priceAnalysisLists.size() > 0) {
            List<String> supplierIds = priceAnalysisLists
                    .stream()
                    .map(e->e.getMaterielId()+"&"+e.getSupplierId())
                    .distinct()
                    .collect(Collectors.toList());
            int total = supplierIds.size();
            // 将供应商分页
            List<String> supplierIdsPage = PageUtils.startPage(supplierIds, pageno, pagesize);
            // 获取分页后的供应商
            List<PurchaseContractVO> priceAnalysisList = priceAnalysisLists
                    .stream()
                    .filter(e -> supplierIdsPage.contains(e.getMaterielId()+"&"+e.getSupplierId()))
                    .collect(Collectors.toList());
            // 统计供应商物料总数量
            Map<Long, Map<Long, Double>> countMap = priceAnalysisList
                    .stream()
                    .collect(Collectors.groupingBy(PurchaseContractVO::getSupplierId,Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.summingDouble(PurchaseContractVO::getCount))));
            // 统计供应商物料总金额
            Map<Long, Map<Long, Double>> amountMap = priceAnalysisList
                    .stream()
                    .collect(Collectors.groupingBy(PurchaseContractVO::getSupplierId
                                    , Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.summingDouble(PurchaseContractVO::getTaxAmount))));
            // 统计供应商最高价格 最低价格 平均价格
            Map<Long, Map<Long, DoubleSummaryStatistics>> priceMap = priceAnalysisList
                    .stream()
                    .collect(Collectors.groupingBy(PurchaseContractVO::getSupplierId
                                    , Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.summarizingDouble(PurchaseContractVO::getTaxUnitPrice))));
            // 最新价格
            Map<Long, Map<Long, Optional<PurchaseContractVO>>> latestPriceMap = priceAnalysisList
                    .stream()
                    .collect(Collectors.groupingBy(PurchaseContractVO::getSupplierId
                                    , Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.maxBy(Comparator.comparing(PurchaseContractVO::getId)))));
            List<PurchaseContractVO> resultList = Lists.newArrayList();
            for (Long supplier : amountMap.keySet()) {
                Map<Long, Double> materielMapForCount = countMap.get(supplier);
                Map<Long, Double> materielMapForAmount = amountMap.get(supplier);
                Map<Long, DoubleSummaryStatistics> materielMapForPrice = priceMap.get(supplier);
                Map<Long, Optional<PurchaseContractVO>> materielMapForLatestPrice = latestPriceMap.get(supplier);

                for (Long materiel : materielMapForAmount.keySet()) {
                    for (PurchaseContractVO purchaseContractVO : priceAnalysisList) {
                        Long supplierId1 = purchaseContractVO.getSupplierId();
                        Long materielId1 = purchaseContractVO.getMaterielId();
                        if(supplier.equals(supplierId1)){
                            if(materiel.equals(materielId1)){
                                DoubleSummaryStatistics doubleSummaryStatistics = materielMapForPrice.get(materiel);
                                purchaseContractVO.setCount(materielMapForCount.get(materiel));
                                purchaseContractVO.setTaxAmount(materielMapForAmount.get(materiel));
                                purchaseContractVO.setMaxUnitPrice(doubleSummaryStatistics.getMax());
                                purchaseContractVO.setMinUnitPrice(doubleSummaryStatistics.getMin());
                                purchaseContractVO.setAvgUnitPrice(doubleSummaryStatistics.getAverage());
                                purchaseContractVO.setLatestUnitPrice(materielMapForLatestPrice.get(materiel).orElse(new PurchaseContractVO()).getTaxUnitPrice());
                                resultList.add(purchaseContractVO);
                                break;
                            }
                        }
                    }
                }
            }
            results.put("data", new DsResultResponse(pageno, pagesize, total, resultList));
        }
        return R.ok(results);
    }


    @Override
    public R  disposeBalance(Map<String, Object>  params,int showItem,int showUser){

        Map<String, Object> results = Maps.newHashMap();
        if(Objects.equals(0,showItem)&&Objects.equals(0,showUser)){
            return R.ok(results);
        }

        List<Map<String, Object>> balanceLists = this.balanceList(params);
        if (balanceLists.size() > 0) {
            // 获取所有的供应商
            List<String> supplierIds = balanceLists
                    .stream()
                    .map(e -> e.get("supplierId").toString())
                    .distinct()
                    .collect(Collectors.toList());

            // 应付
            Map<String, Double> payAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> amountPaidMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("amountPaid").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unPayAmountMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unPayAmount").toString())
                            , Double::sum));

            Map<String, String> supplierNameMap = balanceLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> v.get("supplierName").toString()
                            , (v1, v2) -> v1));

            List<Map<String, Object>> totalList = Lists.newArrayList();
            Map<String, Object> totalMap;
            for (String supplierId : supplierNameMap.keySet()) {
                totalMap = Maps.newHashMap();
                totalMap.put("sign","end");
                totalMap.put("sortNo", 1);
                totalMap.put("supplierId", supplierId);
                totalMap.put("supplierName", supplierNameMap.get(supplierId) + "小计");
                totalMap.put("payAmount", payAmountMap.get(supplierId));
                totalMap.put("amountPaid", amountPaidMap.get(supplierId));
                totalMap.put("unPayAmount", unPayAmountMap.get(supplierId));
                totalList.add(totalMap);
            }
        List<Map<String, Object>> ultimatelyDate = Lists.newArrayList();

        if(Objects.equals(0,showItem)&&Objects.equals(1,showUser)){
            ultimatelyDate.addAll(totalList);
        }else if(Objects.equals(1,showItem)&&Objects.equals(0,showUser)){
            ultimatelyDate.addAll(balanceLists);
        }else if(Objects.equals(1,showItem)&&Objects.equals(1,showUser)){
            totalList.addAll(balanceLists);
            ultimatelyDate.addAll(totalList);
        }
        List<Map<String, Object>> collect = ultimatelyDate.stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("supplierId").toString())))
                .collect(Collectors.toList());

            Double totalPayAmount = totalList
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = totalList
                    .stream()
                    .map(v -> Double.parseDouble(v.get("amountPaid").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnreceivedAmount = totalList
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unPayAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("ultimatelyDate",collect);
            results.put("totalPayAmount",totalPayAmount);
            results.put("totalAmountPaid",totalReceivedAmount);
            results.put("totalUnPayAmount",totalUnreceivedAmount);
        }
        return R.ok(results);
    }






}
