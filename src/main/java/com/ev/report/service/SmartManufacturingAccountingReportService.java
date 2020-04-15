package com.ev.report.service;

import com.ev.framework.utils.R;
import com.ev.report.vo.CommonVO;
import com.ev.report.vo.PieceRateVO;
import com.ev.report.vo.ProcessReportVO;
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

    Pair<List<PieceRateVO>, Double>  pieceRateGroup(CommonVO commonVO);

    R pieceRateGroupByUser(CommonVO commonVO);

    R productionBatch(Long id);

    List<Map<String, Object>> productionPlanList(Map<String, Object> params);

    int productionPlanCount(Map<String, Object> params);

    List<Map<String, Object>> processPlanList(Map<String, Object> params);

    int processPlanCount(Map<String, Object> params);

    List<ProcessReportVO> processOutputList(Map<String, Object> params);

    Map<String, Object> processOutputCount(Map<String, Object> params);

}
