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

    List<Map<String, Object>> getBomItem(Long ContractItemId);

    List<StockOutItemDO> getStockOutDetail(Long ContractItemId);

    int batchUpdateStockOutItem(List<StockOutItemDO> stockOutItemDOs);

    Map<String, Object> getTotalTaxAmountCount(Long stockInItemId);

    R disposeRollbackAccccounting(Long[] stockInItemIds);

    R disposeallocationOutIn(Long[] stockInIds);

    List<Map<String, Object>> getUnitPrice(Long stockInItemId);

    R disposeaccountingPrice(Long[] stockInIds);

    int getCountOfSignIsO(Map<String, Object> map);

    R disposeAutoAccounting(Long stockInItemId,String detailAccounting);

    R disposeAffirmAndBack(Long stockInItemId,String detailAccounting);

    int  getAnalysisDate(Map<String, Object> map);


    boolean disposeIsClose(Long[] itemOrHaedIds,Boolean sign);


}
