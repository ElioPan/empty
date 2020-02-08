package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.NewsDO;

import java.util.List;
import java.util.Map;

/**
 * 新闻公告
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 16:53:40
 */
public interface NewsService {
	
	NewsDO get(Long id);
	
	List<NewsDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(NewsDO news);
	
	int update(NewsDO news);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R dealDetails(Long newsId);

	R  listOfCanDelet(Long[] ids);

	R saveChangOfUpdate(NewsDO newsDO, String pathAndName);

}
