package com.ev.custom.dao;

import com.ev.custom.domain.TaskEmployeeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 任务关联人
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-09 16:39:45
 */
@Mapper
public interface TaskEmployeeDao {

	TaskEmployeeDO get(Integer id);
	
	List<TaskEmployeeDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TaskEmployeeDO taskEmployee);
	
	int update(TaskEmployeeDO taskEmployee);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int removeByAssocIdAndTaskId(Map<String, Object> map);
}
