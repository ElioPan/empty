package com.ev.custom.dao;

import com.ev.custom.domain.UpkeepRecordProjectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 保养工单与实际保养项目中间表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-29 10:09:20
 */
@Mapper
public interface UpkeepRecordProjectDao {

	UpkeepRecordProjectDO get(Long id);
	
	List<UpkeepRecordProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepRecordProjectDO upkeepRecordProject);
	
	int update(UpkeepRecordProjectDO upkeepRecordProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> projectDetailByRecordID(Map<String, Object> map);

	List<Map<String, Object>> getMsgforDevice(Map<String, Object> map);

	int removeByRecordId(Long id);

	int batchRemoveByRecordId(Long[] ids);


}
