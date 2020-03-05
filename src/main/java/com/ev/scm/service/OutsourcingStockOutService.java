package com.ev.scm.service;

import com.ev.framework.utils.R;

public interface OutsourcingStockOutService extends StockOutService{
    /**
     * 审核委外出库
     */
    R auditOutsourcingStockOut(Long id);

    /**
     * 反审核委外出库
     */
    R reverseAuditOutsourcingStockOut(Long id);

}
