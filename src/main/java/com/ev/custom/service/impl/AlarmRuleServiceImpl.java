package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.AlarmRuleDao;
import com.ev.custom.domain.AlarmRuleDO;
import com.ev.custom.service.AlarmRuleService;
import com.ev.custom.service.MeasurePointService;
import com.google.common.collect.Maps;

@Service
public class AlarmRuleServiceImpl implements AlarmRuleService {
	@Autowired
	private AlarmRuleDao alarmRuleDao;
	@Autowired
	private MeasurePointService measurePointService;

	@Override
	public AlarmRuleDO get(Long id) {
		return alarmRuleDao.get(id);
	}

	@Override
	public List<AlarmRuleDO> list(Map<String, Object> map) {
		return alarmRuleDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return alarmRuleDao.count(map);
	}

	@Override
	public int save(AlarmRuleDO alarmRule) {
		return alarmRuleDao.save(alarmRule);
	}

	@Override
	public int update(AlarmRuleDO alarmRule) {
		return alarmRuleDao.update(alarmRule);
	}

	@Override
	public int remove(Long id) {
		return alarmRuleDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return alarmRuleDao.batchRemove(ids);
	}

	@Override
	public int add(AlarmRuleDO alarmRule, Long[] pointArray, Long deviceId) {
//		Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
//		param.put("deviceId", deviceId);
//		param.put("offset", 0);
//		param.put("limit", 1);
//		List<Map<String,Object>> alarmRules=this.listForMap(param);
//		Long index = 0L;
//		Long groupId = 0L;
//		if (alarmRules.size()>0) {
//			Map<String, Object> map = alarmRules.get(0);
//			index = Long.parseLong(map.get("sortNo").toString()) + 10000000L;
//			groupId = Long.parseLong(map.get("groupId").toString()) + 1;
//		}
		Long userId = ShiroUtils.getUserId();
		Date date = new Date();
//		List<AlarmRuleDO> alarmRuleDOs = new ArrayList<>();
		String name = alarmRule.getName();
		Integer alarmType = alarmRule.getAlarmType();
		Integer alarmLevel = alarmRule.getAlarmLevel();
		Integer triggerMode = alarmRule.getTriggerMode();
		Integer triggerTime = alarmRule.getTriggerTime();
		Integer continueTime = alarmRule.getContinueTime();
		String alarmWay = alarmRule.getAlarmWay();
		AlarmRuleDO alarmRuleDO;
		int count = 0;
		for (Long pointId : pointArray) {
			alarmRuleDO = new AlarmRuleDO();
			alarmRuleDO.setName(name);
			alarmRuleDO.setAlarmType(alarmType);
			alarmRuleDO.setAlarmLevel(alarmLevel);
			alarmRuleDO.setTriggerMode(triggerMode);
			alarmRuleDO.setTriggerTime(triggerTime);
			alarmRuleDO.setContinueTime(continueTime);
			alarmRuleDO.setAlarmWay(alarmWay);
//			alarmRule.setGroupId(groupId);
			alarmRuleDO.setPointId(pointId);
//			alarmRule.setSortNo(index);
			alarmRuleDO.setCreateBy(userId);
			alarmRuleDO.setCreateTime(date);
			alarmRuleDO.setUpdateBy(userId);
			alarmRuleDO.setUpdateTime(date);
			count += this.save(alarmRuleDO);
//			alarmRuleDOs.add(alarmRule);
//			count += this.save(alarmRule);
//			index += 10000000L ;
		}
//		int count = this.batchSave(alarmRuleDOs);

		return count == pointArray.length ? count : -1;
	}

	@Override
	public int batchSave(List<AlarmRuleDO> alarmRuleDOs) {
		return alarmRuleDao.batchSave(alarmRuleDOs);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> param) {
		return alarmRuleDao.listForMap(param);
	}

	@Override
	public Map<String, Object> getDetailInfo(Long id, Long deviceId) {
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(2);
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
		params.put("groupId", id);
		params.put("deviceId", deviceId);
		List<Map<String, Object>> alarmRules = this.listForMap(params);
		List<String> pointIds = new ArrayList<>();
		String pointId;
		List<Map<String, Object>> measurePoints = measurePointService.listForMap(params);
		result.put("alarmRule", measurePoints);
		if (alarmRules.size() > 0) {
			result.put("alarmRule", alarmRules.get(0));
			for (Map<String, Object> map : alarmRules) {
				pointId = map.get("pointId").toString();
				pointIds.add(pointId);
			}
			params.remove("groupId");
			for (String point : pointIds) {
				for (Map<String, Object> map : measurePoints) {
					if (point.equals(map.get("id").toString())) {
						// 1 为选中
						map.put("isCheck", 1);
						break;
					}
				}
			}
		}
		return result;
	}

	@Override
	public int edit(AlarmRuleDO alarmRule, Long[] pointArray, Long deviceId) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
		params.put("groupId", alarmRule.getGroupId());
		params.put("deviceId", deviceId);
		List<Map<String, Object>> alarmRules = this.listForMap(params);
		List<Long> addInfo = new ArrayList<>();
		List<Long> deleteInfo = new ArrayList<>();
		List<Long> oldInfo = new ArrayList<>();
		List<Long> pointArrays = Arrays.asList(pointArray);
		for (Map<String, Object> map : alarmRules) {
			oldInfo.add(Long.parseLong(map.get("id").toString()));
		}
		for (Long pointId : pointArrays) {
			if (!oldInfo.contains(pointId)) {
				addInfo.add(pointId);
			}
		}
		for (Long pointId : oldInfo) {
			if (!pointArrays.contains(pointId)) {
				deleteInfo.add(pointId);
			}
		}
		int update = this.update(alarmRule);
		if (update > 0) {
			if (addInfo.size() > 0) {
				this.add(alarmRule, addInfo.toArray(new Long[0]), deviceId);
			}
			if (deleteInfo.size() > 0) {
				this.batchRemove(addInfo.toArray(new Long[0]));
			}
		}

		return update;
	}

//	@Override
//	public int batchEdit(String alarmRuleArray, Long deviceId) {
//		return 0;
//	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return alarmRuleDao.countForMap(params);
	}

	@Override
	public Map<String, Object> getDetailInfo(Long id) {
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("id", id);
		params.put("offset", 0);
		params.put("limit", 1);
		List<Map<String, Object>> detailList = this.listForMap(params);
		if (detailList.size() > 0) {
			result.put("data", detailList.get(0));
		}
		return result;
	}

	@Override
	public int logicRemove(Long id) {
		AlarmRuleDO alarmRuleDO = new AlarmRuleDO();
		alarmRuleDO.setId(id);
		alarmRuleDO.setDelFlag(1);
		return this.update(alarmRuleDO);
	}

	@Override
	public int logicBatchRemove(Long[] ids) {
		int count = 0;
		for (Long id : ids) {
			count += this.logicRemove(id);
		}
		return count;
	}

}
