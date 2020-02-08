package com.ev.custom.dao;

import com.ev.custom.domain.PatrolCheckDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检验收表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-30 16:39:24
 */
@Mapper
public interface PatrolCheckDao {

	PatrolCheckDO get(Long id);
	
	List<PatrolCheckDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PatrolCheckDO patrolCheck);
	
	int update(PatrolCheckDO patrolCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
