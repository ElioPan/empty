package com.ev.scm.service;

import com.ev.scm.domain.PaymentReceivedDO;

import java.util.List;
import java.util.Map;

/**
 * 收款付款单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:18:04
 */
public interface PaymentReceivedService {
	
	PaymentReceivedDO get(Long id);
	
	List<PaymentReceivedDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentReceivedDO paymentReceived);
	
	int update(PaymentReceivedDO paymentReceived);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
