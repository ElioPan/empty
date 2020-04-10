package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.PurchaseManagementAccountingReportService;
import com.ev.report.vo.PurchaseContractVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-31 09:51:41
 */
@Api(value = "/", tags = "采购管理报表分析API")
@RestController
public class PurchaseManagementAccountingReportApiController {
    @Autowired
    private PurchaseManagementAccountingReportService reportService;

    @EvApiByToken(value = "/apis/purchaseManagement/tracking", method = RequestMethod.POST, apiTitle = "采购全程跟踪")
    @ApiOperation("采购全程跟踪")
    public R tracking(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "要显示详情 1是0否", required = true) @RequestParam(value = "showItem", defaultValue ="1") Integer showItem,
            @ApiParam(value = "要显示小计 1是0否", required = true) @RequestParam(value = "showUser", defaultValue = "1") Integer showUser,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
//        params.put("offset", (pageno - 1) * pagesize);
//        params.put("limit", pagesize);
        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("materielType", materielType);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("auditSign", ConstantForMES.OK_AUDITED);

       return  reportService.disposeTracking(params,showItem, showUser);
    }

    @EvApiByToken(value = "/apis/purchaseManagement/debtDue", method = RequestMethod.POST, apiTitle = "采购到期债务(供应商小计)")
    @ApiOperation("采购到期债务(供应商小计)")
    public R debtDue(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
//            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商Id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,

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
            Map<String, Double> payAmountMap = debtDueList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> amountPaidMap = debtDueList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("amountPaid").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unPayAmountMap = debtDueList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unPayAmount").toString())
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
                map.put("totalPayAmount", payAmountMap.get(s));
                map.put("totalAmountPaid", amountPaidMap.get(s));
                map.put("totalUnPayAmount", unPayAmountMap.get(s));
                data.add(map);
            }
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));

            Double totalReceivableAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("amountPaid").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnreceivedAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unPayAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("totalPayAmount",totalReceivableAmount);
            results.put("totalAmountPaid",totalReceivedAmount);
            results.put("totalUnPayAmount",totalUnreceivedAmount);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/purchaseManagement/debtDue/item", method = RequestMethod.POST, apiTitle = "采购到期债务(详细)")
    @ApiOperation("采购到期债务(详细)")
    public R debtDueItem(
            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
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

    @EvApiByToken(value = "/apis/purchaseManagement/balance", method = RequestMethod.POST, apiTitle = "采购合同余额(供应商小计)")
    @ApiOperation("采购合同余额(供应商小计)")
    public R balance(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,

            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("supplierName", StringUtils.sqlLike(supplierName));
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
            Map<String, Double> payAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("payAmount").toString())
                            , Double::sum));
            // 已付
            Map<String, Double> amountPaidMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("amountPaid").toString())
                            , Double::sum));

            // 未付
            Map<String, Double> unPayAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> Double.parseDouble(v.get("unPayAmount").toString())
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
                map.put("totalPayAmount", payAmountMap.get(s));
                map.put("totalAmountPaid", amountPaidMap.get(s));
                map.put("totalUnPayAmount", unPayAmountMap.get(s));
                data.add(map);
            }

            results.put("data", new DsResultResponse(pageno, pagesize, total, data));

            Double totalReceivableAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("payAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("amountPaid").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnreceivedAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unPayAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("totalPayAmount",totalReceivableAmount);
            results.put("totalAmountPaid",totalReceivedAmount);
            results.put("totalUnPayAmount",totalUnreceivedAmount);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/purchaseManagement/balance/item", method = RequestMethod.POST, apiTitle = "采购合同余额(详细)")
    @ApiOperation("采购合同余额(详细)")
    public R balanceItem(
            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
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

    @EvApiByToken(value = "/apis/purchaseManagement/priceAnalysis", method = RequestMethod.POST, apiTitle = "采购价格分析")
    @ApiOperation("采购价格分析")
    public R priceAnalysis(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("materielId", materielId);

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForMES.OK_AUDITED);
        Map<String, Object> results = Maps.newHashMap();

        // 获取采购合同列表
        List<PurchaseContractVO> priceAnalysisLists = reportService.priceAnalysisList(params);
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
                    .collect(
                            Collectors.groupingBy(PurchaseContractVO::getSupplierId
                                    , Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.summingDouble(PurchaseContractVO::getCount))));
            // 统计供应商物料总金额
            Map<Long, Map<Long, Double>> amountMap = priceAnalysisList
                    .stream()
                    .collect(
                            Collectors.groupingBy(PurchaseContractVO::getSupplierId
                                    , Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.summingDouble(PurchaseContractVO::getTaxAmount))));
            // 统计供应商最高价格 最低价格 平均价格
            Map<Long, Map<Long, DoubleSummaryStatistics>> priceMap = priceAnalysisList
                    .stream()
                    .collect(
                            Collectors.groupingBy(PurchaseContractVO::getSupplierId
                                    , Collectors.groupingBy(PurchaseContractVO::getMaterielId
                                            , Collectors.summarizingDouble(PurchaseContractVO::getTaxUnitPrice))));
            // 最新价格
            Map<Long, Map<Long, Optional<PurchaseContractVO>>> latestPriceMap = priceAnalysisList
                    .stream()
                    .collect(
                            Collectors.groupingBy(PurchaseContractVO::getSupplierId
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
}
