package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.ReimApplyItemDao;
import com.ev.custom.domain.ReimApplyItemDO;
import com.ev.custom.service.ReimApplyItemService;



@Service
public class ReimApplyItemServiceImpl implements ReimApplyItemService {
	@Autowired
	private ReimApplyItemDao reimApplyItemDao;
	
	@Override
	public ReimApplyItemDO get(Long id){
		return reimApplyItemDao.get(id);
	}
	
	@Override
	public List<ReimApplyItemDO> list(Map<String, Object> map){
		return reimApplyItemDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return reimApplyItemDao.count(map);
	}
	
	@Override
	public int save(ReimApplyItemDO reimApplyItem){
		return reimApplyItemDao.save(reimApplyItem);
	}
	
	@Override
	public int update(ReimApplyItemDO reimApplyItem){
		return reimApplyItemDao.update(reimApplyItem);
	}
	
	@Override
	public int remove(Long id){
		return reimApplyItemDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return reimApplyItemDao.batchRemove(ids);
	}

	@Override
	public int removeByReimId(Long reimApplayId) {
		return reimApplyItemDao.removeByReimId(reimApplayId);
	}

}
