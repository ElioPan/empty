package com.ev.custom.dao;

import com.ev.custom.domain.MeasurePointDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:20:41
 */
@Mapper
public interface MeasurePointDao {

	MeasurePointDO get(Long id);
	
	List<MeasurePointDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(MeasurePointDO measurePoint);
	
	int update(MeasurePointDO measurePoint);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	int countForMap(Map<String, Object> params);

	int batchEdit(List<MeasurePointDO> parseArray);
	
	List<MeasurePointDO> listGroupByDevice(Map<String,Object> map);

    List<Map<String, Object>> listForDevice(Map<String, Object> params);
}
