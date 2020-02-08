package com.ev.mes.dao;

import com.ev.mes.domain.MaterialsCollectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用料采集
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:17
 */
@Mapper
public interface MaterialsCollectDao {

	MaterialsCollectDO get(Long id);
	
	List<MaterialsCollectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterialsCollectDO materialsCollect);
	
	int update(MaterialsCollectDO materialsCollect);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> getListssDetail(Map<String, Object> map);

	int isQuote(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);
}
