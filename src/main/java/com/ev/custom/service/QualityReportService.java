package com.ev.custom.service;

import com.ev.custom.domain.QualityReportDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-16 13:15:40
 */
public interface QualityReportService {
	
	QualityReportDO get(Long id);
	
	List<QualityReportDO> list(Map<String, Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
	
	Map<String,Object> detail(Long id);
	
	int count(Map<String, Object> map);
	
	int save(QualityReportDO qualityReport);
	
	int update(QualityReportDO qualityReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String, Object> add(QualityReportDO qualityReport,String groupList) ;
	
	int edit(QualityReportDO qualityReport, String groupList, Long[] groupIds);

	int checkSave(Long id,Long userId, String resultContent);
	
	int editReason(Long id, String analyzeReason);

	Map<String, Object> getDetailPlan(Long id, Integer planTypeId);
	
	Map<String,Object> params(Map<String,Object> params);

	/**
	 * 验证8D报告的子任务有无未完成状态
	 * @param id
	 * @return
	 */
	boolean checkBDTask(Long id);
	
	boolean isDelete(Long id);

	/**
	 * 获取8D报告下属的所有任务
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getTaskInfo(Long id);

}
