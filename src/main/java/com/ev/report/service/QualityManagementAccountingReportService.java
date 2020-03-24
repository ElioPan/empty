package com.ev.report.service;

import com.ev.custom.domain.SupplierDO;

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

}
