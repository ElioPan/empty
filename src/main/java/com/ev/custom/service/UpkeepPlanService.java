package com.ev.custom.service;

import com.ev.custom.domain.UpkeepPlanDO;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 保养计划表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:53
 */
public interface UpkeepPlanService {
	
	UpkeepPlanDO get(Long id);
	
	List<UpkeepPlanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepPlanDO upkeepPlan);
	
	int update(UpkeepPlanDO upkeepPlan);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<String,Object> planListForDevice(int pageno, int pagesize, Long deviceId);

    Map<String,Object> getPlanDetail(Long id);

	Map<String,Object> savePlan(UpkeepPlanDO plan, String projectIds, String partIds);

	void savePlanChangeAndSbmit(UpkeepPlanDO planDO, String projectIds, String partIds,int sign);

	List<Map<String,Object>> getPlanListByUser(Map<String,Object> map);

    Map<String,Object> planListForUser(int pageno, int pagesize, Long userId);

	List<Map<String, Object>> getNoticeValidPlanids(Map<String,Object> map);

	int makeGeneratemMaintenance(List<Map<String, Object>> list) throws ParseException;

	List<Long> getPlanstatusByEndTime();

	int updateStatus(Long[] ids);

	Map<String,Object> getPlanMsgById(Long id);

	List<Map<String,Object>> getProMsgById(Long id);

	List<Map<String,Object>> getPartMsgById(Long id);

	//根据notice生成工单的数据前身
	Map<String,Object> predecessorById(Map<String,Object> map);

	int checkTimeByNoticeId(Map<String,Object> map);

	List<Map<String,Object>> getNoticeMsg(Map<String,Object> map);

	int countOfList(Map<String, Object> map);


	void makeWorkOrder(Long planId, Date startTime, Date endTime);

	int deletOfPlan(Map<String, Object> map);


}
