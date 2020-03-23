package com.ev.report.service;

import com.ev.framework.utils.R;
import com.ev.report.vo.CommonVO;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 智能制造报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 17:22:16
 */
public interface SmartManufacturingAccountingReportService {
    Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processPlan(List<Map<String, Object>> data, boolean isTotalData);

    Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionPlan(List<Map<String, Object>> data, boolean isTotalData);

    R pieceRate(CommonVO commonVO);

    R pieceRateGroupByDept(CommonVO commonVO);

    R pieceRateGroupByUser(CommonVO commonVO);

    R productionBatch(Long id);

    Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processOutput(List<Map<String, Object>> data);

    List<Map<String, Object>> productionPlanList(Map<String, Object> params);

    int productionPlanCount(Map<String, Object> params);

    List<Map<String, Object>> processPlanList(Map<String, Object> params);

    int processPlanCount(Map<String, Object> params);

    List<Map<String, Object>> productionBatchList(Map<String, Object> params);

    int productionBatchCount(Map<String, Object> params);

    List<Map<String, Object>> processOutputList(Map<String, Object> params);

    int processOutputCount(Map<String, Object> params);

}
