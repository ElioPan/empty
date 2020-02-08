package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.UserAssocDao;
import com.ev.custom.domain.UserAssocDO;
import com.ev.custom.service.UserAssocService;



@Service
public class UserAssocServiceImpl implements UserAssocService {
	@Autowired
	private UserAssocDao userAssocDao;
	
	@Override
	public UserAssocDO get(Long id){
		return userAssocDao.get(id);
	}
	
	@Override
	public List<Map<String, Object>> list(Map<String, Object> map){
		return userAssocDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return userAssocDao.count(map);
	}
	
	@Override
	public int save(UserAssocDO userAssoc){
		return userAssocDao.save(userAssoc);
	}
	
	@Override
	public int update(UserAssocDO userAssoc){
		return userAssocDao.update(userAssoc);
	}
	
	@Override
	public int remove(Long id){
		return userAssocDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return userAssocDao.batchRemove(ids);
	}

	@Override
	public int removeByAssocIdAndUserId(Map<String, Object> map) {
		return userAssocDao.removeByAssocIdAndUserId(map);
	}

	@Override
	public int batchRemoveByAssocIdAadType(Map<String, Object> map) {
		return userAssocDao.batchRemoveByAssocIdAadType(map);
	}

	@Override
	public int changeOfUserAssocSign(Long userId,Long assocId,String assocType){

		UserAssocDO userAssocDO=new UserAssocDO();
		userAssocDO.setAssocId(assocId);
		userAssocDO.setUserId(userId);
		userAssocDO.setAssocType(assocType);
		userAssocDO.setSign(1);
		int rows = userAssocDao.updateOfSign(userAssocDO);
		return  rows;
	}

}
