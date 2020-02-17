package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.PaymentReceivedDO;
import com.ev.scm.domain.SalescontractPayDO;

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

	R addReceived(PaymentReceivedDO paymentReceivedDO, String paymentBodys, Long[] deleItemIds,String sign);

	R audit(Long id,String sign);

	R rollBackAudit(Long id,String sign);

	int updateAuditSign(PaymentReceivedDO paymentReceived);

	R removeReceived(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);

	R getdetail(Long id);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	Map<String, Object> detailOfReceived(Map<String, Object> map);

	List<SalescontractPayDO> detailOfSalePayById(Map<String, Object> map);




}
