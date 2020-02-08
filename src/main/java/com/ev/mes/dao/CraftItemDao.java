package com.ev.mes.dao;

import com.ev.mes.domain.CraftItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工艺路线 明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:17
 */
@Mapper
public interface CraftItemDao {

	CraftItemDO get(Long id);
	
	List<CraftItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CraftItemDO craftItem);
	
	int update(CraftItemDO craftItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> itemDetalByCraftId(Map<String, Object> map);

	int removeByCraftIds(Long[] craftId);

	int canDeletaByProcessId(Map<String, Object> map);

	List<Map<String, Object>> getItemIds(Map<String, Object> map);

}
