package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.PaymentformDO;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-06 13:23:31
 */
public interface PaymentformService {
	
	PaymentformDO get(Long id);
	
	List<PaymentformDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PaymentformDO paymentform);
	
	int update(PaymentformDO paymentform);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	R addPaymentform(PaymentformDO paymentform,String bodyItem);
	
	R removePaymentform(Long id);
	
	R updatePaymentform(PaymentformDO paymentform,String bodyItem,Long[] deleteId);
	
	R audit(Long id);
	
	R reverseAudit(Long id);
	
	R listApi(Map<String,Object> map);
	
	R getdetail(Long id);
}
