package com.ev.scm.service;

import com.ev.framework.utils.R;

/**
 * @Author Kuzi
 * @Date 2020-2-17 13:06
 **/

public  interface InStockAccountingService  {


    public R saveAccounting(String detailAccounting);

    public R  allocationAmount(Long distributionType,String toatalCount,String toatalAmount,String detailAccounting);





}
