package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.scm.domain.StockItemDO;
import com.ev.scm.service.InStockAccountingService;
import com.ev.scm.service.PurchaseExpenseItemService;
import com.ev.scm.service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Kuzi
 * @Date 2020-2-17 13:08
 **/

@Service
public class InStockAccountingServiceImpl implements InStockAccountingService {
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private StockItemService stockItemService;

    @Autowired
    private PurchaseExpenseItemService purchaseExpenseItemService;


    @Override
    public R saveAccounting(String detailAccounting) {
        List<StockItemDO> stockItemDOS = JSONObject.parseArray(detailAccounting, StockItemDO.class);
        if(stockItemDOS.isEmpty()){
            return R.error();
        }else{
            for (StockItemDO stockItemDO:stockItemDOS){
                stockItemService.update(stockItemDO);
            }
            return R.ok();
        }
    }

    @Override
    public R allocationAmount(Long distributionType,String toatalCount,String toatalAmount,String detailAccounting) {

        List<StockItemDO> stockItemDOs = JSONObject.parseArray(detailAccounting, StockItemDO.class);

        if(stockItemDOs.isEmpty()){
            return R.error();
        }else{
            Long[] purchaseInStockId=new Long[stockItemDOs.size()];
            for (int i=0;i<stockItemDOs.size();i++){
                purchaseInStockId[i]=stockItemDOs.get(i).getInheadId();
            }
            //统计所有有关联的费用总和
            Map<String,Object> map= new HashMap<>();
            map.put("purchaseId",purchaseInStockId);
            int totailTaxAmount = purchaseExpenseItemService.getTotailTaxAmount(map);
            if(Objects.equals(0,totailTaxAmount)){
                return R.ok(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsNull",null));
            }else{
                if(Objects.equals(detailAccounting, ConstantForGYL.COUNT_DISTRIBUTION)){
                    //按数量



                    return R.ok(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsOkByCount",null));
                }else {
                    //按金额


                    return R.ok(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsOkByAmount",null));
                }
            }
        }
    }



}
