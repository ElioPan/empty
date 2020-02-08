package com.ev.custom.service;

import com.ev.custom.domain.AlarmRuleDO;

import java.util.List;
import java.util.Map;

/**
 * 报警规则
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:22:16
 */
public interface AlarmRuleService {
	
	AlarmRuleDO get(Long id);
	
	List<AlarmRuleDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AlarmRuleDO alarmRule);
	
	int update(AlarmRuleDO alarmRule);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int add(AlarmRuleDO alarmRule, Long[] pointArray, Long deviceId);

	List<Map<String, Object>> listForMap(Map<String, Object> param);

	int batchSave(List<AlarmRuleDO> alarmRuleDOs);

	Map<String, Object> getDetailInfo(Long id, Long deviceId);

	int edit(AlarmRuleDO alarmRule, Long[] pointArray, Long deviceId);

//	int batchEdit(String alarmRuleArray, Long deviceId);

	int countForMap(Map<String, Object> params);

	Map<String,Object> getDetailInfo(Long id);

	int logicRemove(Long id);

	int logicBatchRemove(Long[] ids);
}
