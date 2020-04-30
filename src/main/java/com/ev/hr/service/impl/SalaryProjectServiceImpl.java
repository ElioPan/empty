package com.ev.hr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.hr.dao.SalaryProjectDao;
import com.ev.hr.domain.SalaryProjectDO;
import com.ev.hr.service.SalaryProjectService;



@Service
public class SalaryProjectServiceImpl implements SalaryProjectService {
	@Autowired
	private SalaryProjectDao salaryProjectDao;
	
	@Override
	public SalaryProjectDO get(Long id){
		return salaryProjectDao.get(id);
	}
	
	@Override
	public List<SalaryProjectDO> list(Map<String, Object> map){
		return salaryProjectDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salaryProjectDao.count(map);
	}
	
	@Override
	public int save(SalaryProjectDO salaryProject){
		return salaryProjectDao.save(salaryProject);
	}
	
	@Override
	public int update(SalaryProjectDO salaryProject){
		return salaryProjectDao.update(salaryProject);
	}

	@Override
	public int updateAll(SalaryProjectDO salaryProject){
		return salaryProjectDao.updateAll(salaryProject);
	}
	
	@Override
	public int remove(Long id){
		return salaryProjectDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return salaryProjectDao.batchRemove(ids);
	}
	
}
