package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchaseDO;

import java.util.List;
import java.util.Map;

/**
 * 采购申请表主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 13:11:44
 */
public interface PurchaseService {
	
	PurchaseDO get(Long id);
	
	List<PurchaseDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseDO purchase);
	
	int update(PurchaseDO purchase);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R addPurchase( PurchaseDO purchaseDO, String body,Long[] itemId);

	R audit(Long id);

	R rollBackAudit(Long id);

	R removePurchase(Long[] purchaseId);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	R getdetail(Long id);

	String checkSourceCounts(String itemDos, Long storageType);
}
