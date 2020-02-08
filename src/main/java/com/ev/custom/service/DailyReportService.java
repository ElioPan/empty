package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.DailyReportDO;

import java.util.List;
import java.util.Map;

/**
 * 日报
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
public interface DailyReportService {
	
	DailyReportDO get(Long id);
	
	List<DailyReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DailyReportDO dailyReport);
	
	int update(DailyReportDO dailyReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	Map<String,Object> detail(Long id);

	void add(DailyReportDO dailyReportDO, Long[] targetList, String[] taglocationappearanceImage);

	void edit(DailyReportDO dailyReportDO, Long[] targetList, String[] taglocationappearanceImage,String[] deletetag_appearanceImage);

	void commentDailyReport(Long dailyReportId,String comment);

	 void saveChangeDetail(DailyReportDO dailyReportDO, Long[] newTargetList,Long[] deletTargetList, String[] newAttachment, String[] deleteAttachment,int sign);

	void allPowerfulMelthod(DailyReportDO dailyReportDO,  Long[] targetList,String[] taglocationappearanceImage,int sign);

	int countOfQuantyForward(Map<String, Object> map);

	R listOfCanDelet(Map<String, Object> map,Long[] ids);

	Boolean duplicateDetectionOrNot();

}
