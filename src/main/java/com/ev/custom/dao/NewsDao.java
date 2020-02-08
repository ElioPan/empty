package com.ev.custom.dao;

import com.ev.custom.domain.NewsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 新闻公告
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 16:53:40
 */
@Mapper
public interface NewsDao {

	NewsDO get(Long id);
	
	List<NewsDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(NewsDO news);
	
	int update(NewsDO news);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listOfCanDelet(Map<String, Object> map);
}
