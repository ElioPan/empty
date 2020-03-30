package com.ev.custom.dao;

import com.ev.custom.domain.NoticeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 通知单
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-03-27 14:40:59
 */
@Mapper
public interface NoticeDao {

	NoticeDO get(Long id);
	
	List<NoticeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(NoticeDO notice);
	
	int update(NoticeDO notice);

	int updateAll(NoticeDO notice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
