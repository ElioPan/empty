package com.ev.mes.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ev.mes.domain.BomDO;

/**
 * BOM主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-19 08:45:05
 */
@Mapper
public interface BomDao {

	BomDO get(Long id);

	List<BomDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(BomDO bom);

	int update(BomDO bom);

	int remove(Long id);

	int batchRemove(Long[] ids);

	Map<String, Object> getDetail(Long id);
	
	List<Map<String, Object>>listForMap(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
}
