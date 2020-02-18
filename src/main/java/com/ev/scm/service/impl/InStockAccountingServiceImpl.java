package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.service.InStockAccountingService;
import com.ev.scm.service.PurchaseExpenseItemService;
import com.ev.scm.service.StockInItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author Kuzi
 * @Date 2020-2-17 13:08
 **/

@Service
public class InStockAccountingServiceImpl implements InStockAccountingService {
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private StockInItemService stockIntemService;

    @Autowired
    private PurchaseExpenseItemService purchaseExpenseItemService;


    @Override
    public R saveAccounting(String detailAccounting) {
        List<StockInItemDO> stockInItemDOs = JSONObject.parseArray(detailAccounting, StockInItemDO.class);
        if(stockInItemDOs.isEmpty()){
            return R.error();
        }else{
            for (StockInItemDO stockInItemDO:stockInItemDOs){
                stockIntemService.update(stockInItemDO);
            }
            return R.ok();
        }
    }

    @Override
    public R allocationAmount(Long distributionType,Long[] detailAccounting) {

        List nowList=new ArrayList();
        for (int i = 0; i < detailAccounting.length; i++) {
            nowList.add(detailAccounting[i]);
        }
        nowList=new ArrayList(new HashSet(nowList));


        Long[] stockInheadIds=new Long[nowList.size()];
        for (int i = 0; i < nowList.size(); i++) {
            stockInheadIds[i]=Long.valueOf(String.valueOf(nowList.get(i)));
        }
        Map<String,Object> map= new HashMap<>();
        map.put("purchaseId",stockInheadIds);
        //提交的所有id的费用的总和
        int totailTaxAmount = purchaseExpenseItemService.getTotailTaxAmount(map);

        if(Objects.equals(0,totailTaxAmount)) {
            return R.ok(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsNull", null));

        }else{
                if(Objects.equals(distributionType, ConstantForGYL.COUNT_DISTRIBUTION)){
                    //按数量
                    changeExpenseOfStockInItem(stockInheadIds,true);
                    return R.ok(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsOkByCount",null));
                }else {

                    changeExpenseOfStockInItem(stockInheadIds,false);
                    return R.ok(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsOkByAmount",null));
                }
        }
    }

    public void  changeExpenseOfStockInItem( Long[] stockInheadIds,Boolean sign){

            for(int j=0;j<stockInheadIds.length;j++){

                //某个采购单关联的所有费用总和
                int totailExprnseAmount = purchaseExpenseItemService.getTotailCountAmount(stockInheadIds[j]);
                if(!Objects.equals(0,totailExprnseAmount)){
                    BigDecimal totailExprnseAmountBig=new BigDecimal(totailExprnseAmount);
                    //totailAmount,totailCount
                    Map<String, Object> totailCountAmount = stockIntemService.getTotailCountAmount(stockInheadIds[j]);

                    BigDecimal meanValue;
                    if(sign){
                        BigDecimal totailCount= new BigDecimal(String.valueOf(totailCountAmount.get("totailCount")));
                        meanValue=totailExprnseAmountBig.divide(totailCount, Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                    }else{
                        BigDecimal totailAmount= new BigDecimal(String.valueOf(totailCountAmount.get("totailAmount")));
                        meanValue=totailExprnseAmountBig.divide(totailAmount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                    }
                    Map<String,Object>  query= new HashMap<>();
                    query.put("expence",meanValue);
                    query.put("stockInId",stockInheadIds[j]);
                    stockIntemService.updateExpense(query);
                }
            }
    }





}
