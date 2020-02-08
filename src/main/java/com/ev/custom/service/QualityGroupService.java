package com.ev.custom.service;

import com.ev.custom.domain.QualityGroupDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-16 13:59:08
 */
public interface QualityGroupService {
	
	QualityGroupDO get(Long id);
	
	List<QualityGroupDO> list(Map<String, Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(QualityGroupDO qualityGroup);
	
	int update(QualityGroupDO qualityGroup);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	void deleteList(Long[] groupIds);
}
