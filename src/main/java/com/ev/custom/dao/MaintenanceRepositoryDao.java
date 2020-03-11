package com.ev.custom.dao;

import com.ev.custom.domain.MaintenanceRepositoryDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 维保知识库
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-10 10:25:28
 */
@Mapper
public interface MaintenanceRepositoryDao {

	MaintenanceRepositoryDO get(Long id);
	
	List<MaintenanceRepositoryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaintenanceRepositoryDO maintenanceRepository);
	
	int update(MaintenanceRepositoryDO maintenanceRepository);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int checkDuplicate(MaintenanceRepositoryDO maintenanceRepositoryDO);

	List<Map<String, Object>> listForMap(Map<String, Object> params);
}
