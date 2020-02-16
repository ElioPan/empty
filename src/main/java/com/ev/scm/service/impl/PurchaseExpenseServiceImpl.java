package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.PurchaseExpenseDao;
import com.ev.scm.domain.PurchaseExpenseDO;
import com.ev.scm.domain.PurchaseExpenseItemDO;
import com.ev.scm.service.PurchaseExpenseItemService;
import com.ev.scm.service.PurchaseExpenseService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PurchaseExpenseServiceImpl implements PurchaseExpenseService {
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	@Autowired
	private PurchaseExpenseDao purchaseExpenseDao;

	@Autowired
	private PurchaseExpenseItemService purchaseExpenseItemService;
	
	@Override
	public PurchaseExpenseDO get(Long id){
		return purchaseExpenseDao.get(id);
	}
	
	@Override
	public List<PurchaseExpenseDO> list(Map<String, Object> map){
		return purchaseExpenseDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return purchaseExpenseDao.count(map);
	}
	
	@Override
	public int save(PurchaseExpenseDO purchaseExpense){
		return purchaseExpenseDao.save(purchaseExpense);
	}
	
	@Override
	public int update(PurchaseExpenseDO purchaseExpense){
		return purchaseExpenseDao.update(purchaseExpense);
	}
	
	@Override
	public int remove(Long id){
		return purchaseExpenseDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return purchaseExpenseDao.batchRemove(ids);
	}

	@Override
	public R addPurchase(PurchaseExpenseDO purchaseExpenseDO, String body, Long[] itemIds) {

		if (Objects.isNull(purchaseExpenseDO.getId())) {
			String prefix = DateFormatUtil.getWorkOrderno(ConstantForGYL.EXPENCE, new Date());
			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
			params.put("maxNo", prefix);
			params.put("offset", 0);
			params.put("limit", 1);
			List<PurchaseExpenseDO> list = purchaseExpenseDao.list(params);
			String suffix = null;
			if (list.size() > 0) {
				suffix = list.get(0).getExpenseCode();
			}
			purchaseExpenseDO.setExpenseCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
			purchaseExpenseDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
			int row = purchaseExpenseDao.save(purchaseExpenseDO);

			if (row > 0) {
				List<PurchaseExpenseItemDO> bodys = JSON.parseArray(body, PurchaseExpenseItemDO.class);

				for (PurchaseExpenseItemDO pdata : bodys) {
					pdata.setPurchaseExpenseId(purchaseExpenseDO.getId());
					purchaseExpenseItemService.save(pdata);
				}
				return R.ok();
			} else {
				return R.error();
			}
		} else {

			int rows = purchaseExpenseDao.update(purchaseExpenseDO);
			if (Objects.nonNull(itemIds)&&itemIds.length > 0){
				purchaseExpenseItemService.batchRemove(itemIds);
			}
			if (rows > 0) {
				List<PurchaseExpenseItemDO> bodys = JSON.parseArray(body, PurchaseExpenseItemDO.class);
				for (PurchaseExpenseItemDO pdata : bodys) {
					if (Objects.nonNull(pdata.getId())) {
						purchaseExpenseItemService.update(pdata);
					} else {
						pdata.setExpenseId(purchaseExpenseDO.getId());
						purchaseExpenseItemService.save(pdata);
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
		PurchaseExpenseDO purchaseExpenseDO = purchaseExpenseDao.get(id);
		if(Objects.nonNull(purchaseExpenseDO)){
			if(Objects.equals(purchaseExpenseDO.getAuditSign(),ConstantForGYL.WAIT_AUDIT)){
				PurchaseExpenseDO pDo=new PurchaseExpenseDO();
				pDo.setAuditSign(ConstantForGYL.OK_AUDITED);
				pDo.setAuditor(ShiroUtils.getUserId());
				pDo.setAuditTime(new Date());
				pDo.setId(id);
				purchaseExpenseDao.update(pDo);
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
		PurchaseExpenseDO purchaseExpenseDO= purchaseExpenseDao.get(id);

		//此处需要验证是否被采购合同引用了（目前采购必走合同流程）
		//TODO
		if(Objects.nonNull(purchaseExpenseDO)){
			if(Objects.equals(purchaseExpenseDO.getAuditSign(),ConstantForGYL.OK_AUDITED)){
				PurchaseExpenseDO pDo=new PurchaseExpenseDO();
				pDo.setAuditSign(ConstantForGYL.WAIT_AUDIT);
				pDo.setAuditor(0L);
				pDo.setId(id);
				purchaseExpenseDao.update(pDo);
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
		int rows = purchaseExpenseDao.canDeletOfCount(map);
		if(Objects.equals(rows,ids.length)) {
			purchaseExpenseDao.batchRemove(ids);
			purchaseExpenseItemService.batchRemoveByPurcahseId(ids);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk",null));
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return purchaseExpenseDao.listForMap(map);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> map) {
		return purchaseExpenseDao.countForMap(map);
	}

	@Override
	public R getDetail(Long id ){
		Map<String,Object>  map= new HashMap<>();
		map.put("id",id);
		Map<String, Object> detailOfHead = purchaseExpenseDao.getDetailOfHead(map);
		List<Map<String, Object>> detailOfBody = purchaseExpenseItemService.getDetailOfBody(map);
		map.clear();
		Map<String,Object>  result= new HashMap<>();
		if(Objects.nonNull(detailOfHead)){
			map.put("detailOfHead",detailOfHead);
			map.put("detailOfBody",detailOfBody);
			result.put("date",map);
		}
		return R.ok(map);
	}


}
