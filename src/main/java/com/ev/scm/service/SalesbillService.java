package com.ev.scm.service;

import com.ev.scm.domain.SalesbillDO;
import com.ev.framework.utils.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-30 13:24:10
 */
public interface SalesbillService {
	
	SalesbillDO get(Long id);
	
	List<SalesbillDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalesbillDO salesbill);
	
	int update(SalesbillDO salesbill);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	R addOrUpdateSalesBill(SalesbillDO salesBillDO, String bodyItem, Long[] itemIds);

	R audit(Long id);
	
	R reverseAudit(Long id);
	
	R getDetail(Long id);

    R batchRemoveSalesBill(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    int countForMap(Map<String, Object> map);

    BigDecimal getCountBySource(Map<String, Object> map);
}
