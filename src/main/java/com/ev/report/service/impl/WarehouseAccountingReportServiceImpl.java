package com.ev.report.service.impl;

import com.ev.report.dao.SmartManufacturingAccountingReportDao;
import com.ev.report.dao.WarehouseAccountingReportDao;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WarehouseAccountingReportServiceImpl implements WarehouseAccountingReportService {
    @Autowired
    private WarehouseAccountingReportDao reportDao;
    @Autowired
    private SmartManufacturingAccountingReportDao smartManufacturingReportDao;


    @Override
    public List<StockOutItemVO> stockOutItem(Map<String, Object> params) {
        return smartManufacturingReportDao.stockOutItem(params);
    }

    @Override
    public List<StockInItemVO> stockInItem(Map<String, Object> params) {
        return smartManufacturingReportDao.stockInItem(params);
    }

    @Override
    public List<InOutStockItemVO> inOutStockItem(Map<String, Object> params) {
        return reportDao.inOutStockItem(params);
    }

    @Override
    public List<Map<String, Object>> stockList(Map<String, Object> params) {
        return reportDao.stockList(params);
    }

    @Override
    public List<Map<String, Object>> pickingSummary(Map<String, Object> params) {
        return reportDao.pickingSummary(params);
    }
}
