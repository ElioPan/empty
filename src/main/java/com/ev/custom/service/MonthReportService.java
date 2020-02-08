package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.MonthReportDO;

import java.util.List;
import java.util.Map;

/**
 * 月报
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
public interface MonthReportService {
	
	MonthReportDO get(Long id);
	
	List<MonthReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(MonthReportDO monthReport);
	
	int update(MonthReportDO monthReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);


	Map<String,Object> detail(Long id);

	void add(MonthReportDO monthReportDO, Long[] targetList, String[] taglocationappearanceImage);

	void edit(MonthReportDO monthReportDO, Long[] targetList, String[] taglocationappearanceImage,String[] deletetag_appearanceImage);

	void commentMonthReport(Long monthReportId,String comment);

	int countOfQuantyForward(Map<String, Object> map);

	void allPowerfulMelthod(MonthReportDO monthReportDO, Long[] targetList, String[] taglocationappearanceImage, int sign);

	R listOfCanDelet(Map<String, Object> map,Long[]ids);

	Boolean duplicateDetectionOrNot();

}
