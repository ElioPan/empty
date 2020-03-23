package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.OtherReceivablesItemDao;
import com.ev.scm.domain.OtherReceivablesItemDO;
import com.ev.scm.service.OtherReceivablesItemService;



@Service
public class OtherReceivablesItemServiceImpl implements OtherReceivablesItemService {
	@Autowired
	private OtherReceivablesItemDao otherReceivablesItemDao;
	
	@Override
	public OtherReceivablesItemDO get(Long id){
		return otherReceivablesItemDao.get(id);
	}
	
	@Override
	public List<OtherReceivablesItemDO> list(Map<String, Object> map){
		return otherReceivablesItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return otherReceivablesItemDao.count(map);
	}
	
	@Override
	public int save(OtherReceivablesItemDO otherReceivablesItem){
		return otherReceivablesItemDao.save(otherReceivablesItem);
	}
	
	@Override
	public int update(OtherReceivablesItemDO otherReceivablesItem){
		return otherReceivablesItemDao.update(otherReceivablesItem);
	}

	@Override
	public int updateAll(OtherReceivablesItemDO otherReceivablesItem){
		return otherReceivablesItemDao.updateAll(otherReceivablesItem);
	}
	
	@Override
	public int remove(Long id){
		return otherReceivablesItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return otherReceivablesItemDao.batchRemove(ids);
	}

	@Override
	public int batchRemoveByHeadId(Long[] ids) {
		return otherReceivablesItemDao.batchRemoveByHeadId(ids);
	}

	@Override
	public List<Map<String, Object>> getBodyDetail(Long id) {
		return otherReceivablesItemDao.getBodyDetail(id);
	}

	@Override
	public Map<String, Object> totailAmount(Long id) {
		return otherReceivablesItemDao.totailAmount(id);
	}

	@Override
	public List<Map<String, Object>> getDetailOfIntroduce(Map<String, Object> map) {
		return otherReceivablesItemDao.getDetailOfIntroduce(map);
	}

	@Override
	public Map<String, Object> totailAmountOfIntroduce(Map<String, Object> map) {
		return otherReceivablesItemDao.totailAmountOfIntroduce(map);
	}

}
