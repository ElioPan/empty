package com.ev.custom.dao;

import com.ev.custom.domain.RepairEventDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 维修事件表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
@Mapper
public interface RepairEventDao {

	RepairEventDO get(Long id);
	
	List<RepairEventDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RepairEventDO repairEvent);
	
	int update(RepairEventDO repairEvent);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<Map<String, Object>> listForMap(Map<String,Object> map);
	
	int countForMap(Map<String,Object> map);
	
	Map<String, Object> getDetail(Long id);
	    
}
