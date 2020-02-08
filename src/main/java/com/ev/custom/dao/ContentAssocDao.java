package com.ev.custom.dao;

import com.ev.custom.domain.ContentAssocDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 附件关联表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-10 14:51:45
 */
@Mapper
public interface ContentAssocDao {

	ContentAssocDO get(Integer id);
	
	List<ContentAssocDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ContentAssocDO contentAssoc);
	
	int update(ContentAssocDO contentAssoc);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int removeByAssocIdAndType(Map<String, Object> map);

	int removeByAssocIdsAndTypes(Map<String, Object> map);

}
