package com.ev.custom.dao;

import com.ev.custom.domain.FacilityLocationDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 库位
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@Mapper
public interface FacilityLocationDao {

	FacilityLocationDO get(Integer id);
	
	List<FacilityLocationDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FacilityLocationDO facilityLocation);
	
	int update(FacilityLocationDO facilityLocation);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
