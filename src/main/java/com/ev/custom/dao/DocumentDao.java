package com.ev.custom.dao;

import com.ev.custom.domain.DocumentDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 文档
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 12:59:46
 */
@Mapper
public interface DocumentDao {

	DocumentDO get(Long id);
	
	List<DocumentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(DocumentDO document);
	
	int update(DocumentDO document);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
