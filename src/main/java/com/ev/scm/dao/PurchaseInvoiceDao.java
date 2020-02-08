package com.ev.scm.dao;

import com.ev.scm.domain.PurchaseInvoiceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购发票主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-16 10:13:42
 */
@Mapper
public interface PurchaseInvoiceDao {

	PurchaseInvoiceDO get(Long id);
	
	List<PurchaseInvoiceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseInvoiceDO purchaseInvoice);
	
	int update(PurchaseInvoiceDO purchaseInvoice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	Map<String, Object> getDetailOfHead(Map<String, Object> map);


}
