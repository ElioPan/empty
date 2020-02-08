package com.ev.custom.dao;

import com.ev.custom.domain.PatrolProjectDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检标准表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@Mapper
public interface PatrolProjectDao {

	PatrolProjectDO get(Long id);
	
	List<PatrolProjectDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PatrolProjectDO patrolProject);
	
	int update(PatrolProjectDO patrolProject);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    PatrolProjectDO getByCode(String code);

	PatrolProjectDO getByName(String code);

	/**
	 * 批量删除巡检项目标准（逻辑删除）
	 * @return
	 */
	int batchRemoveProject(Long[] ids);
}
