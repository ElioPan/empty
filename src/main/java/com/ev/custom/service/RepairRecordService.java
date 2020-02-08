package com.ev.custom.service;

import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;

import java.util.List;
import java.util.Map;

/**
 * 维修记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
public interface RepairRecordService {
	
	RepairRecordDO get(Long id);
	
	List<RepairRecordDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(RepairRecordDO repairRecord);
	
	int update(RepairRecordDO repairRecord);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<String,Object> addRepairRecord(Long eventId, RepairRecordDO record,String partIdArray,String[] taglocationappearanceImage);

    List<RepairRecordDO> getByEventId(Long id);

//	Map<String,Object> saveRepairRecord(RepairEventDO eventDO, String repairRecords);
	
	List<Map<String,Object>> listForMap(Map<String,Object> map);
	
	Map<String,Object> countForMap(Map<String,Object> map);

	Map<String, Object> recordDetail(Long recordId);

	void saveRecordDetail(RepairRecordDO record, String partIds, String[] taglocationappearanceImage,
			RepairEventDO event);

	/**
	 * 获取设备故障类型故障排名
	 */
	Map<String,Object> getFaultRank();

	/**
	 * 获取设备修复能力排名
	 */
	Map<String, Object> getRepairPower();
}
