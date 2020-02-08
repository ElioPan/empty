package com.ev.custom.dao;

import com.ev.custom.domain.SupplierDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 供应商表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:03
 */
@Mapper
public interface SupplierDao {

	SupplierDO get(Long id);
	
	List<SupplierDO> list(Map<String,Object> map);

	List<Map<String,Object>> allTypeSupplier(Long spperType);
	
	int count(Map<String,Object> map);

	int save(SupplierDO supplier);
	
	int update(SupplierDO supplier);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String,Object>> listForMap(Map<String,Object> map);

    List<SupplierDO> objectList();

	List<Map<String,Object>> checkListByparamete(Map<String,Object> map);

	int countCheckListByparam(Map<String,Object> map);

	List<Map<String,Object>> oneTypeGroupList(Map<String,Object> map);

	int deletOfDevices(Map<String,Object> map);

	int countOfClientStatus(Map<String,Object>  map);


    List<String> getAllCode();

    int batchSave(List<SupplierDO> supplierDOs);

	int checkSave(Map<String,Object>map);

}
