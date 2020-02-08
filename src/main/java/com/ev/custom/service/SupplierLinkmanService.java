package com.ev.custom.service;

import com.ev.custom.domain.SupplierLinkmanDO;

import java.util.List;
import java.util.Map;

/**
 * 供应商联系人表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:03
 */
public interface SupplierLinkmanService {
	
	SupplierLinkmanDO get(Long id);
	
	List<SupplierLinkmanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SupplierLinkmanDO supplierLinkman);
	
	int update(SupplierLinkmanDO supplierLinkman);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);


	int updateDetail(SupplierLinkmanDO supplierLinkman);

    int batchSave(List<SupplierLinkmanDO> supplierLinkmanDOs);
}
