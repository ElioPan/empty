package com.ev.custom.dao;

import com.ev.custom.domain.RepairRecordDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 维修记录表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
@Mapper
public interface RepairRecordDao {

	RepairRecordDO get(Long id);
	
	List<RepairRecordDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(RepairRecordDO repairRecord);
	
	int update(RepairRecordDO repairRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<RepairRecordDO> getByEventId(Long id);
	
	List<Map<String,Object>> listForMap(Map<String,Object> map);
	
	Map<String,Object> countForMap(Map<String,Object> map);

	/**
	 * 获取设备的故障类型排名
	 * @return
	 */
	List<Map<String, Object>> getFaultRank();

	/**
	 * 获取设备的修复能力平均修理时间
	 * @return
	 */
	List<Map<String,Object>> getRepairPowerAvgTime();

	/**
	 * 获取设备的修复能力平均间隔时间
	 */
	List<Map<String,Object>> getRepairPowerTaktTime();
}
