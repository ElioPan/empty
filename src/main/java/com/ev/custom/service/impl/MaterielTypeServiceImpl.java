package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.MaterielTypeDao;
import com.ev.custom.domain.MaterielTypeDO;
import com.ev.custom.service.MaterielTypeService;



@Service
public class MaterielTypeServiceImpl implements MaterielTypeService {
	@Autowired
	private MaterielTypeDao materielTypeDao;
	
	@Override
	public MaterielTypeDO get(Long id){
		return materielTypeDao.get(id);
	}
	
	@Override
	public List<MaterielTypeDO> list(Map<String, Object> map){
		return materielTypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return materielTypeDao.count(map);
	}
	
	@Override
	public int save(MaterielTypeDO materielType){
		return materielTypeDao.save(materielType);
	}
	
	@Override
	public int update(MaterielTypeDO materielType){
		return materielTypeDao.update(materielType);
	}
	
	@Override
	public int remove(Long id){
		return materielTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return materielTypeDao.batchRemove(ids);
	}

	@Override
	public int checkSave(MaterielTypeDO materielType) {
		return materielTypeDao.checkSave(materielType);
	}
	
	@Override
	public int checkDelete(Long[] ids) {
		return materielTypeDao.checkDelete(ids);
	}

    @Override
    public List<Map<String,Object>> listForMap(Map<String, Object> params) {
        return materielTypeDao.listForMap(params);
    }
}
