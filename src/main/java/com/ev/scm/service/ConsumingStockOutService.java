package com.ev.scm.service;

import com.ev.framework.utils.R;

import java.math.BigDecimal;
import java.util.Map;

public interface ConsumingStockOutService extends StockOutService{
    /**
     * 验证源单数量
     */
    R checkSourceNumber(String item);

    /**
     * 获取投料单数量
     */
    Map<Long, BigDecimal> getFeedingCountMap(Long id);

    /**
     * 审核生产领料单
     */
    R auditConsumingStockOut(Long id);

    /**
     * 反审核生产领料单
     */
    R reverseAuditConsumingStockOut(Long id);

    /**
     * 反写生产投料单
     */
    void writeFeedingCount(Long id, boolean isAudit);

}
