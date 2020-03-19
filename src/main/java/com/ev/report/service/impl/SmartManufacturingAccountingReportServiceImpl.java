package com.ev.report.service.impl;

import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.report.dao.SmartManufacturingAccountingReportDao;
import com.ev.report.domain.*;
import com.ev.report.service.SmartManufacturingAccountingReportService;
import com.ev.report.vo.CommonVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SmartManufacturingAccountingReportServiceImpl implements SmartManufacturingAccountingReportService {

    @Autowired
    private SmartManufacturingAccountingReportDao reportDao;

    @Override
    public Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processPlan(List<Map<String, Object>> data,boolean isTotalData) {
        Map<String, BigDecimal> totalMap = Maps.newHashMap();
        // 获取所有工序计划ID
        List<Object> planIds = data.stream()
                .map(stringObjectMap -> stringObjectMap.get("id"))
                .collect(Collectors.toList());

        Map<String, Object> param = Maps.newHashMap();
        param.put("planIds", planIds);
        // 工序计划下所有报工记录
        List<ProcessReportDO> processReportList = reportDao.processReport(param);
        // 工序计划下所有工序检验记录
        List<ProcessReportCheckDO> processReportCheckList = reportDao.processReportCheck(param);
        // 工序计划下所有用料报废单记录
//        List<MaterialsScrapItemDO> materielScrapItemList = reportDao.materielScrapItem(param);
        // 工序计划下所有生产入库单
//        param.put("sourceType", ConstantForMES.SCJH);
//        List<StockInItemDO> stockInItemList = reportDao.stockInItem(param);

        // 完工数量
        Map<Long, BigDecimal> completionCountMap = processReportList
                .stream()
                .collect(Collectors.toMap(ProcessReportDO::getProcessPlanItemId, ProcessReportDO::getCompletionCount, BigDecimal::add));

        // 检验数量（工序检验内的报检数量）
        Map<Long, BigDecimal> checkCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getCheckCount, BigDecimal::add));

        // 合格数量（工序检验内的合格数量）
        Map<Long, BigDecimal> conformityCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getConformityCount, BigDecimal::add));

        // 返工数量（工序检验内的返工数量）
        Map<Long, BigDecimal> reworkCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getReworkCount, BigDecimal::add));

        // 报废数量（工序检验内的的报废数量）
        Map<Long, BigDecimal> totalScrapCountMap = processReportCheckList
                .stream()
                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getScrapCount, BigDecimal::add));

        // XXX 因料报废数量（用料报废单内的报废数量）
//        Map<Long, BigDecimal> materielScrapCountMap = materielScrapItemList
//                .stream()
//                .collect(Collectors.toMap(MaterialsScrapItemDO::getProcessPlanItemId, MaterialsScrapItemDO::getScrapCount, BigDecimal::add));

        // 完工入库（工序计划关联下的生产入库数量）
//        Map<Long, BigDecimal> stockInCountMap = stockInItemList
//                .stream()
//                .collect(Collectors.toMap(StockInItemDO::getSourceId, StockInItemDO::getCost, BigDecimal::add));

        // 若是分页统计总数则不处理列表内的内容
        if (isTotalData) {
            // 计划生产数量合计
            BigDecimal totalPlanCount = BigDecimal.valueOf(
                    data.stream()
                    .map(stringObjectMap -> Double.parseDouble(stringObjectMap.getOrDefault("planCount", 0.0d).toString()))
                    .reduce(Double::sum)
                    .orElse(0.0d)
            );
            totalMap.put("totalPlanCount",totalPlanCount);

            // 完工数量合计
            BigDecimal totalCompletionCount = completionCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCompletionCount",totalCompletionCount);

            // 差异数量合计 差异数量合计= 完工数量合计-计划生产数量合计
            BigDecimal totalDifferenceCount = totalCompletionCount.subtract(totalPlanCount);
            totalMap.put("totalDifferenceCount",totalDifferenceCount);

            // 检验数量合计
            BigDecimal totalCheckCount = checkCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCheckCount",totalCheckCount);

            // 合格数量合计
            BigDecimal totalConformityCount = conformityCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalConformityCount",totalConformityCount);

            // 不合格数量合计 不合格数量合计= 检验数量合计-合格数量合计
            BigDecimal totalUnConformityCount = totalCheckCount.subtract(totalConformityCount);
            totalMap.put("totalUnConformityCount",totalUnConformityCount);

            // 返工数量合计
            BigDecimal totalReworkCount = reworkCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalReworkCount",totalReworkCount);

            // 报废数量合计
            BigDecimal totalScrapCount = totalScrapCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalScrapCount",totalScrapCount);

//            // 完工入库数量合计
//            BigDecimal totalStockInCount = stockInCountMap
//                    .values()
//                    .stream()
//                    .reduce(BigDecimal::add)
//                    .orElse(BigDecimal.ZERO);
//            totalMap.put("totalStockInCount",totalStockInCount);
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
                passRate = conformityCount.divide(checkCount, Constant.BIGDECIMAL_ZERO);
            }
            map.put("passRate", passRate);

//            // 完工入库（工序计划关联下的生产入库数量）
//            BigDecimal stockInCount = stockInCountMap.getOrDefault(planId, BigDecimal.ZERO);
//            map.put("stockInCount", stockInCount);

//            // XXX 完工率(4位小数) 完工率=完工数量/计划生产数量
//            BigDecimal completionRate = BigDecimal.ZERO;
//            if (completionCount.compareTo(BigDecimal.ZERO) > 0) {
//                completionRate = completionCount.divide(planCount, Constant.BIGDECIMAL_ZERO);
//            }
//            map.put("completionRate", completionRate);
        }
        return Pair.of(data, totalMap);
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionPlan(List<Map<String, Object>> data, boolean isTotalData ) {
//        Map<String, BigDecimal> totalMap = Maps.newHashMap();
//        // 获取所有生产计划ID
//        List<Object> planIds = data.stream()
//                .map(stringObjectMap -> stringObjectMap.get("id"))
//                .collect(Collectors.toList());
//
//        Map<String, Object> param = Maps.newHashMap();
//        param.put("planIds", planIds);
//
//        // 生产计划下所有的产品检验单
//        param.put("inspectionType", ConstantForMES.CPJY);
//        param.put("sourceType", ConstantForMES.SCJH);
//        List<MaterialInspectionDO> materielInspectionList = reportDao.productionInspection(param);
//
//        // 生产计划下所有生产入库单
//        param.put("sourceType", ConstantForMES.SCJH);
//        List<StockInItemDO> stockInItemList = reportDao.stockInItem(param);
//
//        // 完工数量
//        Map<Long, BigDecimal> completionCountMap = processReportList
//                .stream()
//                .collect(Collectors.toMap(ProcessReportDO::getProcessPlanItemId, ProcessReportDO::getCompletionCount, BigDecimal::add));
//
//        // 检验数量（工序检验内的报检数量）
//        Map<Long, BigDecimal> checkCountMap = processReportCheckList
//                .stream()
//                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getCheckCount, BigDecimal::add));
//
//        // 合格数量（工序检验内的合格数量）
//        Map<Long, BigDecimal> conformityCountMap = processReportCheckList
//                .stream()
//                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getConformityCount, BigDecimal::add));
//
//        // 返工数量（工序检验内的返工数量）
//        Map<Long, BigDecimal> reworkCountMap = processReportCheckList
//                .stream()
//                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getReworkCount, BigDecimal::add));
//
//        // 报废数量（工序检验内的的报废数量）
//        Map<Long, BigDecimal> totalScrapCountMap = processReportCheckList
//                .stream()
//                .collect(Collectors.toMap(ProcessReportCheckDO::getProcessPlanItemId, ProcessReportCheckDO::getScrapCount, BigDecimal::add));
//
//        // XXX 因料报废数量（用料报废单内的报废数量）
////        Map<Long, BigDecimal> materielScrapCountMap = materielScrapItemList
////                .stream()
////                .collect(Collectors.toMap(MaterialsScrapItemDO::getProcessPlanItemId, MaterialsScrapItemDO::getScrapCount, BigDecimal::add));
//
//        // 完工入库（工序计划关联下的生产入库数量）
////        Map<Long, BigDecimal> stockInCountMap = stockInItemList
////                .stream()
////                .collect(Collectors.toMap(StockInItemDO::getSourceId, StockInItemDO::getCost, BigDecimal::add));
//
//        // 若是分页统计总数则不处理列表内的内容
//        if (isTotalData) {
//            // 计划生产数量合计
//            BigDecimal totalPlanCount = BigDecimal.valueOf(
//                    data.stream()
//                            .map(stringObjectMap -> Double.parseDouble(stringObjectMap.getOrDefault("planCount", 0.0d).toString()))
//                            .reduce(Double::sum)
//                            .orElse(0.0d)
//            );
//            totalMap.put("totalPlanCount",totalPlanCount);
//
//            // 完工数量合计
//            BigDecimal totalCompletionCount = completionCountMap
//                    .values()
//                    .stream()
//                    .reduce(BigDecimal::add)
//                    .orElse(BigDecimal.ZERO);
//            totalMap.put("totalCompletionCount",totalCompletionCount);
//
//            // 差异数量合计 差异数量合计= 完工数量合计-计划生产数量合计
//            BigDecimal totalDifferenceCount = totalCompletionCount.subtract(totalPlanCount);
//            totalMap.put("totalDifferenceCount",totalDifferenceCount);
//
//            // 检验数量合计
//            BigDecimal totalCheckCount = checkCountMap
//                    .values()
//                    .stream()
//                    .reduce(BigDecimal::add)
//                    .orElse(BigDecimal.ZERO);
//            totalMap.put("totalCheckCount",totalCheckCount);
//
//            // 合格数量合计
//            BigDecimal totalConformityCount = conformityCountMap
//                    .values()
//                    .stream()
//                    .reduce(BigDecimal::add)
//                    .orElse(BigDecimal.ZERO);
//            totalMap.put("totalConformityCount",totalConformityCount);
//
//            // 不合格数量合计 不合格数量合计= 检验数量合计-合格数量合计
//            BigDecimal totalUnConformityCount = totalCheckCount.subtract(totalConformityCount);
//            totalMap.put("totalUnConformityCount",totalUnConformityCount);
//
//            // 返工数量合计
//            BigDecimal totalReworkCount = reworkCountMap
//                    .values()
//                    .stream()
//                    .reduce(BigDecimal::add)
//                    .orElse(BigDecimal.ZERO);
//            totalMap.put("totalReworkCount",totalReworkCount);
//
//            // 报废数量合计
//            BigDecimal totalScrapCount = totalScrapCountMap
//                    .values()
//                    .stream()
//                    .reduce(BigDecimal::add)
//                    .orElse(BigDecimal.ZERO);
//            totalMap.put("totalScrapCount",totalScrapCount);
//
////            // 完工入库数量合计
////            BigDecimal totalStockInCount = stockInCountMap
////                    .values()
////                    .stream()
////                    .reduce(BigDecimal::add)
////                    .orElse(BigDecimal.ZERO);
////            totalMap.put("totalStockInCount",totalStockInCount);
//            return Pair.of(data, totalMap);
//        }
//
//        for (Map<String, Object> map : data) {
//            Long planId = Long.parseLong(map.get("id").toString());
//
//            // 计划生产数量
//            BigDecimal planCount = MathUtils.getBigDecimal(map.get("planCount"));
//
//            // 完工数量
//            BigDecimal completionCount = completionCountMap.getOrDefault(planId, BigDecimal.ZERO);
//            map.put("completionCount", completionCount);
//
//            // 差异数量 差异数量= 计划数量-完工数量
//            BigDecimal differenceCount = planCount.subtract(completionCount);
//            map.put("differenceCount", differenceCount);
//
//            // 检验数量（工序检验内的报检数量）
//            BigDecimal checkCount = checkCountMap.getOrDefault(planId, BigDecimal.ZERO);
//            map.put("checkCount", checkCount);
//
//            // 合格数量（工序检验内的合格数量）
//            BigDecimal conformityCount = conformityCountMap.getOrDefault(planId, BigDecimal.ZERO);
//            map.put("conformityCount", conformityCount);
//
//            // 不合格数量 不合格数量= 检验数量-合格数量
//            BigDecimal unConformityCount = checkCount.subtract(conformityCount);
//            map.put("unConformityCount", unConformityCount);
//
//            // 返工数量（工序检验内的返工数量）
//            BigDecimal reworkCount = reworkCountMap.getOrDefault(planId, BigDecimal.ZERO);
//            map.put("reworkCount", reworkCount);
//
//            // 报废数量（工序检验内的的报废数量）
//            BigDecimal scrapCount = totalScrapCountMap.getOrDefault(planId, BigDecimal.ZERO);
//            map.put("scrapCount", scrapCount);
//
//            // 合格率(4位小数) 合格率=合格数量/检验数量
//            BigDecimal passRate = BigDecimal.ZERO;
//            if (checkCount.compareTo(BigDecimal.ZERO) > 0) {
//                passRate = conformityCount.divide(checkCount, Constant.BIGDECIMAL_ZERO);
//            }
//            map.put("passRate", passRate);
//
////            // 完工入库（工序计划关联下的生产入库数量）
////            BigDecimal stockInCount = stockInCountMap.getOrDefault(planId, BigDecimal.ZERO);
////            map.put("stockInCount", stockInCount);
//
////            // XXX 完工率(4位小数) 完工率=完工数量/计划生产数量
////            BigDecimal completionRate = BigDecimal.ZERO;
////            if (completionCount.compareTo(BigDecimal.ZERO) > 0) {
////                completionRate = completionCount.divide(planCount, Constant.BIGDECIMAL_ZERO);
////            }
////            map.put("completionRate", completionRate);
//        }
//        return Pair.of(data, totalMap);
        return null;
    }

    @Override
    public R pieceRate(CommonVO commonVO) {
        return null;
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionBatch(List<Map<String, Object>> data) {
        Map<String,BigDecimal> totalMap = Maps.newHashMap();




        return Pair.of(data,totalMap);
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processOutput(List<Map<String, Object>> data) {
        Map<String,BigDecimal> totalMap = Maps.newHashMap();




        return Pair.of(data,totalMap);
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
    public List<Map<String, Object>> productionBatchList(Map<String, Object> params) {
        return reportDao.productionBatchList(params);
    }

    @Override
    public int productionBatchCount(Map<String, Object> params) {
        return reportDao.productionBatchCount(params);
    }

    @Override
    public List<Map<String, Object>> processOutputList(Map<String, Object> params) {
        return reportDao.processOutputList(params);
    }

    @Override
    public int processOutputCount(Map<String, Object> params) {
        return reportDao.processOutputCount(params);
    }
}
