package com.ev.mes.dao;

import com.ev.mes.domain.CraftDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工艺路线
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:00
 */
@Mapper
public interface CraftDao {

	CraftDO get(Long id);
	
	List<CraftDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CraftDO craft);
	
	int update(CraftDO craft);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	int canDelet(Map<String, Object> map);

	int isQuote(Map<String, Object> map);

	Map<String, Object> getOneCraftDetail(Map<String, Object> map);


	List<Map<String, Object>>listBodyForMap(Map<String, Object> map);

	int countListBodyForMap(Map<String, Object> map);
}
