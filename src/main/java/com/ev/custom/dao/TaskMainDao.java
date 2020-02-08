package com.ev.custom.dao;

import com.ev.custom.domain.TaskMainDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 任务
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-08 13:03:04
 */
@Mapper
public interface TaskMainDao {

	TaskMainDO get(Long id);
	
	int count(Map<String, Object> map);
	
	int save(TaskMainDO taskMain);
	
	int update(TaskMainDO taskMain);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<TaskMainDO> list(Map<String, Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int countBackLog(Map<String, Object> params);

	List<Map<String, Object>> countWeekBackLog(Map<String, Object> params);
}
