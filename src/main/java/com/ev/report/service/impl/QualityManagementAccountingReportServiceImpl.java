package com.ev.report.service.impl;

import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.report.dao.QualityManagementAccountingReportDao;
import com.ev.report.service.QualityManagementAccountingReportService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QualityManagementAccountingReportServiceImpl implements QualityManagementAccountingReportService {
    @Autowired
    private QualityManagementAccountingReportDao reportDao;

    @Override
    public List<Map<String, Object>> badPurchaseList(Map<String, Object> params) {
        return reportDao.badPurchaseList(params);
    }

    @Override
    public List<Map<String, Object>> badProcessList(Map<String, Object> params) {
        return reportDao.badProcessList(params);
    }

    @Override
    public List<Map<String, Object>> qualityTraceabilityList(Map<String, Object> params) {
        return reportDao.qualityTraceabilityList(params);
    }

    @Override
    public int qualityTraceabilityCount(Map<String, Object> params) {
        return reportDao.qualityTraceabilityList(params).size();
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> badPurchaseResult(int showTotalInt, int showItemInt, Long supplierId, Long materielId, String startTime, String endTime) {
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

        // 获取所有来料检验单
        List<Map<String, Object>> badPurchaseLists = this.badPurchaseList(params);
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
                    map.put("sign", ConstantForReport.COLOUR_END);
                    // 排序号
                    map.put("sortNo", 1);
                    map.put("supplierName", supplierNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("unqualifiedCount", unqualifiedCountMap.get(s));
                    showList.add(map);
                }
            }

            if (showItem) {
                showList.addAll(badPurchaseLists);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("supplierId").toString())))
                    .collect(Collectors.toList());
            return Pair.of(collect,Maps.newHashMap());
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>,Double> badProcessForDeptResult(int showDeptTotalInt, int showItemInt, int showProcessTotalInt, Long deptId, Long processId, Long operator, Long deviceId, String startTime, String endTime) {
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

        List<Map<String, Object>> badProcessLists = this.badProcessList(params);
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
                    map.put("deptName", deptNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    // 最后一层小计赋end
                    map.put("sign",ConstantForReport.COLOUR_END);
                    map.put("sortNo", max);
                    map.put("rejectsCount", unqualifiedCountMap.get(s));
                    map.put("processId", max);
                    showList.add(map);
                }
            }

            if (showProcessTotal) {
                Map<String, Map<String, Double>> priceGroupByDeptAndProcess = badProcessLists
                        .stream()
                        .collect(
                                Collectors.groupingBy(e->e.getOrDefault("deptId",0).toString()
                                        , Collectors.groupingBy(e->e.getOrDefault("processId",0).toString()
                                                , Collectors.summingDouble(e->Double.parseDouble(e.getOrDefault("rejectsCount",0.0d).toString())))));
                Map<String, Double> groupByDept;
                Map<String, Object> totalMap;
                for (String deptIdParam : priceGroupByDeptAndProcess.keySet()) {
                    groupByDept = priceGroupByDeptAndProcess.get(deptIdParam);
                    for (String processIdParam : groupByDept.keySet()) {
                        totalMap = Maps.newHashMap();
                        Double rejectsCount = groupByDept.getOrDefault(processIdParam,0.0d);
                        // 部门总计标end 工序总计标1 详情标0
                        totalMap.put("sign",1);
                        totalMap.put("sortNo",processIdParam);
                        totalMap.put("deptId",deptIdParam);
                        totalMap.put("processName", processNameMap.get(processIdParam)+ConstantForReport.TOTAL_SUFFIX);
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

            return Pair.of(collect,total);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Double> badProductForDeptResult(int showDeptTotalInt, int showItemInt, int showProductTotalInt, Long deptId, Long materielId, String startTime, String endTime) {

        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        boolean showItem = showItemInt == 1;
        boolean showDeptTotal = showDeptTotalInt == 1;
        boolean showProductTotal = showProductTotalInt == 1;

        params.put("deptId", deptId);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("status", ConstantForMES.OK_AUDITED);
        params.put("inspectionType", ConstantForMES.CPJY);
        List<Map<String, Object>> badProductLists = this.badPurchaseList(params);

        if (badProductLists.size() > 0) {

            List<Map<String, Object>> showList = Lists.newArrayList();

            int max = Integer.parseInt(
                    badProductLists
                            .stream()
                            .max(Comparator.comparing(e->Integer.parseInt(e.getOrDefault("processId",0).toString())))
                            .get()
                            .get("materielId")
                            .toString()
            );
            // 最大一个物料ID+1
            max = max + 1;


            Map<String, Double> unqualifiedCountMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> Double.parseDouble(v.get("unqualifiedCount").toString())
                            , Double::sum));

            Map<String, String> deptNameMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> v.get("deptName").toString()
                            , (v1, v2) -> v1));

            Map<String, String> materielNameMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("materielId").toString()
                            , v -> v.get("materielName").toString()
                            , (v1, v2) -> v1));
            if (showDeptTotal) {
                Map<String, Object> map;
                for (String s : deptNameMap.keySet()) {
                    map = Maps.newHashMap();
                    // 最后一层小计赋end
                    map.put("sign",ConstantForReport.COLOUR_END);
                    map.put("sortNo", max);

                    map.put("deptId", s);
                    map.put("deptName", deptNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("unqualifiedCount", unqualifiedCountMap.get(s));
                    map.put("materielId", max);
                    showList.add(map);
                }
            }
            if (showProductTotal) {
                Map<String, Map<String, Double>> priceGroupByDeptAndMateriel = badProductLists
                        .stream()
                        .collect(
                                Collectors.groupingBy(e->e.getOrDefault("deptId",0).toString()
                                        , Collectors.groupingBy(e->e.getOrDefault("materielId",0).toString()
                                                , Collectors.summingDouble(e->Double.parseDouble(e.getOrDefault("unqualifiedCount",0.0d).toString())))));
                Map<String, Double> groupByDept;
                Map<String, Object> totalMap;
                for (String deptIdParam : priceGroupByDeptAndMateriel.keySet()) {
                    groupByDept = priceGroupByDeptAndMateriel.get(deptIdParam);
                    for (String materielIdParam : groupByDept.keySet()) {
                        totalMap = Maps.newHashMap();
                        Double unqualifiedCount = groupByDept.getOrDefault(materielIdParam,0.0d);
                        // 部门总计标end 工序总计标1 详情标0
                        totalMap.put("sign",1);
                        totalMap.put("sortNo",materielIdParam);
                        totalMap.put("deptId",deptIdParam);
                        totalMap.put("materielName", materielNameMap.get(materielIdParam)+ConstantForReport.TOTAL_SUFFIX);
                        totalMap.put("materielId",materielIdParam);
                        totalMap.put("unqualifiedCount",unqualifiedCount);
                        showList.add(totalMap);
                    }
                }
            }
            if (showItem) {
                showList.addAll(badProductLists);
            }

            // 总合计
            double total = badProductLists
                    .stream()
                    .map(e->Double.parseDouble(e.getOrDefault("unqualifiedCount"
                            ,0.0d).toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d);

            // 排序
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("materielId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("deptId").toString())))
                    .collect(Collectors.toList());

            return Pair.of(collect,total);
        }
        return null;
    }


}
