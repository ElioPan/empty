package com.ev.mes.service;

import com.ev.mes.domain.ProcessReportCheckItemDO;

import java.util.List;
import java.util.Map;

/**
 * 工序检验明细（子表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:15:25
 */
public interface ProcessReportCheckItemService {
	
	ProcessReportCheckItemDO get(Long id);
	
	List<ProcessReportCheckItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessReportCheckItemDO processReportCheckItem);
	
	int update(ProcessReportCheckItemDO processReportCheckItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByReportId(Long[] ids);

	List<Map<String, Object>> detailByCheckId(Long id);



}
