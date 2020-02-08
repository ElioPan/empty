package com.ev.custom.service;

import com.ev.custom.domain.PatrolProjectDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 巡检标准表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
public interface PatrolProjectService {
	
	PatrolProjectDO get(Long id);
	
	List<PatrolProjectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PatrolProjectDO patrolProject);
	
	int update(PatrolProjectDO patrolProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<String,Object> listApi(int pageno, int pagesize,String fuzzySearch);

	/**
	 * 删除巡检标准信息
	 * @param projectId
	 * @return
	 */
	R removeProject(Long projectId);

	/**
	 * 批量删除巡检标准信息
	 * @param projectId
	 * @return
	 */
	R batchRemoveProject(Long[] projectId);
}
