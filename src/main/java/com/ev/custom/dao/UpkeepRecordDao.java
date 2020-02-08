package com.ev.custom.dao;

import com.ev.custom.domain.UpkeepRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 保养记录表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
@Mapper
public interface UpkeepRecordDao {

	UpkeepRecordDO get(Long id);
	
	List<UpkeepRecordDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UpkeepRecordDO upkeepRecord);
	
	int update(UpkeepRecordDO upkeepRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    UpkeepRecordDO getByPlanId(Long id);

	List<Map<String,Object>> listRecords(Map<String,Object> map);

	List<Map<String,Object>> newOfListRecords(Map<String,Object> map);

	List<Map<String,Object>> oneRecordDetail(Map<String,Object> map);

//	List<Map<String,Object>> getMsgByDeviceId (Map<String,Object> map);
	List<Map<String, Object>> getMsgByDeviceId(Map<String,Object> map);


	Map<String,Object> countListRecords(Map<String,Object> map);

	int countOfWaitingDo (Map<String,Object> map);

	List<Map<String,Object>> idsOfCanChange(Map<String,Object> map);

	List<Map<String,Object>> oneRecordDetaiOfNoPlan(Map<String,Object> map);

	int countOfMsgByDeviceId(Map<String,Object> map);

	List<Map<String, Object>> idsOfCanDelet(Map<String,Object> map);

	List<Map<String,Object>> upkeepDeatailOfBoard(Map<String,Object> map);


	int countOfupkeepOfBoard (Map<String,Object> map);


}
