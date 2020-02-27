package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.StockOutItemDO;

import java.util.List;
import java.util.Map;

/**
 * @Author Kuzi
 * @Date 2020-2-17 13:06
 **/

public  interface InStockAccountingService  {


    public R saveAccounting(String detailAccounting);

    public R allocationAmount(Long distributionType,Long[] detailAccounting);

    R disposeBusinessAccounting(Long[] stockInIds);

    R disposeAutoAccounting(String detailAccounting);

    List<Map<String, Object>> getBomItem(Long ContractItemId);

    List<StockOutItemDO> getStockOutDetail(Long ContractItemId);

    int batchUpdateStockOutItem(List<StockOutItemDO> stockOutItemDOs);

    Map<String, Object> getTotalTaxAmountCount(Long stockInItemId);

    R disposerollbackAccccounting(Long[] stockInItemIds);


}
