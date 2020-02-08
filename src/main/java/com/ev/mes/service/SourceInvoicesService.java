package com.ev.mes.service;

import com.ev.mes.domain.SourceInvoicesDO;

import java.util.List;
import java.util.Map;

/**
 * 来源单与目标单中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 18:33:37
 */
public interface SourceInvoicesService {
	
	SourceInvoicesDO get(Long id);
	
	List<SourceInvoicesDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SourceInvoicesDO sourceInvoices);
	
	int update(SourceInvoicesDO sourceInvoices);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
