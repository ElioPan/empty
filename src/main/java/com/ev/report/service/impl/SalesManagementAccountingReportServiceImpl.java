package com.ev.report.service.impl;

import com.ev.report.dao.SalesManagementAccountingReportDao;
import com.ev.report.service.SalesManagementAccountingReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SalesManagementAccountingReportServiceImpl implements SalesManagementAccountingReportService {

    @Autowired
    private SalesManagementAccountingReportDao reportDao;


    @Override
    public List<Map<String, Object>> salesContractList(Map<String, Object> params) {
        return reportDao.salesContractList(params);
    }

    @Override
    public List<Map<String, Object>> debtDueList(Map<String, Object> params) {
        return reportDao.debtDueList(params);
    }

    @Override
    public List<Map<String, Object>> summaryList(Map<String, Object> params) {
        return reportDao.summaryList(params);
    }

    @Override
    public List<Map<String, Object>> balanceList(Map<String, Object> params) {
        return reportDao.balanceList(params);
    }
}
