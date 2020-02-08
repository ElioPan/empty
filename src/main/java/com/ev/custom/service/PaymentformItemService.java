package com.ev.custom.service;

import com.ev.custom.domain.PaymentformItemDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-06 13:23:32
 */
public interface PaymentformItemService {
	
	PaymentformItemDO get(Long pid);
	
	List<PaymentformItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentformItemDO paymentformItem);
	
	int update(PaymentformItemDO paymentformItem);
	
	int remove(Long pid);
	
	int batchRemove(Long[] pids);
}
