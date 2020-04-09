package com.ev.custom.service;

import com.ev.custom.domain.UpkeepProjectDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 保养项目表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-12 16:53:16
 */
public interface UpkeepProjectService {
	
	UpkeepProjectDO get(Long id);
	
	List<UpkeepProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UpkeepProjectDO upkeepProject);
	
	int update(UpkeepProjectDO upkeepProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<UpkeepProjectDO> objectList();

    List<UpkeepProjectDO> getByProjectIds(Long[] projectIds);

	List<Map<String,Object>> findListsPros(Map<String,Object> map);

	int countOfList(Map<String,Object> map);

    R delete(Long[] ids);

	int countOfCode(Map<String,Object> map);

	R addProject(UpkeepProjectDO upkeepProject);
}
