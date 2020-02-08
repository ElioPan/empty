package com.ev.custom.service;

import com.ev.custom.domain.RepairEventPartDO;

import java.util.List;
import java.util.Map;

/**
 * 修维事件与备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
public interface RepairEventPartService {
	
	RepairEventPartDO get(Long id);
	
	List<RepairEventPartDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(RepairEventPartDO repairEventPart);
	
	int update(RepairEventPartDO repairEventPart);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String,Object>> listForMap(Map<String, Object> param);

	Map<String,Object> countForMap(Map<String, Object> param);
}
