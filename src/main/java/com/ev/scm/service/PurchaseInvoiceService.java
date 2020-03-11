package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchaseInvoiceDO;

import java.util.List;
import java.util.Map;

/**
 * 采购发票主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-16 10:13:42
 */
public interface PurchaseInvoiceService {
	
	PurchaseInvoiceDO get(Long id);
	
	List<PurchaseInvoiceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseInvoiceDO purchaseInvoice);
	
	int update(PurchaseInvoiceDO purchaseInvoice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R addAndChange(PurchaseInvoiceDO purchaseInvoiceDO, String bodyItem, Long[]itemIds);

	R audit(Long id);

	R rollBackAudit(Long id);

	R removePurchase(Long[] id);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	R getDetail(Long id );

	String checkSourceCounts(String bodyDetail);

	R checkSourceCount(String bodyDetail,Long id );

}
