package com.ev.custom.service;

import com.ev.custom.domain.MaintenanceRepositoryDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 维保知识库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-10 10:25:28
 */
public interface MaintenanceRepositoryService {
	
	MaintenanceRepositoryDO get(Long id);
	
	List<MaintenanceRepositoryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaintenanceRepositoryDO maintenanceRepository);
	
	int update(MaintenanceRepositoryDO maintenanceRepository);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R addOrUpdate(MaintenanceRepositoryDO maintenanceRepositoryDO);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	R detail(Long id);

	R batchDelete(Long[] ids);

	boolean checkDuplicate(MaintenanceRepositoryDO maintenanceRepositoryDO);
}
