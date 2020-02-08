package com.ev.custom.dao;

import com.ev.custom.domain.UpkeepPlanDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 保养计划表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:53
 */
@Repository
@Mapper
public interface UpkeepPlanDao {

	UpkeepPlanDO get(Long id);
	
	List<UpkeepPlanDO> list(Map<String,Object> map);

	List<Map<String,Object>> getPlanListByUserId(Map<String,Object> map);

	List<Map<String,Object>> getNoticeValidPlanid(Map<String,Object> map);

	int count(Map<String,Object> map);
	
	int save(UpkeepPlanDO upkeepPlan);
	
	int update(UpkeepPlanDO upkeepPlan);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Long> getPlanstatusByEndTime();

	int updateStatus(Long[] ids);

	Map<String,Object> getPlanMsgById(Long id);

	List<Map<String,Object>> getProMsgById(Long id);

	List<Map<String,Object>> getPartMsgById(Long id);

    //根据notice生成工单的数据前身
	Map<String,Object> predecessorById(Map<String,Object> map);

	//判断消息时间是否过期
	int checkTimeByNoticeId(Map<String,Object> map);

	List<Map<String,Object>> getNoticeMsg(Map<String,Object> map);

	int countOfList(Map<String, Object> map);

	int deletOfPlan(Map<String, Object> map);
}
