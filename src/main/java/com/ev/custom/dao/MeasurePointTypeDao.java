package com.ev.custom.dao;

import com.ev.custom.domain.MeasurePointTypeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:21:32
 */
@Mapper
public interface MeasurePointTypeDao {

	MeasurePointTypeDO get(Integer id);
	
	List<MeasurePointTypeDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(MeasurePointTypeDO measurePointType);
	
	int update(MeasurePointTypeDO measurePointType);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	MeasurePointTypeDO getByName(String childTypeName);

	/**
	 * @param param
	 * @return
	 */
	List<MeasurePointTypeDO> listByDeviceId(Map<String, Object> param);
}
