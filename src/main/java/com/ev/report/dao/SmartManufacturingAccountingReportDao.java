package com.ev.report.dao;

import com.ev.report.domain.MaterialsScrapItemDO;
import com.ev.report.domain.ProcessReportCheckDO;
import com.ev.report.domain.ProcessReportDO;
import com.ev.report.domain.StockInItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 智能制造报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 17:22:16
 */
@Mapper
public interface SmartManufacturingAccountingReportDao {

    List<Map<String, Object>> productionPlanList(Map<String, Object> params);

    int productionPlanCount(Map<String, Object> params);

    List<Map<String, Object>> processPlanList(Map<String, Object> params);

    int processPlanCount(Map<String, Object> params);

    List<Map<String, Object>> productionBatchList(Map<String, Object> params);

    int productionBatchCount(Map<String, Object> params);

    List<Map<String, Object>> processOutputList(Map<String, Object> params);

    int processOutputCount(Map<String, Object> params);

    List<ProcessReportDO> processReport(Map<String, Object> param);

    List<ProcessReportCheckDO> processReportCheck(Map<String, Object> param);

    List<MaterialsScrapItemDO> materielScrapItem(Map<String, Object> param);

    List<StockInItemDO> stockInItem(Map<String, Object> param);
}
