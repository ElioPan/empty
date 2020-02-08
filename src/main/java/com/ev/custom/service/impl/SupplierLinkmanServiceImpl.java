package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.SupplierLinkmanDao;
import com.ev.custom.domain.SupplierLinkmanDO;
import com.ev.custom.service.SupplierLinkmanService;



@Service
public class SupplierLinkmanServiceImpl implements SupplierLinkmanService {
	@Autowired
	private SupplierLinkmanDao supplierLinkmanDao;
	
	@Override
	public SupplierLinkmanDO get(Long id){
		return supplierLinkmanDao.get(id);
	}
	
	@Override
	public List<SupplierLinkmanDO> list(Map<String, Object> map){
		return supplierLinkmanDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return supplierLinkmanDao.count(map);
	}
	
	@Override
	public int save(SupplierLinkmanDO supplierLinkman){
		return supplierLinkmanDao.save(supplierLinkman);
	}
	
	@Override
	public int update(SupplierLinkmanDO supplierLinkman){
		return supplierLinkmanDao.update(supplierLinkman);
	}
	
	@Override
	public int remove(Long id){
		return supplierLinkmanDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return supplierLinkmanDao.batchRemove(ids);
	}

	@Override
	public int updateDetail(SupplierLinkmanDO supplierLinkman) {
		return supplierLinkmanDao.updateDetail(supplierLinkman);
	}

    @Override
    public int batchSave(List<SupplierLinkmanDO> supplierLinkmanDOs) {
        return supplierLinkmanDao.batchSave(supplierLinkmanDOs);
    }

}
