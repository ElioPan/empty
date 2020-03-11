package com.ev.mes.dao;

import com.ev.mes.domain.StationDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 工位
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-11 09:25:14
 */
@Mapper
public interface StationDao {

	StationDO get(Long id);
	
	List<StationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StationDO station);
	
	int update(StationDO station);

	int updateAll(StationDO station);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> params);

	int checkSave(StationDO stationDO);
}
