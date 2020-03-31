package com.ev.report.service.impl;

import com.ev.report.dao.PurchaseManagementAccountingReportDao;
import com.ev.report.service.PurchaseManagementAccountingReportService;
import com.ev.report.vo.PurchaseContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PurchaseManagementAccountingReportServiceImpl implements PurchaseManagementAccountingReportService {

    @Autowired
    private PurchaseManagementAccountingReportDao reportDao;


    @Override
    public List<Map<String, Object>> purchaseContractList(Map<String, Object> params) {
        return reportDao.purchaseContractList(params);
    }

    @Override
    public List<Map<String, Object>> debtDueList(Map<String, Object> params) {
        return reportDao.debtDueList(params);
    }

    @Override
    public List<PurchaseContractVO> priceAnalysisList(Map<String, Object> params) {
        return reportDao.priceAnalysisList(params);
    }

    @Override
    public List<Map<String, Object>> balanceList(Map<String, Object> params) {
        return reportDao.balanceList(params);
    }
}
