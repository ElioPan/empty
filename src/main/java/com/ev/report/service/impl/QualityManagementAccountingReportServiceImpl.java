package com.ev.report.service.impl;

import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.BeanUtils;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.report.dao.QualityManagementAccountingReportDao;
import com.ev.report.service.QualityManagementAccountingReportService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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

            Map<String, BigDecimal> unqualifiedCountMap = badPurchaseLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("supplierId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("unqualifiedCount").toString())
                            , BigDecimal::add));

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
    public Pair<List<Map<String, Object>>,BigDecimal> badProcessForDeptResult(int showDeptTotalInt, int showItemInt, int showProcessTotalInt, Long deptId, Long processId, Long operator, Long deviceId, String startTime, String endTime) {
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

            Map<String, BigDecimal> unqualifiedCountMap = badProcessLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("rejectsCount").toString())
                            , BigDecimal::add));

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
                Map<String, Map<String, BigDecimal>> priceGroupByDeptAndProcess = badProcessLists
                        .stream()
                        .collect(
                                Collectors.groupingBy(e->e.getOrDefault("deptId",0).toString()
                                        , Collectors.toMap(e->e.getOrDefault("processId",0).toString()
                                                , e->MathUtils.getBigDecimal(e.getOrDefault("rejectsCount",BigDecimal.ZERO)),BigDecimal::add)));
                Map<String, BigDecimal> groupByDept;
                Map<String, Object> totalMap;
                for (String deptIdParam : priceGroupByDeptAndProcess.keySet()) {
                    groupByDept = priceGroupByDeptAndProcess.get(deptIdParam);
                    for (String processIdParam : groupByDept.keySet()) {
                        totalMap = Maps.newHashMap();
                        BigDecimal rejectsCount = groupByDept.getOrDefault(processIdParam,BigDecimal.ZERO);
                        // 部门总计标end 工序总计标1 详情标0
                        totalMap.put("sign",1);
                        totalMap.put("sortNo",processIdParam);
                        totalMap.put("deptId",deptIdParam);
                        totalMap.put("deptName",deptNameMap.get(deptIdParam));
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
            BigDecimal total = badProcessLists
                    .stream()
                    .map(e->MathUtils.getBigDecimal(e.getOrDefault("rejectsCount"
                            ,BigDecimal.ZERO)))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

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
    public Pair<List<Map<String, Object>>, BigDecimal> badProductForDeptResult(int showDeptTotalInt, int showItemInt, int showProductTotalInt, Long deptId, Long materielId, String startTime, String endTime) {

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
                            .max(Comparator.comparing(e->Integer.parseInt(e.getOrDefault("materielId",0).toString())))
                            .get()
                            .get("materielId")
                            .toString()
            );
            // 最大一个物料ID+1
            max = max + 1;


            Map<String, BigDecimal> unqualifiedCountMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("unqualifiedCount").toString())
                            , BigDecimal::add));

            Map<String, String> deptNameMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("deptId").toString()
                            , v -> v.get("deptName").toString()
                            , (v1, v2) -> v1));

            Map<String, Map<String, Object>> materielNameMap = badProductLists
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("materielId").toString()
                            , v -> v
                            , (v1, v2) -> v1));
            if (showDeptTotal) {
                Map<String, Object> map;
                for (String s : deptNameMap.keySet()) {
                    map = Maps.newHashMap();
                    // 最后一层小计赋end
                    map.put("sign",ConstantForReport.COLOUR_END);
                    map.put("sortNo", 1);

                    map.put("deptId", s);
                    map.put("deptName", deptNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("unqualifiedCount", unqualifiedCountMap.get(s));
                    map.put("materielId", max);
                    showList.add(map);
                }
            }
            if (showProductTotal) {
                Map<String, Map<String, BigDecimal>> priceGroupByDeptAndMateriel = badProductLists
                        .stream()
                        .collect(
                                Collectors.groupingBy(e->e.getOrDefault("deptId",0).toString()
                                        , Collectors.toMap(e->e.getOrDefault("materielId",0).toString()
                                                , e->MathUtils.getBigDecimal(e.getOrDefault("unqualifiedCount",BigDecimal.ZERO)),BigDecimal::add)));
                Map<String, BigDecimal> groupByDept;
                Map<String, Object> totalMap;
                for (String deptIdParam : priceGroupByDeptAndMateriel.keySet()) {
                    groupByDept = priceGroupByDeptAndMateriel.get(deptIdParam);
                    for (String materielIdParam : groupByDept.keySet()) {
                        totalMap = Maps.newHashMap(materielNameMap.get(materielIdParam));
                        BigDecimal unqualifiedCount = groupByDept.getOrDefault(materielIdParam,BigDecimal.ZERO);
                        // 部门总计标end 工序总计标1 详情标0
                        totalMap.put("sign",1);
                        totalMap.put("sortNo",2);
                        totalMap.put("deptId",deptIdParam);
                        totalMap.put("deptName",deptNameMap.get(deptIdParam));
                        totalMap.put("materielName", totalMap.get("materielName")+ConstantForReport.TOTAL_SUFFIX);
                        totalMap.put("materielId",materielIdParam);
                        totalMap.put("unqualifiedCount",unqualifiedCount);
                        totalMap.remove("poorName");
                        totalMap.remove("batch");
                        showList.add(totalMap);
                    }
                }
            }
            if (showItem) {
                showList.addAll(badProductLists);
            }

            // 总合计
            BigDecimal total = badProductLists
                    .stream()
                    .map(e->MathUtils.getBigDecimal(e.getOrDefault("unqualifiedCount"
                            ,BigDecimal.ZERO)))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

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

    @Override
    public List<Map<String, Object>> qualityTraceabilityListCollect(List<Map<String, Object>> data) {
        List<Map<String, Object>> inspectionList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.LLJY.toString()))
                .collect(Collectors.toList());
        data.removeAll(inspectionList);

        Map<String, List<Map<String, Object>>> batchToList = data
                .stream()
                .collect(Collectors.groupingBy(e -> e.get("batch").toString()));

        List<Map<String, Object>> batchList;
        for (String batch : batchToList.keySet()) {
            batchList = batchToList.get(batch);
            boolean nonRemove = batchList.stream().anyMatch(e -> {
                String typeId = e.get("typeId").toString();
                return Objects.equals(typeId, ConstantForGYL.PURCHASE_INSTOCK.toString()) || Objects.equals(typeId, ConstantForGYL.OUTSOURCING_INSTOCK.toString());
            });
            if(!nonRemove){
                data.removeAll(batchList);
            }
        }

        Map<String,String> batchForSourceCode = Maps.newHashMap();
        List<HashMap<String, Object>> inspectionLists= Lists.newArrayList();
        List<String> emptyId = Lists.newArrayList();
        for (Map<String, Object> datum : data) {
            if (Objects.equals(datum.get("way").toString(),"in") ) {
                String batchP = datum.get("batch").toString();
                String sourceCode = datum.get("sourceCode").toString();
                if(batchForSourceCode.containsKey(batchP)&&Objects.equals(batchForSourceCode.get(batchP),sourceCode)){
                    continue;
                }
                List<HashMap<String, Object>> s = inspectionList
                        .stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            boolean isMatch = batch1==null|| StringUtils.isEmpty(batch1.toString())||Objects.equals(batch1.toString(),batchP);
                            return Objects.equals(e.get("sourceCode"), sourceCode) && isMatch;
                        })
                        .map(Maps::newHashMap)
                        .collect(Collectors.toList());
                List<String> collect = s.stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            return batch1 == null || StringUtils.isEmpty(batch1.toString());
                        })
                        .map(e -> e.get("id").toString())
                        .collect(Collectors.toList());
                emptyId.addAll(collect);
                s.forEach(e->e.put("batch",batchP));

                inspectionLists.addAll(s);
                batchForSourceCode.put(batchP,sourceCode);
            }
        }

        ArrayList<Map<String, Object>> clone = BeanUtils.clone((ArrayList<Map<String, Object>>) data);
        Map<String, Map<String, Object>> batchGroup = clone.stream()
                .collect(Collectors.toMap(k -> k.get("batch").toString(), v -> v, (v1, v2) -> v1));
        for (Map<String, Object> value : batchGroup.values()) {
            value.remove("typeName");
            value.remove("id");
            value.remove("sourceCode");
            value.remove("code");
            value.remove("count");
            value.remove("facilityName");
            value.remove("way");
            value.remove("locationName");
            value.remove("sourceTypeName");
            value.remove("time");
            value.put("times",0);
            value.put("sign", ConstantForReport.COLOUR_END);
            value.put("sortNo",0);
        }
        data.addAll(batchGroup.values());
        data.addAll(inspectionLists);

        List<Map<String, Object>> collect = data
                .stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("times").toString())))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                .sorted(Comparator.comparing(e -> e.get("batch").toString()))
                .collect(Collectors.toList());

        if (emptyId.size() > 0) {
            for (Map<String, Object> stringObjectMap : collect) {
                if(emptyId.contains(stringObjectMap.get("id").toString())&&Objects.equals(stringObjectMap.get("typeId").toString(),ConstantForMES.LLJY.toString())){
                    stringObjectMap.remove("batch");
                }
            }
        }
        return collect;
    }


}
