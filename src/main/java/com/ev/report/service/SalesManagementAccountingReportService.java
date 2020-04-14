package com.ev.report.service;

import com.ev.report.vo.ContractPayItemVO;
import com.ev.report.vo.ContractBillItemVO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * 销售管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 17:22:16
 */
public interface SalesManagementAccountingReportService {

    List<Map<String, Object>> salesContractList(Map<String, Object> params);

    List<Map<String, Object>> debtDueList(Map<String, Object> params);

    List<Map<String, Object>> summaryList(Map<String, Object> params);

    List<Map<String, Object>> balanceList(Map<String, Object> params);

    List<ContractBillItemVO> stockBillItem(Map<String, Object> params);

    List<ContractPayItemVO> contractPayItem(Map<String, Object> params);

    Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult(Long clientId, String contractCode, Long contractId, Long deptId, Long userId, int showItemInt, int showTotalInt, String startTime, String endTime);

    Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult(Long clientId, int showTotalInt, int showItemInt, Long deptId, Long userId, String endTime);

    Pair<List<Map<String, Object>>, Map<String, Object>> summaryResult(int showTotalInt, int showItemInt, String clientName, Long clientId, Long deptId, Long userId, String startTime, String endTime, Integer type);

    Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult(String clientName, Long clientId, int showTotalInt, int showItemInt, Long deptId, Long userId, String endTime);
}
