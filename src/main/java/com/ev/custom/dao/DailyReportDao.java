package com.ev.custom.dao;

import com.ev.custom.domain.DailyReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 日报
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
@Mapper
public interface DailyReportDao {

	DailyReportDO get(Long id);
	
	List<DailyReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DailyReportDO dailyReport);
	
	int update(DailyReportDO dailyReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int countOfQuantyForward(Map<String, Object> map);

	List<Map<String, Object>> listOfCanDelet(Map<String, Object> map);

	Map<String, Object> dailyOfDetail(Map<String, Object> map);

	int haveOrNot(Long createById);
}
