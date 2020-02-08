package com.ev.custom.service;

import com.ev.custom.domain.SupplierDO;
import com.ev.custom.domain.SupplierLinkmanDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 供应商表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:03
 */
public interface SupplierService {
	
	SupplierDO get(Long id);
	
	List<SupplierDO> list(Map<String, Object> map);

	List<Map<String,Object>> supplierAllType(Long spperType);
	
	int count(Map<String, Object> map);
	
	int save(SupplierDO supplier);
	
	int update(SupplierDO supplier);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String,Object>> listForMap(Map<String, Object> map);

    List<SupplierDO> objectList();

	List<Map<String,Object>> checkListByparamete(Map<String,Object> map);

	int countCheckListByparam(Map<String,Object> map);

	List<Map<String,Object>> oneTypeGroupList(Map<String,Object> map);

	void addSupplierOne(SupplierDO supplierDO,SupplierLinkmanDO supplierLinkmanDO);

	R deletOfDevices(Long[] ids);


    List<String> getAllCode();

    void batchSave(List<SupplierDO> supplierDOs);

	int checkSave(Map<String,Object>map);

}
