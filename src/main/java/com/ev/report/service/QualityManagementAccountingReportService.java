package com.ev.report.service;

import com.ev.custom.domain.SupplierDO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
/**
 * 质量管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface QualityManagementAccountingReportService {
    List<Map<String, Object>> badPurchaseList(Map<String, Object> params);

    List<Map<String, Object>> badProcessList(Map<String, Object> params);

    List<Map<String, Object>> qualityTraceabilityList(Map<String, Object> params);

    int qualityTraceabilityCount(Map<String, Object> params);

    Pair<List<Map<String, Object>>, Map<String, Object>> badPurchaseResult(int showTotalInt, int showItemInt, Long supplierId, Long materielId, String startTime, String endTime);

    Pair<List<Map<String, Object>>, Double> badProcessForDeptResult(int showDeptTotalInt, int showItemInt, int showProcessTotalInt, Long deptId, Long processId, Long operator, Long deviceId, String startTime, String endTime);

    Pair<List<Map<String, Object>>, Double> badProductForDeptResult(int showDeptTotalInt, int showItemInt, int showProductTotalInt, Long deptId, Long materielId, String startTime, String endTime);
}
