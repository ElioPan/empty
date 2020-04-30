package com.ev.hr.service;

import com.ev.hr.domain.SalaryProjectDO;

import java.util.List;
import java.util.Map;

/**
 * 薪资项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 13:10:37
 */
public interface SalaryProjectService {
	
	SalaryProjectDO get(Long id);
	
	List<SalaryProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalaryProjectDO salaryProject);
	
	int update(SalaryProjectDO salaryProject);

	int updateAll(SalaryProjectDO salaryProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
