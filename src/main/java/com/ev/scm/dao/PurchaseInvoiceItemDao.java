package com.ev.scm.dao;

import com.ev.scm.domain.PurchaseInvoiceItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购票据明细表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-16 10:13:59
 */
@Mapper
public interface PurchaseInvoiceItemDao {

	PurchaseInvoiceItemDO get(Long id);
	
	List<PurchaseInvoiceItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseInvoiceItemDO purchaseInvoiceItem);
	
	int update(PurchaseInvoiceItemDO purchaseInvoiceItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchRemoveByPurcahseId(Long[] ids);

	List<Map<String, Object>> getDetailOfBody(Map<String, Object> map);

	Map<String, Object> totalOfItem(Map<String, Object> map);


}
