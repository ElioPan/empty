package com.ev.report.service.impl;

import com.ev.report.dao.OutsourcedManagementAccountingReportDao;
import com.ev.report.service.OutsourcedManagementAccountingReportService;
import com.ev.report.vo.ContractBillItemVO;
import com.ev.report.vo.ContractPayItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OutsourcedManagementAccountingReportServiceImpl implements OutsourcedManagementAccountingReportService {

    @Autowired
    private OutsourcedManagementAccountingReportDao reportDao;

    @Override
    public List<Map<String, Object>> debtDueList(Map<String, Object> params) {
        return reportDao.debtDueList(params);
    }

    @Override
    public List<Map<String, Object>> balanceList(Map<String, Object> params) {
        return reportDao.balanceList(params);
    }

    @Override
    public List<Map<String, Object>> outsourcedContractList(Map<String, Object> params) {
        return reportDao.outsourcedContractList(params);
    }

    @Override
    public List<Map<String, Object>> inOutReconciliationList(Map<String, Object> params) {
        return reportDao.inOutReconciliationList(params);
    }

    @Override
    public List<ContractPayItemVO> contractPayItem(Map<String, Object> params) {
        return reportDao.contractPayItem(params);
    }

    @Override
    public List<ContractBillItemVO> billItem(Map<String, Object> params) {
        return reportDao.billItem(params);
    }
}
