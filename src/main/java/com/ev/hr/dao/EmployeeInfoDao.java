package com.ev.hr.dao;

import com.ev.hr.domain.EmployeeInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 员工信息表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 14:47:42
 */
@Mapper
public interface EmployeeInfoDao {

	EmployeeInfoDO get(Long id);
	
	List<EmployeeInfoDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(EmployeeInfoDO employeeInfo);
	
	int update(EmployeeInfoDO employeeInfo);

	int updateAll(EmployeeInfoDO employeeInfo);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchUpdate(List<EmployeeInfoDO> param);
}
