package com.ev.hr.service;

import com.ev.framework.utils.R;
import com.ev.hr.domain.SalaryProjectDO;
import com.ev.hr.vo.SalaryProjectPageParam;

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

	Map<String, Object> pageList(SalaryProjectPageParam pageParam);

	Map<String, Object> getById(Long id);

	R delete(List<Long> ids);

	R saveAndVerify(SalaryProjectDO saveParam);

	Map<String, Object> getListByType(String type);
}
