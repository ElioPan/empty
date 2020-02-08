package com.ev.custom.service;

import com.alibaba.fastjson.JSONArray;
import com.ev.custom.domain.ContentAssocDO;

import java.util.List;
import java.util.Map;

/**
 * 附件关联表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-10 14:51:45
 */
public interface ContentAssocService {
	
	ContentAssocDO get(Integer id);
	
	List<ContentAssocDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ContentAssocDO contentAssoc);
	
	int update(ContentAssocDO contentAssoc);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	void saveList(Long assocerId, String[] paths,String type);

	void saveList(Long assocerId, JSONArray pathList, String type);

	void deleteList(String[] paths);

	int removeByAssocIdAndType(Long[] id,String type);

	int removeByAssocIdsAndTypes(Long[] id, String[] type);
}
