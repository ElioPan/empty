package com.ev.custom.dao;

import com.ev.custom.domain.WeekReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 周报
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
@Mapper
public interface WeekReportDao {

	WeekReportDO get(Long id);
	
	List<WeekReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(WeekReportDO weekReport);
	
	int update(WeekReportDO weekReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int countOfQuantyForward(Map<String, Object> map);

	List<Map<String, Object>> listOfCanDelet(Map<String, Object> map);

	Map<String, Object> weekOfDetail(Map<String, Object> map);

	int weekHaveOrNot(Map<String, Object> map);
}
