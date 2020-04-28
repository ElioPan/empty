package com.ev.custom.dao;

import com.ev.custom.domain.FacilityDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@Mapper
public interface FacilityDao {

	FacilityDO get(Long id);
	
	List<FacilityDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FacilityDO facility);
	
	int update(FacilityDO facility);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    int countForMap(Map<String, Object> params);
}
