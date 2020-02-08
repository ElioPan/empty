package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.PurchaseInvoiceDao;
import com.ev.scm.domain.PurchaseInvoiceDO;
import com.ev.scm.domain.PurchaseInvoiceItemDO;
import com.ev.scm.service.PurchaseInvoiceItemService;
import com.ev.scm.service.PurchaseInvoiceService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private PurchaseInvoiceDao purchaseInvoiceDao;
	@Autowired
	private PurchaseInvoiceItemService purchaseInvoiceItemService;

	@Override
	public PurchaseInvoiceDO get(Long id){
		return purchaseInvoiceDao.get(id);
	}
	
	@Override
	public List<PurchaseInvoiceDO> list(Map<String, Object> map){
		return purchaseInvoiceDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchaseInvoiceDao.count(map);
	}
	
	@Override
	public int save(PurchaseInvoiceDO purchaseInvoice){
		return purchaseInvoiceDao.save(purchaseInvoice);
	}
	
	@Override
	public int update(PurchaseInvoiceDO purchaseInvoice){
		return purchaseInvoiceDao.update(purchaseInvoice);
	}
	
	@Override
	public int remove(Long id){
		return purchaseInvoiceDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchaseInvoiceDao.batchRemove(ids);
	}

	@Override
	public R addAndChange(PurchaseInvoiceDO purchaseInvoiceDO, String bodyItem, Long[]itemIds){

		if (Objects.isNull(purchaseInvoiceDO.getId())) {
			String prefix = DateFormatUtil.getWorkOrderno(ConstantForGYL.INVOICE, new Date());
			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
			params.put("maxNo", prefix);
			params.put("offset", 0);
			params.put("limit", 1);
			List<PurchaseInvoiceDO> list = purchaseInvoiceDao.list(params);
			String suffix = null;
			if (list.size() > 0) {
				suffix = list.get(0).getBillCode();
			}
			purchaseInvoiceDO.setBillCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
			purchaseInvoiceDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			int row = purchaseInvoiceDao.save(purchaseInvoiceDO);

			if (row > 0) {
				List<PurchaseInvoiceItemDO> bodys = JSON.parseArray(bodyItem, PurchaseInvoiceItemDO.class);
				for (PurchaseInvoiceItemDO pdata : bodys) {
					pdata.setPurchasebillId(purchaseInvoiceDO.getId());
					purchaseInvoiceItemService.save(pdata);
				}
				return R.ok();
			} else {
				return R.error();
			}
		} else {

			int rows = purchaseInvoiceDao.update(purchaseInvoiceDO);
			if (Objects.nonNull(itemIds)&&itemIds.length > 0){
				purchaseInvoiceItemService.batchRemove(itemIds);
			}
			if (rows > 0) {
				List<PurchaseInvoiceItemDO> bodys = JSON.parseArray(bodyItem, PurchaseInvoiceItemDO.class);
				for (PurchaseInvoiceItemDO pdata : bodys) {
					if (Objects.nonNull(pdata.getId())) {
						purchaseInvoiceItemService.update(pdata);
					} else {
						pdata.setPurchasebillId(purchaseInvoiceDO.getId());
						purchaseInvoiceItemService.save(pdata);
					}
				}
				return R.ok();
			} else {
				return R.error();
			}
		}
	}

	@Override
	public R audit(Long id) {
		PurchaseInvoiceDO purchaseInvoiceDO = purchaseInvoiceDao.get(id);
		if(Objects.nonNull(purchaseInvoiceDO)){
			if(Objects.equals(purchaseInvoiceDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)){
				PurchaseInvoiceDO pDo=new PurchaseInvoiceDO();
				pDo.setAuditSign(ConstantForGYL.OK_AUDITED);
				pDo.setAuditor(ShiroUtils.getUserId());
				pDo.setAuditTime(new Date());
				pDo.setId(id);
				purchaseInvoiceDao.update(pDo);
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
		PurchaseInvoiceDO purchaseInvoiceDO = purchaseInvoiceDao.get(id);
		if(Objects.nonNull(purchaseInvoiceDO)){
			if(Objects.equals(purchaseInvoiceDO.getAuditSign(),ConstantForGYL.OK_AUDITED)){
				PurchaseInvoiceDO pDo=new PurchaseInvoiceDO();
				pDo.setAuditSign(ConstantForGYL.WAIT_AUDIT);
				pDo.setAuditor(0L);
				pDo.setId(id);
				purchaseInvoiceDao.update(pDo);
				return R.ok();
			}else{
				return R.error(messageSourceHandler.getMessage("common.massge.okWaitAudit",null));
			}
		}else{
			return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
		}
	}

	@Override
	public R removePurchase(Long[] ids) {

		Map<String,Object>  map= new HashMap<String,Object>();
		map.put("id",ids);
		int rows = purchaseInvoiceDao.canDeletOfCount(map);
		if(Objects.equals(rows,ids.length)) {
			purchaseInvoiceDao.batchRemove(ids);
			purchaseInvoiceItemService.batchRemoveByPurcahseId(ids);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return purchaseInvoiceDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return purchaseInvoiceDao.countForMap(map);
	}

	@Override
	public R getDetail(Long id ){
		Map<String,Object>  map= new HashMap<String,Object>();
		map.put("id",id);
		Map<String, Object> detailOfHead = purchaseInvoiceDao.getDetailOfHead(map);

		List<Map<String, Object>> detailOfBody = purchaseInvoiceItemService.getDetailOfBody(map);
		Map<String, Object> totalOfItem = purchaseInvoiceItemService.totalOfItem(map);

		map.clear();
		Map<String,Object>  result= new HashMap<String,Object>();
		if(Objects.nonNull(detailOfHead)){
			map.put("detailOfHead",detailOfHead);
			map.put("detailOfBody",detailOfBody);
			map.put("totalOfItem",totalOfItem);

			result.put("date",map);
		}
		return R.ok(map);
	}


}
