package com.ev.scm.service.impl;

import com.ev.scm.dao.InventoryPlanItemDao;
import com.ev.scm.domain.InventoryPlanItemDO;
import com.ev.scm.service.InventoryPlanItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class InventoryPlanItemServiceImpl implements InventoryPlanItemService {
	@Autowired
	private InventoryPlanItemDao inventoryPlanItemDao;
	
	@Override
	public InventoryPlanItemDO get(Long id){
		return inventoryPlanItemDao.get(id);
	}
	
	@Override
	public List<InventoryPlanItemDO> list(Map<String, Object> map){
		return inventoryPlanItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return inventoryPlanItemDao.count(map);
	}
	
	@Override
	public int save(InventoryPlanItemDO checkBody){
		return inventoryPlanItemDao.save(checkBody);
	}
	
	@Override
	public int update(InventoryPlanItemDO checkBody){
		return inventoryPlanItemDao.update(checkBody);
	}
	
	@Override
	public int remove(Long id){
		return inventoryPlanItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return inventoryPlanItemDao.batchRemove(ids);
	}

	@Override
	public int removeByPlanId(Long id) {
		return inventoryPlanItemDao.removeByPlanId(id);
	}

	@Override
	public List<Map<String, Object>> getProfitLossMsg(Map<String, Object> map) {
		return inventoryPlanItemDao.getProfitLossMsg(map);
	}

	@Override
	public List<Map<String, Object>> getProMsgCount(Map<String, Object> map) {
		return inventoryPlanItemDao.getProMsgCount(map);
	}

	@Override
	public int countOfWinLoss(Map<String, Object> map) {
		return inventoryPlanItemDao.countOfWinLoss(map);
	}

	@Override
	public List<Map<String, Object>> getMaterialAll(Map<String, Object> map) {
		return inventoryPlanItemDao.getMaterialAll(map);
	}

}
