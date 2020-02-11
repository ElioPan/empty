package com.ev.custom.dao;

import com.ev.custom.domain.NoticeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 通知单
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-11 17:00:08
 */
@Mapper
public interface NoticeDao {

	NoticeDO get(Long id);
	
	List<NoticeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(NoticeDO notice);
	
	int update(NoticeDO notice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
