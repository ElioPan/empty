package com.ev.report.service.impl;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.report.dao.SmartManufacturingAccountingReportDao;
import com.ev.report.service.SmartManufacturingAccountingReportService;
import com.ev.report.vo.*;
import com.google.common.collect.Lists;
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
            BigDecimal totalPlanCount = BigDecimal.valueOf(
                    data.stream()
                            .map(stringObjectMap -> Double.parseDouble(stringObjectMap.getOrDefault("planCount", 0.0d).toString()))
                            .reduce(Double::sum)
                            .orElse(0.0d)
            );
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
        List<StockInItemVO> stockInItemList = reportDao.stockInItem(param);

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
            BigDecimal totalPlanCount = BigDecimal.valueOf(
                    data.stream()
                            .map(stringObjectMap -> Double.parseDouble(stringObjectMap.getOrDefault("planCount", 0.0d).toString()))
                            .reduce(Double::sum)
                            .orElse(0.0d)
            );
            totalMap.put("totalPlanCount", totalPlanCount);

            // 完工数量(为入库数量)
            BigDecimal totalCompletionCount = stockInCountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalMap.put("totalCompletionCount", totalCompletionCount);

            // 差异数量合计 差异数量合计= 完工数量合计-计划生产数量合计
            BigDecimal totalDifferenceCount = totalCompletionCount.subtract(totalPlanCount);
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
            map.put("passRate", passRate);

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
    public R pieceRate(CommonVO commonVO) {
        Long deptId = commonVO.getDeptId();
        Long userId = commonVO.getUserId();
        if (deptId == null || userId == null) {
            return R.ok();
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("deptId", deptId);
        param.put("userId", userId);
        List<PieceRateVO> pieceRateVOLists = reportDao.pieceRateItem(param);
        if (pieceRateVOLists.size() == 0) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", pieceRateVOLists);
        return R.ok(results);
    }

    @Override
    public R pieceRateGroupByDept(CommonVO commonVO) {
        int pageNo = commonVO.getPageno();
        int pageSize = commonVO.getPagesize();
        Long userIdInfo = commonVO.getUserId();
        Long deptIdInfo = commonVO.getDeptId();
        // 获取所有的报工单
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userIdInfo);
        param.put("deptId", deptIdInfo);
        List<PieceRateVO> pieceRateVOLists = reportDao.pieceRateItem(param);
        if (pieceRateVOLists.size() == 0) {
            return R.ok();
        }
        List<PieceRateVO> pieceRateVOList = PageUtils.startPage(pieceRateVOLists, pageNo, pageSize);
        // 各个部门工资小计
        Map<Long, BigDecimal> deptTotal = pieceRateVOList
                .stream()
                .collect(Collectors.toMap(PieceRateVO::getDeptId, PieceRateVO::getTotalPrice, BigDecimal::add));
        Map<Long, String> deptNameMap = pieceRateVOList
                .stream()
                .collect(Collectors.toMap(PieceRateVO::getDeptId, PieceRateVO::getDeptName, (v1, v2) -> v1));
        List<Map<String, Object>> data = Lists.newArrayList();
        Map<String, Object> map;
        for (Long s : deptTotal.keySet()) {
            map = Maps.newHashMap();
            map.put("deptId", s);
            map.put("deptName", deptNameMap.get(s) + "小计");
            map.put("totalPieceRate", deptTotal.get(s));
            data.add(map);
        }
        // 获取不分页的总合计
        BigDecimal total = pieceRateVOLists
                .stream()
                .map(PieceRateVO::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        Map<String, Object> results = Maps.newHashMap();
        // 获取部门个数
        int size = (int) pieceRateVOLists
                .stream()
                .map(PieceRateVO::getDeptId)
                .distinct()
                .count();
        if (data.size() > 0) {
            results.put("total", total);
            results.put("data", new DsResultResponse(pageNo, pageSize, size, data));
        }
        return R.ok(results);
    }

    @Override
    public R pieceRateGroupByUser(CommonVO commonVO) {
        Long deptId = commonVO.getDeptId();
        if (deptId == null) {
            return R.ok();
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("deptId", deptId);
        List<PieceRateVO> pieceRateVOLists = reportDao.pieceRateItem(param);
        if (pieceRateVOLists.size() == 0) {
            return R.ok();
        }
        // 各个部门工资小计
        Map<Long, BigDecimal> userTotal = pieceRateVOLists
                .stream()
                .collect(Collectors.toMap(PieceRateVO::getOperator, PieceRateVO::getTotalPrice, BigDecimal::add));
        Map<Long, String> userNameMap = pieceRateVOLists
                .stream()
                .collect(Collectors.toMap(PieceRateVO::getOperator, PieceRateVO::getOperatorName, (v1, v2) -> v1));
        List<Map<String, Object>> data = Lists.newArrayList();
        Map<String, Object> map;
        for (Long s : userTotal.keySet()) {
            map = Maps.newHashMap();
            map.put("userId", s);
            map.put("userName", userNameMap.get(s) + "小计");
            map.put("totalPieceRate", userTotal.get(s));
            data.add(map);
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", data);
        return R.ok(results);
    }

    @Override
    public R productionBatch(Long id) {
        Map<String, Object> param = Maps.newHashMap();
        // 查询领料单
        param.put("planId", id);
        param.put("sourceType", ConstantForMES.SCTL);
        List<StockOutItemVO> stockOutItemList = reportDao.stockOutItem(param);
        // 查询入库单
        param.put("sourceType", ConstantForMES.SCJH);
        List<StockInItemVO> stockInItemList = reportDao.stockInItem(param);
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
}
