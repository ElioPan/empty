package com.ev.scm.service;

import com.ev.scm.domain.SalesbillItemDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-30 13:24:10
 */
public interface SalesbillItemService {
	
	SalesbillItemDO get(Long bid);
	
	List<SalesbillItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalesbillItemDO salesbillItem);
	
	int update(SalesbillItemDO salesbillItem);
	
	int remove(Long bid);
	
	int batchRemove(Long[] bids);
}
