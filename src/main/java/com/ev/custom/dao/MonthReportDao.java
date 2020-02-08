package com.ev.custom.dao;

import com.ev.custom.domain.MonthReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 月报
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
@Mapper
public interface MonthReportDao {

	MonthReportDO get(Long id);
	
	List<MonthReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(MonthReportDO monthReport);
	
	int update(MonthReportDO monthReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int countOfQuantyForward(Map<String, Object> map);

	List<Map<String,Object>> listOfCanDelet(Map<String, Object> map);

	Map<String,Object> getOneMonthDetail(Map<String, Object> map);

	int monthOfHaveOrNot(Long createById);

}
