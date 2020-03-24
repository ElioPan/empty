package com.ev.scm.service;

import com.ev.scm.domain.PaymentReceivedItemDO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收款/付款明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:18:17
 */
public interface PaymentReceivedItemService {
	
	PaymentReceivedItemDO get(Long id);
	
	List<PaymentReceivedItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentReceivedItemDO paymentReceivedItem);
	
	int update(PaymentReceivedItemDO paymentReceivedItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByReceivedId(Long[] ids);

	List<Map<String, Object>> detailOfitem(Map<String, Object> map);

	Map<String, Object> totallAmount(Map<String, Object> map);

	Boolean whetherTheReference(String sign,Long id,Long[] payItemId);

	BigDecimal getInCountOfPayment(Map<String, Object> map);

	List<PaymentReceivedItemDO> getInOfPaymentAmount(Map<String, Object> map);

	List<Map<String, Object>> getInCountById(Map<String, Object> map);

	List<Map<String, Object>> getsPaymentDetails(Map<String, Object> map);



}
