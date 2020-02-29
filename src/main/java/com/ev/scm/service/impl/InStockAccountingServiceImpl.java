package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.scm.dao.InStockAccountingDao;
import com.ev.scm.domain.OutsourcingContractItemDO;
import com.ev.scm.domain.StockInDO;
import com.ev.scm.domain.StockInItemDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.*;

import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    private OutsourcingContractItemService outsourcingContractItemService;
    @Autowired
    private StockInItemService stockIntemService;
    @Autowired
    private StockOutItemService stockOutItemService;
    @Autowired
    private StockInService stockInService;
    @Autowired
    private PurchaseExpenseItemService purchaseExpenseItemService;
    @Autowired
    private InStockAccountingDao  inStockAccountingDao;

    @Override
    public int getCountOfSignIsO(Map<String, Object> map) {
        return inStockAccountingDao.getCountOfSignIsO(map);
    }
    @Override
    public List<Map<String, Object>> getUnitPrice(Long stockInItemId) {
        return inStockAccountingDao.getUnitPrice(stockInItemId);
    }

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

    private void restoreCountAmount(Long stockInId,List<StockInItemDO> listSotockInItem){
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
    public R disposeAutoAccounting(Long stockInItemId,String detailAccounting){

        List<StockOutItemDO> stockOutItemDos = JSONObject.parseArray(detailAccounting, StockOutItemDO.class);
        StockInItemDO stockInItemDo = stockIntemService.get(stockInItemId);
        //初始化为"0"
        String accountSource=stockInItemDo.getAccountSource().toString();
        if(!Objects.equals("0",accountSource)||!Objects.nonNull(stockInItemDo.getCost())){
            R.error(messageSourceHandler.getMessage("scm.accounting.inStockIsAccountingOver", null));
        }
        //产品id
        Long inStockMaterialId=stockInItemDo.getMaterielId();
        //入库成品数量
        BigDecimal count = stockInItemDo.getCount();
        //委外合同子表id
        Long contrackItemId=stockInItemDo.getSourceId();

        List<Map<String, Object>> bomItems=new ArrayList<>();
        List<Map<String, Object>> bomItem=this.getBomItem(contrackItemId);
        if(bomItem.isEmpty()){
            R.error(messageSourceHandler.getMessage("scm.accounting.haveNoBom", null));
        }else{
            for(int i=0;i<bomItem.size();i++){
                //本次需要核销的数量
                Map<String,Object>  map=bomItem.get(i);
                BigDecimal standardCount=new BigDecimal(map.get("standardCount").toString());
                map.put("standardCount", standardCount.multiply(count));
                bomItems.add(map);
            }
        }

        List<Map<String, Object>> results=new ArrayList<>();
        List<Map<String, Object>> leaveOverMaterials=new ArrayList<>();
        for(int k=0;k<bomItems.size();k++) {
            Map<String, Object> oneNewMaterialCounts = new HashMap<>();

            //需要某一组件物料需要核销的数量
            BigDecimal standardCount = new BigDecimal(bomItems.get(k).get("standardCount").toString());
            //需要核销的物料
            Integer materielId = Integer.parseInt(String.valueOf(bomItems.get(k).get("materielId").toString()));

            for(int i=0;i<stockOutItemDos.size();i++){

                StockOutItemDO stockOutItemDO=stockOutItemDos.get(i);
                if(Objects.equals(0,standardCount.compareTo(BigDecimal.ZERO))){
                    //需要核销的某物料已被核销完毕，跳出循环，进入下个组建物料的分配
                    break;
                }
                BigDecimal  countOnce=BigDecimal.ZERO;

                if(Objects.equals(materielId,stockOutItemDO.getMaterielId())){
                    //已核销数量
                    BigDecimal chargeOffCount=stockOutItemDO.getChargeOffCount().compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:stockOutItemDO.getChargeOffCount();
                    //出库单中可以再核销的数量
                    BigDecimal canChargeOffCount = stockOutItemDO.getCount().subtract(chargeOffCount);

                    if(standardCount.compareTo(canChargeOffCount)==1){

                        standardCount=standardCount.subtract(canChargeOffCount);
                        countOnce=canChargeOffCount;

                    }else if(standardCount.compareTo(canChargeOffCount)==0){

                        standardCount=standardCount.subtract(standardCount);
                        countOnce=canChargeOffCount;

                    }else if(standardCount.compareTo(canChargeOffCount)==-1){

                        standardCount=standardCount.subtract(standardCount);
                        countOnce=standardCount;
                    }
                }

                if(!Objects.equals(BigDecimal.ZERO,countOnce)){
                    Map<String,Object>  map= new HashMap<>();
                    map.put("thisTimeCount",countOnce);
                    map.put("id",stockOutItemDos.get(i).getId());
                    results.add(map);
                }
            }
            //记录否存在未分配完的物料
            if(!Objects.equals(BigDecimal.ZERO,standardCount)){
                Map<String,Object>  map= new HashMap<>();
                map.put("materialId",materielId);
                map.put("count",standardCount);
                leaveOverMaterials.add(map);
            }
        }

        //处理遗留未分配的组件物料------后期作为bug排查吧
        if(Objects.nonNull(leaveOverMaterials)){
            StockInItemDO stockInItemDO = new StockInItemDO();
            stockInItemDO.setMaterialIdCount(leaveOverMaterials.toString());
            stockInItemDO.setId(stockInItemId);
            stockIntemService.update(stockInItemDO);
        }

        Map<String,Object>  result= new HashMap<>();
        result.put("data",results);
        return R.ok(result);
    }

    @Override
    public R disposeAffirmAndBack(Long stockInItemId, String detailAccounting) {

        List<Map<String,Object>> list = JSONArray.parseObject(detailAccounting, List.class);

        List<Map<String,Object>> materialIdCounts=new ArrayList<>();

        List<StockOutItemDO> outItemDOS = new ArrayList<>();
        BigDecimal cost=BigDecimal.ZERO;

        for(int i=0;i<list.size();i++){

            Map<String,Object> outStockDetail=(Map<String,Object>)list.get(i);
            BigDecimal thisTimeCount= new BigDecimal(outStockDetail.get("thisTimeCount").toString()==null?"0":outStockDetail.get("thisTimeCount").toString());
            if(Objects.equals(BigDecimal.ZERO,thisTimeCount)){
                continue;
            }
            Long id =Long.parseLong(outStockDetail.get("id").toString());
            BigDecimal unitPrice= new BigDecimal(outStockDetail.get("unitPrice").toString());
            BigDecimal chargeOffCount= new BigDecimal(outStockDetail.get("chargeOffCount").toString());

            cost=cost.add(thisTimeCount.multiply(unitPrice));

            Map<String,Object>  materialIdCountMap= new HashMap<>();
            materialIdCountMap.put("outItemID",id);
            materialIdCountMap.put("countOnce",thisTimeCount);
            materialIdCounts.add(materialIdCountMap);

            StockOutItemDO stockOutItemDO=new StockOutItemDO();
            stockOutItemDO.setId(id);
            stockOutItemDO.setChargeOffCount(chargeOffCount.add(thisTimeCount));
            outItemDOS.add(stockOutItemDO);
        }

        stockOutItemService.batchUpdate(outItemDOS);

        StockInItemDO stockInItemDo = new StockInItemDO();
        stockInItemDo.setId(stockInItemId);
        stockInItemDo.setCost(cost);
        stockInItemDo.setMaterialIdCount(materialIdCounts.toString());
        stockIntemService.update(stockInItemDo);

        return R.ok();
    }


//    @Override
//    public R disposeAutoAccounting(String detailAccounting){
//
//       //根据入库单的产品去找bom里的组件物料明细，并计算本次核销的物料数量
//        List<StockInItemDO> stockInItemDos = JSONObject.parseArray(detailAccounting, StockInItemDO.class);
//        //更新委外入库费用
////        updateOutSourcingInStockExpense(stockInItemDos);
//
//        for (StockInItemDO stockInItemDo:stockInItemDos){
//            //产品id
//            Long inStockMaterialId=stockInItemDo.getMaterielId();
//            //委外入库子表id
//            Long stockInItemId=stockInItemDo.getId();
//            //入库产品数量
//            BigDecimal count = stockInItemDo.getCount();
//            //委外合同子表id
//            Long contrackItemId=stockInItemDo.getId();
//
//            List<Map<String, Object>> newMaterialCountList=new ArrayList<>();
//
//            List<Map<String,Object>> accountSourceList;
//            //初始化为"0"
//            String accountSource=stockInItemDo.getAccountSource().toString();
//            if(Objects.equals("0",accountSource)){
//                accountSourceList=new ArrayList<>();
//            }else{
//                accountSourceList = JSONArray.parseObject(stockInItemDo.getAccountSource().toString(),List.class);
//            }
//
//            //此行产品所有组件物料核销后的成本，每个组件物料核销数乘委外出库单价的累加
//            BigDecimal totailCost=new BigDecimal(BigInteger.ZERO);
//
//            //获取入库单子表信息验证是否已经核算完毕,
//            StockInItemDO checkStockInItemDO = stockIntemService.get(stockInItemId);
//            if (Objects.equals("1",checkStockInItemDO.getMaterialIdCount())) {
//                //跳出本次循环  1核算完毕
//                continue;
//            }
//
//            //计算出本次要核销的组件物料的数量
//            List<Map<String, Object>> bomItems=new ArrayList<>();
//            if(Objects.equals("0",checkStockInItemDO.getMaterialIdCount())){
//                //初次核算使用bom清单的物料id   //需要核销的物料和基本数量
//                List<Map<String, Object>> bomItem=this.getBomItem(contrackItemId);
//                if(bomItem.isEmpty()){
//                    //跳出本次循环
//                    continue;
//                }else{
//                    for(int i=0;i<bomItem.size();i++){
//                        //本次需要核销的数量
//                        Map<String,Object>  map=bomItem.get(i);
//                        BigDecimal standardCount=new BigDecimal(map.get("standardCount").toString());
//                        map.put("standardCount", standardCount.multiply(count));
//                        bomItems.add(map);
//                    }
//                }
//            }else{
//                //使用上次未核算完的materialIdCount字段下值进行核算
//                JSONObject materialIdCountJSON = JSON.parseObject(checkStockInItemDO.getMaterialIdCount().toString());
//                JSONArray itemArray = materialIdCountJSON.getJSONArray("materialIdCount");
//               for(int j=0;j<itemArray.size();j++){
//                   Map<String,Object>  map= (Map<String,Object>)itemArray.get(j);
//                   bomItems.add(map);
//               }
//            }
//
//            //委外合同子表id 下发出数量大于已核销数量明细（发出数量都已被核销掉完毕筛选出）
//            List<StockOutItemDO> stockOutDetails = this.getStockOutDetail(contrackItemId);
//            if(stockOutDetails.isEmpty()){
//                //跳出本次循环
//                continue;
//            }else{
//                //核销的物料和出库明细的物料相同，即可计算核销。
//
//                for(int k=0;k<bomItems.size();k++){
//                    List<StockOutItemDO> newStockOutItemDoList=new ArrayList<>();
//                    Map<String,Object>  oneNewMaterialCounts= new HashMap<>();
//                    //需要某一组件物料需要核销的数量
//                    BigDecimal standardCount=new BigDecimal(bomItems.get(k).get("standardCount").toString());
//                    //需要核销的物料
//                    Integer materielId=Integer.parseInt(String.valueOf(bomItems.get(k).get("materielId").toString()));
//
//                    //所有id下发出数量大于已核销数量明细
//                    List<StockOutItemDO> stockOutDetail = this.getStockOutDetail(contrackItemId);
//
//                    for(StockOutItemDO stockOutItemDO:stockOutDetail){
//
//                        if(Objects.equals(0,standardCount.compareTo(BigDecimal.ZERO))){
//                            //需要核销的某物料已被核销完毕，跳出循环，进入下个组建物料的分配
//                            break;
//                        }
//                        BigDecimal  countOnce=BigDecimal.ZERO;
//
//                        if(Objects.equals(materielId,stockOutItemDO.getMaterielId())){
//                            //已核销数量
//                            BigDecimal chargeOffCount=stockOutItemDO.getChargeOffCount().compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:stockOutItemDO.getChargeOffCount();
//                            //出库单中可以再核销的数量
//                            BigDecimal canChargeOffCount = stockOutItemDO.getCount().subtract(chargeOffCount);
//
//                                if(standardCount.compareTo(canChargeOffCount)==1){
//                                    stockOutItemDO.setChargeOffCount(chargeOffCount.add(chargeOffCount));
//                                    standardCount=standardCount.subtract(canChargeOffCount);
//                                    newStockOutItemDoList.add(stockOutItemDO);
//
//                                    countOnce=canChargeOffCount;
//                                    totailCost=totailCost.add(stockOutItemDO.getSellUnitPrice().multiply(canChargeOffCount));
//
//                                }else if(standardCount.compareTo(canChargeOffCount)==0){
//                                    stockOutItemDO.setChargeOffCount(chargeOffCount.add(canChargeOffCount));
//                                    standardCount=standardCount.subtract(standardCount);
//                                    newStockOutItemDoList.add(stockOutItemDO);
//
//                                    countOnce=canChargeOffCount;
//                                    totailCost=totailCost.add(stockOutItemDO.getSellUnitPrice().multiply(canChargeOffCount));
//
//                                }else if(standardCount.compareTo(canChargeOffCount)==-1){
//                                    stockOutItemDO.setChargeOffCount(canChargeOffCount.add(standardCount));
//                                    standardCount=standardCount.subtract(standardCount);
//                                    newStockOutItemDoList.add(stockOutItemDO);
//
//                                    countOnce=standardCount;
//                                totailCost=totailCost.add(stockOutItemDO.getSellUnitPrice().multiply(standardCount));
//                            }
//                        }
//                        if(!Objects.equals(0,countOnce.compareTo(BigDecimal.ZERO))){
//                            Map<String,Object>  map= new HashMap<>();
//                            map.put("outItemID",stockOutItemDO.getId());
//                            map.put("countOnce",countOnce);
//                            accountSourceList.add(map);
//                        }
//                    }
//                    //批量更新委外出库的核销数量
//                    this.batchUpdateStockOutItem(newStockOutItemDoList);
//
//                    //如果bom中的某个物料的数量没有在本次核销完毕，则记录在入库单的字段中（需要添加字段）下次根据未核销的物料来核销
//                    if(standardCount.compareTo(BigDecimal.ZERO)>0){
//                        //记录此物料
//                        oneNewMaterialCounts.put("standardCount",standardCount);
//                        oneNewMaterialCounts.put("materielId",materielId);
//                        newMaterialCountList.add(oneNewMaterialCounts);
//                    }
//                }
//            }
//            if(newMaterialCountList.size()>0){
//                checkStockInItemDO.setMaterialIdCount(newMaterialCountList.toString());
//
//            }else{
//                checkStockInItemDO.setMaterialIdCount("1");
//            }
//            checkStockInItemDO.setCost(totailCost);
//            checkStockInItemDO.setAccountSource(accountSourceList.toString());
//            stockIntemService.update(checkStockInItemDO);
//        }
//       return R.ok();
//    }



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

    @Override
    public R disposeRollbackAccccounting(Long[] stockInds){

        //取出所有明细
        Map<String,Object>  map= new HashMap<>();
        map.put("id",stockInds);
        List<StockInItemDO> itemDetailById = stockIntemService.getItemDetailById(map);
        List<StockOutItemDO> LisTStockOutItemDo=new ArrayList<>();

        for (StockInItemDO stockInItemDO : itemDetailById) {
            StockInDO stockInDO = stockInService.get(stockInItemDO.getInheadId());

            if (stockInDO.getSign() == 1) {
                //将金额和单价还原，并将成本和费用更新为0.
               BigDecimal totailAmout = stockInItemDO.getAmount().subtract(stockInItemDO.getCost().add(stockInItemDO.getExpense()));
               BigDecimal unitPrice = totailAmout.divide(stockInItemDO.getCount(),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                stockInItemDO.setAmount(totailAmout);
                stockInItemDO.setUnitPrice(unitPrice);
                stockInItemDO.setExpense(BigDecimal.ZERO);
            }
            if (Objects.equals("0", stockInItemDO.getAccountSource())) {
                continue;
            } else {

                List<Map<String,Object>> objects = JSONArray.parseObject(stockInItemDO.getAccountSource().toString(), List.class);

                for (int i = 0; i < objects.size(); i++) {
                    Map<String, Object> mapOutItrmId = (Map<String, Object>)objects.get(i);
                    Long outItemId = Long.parseLong(mapOutItrmId.get("outItemID").toString());
                    BigDecimal standCount = new BigDecimal(mapOutItrmId.get("countOnce").toString());
                    StockOutItemDO stockOutItemDO = new StockOutItemDO();
                    StockOutItemDO stockOutItemDo = stockOutItemService.get(outItemId);
                    stockOutItemDO.setChargeOffCount(stockOutItemDo.getChargeOffCount().subtract(standCount));
                    LisTStockOutItemDo.add(stockOutItemDO);
                }
            }
            stockInItemDO.setCost(BigDecimal.ZERO);
            stockInItemDO.setAccountSource("0");
            stockInDO.setSign(2);
            stockInService.update(stockInDO);
        }
        //批量更新 入库子表
            stockIntemService.batchUpdate(itemDetailById);
            stockOutItemService.batchUpdate(LisTStockOutItemDo);
        return R.ok();
    }

    @Override
    public R disposeallocationOutIn(Long[] stockInIds){

        for(Long stockInId:stockInIds){
            Map<String,Object>  map= new HashMap<>();
            map.put("inheadId",stockInId);
            List<StockInItemDO> inItemDos = stockIntemService.list(map);
            for(StockInItemDO inItemDo:inItemDos){

                List<Map<String, Object>> unitPriceMap = this.getUnitPrice(inItemDo.getId());
                BigDecimal unitPrice;
                if(unitPriceMap.isEmpty()){
                    //取合同单价
                    OutsourcingContractItemDO outsourcingContractItemDO = outsourcingContractItemService.get(inItemDo.getSourceId());
                    if(outsourcingContractItemDO!=null){
                        unitPrice=outsourcingContractItemDO.getUnitPrice();
                    }else{
                        unitPrice=BigDecimal.ZERO;
                    }
                }else{
                    //取加工费用单价
                    unitPrice=new BigDecimal(unitPriceMap.get(0).get("unitPrice").toString());
                }
                inItemDo.setExpense(inItemDo.getCount().multiply(unitPrice));
            }
            stockIntemService.batchUpdate(inItemDos);

            StockInDO stockInDO=new StockInDO();
            stockInDO.setSign(0);
            stockInDO.setId(stockInId);
            stockInService.update(stockInDO);
        }
        return null;
    }

    @Override
    public R disposeaccountingPrice(Long[] stockInIds) {
        Map<String,Object>  map= new HashMap<>();
        map.put("id",stockInIds);
        if(this.getCountOfSignIsO(map)>0){
            return  R.error(messageSourceHandler.getMessage("scm.accounting.bussenissAccounting", null));
        }
        map.clear();
        for(Long id:stockInIds) {

            map.put("inheadId",id);
            List<StockInItemDO> inItemDos = stockIntemService.list(map);

            for(StockInItemDO inItemDo:inItemDos){

                BigDecimal totailAmout = inItemDo.getAmount().add(inItemDo.getCost().add(inItemDo.getExpense()));
                BigDecimal unitPrice = totailAmout.divide(inItemDo.getCount(),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                inItemDo.setUnitPrice(unitPrice);
                inItemDo.setAmount(totailAmout);
            }
            stockIntemService.batchUpdate(inItemDos);
            StockInDO stockInDO=new StockInDO();
            stockInDO.setSign(1);
            stockInDO.setId(id);
            stockInService.update(stockInDO);
        }
            return null;
    }




}
