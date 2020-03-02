package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.ev.scm.service.PurchaseItemService;
import com.ev.scm.service.PurchaseService;
import com.ev.scm.service.SalescontractItemService;
import com.ev.scm.service.SalescontractService;
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
    public R addPurchase(PurchaseDO purchaseDO, String body,Long[] itemIds) {
        if (Objects.isNull(purchaseDO.getId())) {

            String prefix = DateFormatUtil.getWorkOrderno(ConstantForGYL.PURCHAER, new Date());
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("maxNo", prefix);
            params.put("offset", 0);
            params.put("limit", 1);
            List<PurchaseDO> list = purchaseDao.list(params);
            String suffix = null;
            if (list.size() > 0) {
                suffix = list.get(0).getPurchaseCode();
            }
            purchaseDO.setPurchaseCode(DateFormatUtil.getWorkOrderno(prefix, suffix,4));
            purchaseDO.setPurchaseType(ConstantForGYL.PURCHASE);
            purchaseDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
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
            if(Objects.equals(purchaseDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)){
                PurchaseDO pDo=new PurchaseDO();
                pDo.setAuditSign(ConstantForGYL.OK_AUDITED);
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

        //此处需要验证是否被采购合同引用了（目前采购必走合同流程）
        //TODO
        if(Objects.nonNull(purchaseDO)){
            if(Objects.equals(purchaseDO.getAuditSign(),ConstantForGYL.OK_AUDITED)){
                PurchaseDO pDo=new PurchaseDO();
                pDo.setAuditSign(ConstantForGYL.WAIT_AUDIT);
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
    public int countForMap(Map<String, Object> map) {
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
    public String checkSourceCounts(String purchaseItemDos, Long storageType) {
        List<PurchaseItemDO> itemDos = new ArrayList<>();
        if (StringUtils.isNotEmpty(purchaseItemDos)) {
            itemDos = JSON.parseArray(purchaseItemDos, PurchaseItemDO.class);
        } else {
            return messageSourceHandler.getMessage("common.massge.dateIsNon", null);
        }
        //验证 销售合同 生产投料单
        for (PurchaseItemDO itemDo : itemDos) {
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
                            map.put("sourceType", storageType);
                            //查出采购申请中已关联引入的数量
                            BigDecimal inCounts = purchaseItemService.getInCountOfPurchase(map);

                            BigDecimal inCountOfpurchase = (inCounts == null) ? BigDecimal.ZERO : inCounts;
                            int boo = (salescontractItemDO.getCount().subtract(inCountOfpurchase)).compareTo(thisCount);
                            if (Objects.equals(-1, boo)) {
                                String[] args = {thisCount.toPlainString(), salescontractItemDO.getCount().subtract(inCountOfpurchase).toPlainString(), itemDo.getSourceType().toString()};
                                messageSourceHandler.getMessage("stock.number.checkError", args);
                            }
                        } else {
                            return messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null);
                        }
                    } else if (Objects.equals(soueseType, ConstantForGYL.SCTLD)) {
                        //生产投料单
                        ProductionFeedingDetailDO productionFeedingDetailDO = productionFeedingDetailService.get(sourceId);
                        if (productionFeedingDetailDO != null) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("sourceId", sourceId);
                            map.put("storageType", storageType);


                        } else {
                            return messageSourceHandler.getMessage("scm.stock.haveNoMagOfSource", null);
                        }
                    }

                } else {
                    //返回 验证  子表信息不全，
                }

            }

        }
        return "";
    }


}