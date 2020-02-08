package com.ev.custom.service;

import com.ev.custom.domain.RepairCheckDO;
import com.ev.custom.domain.RepairEventDO;

import java.util.List;
import java.util.Map;

/**
 * 维修验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:36
 */
public interface RepairCheckService {
	
	RepairCheckDO get(Long id);
	
	List<RepairCheckDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(RepairCheckDO repairCheck);
	
	int update(RepairCheckDO repairCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    void completeCheck(RepairEventDO event,RepairCheckDO check);

	RepairCheckDO getByEventId(Long id);

	List<Map<String,Object>> listForMap(Map<String, Object> param);
}
