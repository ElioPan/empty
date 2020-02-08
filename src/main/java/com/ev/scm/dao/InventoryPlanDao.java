package com.ev.scm.dao;

import com.ev.scm.domain.InventoryPlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 盘点主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-19 14:22:10
 */
@Mapper
public interface InventoryPlanDao {

	InventoryPlanDO get(Long id);
	
	List<InventoryPlanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(InventoryPlanDO checkHead);
	
	int update(InventoryPlanDO checkHead);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listByDates(Map<String, Object> map);

	List<Map<String, Object>> getProMsgByHeadId(Map<String, Object> map);

	int countOfStatus(Map<String, Object> map);

	int countOfListByDates(Map<String, Object> map);


}
