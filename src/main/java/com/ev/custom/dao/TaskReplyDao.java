package com.ev.custom.dao;

import com.ev.custom.domain.TaskReplyDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 任务处理
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-12 12:36:25
 */
@Mapper
public interface TaskReplyDao {

	TaskReplyDO get(Long id);
	
	List<TaskReplyDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int count(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(TaskReplyDO taskReply);
	
	int update(TaskReplyDO taskReply);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> detail(Long id);
}
