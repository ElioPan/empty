package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.ProcessReportDO;

import java.util.List;
import java.util.Map;

/**
 * 工序报工单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:14:53
 */
public interface ProcessReportService {
	
	ProcessReportDO get(Long id);
	
	List<ProcessReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessReportDO processReport);
	
	int update(ProcessReportDO processReport);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R saveAndchangeSbumit(String reportDetai, int sign);

	Map<String, Object> reportDetailById(Map<String, Object> map);

	R deletOfReports(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int	countForMap(Map<String, Object> map);

	R reworkRepair(Long id,int sign);

}
