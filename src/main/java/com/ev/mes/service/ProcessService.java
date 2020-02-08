package com.ev.mes.service;

import com.ev.framework.utils.R;
import com.ev.mes.domain.ProcessDO;

import java.util.List;
import java.util.Map;

/**
 * 工序配置
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:45:29
 */
public interface ProcessService {
	
	ProcessDO get(Long id);
	
	List<ProcessDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessDO process);
	
	int update(ProcessDO process);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R saveAndChange(ProcessDO processDO, String processCheck , String processDevice );

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	R getProcessDetail(Long id );

	R  deteBatchProcess(Long[] ids);

	List<Map<String, Object>> getDetailByProcessId(Map<String, Object> map);



}
