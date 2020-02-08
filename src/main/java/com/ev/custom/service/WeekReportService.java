package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.WeekReportDO;

import java.util.List;
import java.util.Map;

/**
 * 周报
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
public interface WeekReportService {
	
	WeekReportDO get(Long id);
	
	List<WeekReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);
	
	int save(WeekReportDO weekReport);
	
	int update(WeekReportDO weekReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object> detail(Long id);

	void add(WeekReportDO weekReportDO, String weekReportItems, Long[] targetList, String[] taglocationappearanceImage);

	void edit(WeekReportDO weekReportDO, String weekReportItems, Long[] targetList, String[] taglocationappearanceImage,String[] deletetag_appearanceImage);

	void commentWeekReport(Long weekReportId,String comment);

	 void saveWeekChangeAndSbmit(WeekReportDO weekReportDO, String newWeekReportItems, Long[] newSenderIds,String[] newAttachment,int sign);


	R listOfCanDelet(Map<String, Object> map,Long[]ids);

	int countOfQuantyForward(Map<String, Object> map);

	Boolean duplicateDetectionOrNot(String weekReportItems);

}
