package com.ev.custom.service;

import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.custom.domain.UpkeepRecordPartDO;

import java.util.List;
import java.util.Map;

/**
 * 保养工单与实际使用备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-23 15:16:53
 */
public interface UpkeepRecordPartService {
	
	UpkeepRecordPartDO get(Integer id);
	
	List<UpkeepRecordPartDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepRecordPartDO upkeepRecordPart);
	
	int update(UpkeepRecordPartDO upkeepRecordPart);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	List<Map<String, Object>> spartByRecordId(Map<String, Object> map);

	List<Map<String, Object>> spartByRecordIdAgain(Map<String, Object> map);

	int dealWithSpareDetails(Map<String, Object> map);

	void updateRecorPartDetail(String project,String spart,UpkeepRecordDO upkeepRecordDO);

	int removeByRecordId(Long id);

	int batchRemoveByRecordId(Long[] ids);


}
