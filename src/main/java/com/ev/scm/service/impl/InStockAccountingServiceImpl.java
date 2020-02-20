package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.mes.domain.BomDetailDO;
import com.ev.scm.dao.InStockAccountingDao;
import com.ev.scm.domain.StockInDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockOutItemDO;
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

    @Autowired
    private InStockAccountingDao  inStockAccountingDao;

    @Override
    public List<Map<String, Object>> getBomItem(Long ContractItemId) {
        return inStockAccountingDao.getBomItem(ContractItemId);
    }

    @Override
    public List<StockOutItemDO> getStockOutDetail(Long ContractItemId) {
        return inStockAccountingDao.getStockOutDetail(ContractItemId);
    }

    @Override
    public int batchUpdateStockOutItem(List<StockOutItemDO> stockOutItemDOs) {
        return inStockAccountingDao.batchUpdateStockOutItem(stockOutItemDOs);
    }

    @Override
    public Map<String, Object> getTotalTaxAmountCount(Long stockInItemId) {
        return inStockAccountingDao.getTotalTaxAmountCount(stockInItemId);
    }

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

       //根据入库单的产品去找bom里的组件物料明细，并计算本次核销的物料数量
        List<StockInItemDO> stockInItemDos = JSONObject.parseArray(detailAccounting, StockInItemDO.class);
        //更新委外入库费用
        updateOutSourcingInStockExpense(stockInItemDos);

        for (StockInItemDO stockInItemDo:stockInItemDos){
            //产品id
            Long inStockMaterialId=stockInItemDo.getMaterielId();
            //委外入库子表id
            Long stockInItemId=stockInItemDo.getId();
            //入库产品数量
            BigDecimal count = stockInItemDo.getCount();
            //委外合同子表id
            Long contrackItemId=stockInItemDo.getId();

            List<Map<String, Object>> newMaterialCountList=new ArrayList<>();
            Map<String,Object>  oneNewMaterialCounts= new HashMap<>();

            //获取入库单子表信息验证是否已经核算完毕,
            StockInItemDO checkStockInItemDO = stockIntemService.get(stockInItemId);

            if (Objects.equals("1",checkStockInItemDO.getMaterialIdCount())) {
                //跳出本次循环  1核算完毕
                break;
            }
            List<Map<String, Object>> bomItems=new ArrayList<>();
            if(Objects.equals("0",checkStockInItemDO.getMaterialIdCount())){
                //初次核算使用bom清单的物料id   //需要核销的物料和基本数量
                List<Map<String, Object>> bomItem=this.getBomItem(contrackItemId);
                if(bomItem.isEmpty()){
                    //跳出本次循环
                    break;
                }else{
                    for(int i=0;i<bomItem.size();i++){
                        //本次需要核销的数量
                        Map<String,Object>  map=bomItem.get(i);
                        BigDecimal standardCount=new BigDecimal(map.get("standardCount").toString());
                        map.put("standardCount", standardCount.multiply(count));
                        bomItems.add(map);
                    }
                }
            }else{
                //使用上次未核算完的materialIdCount字段下值进行核算
                JSONObject materialIdCountJSON = JSON.parseObject(checkStockInItemDO.getMaterialIdCount().toString());
                JSONArray itemArray = materialIdCountJSON.getJSONArray("materialIdCount");
               for(int j=0;j<itemArray.size();j++){
                   Map<String,Object>  map= (Map<String,Object>)itemArray.get(j);
                   bomItems.add(map);
               }
            }

            //委外合同子表id 下发出数量大于已核销数量明细
            List<StockOutItemDO> stockOutDetails = this.getStockOutDetail(contrackItemId);
            if(stockOutDetails.isEmpty()){
                //跳出本次循环
                break;
            }else{
                //核销的物料和出库明细的物料相等，即可计算核销。
                List<StockOutItemDO> newStockOutItemDoList=new ArrayList<>();
                for(int k=0;k<bomItems.size();k++){

                    //需要核销的数量
                    BigDecimal standardCount=new BigDecimal(bomItems.get(k).get("standardCount").toString());
                    //需要核销的物料
                    Integer materielId=Integer.parseInt(String.valueOf(bomItems.get(k).get("materielId").toString()));

                    //所有id下发出数量大于已核销数量明细
                    List<StockOutItemDO> stockOutDetail = this.getStockOutDetail(contrackItemId);

                    for(StockOutItemDO stockOutItemDO:stockOutDetail){
                        if(Objects.equals(0,standardCount.compareTo(BigDecimal.ZERO))){
                            break;
                        }
                        if(Objects.equals(materielId,stockOutItemDO.getMaterielId())){
                            //已核销数量
                            BigDecimal chargeOffCount=stockOutItemDO.getChargeOffCount().compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:stockOutItemDO.getChargeOffCount();
                            //出库单中可以再核销的数量
                            BigDecimal canChargeOffCount = stockOutItemDO.getCount().subtract(chargeOffCount);

                            if(standardCount.compareTo(canChargeOffCount)>0){
                                stockOutItemDO.setChargeOffCount(canChargeOffCount.add(chargeOffCount));
                                standardCount=standardCount.subtract(canChargeOffCount);
                                newStockOutItemDoList.add(stockOutItemDO);

                            }else if(standardCount.compareTo(canChargeOffCount)==0){
                                stockOutItemDO.setChargeOffCount(canChargeOffCount.add(chargeOffCount));
                                standardCount=standardCount.subtract(canChargeOffCount);
                                newStockOutItemDoList.add(stockOutItemDO);
                                break;

                            }else if(standardCount.compareTo(canChargeOffCount)<0){
                                stockOutItemDO.setChargeOffCount(canChargeOffCount.add(standardCount));
                                standardCount=standardCount.subtract(standardCount);
                                newStockOutItemDoList.add(stockOutItemDO);
                                break;
                            }
                        }
                    }
                    //批量更新委外出库的核销数量
                    this.batchUpdateStockOutItem(newStockOutItemDoList);

                    //如果bom中的某个物料的数量没有在本次核销完毕，则记录在入库单的字段中（需要添加字段）下次根据未核销的物料来核销
                    if(standardCount.compareTo(BigDecimal.ZERO)>0){
                        //记录此物料
                        oneNewMaterialCounts.put("standardCount",standardCount);
                        oneNewMaterialCounts.put("materielId",materielId);
                        newMaterialCountList.add(oneNewMaterialCounts);
                    }
                }
            }
            checkStockInItemDO.setMaterialIdCount(newMaterialCountList.toString());
            stockIntemService.update(checkStockInItemDO);
        }
       return R.ok();
    }

    public void updateOutSourcingInStockExpense( List<StockInItemDO> stockInItemDos){
        List<StockInItemDO> newStockInItemDo=new ArrayList<>();
        for (StockInItemDO stockInItemDo:stockInItemDos){
            //委外费用子表明细
            Map<String, Object> totalTaxAmountCount = this.getTotalTaxAmountCount(stockInItemDo.getId());
            BigDecimal totailCount=new BigDecimal(totalTaxAmountCount.get("totailCount").toString());
            BigDecimal taxAmount=new BigDecimal(totalTaxAmountCount.get("taxAmount").toString());
            stockInItemDo.setExpense((taxAmount.multiply(stockInItemDo.getCount()).divide(totailCount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP)));
            newStockInItemDo.add(stockInItemDo);
        }
        //批量更新委外入库子表
        stockIntemService.batchUpdate(newStockInItemDo);
    }





}
