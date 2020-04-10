package com.ev.report.dao;

import com.ev.report.vo.PurchaseContractVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 17:22:16
 */
@Mapper
public interface PurchaseManagementAccountingReportDao {

    List<Map<String, Object>> purchaseContractList(Map<String, Object> params);

    List<Map<String, Object>> debtDueList(Map<String, Object> params);

    List<PurchaseContractVO> priceAnalysisList(Map<String, Object> params);

    List<Map<String, Object>> balanceList(Map<String, Object> params);

    List<Map<String, Object>> getInCount(Map<String, Object> params);

    List<Map<String, Object>> getInvoiceCountAndAmount(Map<String, Object> params);

    List<Map<String, Object>> getPayAmount(Map<String, Object> params);


}
