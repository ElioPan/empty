package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.ProductionFeedingDetailDO;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.scm.dao.PurchaseDao;
import com.ev.scm.domain.*;
import com.ev.scm.service.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private PurchaseDao purchaseDao;
    @Autowired
    private PurchaseItemService purchaseItemService;
    @Autowired
    private SalescontractItemService salescontractItemService;
    @Autowired
    public ProductionFeedingDetailService productionFeedingDetailService;
    @Autowired
    public PurchasecontractItemService purchasecontractItemService;


    @Override
    public PurchaseDO get(Long id) {
        return purchaseDao.get(id);
    }
    @Override
    public List<PurchaseDO> list(Map<String, Object> map) {
        return purchaseDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return purchaseDao.count(map);
    }

    @Override
    public int save(PurchaseDO purchase) {
        return purchaseDao.save(purchase);
    }

    @Override
    public int update(PurchaseDO purchase) {
        return purchaseDao.update(purchase);
    }

    @Override
    public int remove(Long id) {
        return purchaseDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return purchaseDao.batchRemove(ids);
    }

    @Override
    public Map<String, Object> vailableQuantity(Map<String, Object> map) {
        return purchaseDao.vailableQuantity(map);
    }

    @Override
    public R addPurchase(PurchaseDO purchaseDO, String body,Long[] itemIds) {
        if (Objects.isNull(purchaseDO.getId())) {

            String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.PURCHAER);
            Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
            param.put("maxNo", maxNo);
            param.put("offset", 0);
            param.put("limit", 1);
            List<PurchaseDO> list = purchaseDao.list(param);
            String taskNo = null;
            if (!list.isEmpty()) {
                taskNo = list.get(0).getPurchaseCode();
            }
            purchaseDO.setPurchaseCode(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
            purchaseDO.setPurchaseType(ConstantForGYL.PURCHASE);
            purchaseDO.setAuditSign(Constant.WAIT_AUDIT);
            int row = purchaseDao.save(purchaseDO);

            if (row > 0) {
                List<PurchaseItemDO> bodys = JSON.parseArray(body, PurchaseItemDO.class);
                for (PurchaseItemDO pdata : bodys) {
                    pdata.setPurchaseId(purchaseDO.getId());
                    purchaseItemService.save(pdata);
                }
                Map<String,Object>  result= new HashMap<>();
                result.put("id",purchaseDO.getId());
                return R.ok(result);
            } else {
                return R.error();
            }
        } else {
            int rows = purchaseDao.update(purchaseDO);
           if (Objects.nonNull(itemIds)&&itemIds.length > 0){
               purchaseItemService.batchRemove(itemIds);
           }
            if (rows > 0) {
                List<PurchaseItemDO> bodys = JSON.parseArray(body, PurchaseItemDO.class);
                for (PurchaseItemDO pdata : bodys) {
                    if (Objects.nonNull(pdata.getId())) {
                        purchaseItemService.update(pdata);
                    } else {
                        pdata.setPurchaseId(purchaseDO.getId());
                        purchaseItemService.save(pdata);
                    }
                }
                Map<String,Object>  result= new HashMap<>();
                result.put("id",purchaseDO.getId());
                return R.ok(result);
            } else {
                return R.error();
            }
        }
    }


    @Override
    public R audit(Long id) {
        PurchaseDO purchaseDO = purchaseDao.get(id);
        if(Objects.nonNull(purchaseDO)){
            if(Objects.equals(purchaseDO.getAuditSign(),Constant.WAIT_AUDIT)){
                PurchaseDO pDo=new PurchaseDO();
                pDo.setAuditSign(Constant.OK_AUDITED);
                pDo.setAuditor(ShiroUtils.getUserId());
                pDo.setAuditTime(new Date());
                pDo.setId(id);
                purchaseDao.update(pDo);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }

    }

    @Override
    public R rollBackAudit(Long id) {
        PurchaseDO purchaseDO = purchaseDao.get(id);

        //需要验证是否被 采购合同 引用了（目前采购必走合同流程）
        if(Objects.nonNull(purchaseDO)){

            Map<String,Object>  map= new HashMap<>();
            map.put("sourceCode",purchaseDO.getPurchaseCode());
            List<PurchasecontractItemDO> list = purchasecontractItemService.list(map);
            if(list.size()>0){
                return R.error(messageSourceHandler.getMessage("scm.childList.reverseAudit",null));
            }

            if(Objects.equals(purchaseDO.getAuditSign(),Constant.OK_AUDITED)){
                PurchaseDO pDo=new PurchaseDO();
                pDo.setAuditSign(Constant.WAIT_AUDIT);
                pDo.setAuditor(0L);
                pDo.setId(id);
                purchaseDao.update(pDo);
                return R.ok();
            }else{
                return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
            }
        }else{
            return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
        }
    }


    @Override
    public R removePurchase(Long[] purchaseId) {

        Map<String,Object>  map= new HashMap<>();
        map.put("id",purchaseId);
        int rows = purchaseDao.canDeletOfCount(map);
        if(Objects.equals(rows,purchaseId.length)) {
            purchaseDao.batchRemove(purchaseId);
            purchaseItemService.batchRemoveByPurcahseId(purchaseId);
            return R.ok();
        }else{
            return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
        }
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return purchaseDao.listForMap(map);
    }

    @Override
    public Map<String, Object> countForMap(Map<String, Object> map) {
        return purchaseDao.countForMap(map);
    }

    @Override
    public R getdetail(Long id) {
        Map<String,Object>  map= new HashMap<>();
        map.put("purchaseId",id);

        Map<String,Object> purchaseDetail = purchaseDao.detailOfPurchase(map);
        List<Map<String, Object>> detailOfItem = purchaseItemService.detailOfItem(map);
        Map<String, Object> aggregate = purchaseItemService.aggregate(map);
        Map<String,Object>  result= new HashMap<>();
        map.clear();
        if(Objects.nonNull(purchaseDetail)) {
            map.put("purchaseDetail",purchaseDetail);
            map.put("detailOfItem",detailOfItem);
            map.put("aggregate",aggregate); //总数量+总金额
            result.put("data",map);
        }
        return R.ok(result);
    }


    @Override
    public R checkSourceCounts(String purchaseItemDos, Long id ) {
        List<PurchaseItemDO> itemDos;
        if (StringUtils.isNotEmpty(purchaseItemDos)) {
            itemDos = JSON.parseArray(purchaseItemDos, PurchaseItemDO.class);
        } else {
            return  R.error(messageSourceHandler.getMessage("common.massge.dateIsNon", null));
        }
        //合并数量及sourseId
        Map<Long,BigDecimal>  sourseIdCounts= new HashMap<>();
        for (PurchaseItemDO itemDo : itemDos) {
            Long sourseId=itemDo.getSourceId();
            if(sourseId==null){
                continue;
            }
            if(sourseIdCounts.containsKey(sourseId)){
                sourseIdCounts.put(sourseId,sourseIdCounts.get(sourseId).add(itemDo.getCount()));
                continue;
            }
            sourseIdCounts.put(sourseId,itemDo.getCount());
        }
        List<PurchaseItemDO> purchaseItemDo=new ArrayList<>();
        for(Long sourseId:sourseIdCounts.keySet()){

            for(PurchaseItemDO itemDo : itemDos){
                if(Objects.equals(itemDo.getSourceId(),sourseId)){
                    itemDo.setCount(sourseIdCounts.get(sourseId));
                    purchaseItemDo.add(itemDo);
                    break;
                }
            }
        }
        //验证 销售合同 生产投料单
        for (PurchaseItemDO itemDo : purchaseItemDo) {
            if (Objects.nonNull(itemDo.getSourceId())) {
                Long sourceId = itemDo.getSourceId();
                Long soueseType = itemDo.getSourceType();

                BigDecimal thisCount = itemDo.getCount();
                if (Objects.nonNull(soueseType)) {
                    if (Objects.equals(soueseType, ConstantForGYL.XSHT)) {
                        //销售合同
                        SalescontractItemDO salescontractItemDO = salescontractItemService.get(sourceId);
                        if (salescontractItemDO != null) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sourceId", sourceId);
                            map.put("sourceType", soueseType);
                            if(id!=null){map.put("id", id);}

                            //查出采购申请中已关联引入的数量
                            BigDecimal inCountOfpurchase = purchaseItemService.getInCountOfExclude(map);

                            int boo = (salescontractItemDO.getCount().subtract(inCountOfpurchase)).compareTo(thisCount);
                            if (Objects.equals(-1, boo)) {
                                String[] args = {thisCount.toPlainString(), salescontractItemDO.getCount().subtract(inCountOfpurchase).toPlainString(), itemDo.getSourceCode().toString()};
                                Map<String,Object>  maps= new HashMap<>();
                                maps.put("sourceId",sourceId);
                                maps.put("sourceCount",salescontractItemDO.getCount().subtract(inCountOfpurchase));
                                return R.error(500,messageSourceHandler.getMessage("stock.number.checkError", args),maps);
                            }
                        } else {
                            return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
                        }
                    } else if (Objects.equals(soueseType, ConstantForGYL.SCTLD)) {
                        //生产投料单
                        ProductionFeedingDetailDO productionFeedingDetailDO = productionFeedingDetailService.get(sourceId);
                        if (productionFeedingDetailDO != null) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sourceId", sourceId);
                            map.put("storageType", soueseType);
                            if(id!=null){map.put("id", id);}
                            BigDecimal inCountOfpurchase = purchaseItemService.getInCountOfExclude(map);

                            int boo = (productionFeedingDetailDO.getPlanFeeding().subtract(inCountOfpurchase)).compareTo(thisCount);
                            if (Objects.equals(-1, boo)) {
                                String[] args = {thisCount.toPlainString(), productionFeedingDetailDO.getOutCount().subtract(inCountOfpurchase).toPlainString(), itemDo.getSourceType().toString()};
                                Map<String,Object>  maps= new HashMap<>();
                                maps.put("sourceId",sourceId);
                                maps.put("sourceCount",productionFeedingDetailDO.getPlanFeeding().subtract(inCountOfpurchase));
                                return R.error(500,messageSourceHandler.getMessage("stock.number.checkError", args),maps);                            }
                        } else {
                            return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
                        }
                    } else {
                        return R.error(messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null));
                    }
                }else{
                    return R.error(messageSourceHandler.getMessage("scm.purchase.haveNoMagOfSource", null));
                }
            }
        }
        return null;
    }




}




