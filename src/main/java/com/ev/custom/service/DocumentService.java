package com.ev.custom.service;

import com.ev.custom.domain.DocumentDO;

import java.util.List;
import java.util.Map;

/**
 * 文档
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 12:59:46
 */
public interface DocumentService {
	
	DocumentDO get(Long id);
	
	List<DocumentDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DocumentDO document);
	
	int update(DocumentDO document);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	Map<String,Object> detail(Long id);

	void add(DocumentDO document, String[] taglocationappearanceImage);

	void edit(DocumentDO document, String[] taglocationappearanceImage,String[] deletetag_appearanceImage);
}
