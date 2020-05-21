package com.ev.hr.service.impl;

import com.ev.hr.dao.EmployeeInfoDao;
import com.ev.hr.domain.EmployeeInfoDO;
import com.ev.hr.service.EmployeeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeInfoServiceImpl implements EmployeeInfoService {
	@Autowired
	private EmployeeInfoDao employeeInfoDao;
	
	@Override
	public EmployeeInfoDO get(Long id){
		return employeeInfoDao.get(id);
	}
	
	@Override
	public List<EmployeeInfoDO> list(Map<String, Object> map){
		return employeeInfoDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return employeeInfoDao.count(map);
	}
	
	@Override
	public int save(EmployeeInfoDO employeeInfo){
		return employeeInfoDao.save(employeeInfo);
	}
	
	@Override
	public int update(EmployeeInfoDO employeeInfo){
		return employeeInfoDao.update(employeeInfo);
	}

	@Override
	public int updateAll(EmployeeInfoDO employeeInfo){
		return employeeInfoDao.updateAll(employeeInfo);
	}
	
	@Override
	public int remove(Long id){
		return employeeInfoDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return employeeInfoDao.batchRemove(ids);
	}
	
}
