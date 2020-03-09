package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
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
    public int getAnalysisDate(Map<String, Object> map) {
        return inStockAccountingDao.getAnalysisDate(map);
    }

    @Override
    public R saveAccounting(String detailAccounting) {

        List<StockInItemDO> stockInItemDOs = JSONObject.parseArray(detailAccounting, StockInItemDO.class);
        Long[] itemIds=new  Long[stockInItemDOs.size()];
        for (int i=0;i<stockInItemDOs.size();i++){
            itemIds[i]=stockInItemDOs.get(i).getId();
        }
        boolean b = this.disposeIsClose(itemIds,true);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

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

        boolean b = this.disposeIsClose(stockInIds,false);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

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
            return R.error(messageSourceHandler.getMessage("scm.ocation.contractExpenceIsNull", null));

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

                    BigDecimal meanValue;
                    if(sign){
                        BigDecimal totailCount= new BigDecimal(String.valueOf(totailCountAmount.get("totailCount")));
                        meanValue=totailExprnseAmountBig.divide(totailCount, Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                    }else{
                        BigDecimal totailAmount= new BigDecimal(String.valueOf(totailCountAmount.get("totailAmount")));
                        meanValue=totailExprnseAmountBig.divide(totailAmount,Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                    }
                    map.clear();
                    List<StockInItemDO> listItemDo=new ArrayList<>();
                    for (StockInItemDO stockInItemDO:listSotockInItem){

                        if(sign){
                            stockInItemDO.setExpense(meanValue.multiply(stockInItemDO.getCount()));
                        }else{
                            stockInItemDO.setExpense(meanValue.multiply(stockInItemDO.getAmount()));
                        }
                        stockInItemDO.setId(stockInItemDO.getId());
                        listItemDo.add(stockInItemDO);
                    }
                    stockIntemService.batchUpdate(listItemDo);
                }
            }
    }

    private void restoreCountAmount(Long stockInId,List<StockInItemDO> listSotockInItem){
        StockInDO stockInDo = stockInService.get(stockInId);
        if(Objects.equals(1,stockInDo.getSign())){

            //还原金额和单件     批量还原金额单价
            List<StockInItemDO> oldStockInItemDos=new ArrayList<>();
            for(StockInItemDO stockInItemDo:listSotockInItem){

                BigDecimal expense=stockInItemDo.getExpense()==null?BigDecimal.ZERO:stockInItemDo.getExpense();

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

        boolean b = this.disposeIsClose(stockInIds,false);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

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
                    BigDecimal expense=stockInItemDo.getExpense()==null?BigDecimal.ZERO:stockInItemDo.getExpense();

                    if(!Objects.equals(0,expense.compareTo(BigDecimal.ZERO) )){

                        BigDecimal newAmount=stockInItemDo.getAmount().add(expense);

                        stockInItemDo.setAmount(newAmount);
                        stockInItemDo.setUnitPrice(newAmount.divide(stockInItemDo.getCount(),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP));
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
    public R disposeAffirmAndBack(Long stockInItemId, String detailAccounting) {

        Long[] ids={stockInItemId};
        boolean b = this.disposeIsClose(ids,true);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

        StockInItemDO itemDO=stockIntemService.get(stockInItemId);
        if(itemDO!=null){
            if(!Objects.equals(itemDO.getAccountSource(),"0")){
                return R.error(messageSourceHandler.getMessage("scm.accounting.inStockIsAccountingOver", null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoId", null));
        }

        List<Map<String,Object>> list = JSONArray.parseObject(detailAccounting, List.class);

        List<Map<String,Object>> accoutSources=new ArrayList<>();

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

            Map<String,Object>  accoutSource= new HashMap<>();
            accoutSource.put("outItemID",id);
            accoutSource.put("countOnce",thisTimeCount);
            accoutSources.add(accoutSource);

            StockOutItemDO stockOutItemDO=new StockOutItemDO();
            stockOutItemDO.setId(id);
            stockOutItemDO.setChargeOffCount(chargeOffCount.add(thisTimeCount));
            outItemDOS.add(stockOutItemDO);
        }

        stockOutItemService.batchUpdate(outItemDOS);
        //已核销
        StockInDO stockInDO = stockInService.get(itemDO.getInheadId());
        stockInDO.setSign(1);
        stockInService.update(stockInDO);

        StockInItemDO stockInItemDo = new StockInItemDO();
        stockInItemDo.setId(stockInItemId);
        stockInItemDo.setCost(cost);
        stockInItemDo.setAccountSource(accoutSources.toString());
        stockIntemService.update(stockInItemDo);

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

    @Override
    public R disposeRollbackAccccounting(Long[] stockInIds){

        boolean b = this.disposeIsClose(stockInIds,false);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

        //取出所有明细
        Map<String,Object>  map= new HashMap<>();
        map.put("id",stockInIds);
        List<StockInItemDO> itemDetailById = stockIntemService.getItemDetailById(map);
        List<StockOutItemDO> LisTStockOutItemDo=new ArrayList<>();
        List<StockInItemDO> newLitemDetailById=new ArrayList<>();

        for (StockInItemDO stockInItemDO : itemDetailById) {

            StockInDO stockInDO = stockInService.get(stockInItemDO.getInheadId());
            if (Objects.equals(0,stockInDO.getSign())) {
                //nulll 未核销，未核算状态
                continue;
            }
            if(Objects.equals(2,stockInDO.getSign())&& Objects.equals("0",stockInItemDO.getAccountSource())){
                //分配未核销
                continue;
            }

            if (stockInDO.getSign() == 3) {
                //已核算  将金额和单价还原，并将成本和费用更新为0.
               BigDecimal totailAmout = stockInItemDO.getAmount().subtract(stockInItemDO.getCost().add(stockInItemDO.getExpense()));
               BigDecimal unitPrice = totailAmout.divide(stockInItemDO.getCount(),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                stockInItemDO.setAmount(totailAmout);
                stockInItemDO.setUnitPrice(unitPrice);
                stockInItemDO.setExpense(BigDecimal.ZERO);
            }

             //[{count=240.00000000, materialId=45}]
            if(stockInItemDO.getAccountSource()!=null&&!Objects.equals("0", stockInItemDO.getAccountSource())){
                String str="=";
                String accountSource=stockInItemDO.getAccountSource();
                accountSource=accountSource.replaceAll(str,":");
    //          List<Map<String, Object>> objectss = JSONArray.parseObject(stockInItemDO.getAccountSource().toString(), List.class);
                List<Object> objects = JSON.parseArray(accountSource);

                for (int i = 0; i < objects.size(); i++) {
                    Map<String, Object> mapOutItemId = (Map<String, Object>)objects.get(i);
                    Long outItemId = Long.parseLong(mapOutItemId.get("outItemID").toString());
                    BigDecimal standCount = new BigDecimal(mapOutItemId.get("countOnce").toString());
                    StockOutItemDO outItemDo = stockOutItemService.get(outItemId);
                    outItemDo.setChargeOffCount(outItemDo.getChargeOffCount().subtract(standCount));
                    LisTStockOutItemDo.add(outItemDo);
                }
            }else{
                continue;
            }
            stockInItemDO.setCost(BigDecimal.ZERO);
            stockInItemDO.setAccountSource("0");
            stockInItemDO.setMaterialIdCount("0");
            newLitemDetailById.add(stockInItemDO);

            stockInDO.setSign(0);
            stockInService.update(stockInDO);
        }
        //批量更新 入库子表
        if(newLitemDetailById.size()>0){
            stockIntemService.batchUpdate(newLitemDetailById);
        }
        if(LisTStockOutItemDo.size()>0){
            stockOutItemService.batchUpdate(LisTStockOutItemDo);
        }

        return R.ok();
    }

    @Override
    public R disposeallocationOutIn(Long[] stockInIds){

        boolean b = this.disposeIsClose(stockInIds,false);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

        for(Long stockInId:stockInIds){
            Map<String,Object>  map= new HashMap<>();
            map.put("inheadId",stockInId);
            List<StockInItemDO> inItemDos = stockIntemService.list(map);
            if(inItemDos==null){
                continue;
            }
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
            stockInDO.setSign(2);
            stockInDO.setId(stockInId);
            stockInService.update(stockInDO);
        }
        return R.ok();
    }

    @Override
    public R disposeaccountingPrice(Long[] stockInIds) {

        boolean b = this.disposeIsClose(stockInIds,false);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }

        Map<String,Object>  map= new HashMap<>();
        map.put("id",stockInIds);
        if(this.getCountOfSignIsO(map)>0){
            //未经分配的 sign==0，初始0
            return  R.error(messageSourceHandler.getMessage("scm.accounting.bussenissAccounting", null));
        }

        map.clear();
        for(Long id:stockInIds) {

            map.put("inheadId",id);
            List<StockInItemDO> inItemDos = stockIntemService.list(map);

            for(StockInItemDO inItemDo:inItemDos){

                BigDecimal cost=inItemDo.getCost()==null?BigDecimal.ZERO:inItemDo.getCost();
                BigDecimal expense=inItemDo.getExpense()==null?BigDecimal.ZERO:inItemDo.getExpense();

                BigDecimal totailAmout = inItemDo.getAmount().add(cost.add(expense));
                BigDecimal unitPrice = totailAmout.divide(inItemDo.getCount(),Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                inItemDo.setUnitPrice(unitPrice);
                inItemDo.setAmount(totailAmout);
            }
            stockIntemService.batchUpdate(inItemDos);
            StockInDO stockInDO=new StockInDO();
            stockInDO.setSign(3);
            stockInDO.setId(id);
            stockInService.update(stockInDO);
        }
            return R.ok();
    }


    @Override
    public R disposeAutoAccounting(Long stockInItemId,String detailAccounting) {

        Long[] ids={stockInItemId};
        boolean b = this.disposeIsClose(ids,true);
        if (!b){
            return R.error(messageSourceHandler.getMessage("scm.stock.haveCarryOver", null));
        }
        JSONArray stockOutItemDos=new JSONArray();
        if(StringUtils.isNotEmpty(detailAccounting)){
            stockOutItemDos = JSON.parseArray(detailAccounting);
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoMassge", null));
        }

            StockInItemDO stockInItemDo = stockIntemService.get(stockInItemId);
        StockInDO stockInDO = stockInService.get(stockInItemDo.getInheadId());
        //sign初始化为"0
            if (Objects.equals(stockInDO.getSign(), 1) ) {
                return   R.error(messageSourceHandler.getMessage("scm.accounting.inStockIsAccountingOver", null));
            }else if(Objects.equals(stockInDO.getSign(), 3)){
                return   R.error(messageSourceHandler.getMessage("scm.checkCount.accotingOver", null));
            }else if(Objects.equals(stockInDO.getSign(), 2)&&!Objects.equals(stockInItemDo.getAccountSource(),"0")){
                return   R.error(messageSourceHandler.getMessage("scm.accounting.inStockIsAccountingOver", null));
            }

           //入库成品数量
            BigDecimal count = stockInItemDo.getCount();
            //委外合同子表id
            Long contrackItemId = stockInItemDo.getSourceId();

            List<Map<String, Object>> bomItems = new ArrayList<>();
            List<Map<String, Object>> bomItem = this.getBomItem(contrackItemId);
            if (bomItem.isEmpty()) {
                R.error(messageSourceHandler.getMessage("scm.accounting.haveNoBom", null));
            } else {
                for (int i = 0; i < bomItem.size(); i++) {
                    //本次需要核销的数量
                    Map<String, Object> map = bomItem.get(i);
                    BigDecimal standardCount = new BigDecimal(map.get("standardCount").toString());
                    BigDecimal needAccountCount= standardCount.multiply(count);
                    map.put("standardCount",needAccountCount);
                    bomItems.add(map);
                }
            }
            List<Map<String, Object>> results = new ArrayList<>();
            List<Map<String, Object>> leaveOverMaterials = new ArrayList<>();
            for (int k = 0; k < bomItems.size(); k++) {
                //需要某一组件物料需要核销的数量
                BigDecimal standardCount = new BigDecimal(bomItems.get(k).get("standardCount").toString());
                //需要核销的物料
                Integer materielId = Integer.parseInt(String.valueOf(bomItems.get(k).get("materielId").toString()));

                for (int i = 0; i < stockOutItemDos.size(); i++) {

//                StockOutItemDO stockOutItemDO=stockOutItemDos.get(i);
                    Map<String, Object> stockOutItemDO = (Map<String, Object>) stockOutItemDos.get(i);

                    if (Objects.equals(0, standardCount.compareTo(BigDecimal.ZERO))) {
                        //需要核销的某物料已被核销完毕，跳出循环，进入下个组建物料的分配
                        break;
                    }
                    BigDecimal countOnce = BigDecimal.ZERO;

                    // id  materielId count chargeOffCount
                    if (Objects.equals(materielId, stockOutItemDO.get("materielId"))) {
                        //已核销数量
                        BigDecimal chargeOffCount = (new BigDecimal(stockOutItemDO.get("chargeOffCount").toString())).compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : (new BigDecimal(stockOutItemDO.get("chargeOffCount").toString()));
                        //出库单中可以再核销的数量
                        BigDecimal canChargeOffCount = (new BigDecimal(stockOutItemDO.get("count").toString())).subtract(chargeOffCount);

                        if (standardCount.compareTo(canChargeOffCount) == 1) {
                            countOnce = canChargeOffCount;
                            standardCount = standardCount.subtract(canChargeOffCount);
                        } else if (standardCount.compareTo(canChargeOffCount) == 0) {
                            countOnce = canChargeOffCount;
                            standardCount = standardCount.subtract(standardCount);
                        } else if (standardCount.compareTo(canChargeOffCount) == -1) {
                            countOnce = standardCount;
                            standardCount = standardCount.subtract(standardCount);
                        }
                    }
                    if (!Objects.equals(BigDecimal.ZERO, countOnce)) {
                        stockOutItemDO.put("thisTimeCount", countOnce);
                        results.add(stockOutItemDO);
                    }
                }
                //记录否存在未分配完的物料
                if (!Objects.equals(BigDecimal.ZERO, standardCount)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("materialId", materielId);
                    map.put("count", standardCount);
                    leaveOverMaterials.add(map);
                }
            }
            //处理遗留未分配的组件物料------后期作为bug排查吧
            if (Objects.nonNull(leaveOverMaterials)) {
                StockInItemDO stockInItemDO = new StockInItemDO();
                stockInItemDO.setMaterialIdCount(leaveOverMaterials.toString());
                stockInItemDO.setId(stockInItemId);
                stockIntemService.update(stockInItemDO);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("data", results);
            return R.ok(result);
        }


    /**
     *关账验证
     *
     */
    @Override
    public boolean disposeIsClose(Long[] itemOrHaedIds,Boolean sign) {
        //  表id去重
        List nowList = new ArrayList();
        for (int i = 0; i < itemOrHaedIds.length; i++) {
            nowList.add(itemOrHaedIds[i]);
        }
        nowList = new ArrayList(new HashSet(nowList));
        Long[] stockItemOrHaedIds = new Long[nowList.size()];
        for (int i = 0; i < nowList.size(); i++) {
            stockItemOrHaedIds[i] = Long.valueOf(String.valueOf(nowList.get(i)));
        }
        Map<String, Object> map = new HashMap<>();

        if(sign){
            map.put("inItemId", stockItemOrHaedIds);
        }else{
            map.put("stockInId", stockItemOrHaedIds);
        }
        List<Map<String, Object>> inOutTimeMap= stockIntemService.getInOutTime(map);

        if (inOutTimeMap.isEmpty()) {
            return false;
        } else {
            for (Map<String, Object> maps : inOutTimeMap) {
                Map<String, Object> peramy = new HashMap<>();
                peramy.put("period", maps.get("inOutTime"));
                int counts = this.getAnalysisDate(peramy);
                if(counts==0){
                    return false;
                }
            }
            return true;
        }
    }






}
