package com.ev.custom.dao;

import com.ev.custom.domain.RepairCheckDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 维修验收表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:36
 */
@Mapper
public interface RepairCheckDao {

	RepairCheckDO get(Long id);
	
	List<RepairCheckDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RepairCheckDO repairCheck);
	
	int update(RepairCheckDO repairCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	RepairCheckDO getByEventId(Long id);

	List<Map<String,Object>>  listForMap(Map<String, Object> param);
}
