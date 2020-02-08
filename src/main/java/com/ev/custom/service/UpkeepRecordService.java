package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.UpkeepCheckDO;
import com.ev.custom.domain.UpkeepRecordDO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 保养记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
public interface UpkeepRecordService {
	
	UpkeepRecordDO get(Long id);
	
	List<UpkeepRecordDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepRecordDO upkeepRecord);
	
	int update(UpkeepRecordDO upkeepRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<String,Object> recordListForDevice(int pageno, int pagesize, Long deviceId);

	Map<String,Object> getRecordDetail(Long id);

    UpkeepRecordDO getByPlanId(Long id);

    Map<String,Object> saveRecord(UpkeepRecordDO record, String projectIds, String partIdArray);

    Map<String,Object> recordListForUser(int pageno, int pagesize, Long userId);

	Map<String,Object> handleRecord(Long id, String content, String projectArray, String partArray);

	Map<String,Object> checkRecord(HttpServletRequest request, UpkeepCheckDO check, Long id);

	List<Map<String,Object>> listRecords(Map<String,Object> map);

	List<Map<String,Object>> newOfListRecords(Map<String,Object> map);


	List<Map<String,Object>> oneRecordDetail(Map<String,Object> map);

//	List<Map<String,Object>> getMsgByDeviceId (Map<String,Object> map);
	List<Map<String, Object>> getMsgByDeviceId(Map<String,Object> map);

	Map<String,Object> countListRecords(Map<String,Object> map);

	int countOfWaitingDo (Map<String,Object> map);

	R closePaseOfRecord(Long[] recordIds,String closeResen);

	R getOneDetailOfRecord(Map<String,Object> maps);

	R doingRecorder(Long recordId,Date dateNow);

	R  saveRecorderOfNoPlan(UpkeepRecordDO upkeepRecordDO,String proList,String partList,int sign);

	List<Map<String,Object>> oneRecordDetaiOfNoPlan(Map<String,Object> map);

	int countOfMsgByDeviceId(Map<String,Object> map);

	R deletOfRecords(Long[] recordIds);

	List<Map<String,Object>> upkeepDeatailOfBoard(Map<String,Object> map);

	int countOfupkeepOfBoard (Map<String,Object> map);




}
