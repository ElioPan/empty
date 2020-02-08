package com.ev.custom.service;

import com.ev.custom.domain.ReportTaskDO;

import java.util.List;
import java.util.Map;

/**
 * 关联单据与关联任务联系表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-21 13:01:44
 */
public interface ReportTaskService {
	
	ReportTaskDO get(Integer id);
	
	List<ReportTaskDO> list(Map<String, Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ReportTaskDO reportTask);
	
	int update(ReportTaskDO reportTask);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
