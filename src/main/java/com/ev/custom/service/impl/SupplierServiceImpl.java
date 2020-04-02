package com.ev.custom.service.impl;

import com.ev.custom.dao.SupplierDao;
import com.ev.custom.dao.SupplierLinkmanDao;
import com.ev.custom.domain.SupplierDO;
import com.ev.custom.domain.SupplierLinkmanDO;
import com.ev.custom.service.SupplierService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class SupplierServiceImpl implements SupplierService {
	@Autowired
	private SupplierDao supplierDao;
	@Autowired
	private SupplierLinkmanDao supplierLinkmanDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public SupplierDO get(Long id){
		return supplierDao.get(id);
	}
	
	@Override
	public List<SupplierDO> list(Map<String, Object> map){
		return supplierDao.list(map);
	}



	@Override
	public int count(Map<String, Object> map){
		return supplierDao.count(map);
	}
	
	@Override
	public int save(SupplierDO supplier){
		return supplierDao.save(supplier);
	}
	
	@Override
	public int update(SupplierDO supplier){
		return supplierDao.update(supplier);
	}
	
	@Override
	public int remove(Long id){
		return supplierDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return supplierDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return supplierDao.listForMap(map);
	}

	@Override
	public List<SupplierDO> objectList() {
		return supplierDao.objectList();
	}

	@Override
	public List<Map<String, Object>> checkListByparamete(Map<String, Object> map) {
		return supplierDao.checkListByparamete(map);
	}

	@Override
	public int countCheckListByparam(Map<String, Object> map) {
		return supplierDao.countCheckListByparam(map);
	}

	@Override
	public List<Map<String, Object>> oneTypeGroupList(Map<String, Object> map) {
		return supplierDao.oneTypeGroupList(map);
	}

	@Override
	public void addSupplierOne(SupplierDO supplierDO, SupplierLinkmanDO supplierLinkmanDO) {

		supplierDao.save(supplierDO);
		supplierLinkmanDO.setSupplierId(supplierDO.getId());
		supplierLinkmanDao.save(supplierLinkmanDO);

	}

	@Override
	public R deletOfDevices(Long[] ids) {
		Map<String, Object> query = Maps.newHashMap();
		query.put("id",ids);
		int rows = supplierDao.countOfClientStatus(query);

		if(rows ==ids.length){
			supplierDao.deletOfDevices(query);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk", null));
		}
	}

    @Override
    public List<String> getAllCode() {
        return supplierDao.getAllCode();
    }

    @Override
    public void batchSave(List<SupplierDO> supplierDOs) {
        supplierDao.batchSave(supplierDOs);
    }


	@Override
	public List<Map<String, Object>> supplierAllType(Long spperType) {
		return supplierDao.allTypeSupplier(spperType);
	}


	@Override
	public int checkSave(Map<String, Object> map) {
		return supplierDao.checkSave(map);
	}

	@Override
	public List<String> getAllName() {
		return supplierDao.getAllName();
	}


}
