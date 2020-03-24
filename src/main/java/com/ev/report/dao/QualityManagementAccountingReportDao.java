package com.ev.report.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 质量管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-17 17:22:16
 */
@Mapper
public interface QualityManagementAccountingReportDao {

    List<Map<String, Object>> badPurchaseList(Map<String, Object> params);

    List<Map<String, Object>> badProcessList(Map<String, Object> params);

    List<Map<String, Object>> qualityTraceabilityList(Map<String, Object> params);
}
