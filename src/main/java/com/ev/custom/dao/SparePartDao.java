package com.ev.custom.dao;

import com.ev.custom.domain.SparePartDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 备品备件分类表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 15:43:45
 */
@Mapper
public interface SparePartDao {

	SparePartDO get(Long id);
	
	List<SparePartDO> list(Map<String,Object> map);

	List<Map<String,Object>> spareNotAssociated(Map<String, Object> map);

	List<Map<String,Object>> getSpareIdName();

	List<Map<String,Object>> spareWarehouseNameId();
	
	int count(Map<String,Object> map);
	
	int save(SparePartDO sparePart);
	
	int update(SparePartDO sparePart);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
