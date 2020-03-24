package com.ev.report.service.impl;

import com.ev.report.dao.QualityManagementAccountingReportDao;
import com.ev.report.service.QualityManagementAccountingReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QualityManagementAccountingReportServiceImpl implements QualityManagementAccountingReportService {
    @Autowired
    private QualityManagementAccountingReportDao reportDao;

    @Override
    public List<Map<String, Object>> badPurchaseList(Map<String, Object> params) {
        return reportDao.badPurchaseList(params);
    }

    @Override
    public List<Map<String, Object>> badProcessList(Map<String, Object> params) {
        return reportDao.badProcessList(params);
    }

    @Override
    public List<Map<String, Object>> qualityTraceabilityList(Map<String, Object> params) {
        return reportDao.qualityTraceabilityList(params);
    }

    @Override
    public int qualityTraceabilityCount(Map<String, Object> params) {
        return reportDao.qualityTraceabilityList(params).size();
    }

}
