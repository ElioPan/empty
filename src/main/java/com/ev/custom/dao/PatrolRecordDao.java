package com.ev.custom.dao;

import com.ev.custom.domain.PatrolRecordDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 巡检记录表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@Mapper
public interface PatrolRecordDao {

	PatrolRecordDO get(Long id);
	
	List<PatrolRecordDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PatrolRecordDO patrolRecord);
	
	int update(PatrolRecordDO patrolRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    PatrolRecordDO getByPlanId(Long id);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

	Map<String, Object> detail(Long id);

	/**
	 * 批量关闭巡检单
	 * @param param
	 * @return
	 */
	int batchUpdate(Map<Object, Object> param);

}
