package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.MaterialsCollectDO;

import java.util.List;
import java.util.Map;

/**
 * 用料采集
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:17
 */
public interface MaterialsCollectService {
	
	MaterialsCollectDO get(Long id);
	
	List<MaterialsCollectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterialsCollectDO materialsCollect);
	
	int update(MaterialsCollectDO materialsCollect);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R saveAndSubmit(String detail, Long[] ids , Long dispatchId,int sign);

	List<Map<String, Object>> getListssDetail(Map<String, Object> map);

	R removeCollect(Long[]ids);

	int countForMap(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

}
