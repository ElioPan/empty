package com.ev.mes.dao;

import com.ev.mes.domain.MaterialsScrapDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用料报废单
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:28
 */
@Mapper
public interface MaterialsScrapDao {

	MaterialsScrapDO get(Long id);
	
	List<MaterialsScrapDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterialsScrapDO materialsScrap);
	
	int update(MaterialsScrapDO materialsScrap);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDelet(Map<String, Object> map);

	Map<String, Object> getOneDetail(Map<String, Object> map);
}
