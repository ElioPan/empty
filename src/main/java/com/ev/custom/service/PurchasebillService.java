package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.PurchasebillDO;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-17 13:06:48
 */
public interface PurchasebillService {
	
	PurchasebillDO get(Long id);
	
	List<PurchasebillDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchasebillDO purchasebill);
	
	int update(PurchasebillDO purchasebill);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	R addPurchaseBill(PurchasebillDO purchasebillDO, String bodyItem);
	
	R removePurchaseBill(Long purchaseBillId);
	
	R listApi(Map<String,Object> map);
	
	R updatePurchaseBill(PurchasebillDO purchasebillDO,String bodyItem,Long[] deleteId);
	
	R audit(Long id);
	
	R reverseAudit(Long id);
	
	R getdetail(Long id);
}
