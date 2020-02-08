package com.ev.custom.service;

import com.ev.custom.domain.PurchasebillItemDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-17 13:06:48
 */
public interface PurchasebillItemService {
	
	PurchasebillItemDO get(Long bid);
	
	List<PurchasebillItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchasebillItemDO purchasebillItem);
	
	int update(PurchasebillItemDO purchasebillItem);
	
	int remove(Long bid);
	
	int batchRemove(Long[] bids);
}
