package com.ev.scm.service;

import com.ev.framework.utils.R;

/**
 * @Author Kuzi
 * @Date 2020-2-17 13:06
 **/

public  interface InStockAccountingService  {


    public R saveAccounting(String detailAccounting);

    public R allocationAmount(Long distributionType,Long[] detailAccounting);

    R disposeBusinessAccounting(Long[] stockInIds);

    R disposeAutoAccounting(String detailAccounting);

}