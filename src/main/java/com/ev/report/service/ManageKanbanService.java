package com.ev.report.service;

import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 设备管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 17:22:16
 */
public interface ManageKanbanService {

    List<Map<String, Object>> feedingList();

    List<Map<String, Object>> getProductionStatistics(Map<String, Object> param);

    List<Map<String, Object>> badProductList(Map<String, Object> param);

    R deviceTask();
}
