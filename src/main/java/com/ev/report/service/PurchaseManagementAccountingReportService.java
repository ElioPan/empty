package com.ev.report.service;

import com.ev.report.vo.PurchaseContractVO;

import java.util.List;
import java.util.Map;

/**
 * 销售管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 17:22:16
 */
public interface PurchaseManagementAccountingReportService {

    List<Map<String, Object>> purchaseContractList(Map<String, Object> params);

    List<Map<String, Object>> debtDueList(Map<String, Object> params);

    List<PurchaseContractVO> priceAnalysisList(Map<String, Object> params);

    List<Map<String, Object>> balanceList(Map<String, Object> params);

}
