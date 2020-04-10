package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.SalesManagementAccountingReportService;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.ContractBillItemVO;
import com.ev.report.vo.ContractPayItemVO;
import com.ev.report.vo.StockOutItemVO;
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
 * 销售管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-27 09:51:41
 */
@Api(value = "/", tags = "销售管理报表分析API")
@RestController
public class SalesManagementAccountingReportApiController {
    @Autowired
    private SalesManagementAccountingReportService reportService;
    @Autowired
    private WarehouseAccountingReportService stockService;

    @EvApiByToken(value = "/apis/salesManagement/tracking", method = RequestMethod.POST, apiTitle = "销售全程跟踪")
    @ApiOperation("销售全程跟踪")
    public R tracking(
//            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
//            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "客户") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode", defaultValue = "", required = false) String contractCode,
            @ApiParam(value = "合同Id") @RequestParam(value = "contractId", defaultValue = "", required = false) Long contractId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime

    ) {
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
        Map<String, Object> results = Maps.newHashMap();

        // 获取销售合同列表
        List<Map<String, Object>> salesContractList = reportService.salesContractList(params);
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
            params.put("sourceIds",itemIds);
            params.put("sourceType", ConstantForGYL.XSHT);
            params.put("auditSign", ConstantForMES.OK_AUDITED);
            // 出库表
            List<StockOutItemVO> stockOutItemVOS = stockService.stockOutItem(params);
            params.put("sourceIds",contractIds);
            // 合同收款条件表
            List<ContractPayItemVO> contractPayItemVOS = reportService.contractPayItem(params);

            Map<Long, Double> stockCountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(StockOutItemVO::getSourceId
                            , Collectors.summingDouble(StockOutItemVO::getCount)));
            Map<Long, Double> stockAmountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(StockOutItemVO::getSourceId
                            , Collectors.summingDouble(StockOutItemVO::getAmount)));

            // 销售发票表
            List<ContractBillItemVO> contractBillItemVO = reportService.stockBillItem(params);
            Map<Long, Double> billCountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceId
                            , Collectors.summingDouble(ContractBillItemVO::getCount)));
            Map<Long, Double> billAmountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceId
                            , Collectors.summingDouble(ContractBillItemVO::getAmount)));

            // 销售发票合计项
            Map<String, Double> totalBillCountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceCode
                            , Collectors.summingDouble(ContractBillItemVO::getCount)));
            Map<String, Double> totalBillAmountGroup = contractBillItemVO
                    .stream()
                    .collect(Collectors.groupingBy(ContractBillItemVO::getSourceCode
                            , Collectors.summingDouble(ContractBillItemVO::getAmount)));

            // 销售出库合计项
            Map<String, Double> totalStockCountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(StockOutItemVO::getSourceCode
                            , Collectors.summingDouble(StockOutItemVO::getCount)));
            Map<String, Double> totalStockAmountGroup = stockOutItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(StockOutItemVO::getSourceCode
                            , Collectors.summingDouble(StockOutItemVO::getAmount)));

            // 销售数量 销售金额
            Map<String, Double> totalSalesCountGroup = salesContractList
                    .stream()
                    .collect(Collectors.groupingBy(e->e.get("contractCode").toString()
                            , Collectors.summingDouble(e->Double.parseDouble(e.get("count").toString()))));
            Map<String, Double> totalSalesAmountGroup = salesContractList
                    .stream()
                    .collect(Collectors.groupingBy(e->e.get("contractCode").toString()
                            , Collectors.summingDouble(e->Double.parseDouble(e.get("taxAmount").toString()))));
            // 已收款金额 未收金额
            Map<Long, Double> totalReceivedAmountGroup = contractPayItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(ContractPayItemVO::getSourceId
                            , Collectors.summingDouble(ContractPayItemVO::getReceivedAmount)));
            Map<Long, Double> totalUnReceivedAmountGroup = contractPayItemVOS
                    .stream()
                    .collect(Collectors.groupingBy(ContractPayItemVO::getSourceId
                            , Collectors.summingDouble(ContractPayItemVO::getUnReceivedAmount)));

            List<Map<String, Object>> showList = Lists.newArrayList();

            Map<String,Object> totalMap;
            List<Long> copyContractIds = new ArrayList<>(contractIds);

            for (Map<String, Object> map : salesContractList) {
                long itemId = Long.parseLong(map.get("id").toString());
                map.put("outCount",stockCountGroup.getOrDefault(itemId,0.0d));
                map.put("outAmount",stockAmountGroup.getOrDefault(itemId,0.0d));
                map.put("billCount",billCountGroup.getOrDefault(itemId,0.0d));
                map.put("billAmount",billAmountGroup.getOrDefault(itemId,0.0d));

                // 总计
                if(showTotal){
                    String sourceCode = map.get("contractCode").toString();
                    Long sourceId = Long.parseLong(map.get("salesContractId").toString());
                    if (copyContractIds.contains(sourceId)) {
                        totalMap = Maps.newHashMap();
                        totalMap.put("salesContractId",sourceId);
                        totalMap.put("contractCode",sourceCode+"小计");
                        totalMap.put("contractDate",map.getOrDefault("contractDate",""));
                        totalMap.put("deptName",map.getOrDefault("deptName",""));
                        totalMap.put("clientName",map.getOrDefault("clientName",""));

                        totalMap.put("count",totalSalesCountGroup.getOrDefault(sourceCode,0.0d));
                        totalMap.put("taxAmount",totalSalesAmountGroup.getOrDefault(sourceCode,0.0d));
                        totalMap.put("outCount",totalStockCountGroup.getOrDefault(sourceCode,0.0d));
                        totalMap.put("outAmount",totalStockAmountGroup.getOrDefault(sourceCode,0.0d));
                        totalMap.put("billCount",totalBillCountGroup.getOrDefault(sourceCode,0.0d));
                        totalMap.put("billAmount",totalBillAmountGroup.getOrDefault(sourceCode,0.0d));
                        totalMap.put("totalReceivedAmount",totalReceivedAmountGroup.getOrDefault(sourceId,0.0d));
                        totalMap.put("totalUnReceivedAmount",totalUnReceivedAmountGroup.getOrDefault(sourceId,0.0d));

                        totalMap.put("sortNo",1);
                        totalMap.put("sign",1);
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
            results.put("data",collect);
            Map<String,Object> totalResult = Maps.newHashMap();
            totalResult.put("count",totalSalesCountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("taxAmount",totalSalesAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("outCount",totalStockCountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("outAmount",totalStockAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("billCount",totalBillCountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("billAmount",totalBillAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("totalReceivedAmount",totalReceivedAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            totalResult.put("totalUnReceivedAmount",totalUnReceivedAmountGroup
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d));
            results.put("total",totalResult);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/salesManagement/debtDue", method = RequestMethod.POST, apiTitle = "销售到期债务")
    @ApiOperation("销售到期债务")
    public R debtDue(
//            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
//            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
//            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("clientId", clientId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> debtDueLists = reportService.debtDueList(params);


        if (debtDueLists.size() > 0) {

//            // 获取所有的客户
//            List<String> clientIds = debtDueLists
//                    .stream()
//                    .map(e -> e.get("clientId").toString())
//                    .distinct()
//                    .collect(Collectors.toList());
//            int total = clientIds.size();
//            // 将客户分页
//            List<String> clientIdsPage = PageUtils.startPage(clientIds, pageno, pagesize);

            // 获取分页下的部门的所有产品检验单
//            List<Map<String, Object>> debtDueList = debtDueLists
//                    .stream()
//                    .filter(e -> clientIdsPage.contains(e.get("clientId").toString()))
//                    .collect(Collectors.toList());

            List<Map<String, Object>> showList = Lists.newArrayList();

            // 应收
            Map<String, Double> receivableAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> Double.parseDouble(v.get("receivableAmount").toString())
                            , Double::sum));
            // 已收
            Map<String, Double> receivedAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> Double.parseDouble(v.get("receivedAmount").toString())
                            , Double::sum));

            // 未收
            Map<String, Double> unreceivedAmountMap = debtDueLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> Double.parseDouble(v.get("unreceivedAmount").toString())
                            , Double::sum));

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
                    map.put("sign",1);
                    // 排序号
                    map.put("sortNo",1);
                    map.put("clientName", clientNameMap.get(s) + "小计");
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
            results.put("data",collect);

            // 合计项
            Double totalReceivableAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("receivableAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("receivedAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnreceivedAmount = debtDueLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unreceivedAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Map<String,Object> totalResult = Maps.newHashMap();
            totalResult.put("totalReceivableAmount",totalReceivableAmount);
            totalResult.put("totalReceivedAmount",totalReceivedAmount);
            totalResult.put("totalUnreceivedAmount",totalUnreceivedAmount);
            results.put("total",totalResult);
        }
        return R.ok(results);
    }

//    @EvApiByToken(value = "/apis/salesManagement/debtDue/item", method = RequestMethod.POST, apiTitle = "销售到期债务(详细)")
//    @ApiOperation("销售到期债务(详细)")
//    public R debtDueItem(
//            @ApiParam(value = "客户ID", required = true) @RequestParam(value = "clientId", defaultValue = "") Long clientId,
//            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
//            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("clientId", clientId);
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

    @EvApiByToken(value = "/apis/salesManagement/summary", method = RequestMethod.POST, apiTitle = "销售汇总统计(小计)")
    @ApiOperation("销售汇总统计(小计)")
    public R summary(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:0客户，1部门，2销售员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("clientId", clientId);
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

    @EvApiByToken(value = "/apis/salesManagement/summary/item", method = RequestMethod.POST, apiTitle = "销售汇总统计(详细)")
    @ApiOperation("销售汇总统计(详细)")
    public R summaryItem(
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:0客户，1部门，2销售员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        switch (type){
            case 0:
                if (clientId == null) {
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

        params.put("clientId", clientId);
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


    @EvApiByToken(value = "/apis/salesManagement/balance", method = RequestMethod.POST, apiTitle = "销售合同余额(客户小计)")
    @ApiOperation("销售合同余额(客户小计)")
    public R balance(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,

            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("clientName", StringUtils.sqlLike(clientName));
        params.put("clientId", clientId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
        if (balanceLists.size() > 0) {
            // 获取所有的客户
            List<String> clientIds = balanceLists
                    .stream()
                    .map(e -> e.get("clientId").toString())
                    .distinct()
                    .collect(Collectors.toList());
            int total = clientIds.size();
            // 将生产部门分页
            List<String> clientIdsPage = PageUtils.startPage(clientIds, pageno, pagesize);

            // 获取分页下的部门的所有产品检验单
            List<Map<String, Object>> balanceList = balanceLists
                    .stream()
                    .filter(e -> clientIdsPage.contains(e.get("clientId").toString()))
                    .collect(Collectors.toList());

            // 应收
            Map<String, Double> receivableAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> Double.parseDouble(v.get("receivableAmount").toString())
                            , Double::sum));
            // 已收
            Map<String, Double> receivedAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> Double.parseDouble(v.get("receivedAmount").toString())
                            , Double::sum));

            // 未收
            Map<String, Double> unreceivedAmountMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> Double.parseDouble(v.get("unreceivedAmount").toString())
                            , Double::sum));

            Map<String, String> clientNameMap = balanceList
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("clientId").toString()
                            , v -> v.get("clientName").toString()
                            , (v1, v2) -> v1));

            List<Map<String, Object>> data = Lists.newArrayList();
            Map<String, Object> map;
            for (String s : clientNameMap.keySet()) {
                map = Maps.newHashMap();
                map.put("clientId", s);
                map.put("clientName", clientNameMap.get(s) + "小计");
                map.put("totalReceivableAmount", receivableAmountMap.get(s));
                map.put("totalReceivedAmount", receivedAmountMap.get(s));
                map.put("totalUnreceivedAmount", unreceivedAmountMap.get(s));
                data.add(map);
            }

            results.put("data", new DsResultResponse(pageno, pagesize, total, data));

            Double totalReceivableAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("receivableAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalReceivedAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("receivedAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalUnreceivedAmount = balanceLists
                    .stream()
                    .map(v -> Double.parseDouble(v.get("unreceivedAmount").toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);
            results.put("totalReceivableAmount",totalReceivableAmount);
            results.put("totalReceivedAmount",totalReceivedAmount);
            results.put("totalUnreceivedAmount",totalUnreceivedAmount);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/salesManagement/balance/item", method = RequestMethod.POST, apiTitle = "销售合同余额(详细)")
    @ApiOperation("销售合同余额(详细)")
    public R balanceItem(
            @ApiParam(value = "客户ID", required = true) @RequestParam(value = "clientId", defaultValue = "") Long clientId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("clientId", clientId);
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
