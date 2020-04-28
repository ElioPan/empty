package com.ev.report.service.impl;

import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.BeanUtils;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.dao.SmartManufacturingAccountingReportDao;
import com.ev.report.dao.WarehouseAccountingReportDao;
import com.ev.report.service.SmartManufacturingAccountingReportService;
import com.ev.report.vo.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SmartManufacturingAccountingReportServiceImpl implements SmartManufacturingAccountingReportService {

    @Autowired
    private SmartManufacturingAccountingReportDao reportDao;
    @Autowired
    private WarehouseAccountingReportDao stockDao;

    @Override
    public Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processPlan(List<Map<String, Object>> data, boolean isTotalData) {
        Map<String, BigDecimal> totalMap = Maps.newHashMap();
        // 获取所有工序计划ID
        List<Object> planIds = data.stream()
                .map(stringObjectMap -> stringObjectMap.get("id"))
                .collect(Collectors.toList());

        Map<String, Object> param = Maps.newHashMap();
        param.put("planIds", planIds);
        // 工序计划下所有报工记录
        List<ProcessReportVO> processReportList = reportDao.processReport(param);
        // 工序计划下所有工序检验记录
        List<ProcessReportCheckVO> processReportCheckList = reportDao.processReportCheck(param);

        // 完工数量
        Map<Long, BigDecimal> completionCountMap = processReportList
                .stream()
                .collect(Collectors.toMap(ProcessReportVO::getProcessPlanItemId, ProcessReportVO::getCompletionCount, BigDecimal::add));

        // 检验数量（工序检验内的报检数量）
        Map<Long, BigDecimal> checkCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckVO::getProcessPlanItemId, ProcessReportCheckVO::getCheckCount, BigDecimal::add));

        // 合格数量（工序检验内的合格数量）
        Map<Long, BigDecimal> conformityCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckVO::getProcessPlanItemId, ProcessReportCheckVO::getConformityCount, BigDecimal::add));

        // 返工数量（工序检验内的返工数量）
        Map<Long, BigDecimal> reworkCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckVO::getProcessPlanItemId, ProcessReportCheckVO::getReworkCount, BigDecimal::add));

        // 报废数量（工序检验内的的报废数量）
        Map<Long, BigDecimal> totalScrapCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckVO::getProcessPlanItemId, ProcessReportCheckVO::getScrapCount, BigDecimal::add));

        // 若是分页统计总数则不处理列表内的内容
        if (isTotalData) {
            // 计划生产数量合计
            BigDecimal totalPlanCount =
                    data.stream()
                            .map(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.getOrDefault("planCount", BigDecimal.ZERO).toString()))
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO);
            totalMap.put("totalPlanCount", totalPlanCount);

            // 完工数量合计
            BigDecimal totalCompletionCount = completionCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCompletionCount", totalCompletionCount);

            // 差异数量合计 差异数量合计= 计划生产数量合计-完工数量合计
            BigDecimal totalDifferenceCount = totalPlanCount.subtract(totalCompletionCount);
            totalMap.put("totalDifferenceCount", totalDifferenceCount);

            // 检验数量合计
            BigDecimal totalCheckCount = checkCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCheckCount", totalCheckCount);

            // 合格数量合计
            BigDecimal totalConformityCount = conformityCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalConformityCount", totalConformityCount);

            // 不合格数量合计 不合格数量合计= 检验数量合计-合格数量合计
            BigDecimal totalUnConformityCount = totalCheckCount.subtract(totalConformityCount);
            totalMap.put("totalUnConformityCount", totalUnConformityCount);

            // 返工数量合计
            BigDecimal totalReworkCount = reworkCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalReworkCount", totalReworkCount);

            // 报废数量合计
            BigDecimal totalScrapCount = totalScrapCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalScrapCount", totalScrapCount);

            return Pair.of(data, totalMap);
        }

        for (Map<String, Object> map : data) {
            Long planId = Long.parseLong(map.get("id").toString());

            // 计划生产数量
            BigDecimal planCount = MathUtils.getBigDecimal(map.get("planCount"));

            // 完工数量
            BigDecimal completionCount = completionCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("completionCount", completionCount);

            // 差异数量 差异数量= 计划数量-完工数量
            BigDecimal differenceCount = planCount.subtract(completionCount);
            map.put("differenceCount", differenceCount);

            // 检验数量（工序检验内的报检数量）
            BigDecimal checkCount = checkCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("checkCount", checkCount);

            // 合格数量（工序检验内的合格数量）
            BigDecimal conformityCount = conformityCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("conformityCount", conformityCount);

            // 不合格数量 不合格数量= 检验数量-合格数量
            BigDecimal unConformityCount = checkCount.subtract(conformityCount);
            map.put("unConformityCount", unConformityCount);

            // 返工数量（工序检验内的返工数量）
            BigDecimal reworkCount = reworkCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("reworkCount", reworkCount);

            // 报废数量（工序检验内的的报废数量）
            BigDecimal scrapCount = totalScrapCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("scrapCount", scrapCount);

            // 合格率(4位小数) 合格率=合格数量/检验数量
            BigDecimal passRate = BigDecimal.ZERO;
            if (checkCount.compareTo(BigDecimal.ZERO) > 0) {
                passRate = conformityCount.divide(checkCount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
            }
            map.put("passRate", passRate);

            // 完工率(4位小数) 完工率=完工数/计划数
            BigDecimal completionRate = BigDecimal.ZERO;
            if (planCount.compareTo(BigDecimal.ZERO) > 0) {
                completionRate = completionCount.divide(planCount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
            }
            map.put("completionRate", completionRate.multiply(BigDecimal.valueOf(100L)));
        }
        return Pair.of(data, totalMap);
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionPlan(List<Map<String, Object>> data, boolean isTotalData) {
        Map<String, BigDecimal> totalMap = Maps.newHashMap();
        // 获取所有生产计划ID
        List<Object> planIds = data.stream()
                .map(stringObjectMap -> stringObjectMap.get("id"))
                .collect(Collectors.toList());

        Map<String, Object> param = Maps.newHashMap();
        param.put("planIds", planIds);

        // 生产计划下所有的产品检验单
        param.put("inspectionType", ConstantForMES.CPJY);
        param.put("sourceType", ConstantForMES.SCJH);
        param.put("status", ConstantForMES.OK_AUDITED);
        List<MaterialInspectionVO> materielInspectionList = reportDao.productionInspection(param);

        // 生产计划下所有生产入库单
        param.put("sourceType", ConstantForMES.SCJH);
        List<StockInItemVO> stockInItemList = stockDao.stockInItem(param);

        // 完工数量(为入库数量)
//        Map<Long, BigDecimal> completionCountMap = materielInspectionList
//                .stream()
//                .collect(Collectors.toMap(MaterialInspectionVO::getSourceId, MaterialInspectionVO::getInspectionCount, BigDecimal::add));

        // 检验数量（产品检验内的报检数量）
        Map<Long, BigDecimal> checkCountMap = materielInspectionList
                .stream()
                .collect(Collectors.toMap(MaterialInspectionVO::getSourceId, MaterialInspectionVO::getInspectionCount, BigDecimal::add));

        // 合格数量（产品检验内的合格数量）
        Map<Long, BigDecimal> conformityCountMap = materielInspectionList
                .stream()
                .collect(Collectors.toMap(MaterialInspectionVO::getSourceId, MaterialInspectionVO::getQualifiedCount, BigDecimal::add));


        // 因料报废数量 （产品检验内的料废数量）
        Map<Long, BigDecimal> materielScrapCountMap = materielInspectionList
                .stream()
                .collect(Collectors.toMap(MaterialInspectionVO::getSourceId, MaterialInspectionVO::getScrapWasteCount, BigDecimal::add));

        // 因工报废数量 （产品检验内的工废数量）
        Map<Long, BigDecimal> workScrapCountMap = materielInspectionList
                .stream()
                .collect(Collectors.toMap(MaterialInspectionVO::getSourceId, MaterialInspectionVO::getIndustrialWasteCount, BigDecimal::add));


        // 完工入库（工序计划关联下的生产入库数量）
        Map<Long, BigDecimal> stockInCountMap = stockInItemList
                .stream()
                .collect(Collectors.toMap(StockInItemVO::getSourceId, StockInItemVO::getCount, BigDecimal::add));

        // 若是分页统计总数则不处理列表内的内容
        if (isTotalData) {
            // 计划生产数量合计
            BigDecimal totalPlanCount =
                    data.stream()
                            .map(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.getOrDefault("planCount", BigDecimal.ZERO).toString()))
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO);
            totalMap.put("totalPlanCount", totalPlanCount);

            // 完工数量(为入库数量)
            BigDecimal totalCompletionCount = stockInCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCompletionCount", totalCompletionCount);

            // 差异数量合计 差异数量合计= 完工数量合计-计划生产数量合计
            BigDecimal totalDifferenceCount = totalPlanCount.subtract(totalCompletionCount);
            totalMap.put("totalDifferenceCount", totalDifferenceCount);

            // 检验数量（产品检验内的检验数量）
            BigDecimal totalCheckCount = checkCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCheckCount", totalCheckCount);

            // 合格数量合计
            BigDecimal totalConformityCount = conformityCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalConformityCount", totalConformityCount);

            // 不合格数量合计 不合格数量合计= 检验数量合计-合格数量合计
            BigDecimal totalUnConformityCount = totalCheckCount.subtract(totalConformityCount);
            totalMap.put("totalUnConformityCount", totalUnConformityCount);

            // 因料报废数量合计
            BigDecimal totalMaterielScrapCount = materielScrapCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalMaterielScrapCount", totalMaterielScrapCount);

            // 因工报废数量合计
            BigDecimal totalWorkScrapCount = workScrapCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalWorkScrapCount", totalWorkScrapCount);

            // 完工入库数量合计
            BigDecimal totalStockInCount = stockInCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalStockInCount", totalStockInCount);

            return Pair.of(data, totalMap);
        }

        for (Map<String, Object> map : data) {
            Long planId = Long.parseLong(map.get("id").toString());

            // 计划生产数量
            BigDecimal planCount = MathUtils.getBigDecimal(map.get("planCount"));

            // 完工数量(为入库数量)
            BigDecimal completionCount = stockInCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("completionCount", completionCount);

            // 差异数量 差异数量= 计划数量-完工数量
            BigDecimal differenceCount = planCount.subtract(completionCount);
            map.put("differenceCount", differenceCount);

            // 检验数量（产品检验内的检验数量）
            BigDecimal checkCount = checkCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("checkCount", checkCount);

            // 合格数量（产品检验内的合格数量）
            BigDecimal conformityCount = conformityCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("conformityCount", conformityCount);

            // 不合格数量 不合格数量= 检验数量-合格数量
            BigDecimal unConformityCount = checkCount.subtract(conformityCount);
            map.put("unConformityCount", unConformityCount);

            // 因料报废数量（产品检验内的的料废数量）
            BigDecimal materielScrapCount = materielScrapCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("materielScrapCount", materielScrapCount);

            // 因工报废数量（产品检验内的的料废数量）
            BigDecimal workScrapCount = workScrapCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("workScrapCount", workScrapCount);

            // 合格率(4位小数) 合格率=合格数量/检验数量
            BigDecimal passRate = BigDecimal.ZERO;
            if (checkCount.compareTo(BigDecimal.ZERO) > 0) {
                passRate = conformityCount.divide(checkCount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
            }
            map.put("passRate", passRate.multiply(BigDecimal.valueOf(100L)));

            // 完工入库（工序计划关联下的生产入库数量）
            BigDecimal stockInCount = stockInCountMap.getOrDefault(planId, BigDecimal.ZERO);
            map.put("stockInCount", stockInCount);

            // 完工率(4位小数) 完工率=完工数量/计划生产数量
            BigDecimal completionRate = BigDecimal.ZERO;
            if (completionCount.compareTo(BigDecimal.ZERO) > 0) {
                completionRate = completionCount.divide(planCount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
            }
            map.put("completionRate", completionRate);

        }
        return Pair.of(data, totalMap);
    }

    @Override
    public Pair<List<PieceRateVO>, BigDecimal>  pieceRateGroup(CommonVO commonVO) {
        boolean showItem = commonVO.getShowItem() == 1;
        boolean showUser = commonVO.getShowType() == 1;
        boolean showDept = commonVO.getShowUser() == 1;
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();

        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userIdInfo);
        param.put("deptId", deptIdInfo);
        param.put("startTime", commonVO.getStartTime());
        param.put("endTime", commonVO.getEndTime());

        List<PieceRateVO> showList = Lists.newArrayList();
        // 获取所有的报工单
        List<PieceRateVO> pieceRateVOLists = reportDao.pieceRateItem(param);

        if (pieceRateVOLists.size() > 0) {
            // 各个部门工资小计
            Long typeMax = pieceRateVOLists
                    .stream()
                    .max(Comparator.comparing(PieceRateVO::getOperator))
                    .orElse(new PieceRateVO())
                    .getId();
            typeMax = typeMax == null ? 0 : typeMax + 1;

            Map<Long, String> deptNameMap = pieceRateVOLists
                    .stream()
                    .collect(Collectors.toMap(PieceRateVO::getDeptId, PieceRateVO::getDeptName, (v1, v2) -> v1));

            Map<Long, String> userNameMap = pieceRateVOLists
                    .stream()
                    .collect(Collectors.toMap(PieceRateVO::getOperator, PieceRateVO::getOperatorName, (v1, v2) -> v1));

            // 展示部门合计
            if (showDept) {
                Map<Long, BigDecimal> deptTotal = pieceRateVOLists
                        .stream()
                        .collect(Collectors.toMap(PieceRateVO::getDeptId, PieceRateVO::getTotalPrice, BigDecimal::add));
                                PieceRateVO pieceRateVO;
                for (Long s : deptTotal.keySet()) {
                    pieceRateVO = new PieceRateVO();
                    BigDecimal totalPieceRate = deptTotal.get(s);
                    // 部门总计标2 用户总计标1 详情标0
                    pieceRateVO.setSign(ConstantForReport.COLOUR_END);
                    pieceRateVO.setDeptId(s);
                    pieceRateVO.setDeptName(deptNameMap.get(s) + ConstantForReport.TOTAL_SUFFIX);
                    pieceRateVO.setOperator(typeMax);
                    pieceRateVO.setSortNo(typeMax);
                    pieceRateVO.setTotalPrice( totalPieceRate == null ? BigDecimal.ZERO : totalPieceRate);
                    showList.add(pieceRateVO);
                }
            }

            // 展示用户合计
            if (showUser) {
                Map<Long, Map<Long, BigDecimal>> priceGroupByDeptAndUser = pieceRateVOLists
                        .stream()
                        .collect(
                                Collectors.groupingBy(PieceRateVO::getDeptId
                                        , Collectors.toMap(PieceRateVO::getOperator
                                                , PieceRateVO::getTotalPrice,BigDecimal::add)));
                PieceRateVO pieceRateVO;
                Map<Long, BigDecimal> groupByUser;
                for (Long s : priceGroupByDeptAndUser.keySet()) {
                    groupByUser = priceGroupByDeptAndUser.get(s);
                    for (Long userId : groupByUser.keySet()) {
                        pieceRateVO = new PieceRateVO();
                        BigDecimal totalPieceRate = groupByUser.get(userId);
                        // 部门总计标2 用户总计标1 详情标0
                        pieceRateVO.setSign("1");
                        pieceRateVO.setDeptId(s);
                        pieceRateVO.setDeptName(deptNameMap.get(s));
                        pieceRateVO.setOperatorName(userNameMap.get(userId) + ConstantForReport.TOTAL_SUFFIX);
                        pieceRateVO.setOperator(userId);
                        pieceRateVO.setSortNo(userId);
                        pieceRateVO.setTotalPrice( totalPieceRate == null ? BigDecimal.ZERO : totalPieceRate);
                        showList.add(pieceRateVO);
                    }
                }
            }
            if (showItem) {
                showList.addAll(pieceRateVOLists);
            }

            // 总合计
            BigDecimal total = pieceRateVOLists
                    .stream()
                    .map(PieceRateVO::getTotalPrice)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            // 排序
            List<PieceRateVO> collect = showList.stream()
                    .sorted(Comparator.comparing(PieceRateVO::getSortNo))
                    .sorted(Comparator.comparing(PieceRateVO::getOperator))
                    .sorted(Comparator.comparing(PieceRateVO::getDeptId))
                    .collect(Collectors.toList());

            return Pair.of(collect,total);

        }
        return null;
    }

    @Override
    public R productionBatch(Long id) {
        Map<String, Object> param = Maps.newHashMap();
        // 查询领料单
        param.put("planId", id);
        param.put("sourceType", ConstantForMES.SCTL);
        param.put("auditSign", ConstantForMES.OK_AUDITED);
        List<StockOutItemVO> stockOutItemList = stockDao.stockOutItem(param);
        // 查询入库单
        param.put("sourceType", ConstantForMES.SCJH);
        List<StockInItemVO> stockInItemList = stockDao.stockInItem(param);
        Map<String, Object> results = Maps.newHashMap();
        results.put("stockIn", stockInItemList);
        results.put("stockOut", stockOutItemList);
        return R.ok(results);
    }


    @Override
    public List<Map<String, Object>> productionPlanList(Map<String, Object> params) {
        return reportDao.productionPlanList(params);
    }

    @Override
    public int productionPlanCount(Map<String, Object> params) {
        return reportDao.productionPlanCount(params);
    }

    @Override
    public List<Map<String, Object>> processPlanList(Map<String, Object> params) {
        return reportDao.processPlanList(params);
    }

    @Override
    public int processPlanCount(Map<String, Object> params) {
        return reportDao.processPlanCount(params);
    }

    @Override
    public List<ProcessReportVO> processOutputList(Map<String, Object> params) {
        return reportDao.processReport(params);
    }

    @Override
    public Map<String, Object> processOutputCount(Map<String, Object> params) {
        return reportDao.processOutputCount(params);
    }

    @Override
    public List<Map<String, Object>> productionBatchCollect(List<Map<String, Object>> data) {
        List<Map<String, Object>> inspectionList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.CPJY.toString()))
                .collect(Collectors.toList());
        List<Map<String, Object>> planList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.SCJH.toString()))
                .collect(Collectors.toList());
        data.removeAll(inspectionList);
        data.removeAll(planList);

        Map<String, List<Map<String, Object>>> batchToList = data
                .stream()
                .collect(Collectors.groupingBy(e -> e.get("batch").toString()));

        List<Map<String, Object>> batchList;
        for (String batch : batchToList.keySet()) {
            batchList = batchToList.get(batch);
            boolean nonRemove = batchList.stream().anyMatch(e -> Objects.equals(e.get("typeId").toString(), ConstantForGYL.YDGOODS_WAREHOUSE.toString()));
            if(!nonRemove){
                data.removeAll(batchList);
            }
        }


        List<HashMap<String, Object>> inspectionLists= Lists.newArrayList();
        List<HashMap<String, Object>> planLists= Lists.newArrayList();
        Map<String,String> batchForSourceCode = Maps.newHashMap();
        List<String> emptyId = Lists.newArrayList();
        for (Map<String, Object> datum : data) {

            if (Objects.equals(datum.get("typeId").toString(), ConstantForGYL.YDGOODS_WAREHOUSE.toString()) ) {
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
                List<String> eCollect = s.stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            return batch1 == null || StringUtils.isEmpty(batch1.toString());
                        })
                        .map(e -> e.get("id").toString())
                        .collect(Collectors.toList());
                emptyId.addAll(eCollect);
                s.forEach(e->e.put("batch",batchP));

                List<HashMap<String, Object>> p = planList
                        .stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            boolean isMatch = batch1==null||StringUtils.isEmpty(batch1.toString())||Objects.equals(batch1.toString(),batchP);
                            return Objects.equals(e.get("code"), sourceCode) && isMatch;
                        })
                        .map(Maps::newHashMap)
                        .collect(Collectors.toList());
                List<String> pCollect = p.stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            return batch1 == null || StringUtils.isEmpty(batch1.toString());
                        })
                        .map(e -> e.get("id").toString())
                        .collect(Collectors.toList());
                emptyId.addAll(pCollect);
                s.forEach(e->e.put("batch",batchP));
                p.forEach(e->e.put("batch",batchP));
                inspectionLists.addAll(s);
                planLists.addAll(p);
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
        data.addAll(planLists);
        List<Map<String, Object>> collect = data
                .stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("times").toString())))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                .sorted(Comparator.comparing(e -> e.get("batch").toString()))
                .collect(Collectors.toList());
        if (emptyId.size() > 0) {
            for (Map<String, Object> stringObjectMap : collect) {
                String typeId = stringObjectMap.get("typeId").toString();
                if((Objects.equals(typeId,ConstantForMES.LLJY.toString())||(Objects.equals(typeId,ConstantForMES.SCJH.toString())))&&emptyId.contains(stringObjectMap.get("id").toString())){
                    stringObjectMap.remove("batch");
                }
            }
        }
        return collect;
    }
}
