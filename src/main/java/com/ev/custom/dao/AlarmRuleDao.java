package com.ev.custom.dao;

import com.ev.custom.domain.AlarmRuleDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 报警规则
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:22:16
 */
@Mapper
public interface AlarmRuleDao {

	AlarmRuleDO get(Long id);
	
	List<AlarmRuleDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(AlarmRuleDO alarmRule);
	
	int update(AlarmRuleDO alarmRule);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> param);

	int batchSave(List<AlarmRuleDO> alarmRuleDOs);

	int countForMap(Map<String, Object> params);
}
