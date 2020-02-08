package com.ev.mes.dao;

import com.ev.mes.domain.MaterialsScrapItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用料报废明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:39
 */
@Mapper
public interface MaterialsScrapItemDao {

	MaterialsScrapItemDO get(Long id);
	
	List<MaterialsScrapItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterialsScrapItemDO materialsScrapItem);
	
	int update(MaterialsScrapItemDO materialsScrapItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> getItemDetails(Map<String, Object> map);

	int batchRemoveByScrapId(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

}
