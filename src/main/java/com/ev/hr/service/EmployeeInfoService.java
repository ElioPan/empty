package com.ev.hr.service;

import com.ev.hr.domain.EmployeeInfoDO;

import java.util.List;
import java.util.Map;

/**
 * 员工信息表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 14:47:42
 */
public interface EmployeeInfoService {
	
	EmployeeInfoDO get(Long id);
	
	List<EmployeeInfoDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(EmployeeInfoDO employeeInfo);
	
	int update(EmployeeInfoDO employeeInfo);

	int updateAll(EmployeeInfoDO employeeInfo);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
