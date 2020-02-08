package com.ev.custom.service;

import com.ev.custom.domain.ReceiptItemDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:37
 */
public interface ReceiptItemService {
	
	ReceiptItemDO get(Long rid);
	
	List<ReceiptItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ReceiptItemDO receiptItem);
	
	int update(ReceiptItemDO receiptItem);
	
	int remove(Long rid);
	
	int batchRemove(Long[] rids);
}
