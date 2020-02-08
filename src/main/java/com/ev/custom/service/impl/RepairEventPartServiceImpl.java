package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.RepairEventPartDao;
import com.ev.custom.domain.RepairEventPartDO;
import com.ev.custom.service.RepairEventPartService;



@Service
public class RepairEventPartServiceImpl implements RepairEventPartService {
	@Autowired
	private RepairEventPartDao repairEventPartDao;
	
	@Override
	public RepairEventPartDO get(Long id){
		return repairEventPartDao.get(id);
	}
	
	@Override
	public List<RepairEventPartDO> list(Map<String, Object> map){
		return repairEventPartDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return repairEventPartDao.count(map);
	}
	
	@Override
	public int save(RepairEventPartDO repairEventPart){
		return repairEventPartDao.save(repairEventPart);
	}
	
	@Override
	public int update(RepairEventPartDO repairEventPart){
		return repairEventPartDao.update(repairEventPart);
	}
	
	@Override
	public int remove(Long id){
		return repairEventPartDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return repairEventPartDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> param) {
		return repairEventPartDao.listForMap(param);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> param) {
		return repairEventPartDao.countForMap(param);
	}
	
}
