package com.ev.mes.dao;

import com.ev.mes.domain.ReworkRepairMiddleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 返工返修中间表（工序计划 与 报工、报检中间表）
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-19 11:32:31
 */
@Mapper
public interface ReworkRepairMiddleDao {

	ReworkRepairMiddleDO get(Long id);
	
	List<ReworkRepairMiddleDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ReworkRepairMiddleDO reworkRepairMiddle);
	
	int update(ReworkRepairMiddleDO reworkRepairMiddle);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByPlanId(Long id);

	int canDelReportAboutMiddle(Map<String, Object> map);

}
