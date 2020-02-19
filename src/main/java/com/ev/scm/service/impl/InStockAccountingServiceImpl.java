package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.scm.domain.StockInDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.service.InStockAccountingService;
import com.ev.scm.service.PurchaseExpenseItemService;
import com.ev.scm.service.StockInItemService;
import com.ev.scm.service.StockInService;
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
    private StockInService stockInService;

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
    public R allocationAmount(Long distributionType,Long[] stockInIds) {

        List nowList=new ArrayList();
        for (int i = 0; i < stockInIds.length; i++) {
            nowList.add(stockInIds[i]);
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

                    Map<String,Object>  map= new HashMap<>();
                    map.put("inheadId",stockInheadIds[j]);
                    //子表明细
                    List<StockInItemDO> listSotockInItem = stockIntemService.list(map);

                    //如果是核算1标记，则将金额和单件还原，并将标记改为分配
                    restoreCountAmount(stockInheadIds[j],listSotockInItem);

                    //某个采购单关联的子表totailAmount,totailCount
                    Map<String, Object> totailCountAmount = stockIntemService.getTotailCountAmount(stockInheadIds[j]);

                    int pointCount=Constant.BIGDECIMAL_ZERO.intValue()+2;
                    BigDecimal meanValue;
                    if(sign){
                        BigDecimal totailCount= new BigDecimal(String.valueOf(totailCountAmount.get("totailCount")));
                        meanValue=totailExprnseAmountBig.divide(totailCount, pointCount,BigDecimal.ROUND_HALF_UP);
                    }else{
                        BigDecimal totailAmount= new BigDecimal(String.valueOf(totailCountAmount.get("totailAmount")));
                        meanValue=totailExprnseAmountBig.divide(totailAmount,pointCount,BigDecimal.ROUND_HALF_UP);
                    }
                    for (StockInItemDO stockInItemDO:listSotockInItem){
                        map.clear();
                        if(sign){
                            map.put("expence",meanValue.multiply(stockInItemDO.getCount()));
                        }else{
                            map.put("expence",meanValue.multiply(stockInItemDO.getAmount()));
                        }
                        map.put("expence",meanValue);
                        map.put("stockInId",stockInheadIds[j]);
                        stockIntemService.updateExpense(map);
                    }
                }
            }
    }

    public void restoreCountAmount(Long stockInId,List<StockInItemDO> listSotockInItem){
        StockInDO stockInDo = stockInService.get(stockInId);
        if(Objects.equals(1,stockInDo.getSign())){

            //还原金额和单件     批量还原金额单价
            List<StockInItemDO> oldStockInItemDos=new ArrayList<>();
            for(StockInItemDO stockInItemDo:listSotockInItem){
                BigDecimal expense=stockInItemDo.getExpense();
                if(!Objects.equals(0,expense.compareTo(BigDecimal.ZERO) )){
                    BigDecimal oldAmount=stockInItemDo.getAmount().multiply(expense);
                    stockInItemDo.setAmount(oldAmount);
                    stockInItemDo.setUnitPrice(oldAmount.divide(stockInItemDo.getCount(),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP));
                    oldStockInItemDos.add(stockInItemDo);
                }
            }
            //批量执行还原金额单价
            if(!oldStockInItemDos.isEmpty()){
                stockIntemService.batchUpdate(oldStockInItemDos);
            }
            //主表改为分配
            StockInDO newStockInDo=new StockInDO();
            newStockInDo.setSign(0);
            newStockInDo.setId(stockInId);
            stockInService.update(newStockInDo);
        }
    }


    @Override
   public R disposeBusinessAccounting(Long[] stockInIds){

        //判断主表sign是否为1,就略去；为0就计算金额单价回写，并更新主表sign为1
        for(int j=0;j<stockInIds.length;j++){
            StockInDO stockInDo = stockInService.get(stockInIds[j]);
            if(Objects.equals(0,stockInDo.getSign())){

                Map<String,Object>  map= new HashMap<>();
                map.put("inheadId",stockInIds[j]);
                //子表明细
                List<StockInItemDO> listSotockInItem = stockIntemService.list(map);

                List<StockInItemDO> newStockInItemDos=new ArrayList<>();

                for(StockInItemDO stockInItemDo:listSotockInItem){

                    BigDecimal expense=stockInItemDo.getExpense();
                    if(!Objects.equals(0,expense.compareTo(BigDecimal.ZERO) )){

                        int pointCount=Constant.BIGDECIMAL_ZERO+2;
                        BigDecimal newAmount=stockInItemDo.getAmount().add(expense);

                        stockInItemDo.setAmount(newAmount);
                        stockInItemDo.setUnitPrice(newAmount.divide(stockInItemDo.getCount(),pointCount,BigDecimal.ROUND_HALF_UP));
                        newStockInItemDos.add(stockInItemDo);
                    }
                }
                if(!newStockInItemDos.isEmpty()){
                    stockIntemService.batchUpdate(newStockInItemDos);
                }

                //主表改为分配
                StockInDO newStockInDo=new StockInDO();
                newStockInDo.setSign(1);
                newStockInDo.setId(stockInIds[j]);
                stockInService.update(newStockInDo);
            }
        }
        return R.ok();
   }

    @Override
    public R disposeAutoAccounting(String detailAccounting){



       return null;

    }

}
