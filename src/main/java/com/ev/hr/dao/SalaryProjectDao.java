package com.ev.hr.dao;

import com.ev.hr.domain.SalaryProjectDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 薪资项目
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 13:10:37
 */
@Mapper
public interface SalaryProjectDao {

	SalaryProjectDO get(Long id);
	
	List<SalaryProjectDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(SalaryProjectDO salaryProject);
	
	int update(SalaryProjectDO salaryProject);

	int updateAll(SalaryProjectDO salaryProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchUpdate(List<SalaryProjectDO> param);
}
