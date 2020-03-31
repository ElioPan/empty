package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.domain.RepairRecordDO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 维修事件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
public interface RepairEventService {

	RepairEventDO get(Long id);
	
	Map<String, Object> getDetail(Long eventId);
	
	List<RepairEventDO> list(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	List<Map<String, Object>> getRecordInfoByEventId(Long eventId);

	List<Map<String, Object>> getRepairCheckInfo(Long eventId);

	Map<String, Object> getRecordDetailByEventId(Long eventId);

	int countForMap(Map<String, Object> params);

	int count(Map<String, Object> map);

	Map<String, Object> getCountBacklog(Long userId, Long deptId);

	int save(RepairEventDO repairEvent);

	int saveEvengts(Long eventId, Long[] ids);

	Map<String, Object> addRepairEvent(RepairEventDO event);

	int update(RepairEventDO repairEvent);

	int remove(Long id);

	int batchRemove(Long[] ids);

	void removeSatellite(Long[] ids, Integer[] assocTypes, String imageType);

	void removeRecordSatellite(Long recordId);

	void saveEventSatellite(String[] taglocationappearanceImage, Long[] carbonCopyRecipients, Long eventId);

	void removeAndSaveEventSatellite(String[] taglocationappearanceImage, Long[] carbonCopyRecipients, Long eventId,
			Long[] eventIds);

	void removeAndSaveRecordSatellite(RepairEventDO event, RepairRecordDO record, String partIdArray,
			String[] taglocationappearanceRecordImage, Long recordId);

	R saveRepairInfo(RepairEventDO event, String[] taglocationappearanceImage, Long[] carbonCopyRecipients) throws IOException, ParseException;

	R saveProactiveRepairInfo(RepairEventDO event, RepairRecordDO record, String[] taglocationappearanceEventImage,
			Long[] carbonCopyRecipients, String partIdArray, String[] taglocationappearanceRecordImage) throws IOException, ParseException;

	R saveRepairInfo(Long eventId, String partIdArray, String[] taglocationappearanceImage, RepairRecordDO record,
			RepairEventDO repairEventDO);

	boolean nonWaitingDeal(Integer status);

	boolean nonTS(Integer status);

	boolean nonWaitingCheck(Integer status);

	Map<String, Object> params(Map<String, Object> params);

	/**
	 * 保存维修记录单
	 * @param eventId
	 * @param partIdArray
	 * @param taglocationappearanceImage
	 * @param record
	 * @param repairEventDO
	 * @return
	 */
	R saveRepairRecord(Long eventId, String partIdArray, String[] taglocationappearanceImage, RepairRecordDO record,
			RepairEventDO repairEventDO);

}
