package com.ev.custom.dao;

import com.ev.custom.domain.CommentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 回复信息表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-22 08:55:56
 */
@Mapper
public interface CommentDao {

	CommentDO get(Long id);
	
	List<CommentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CommentDO comment);
	
	int update(CommentDO comment);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listOfDetail(Map<String, Object> map);


}
