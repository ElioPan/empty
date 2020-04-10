package com.ev.report.dao;

import com.ev.report.vo.ContractBillItemVO;
import com.ev.report.vo.ContractPayItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 销售管理报表分析
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 17:22:16
 */
@Mapper
public interface OutsourcedManagementAccountingReportDao {

    List<Map<String, Object>> outsourcedContractList(Map<String, Object> params);

    List<Map<String, Object>> debtDueList(Map<String, Object> params);

    List<Map<String, Object>> balanceList(Map<String, Object> params);

    List<Map<String, Object>> inOutReconciliationList(Map<String, Object> params);

    List<ContractPayItemVO> contractPayItem(Map<String, Object> params);

    List<ContractBillItemVO> billItem(Map<String, Object> params);
}
