package com.ev.custom.dao;

import com.ev.custom.domain.PatrolProjectDetailDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检标准明细表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 15:22:51
 */
@Mapper
public interface PatrolProjectDetailDao {

	PatrolProjectDetailDO get(Long id);
	
	List<PatrolProjectDetailDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PatrolProjectDetailDO patrolProjectDetail);
	
	int update(PatrolProjectDetailDO patrolProjectDetail);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
