package com.ev.scm.service;

import com.ev.framework.utils.R;

public interface SaleStockOutService extends StockOutService{
    /**
     * 反审核委外出库
     */
    R reverseAuditSaleStockOut(Long id);
}
