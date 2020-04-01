package com.ev.custom.service;

import com.ev.custom.domain.DeviceSpareDO;
import com.ev.scm.domain.StockDO;

import java.util.List;
import java.util.Map;

/**
 * 设备-备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-15 08:55:07
 */
public interface DeviceSpareService {
	
	DeviceSpareDO get(Long id);
	
	List<DeviceSpareDO> list(Map<String, Object> map);


	List<Map<String, Object>> RelatedSpareParts (Map<String, Object> map);

	int count(Map<String, Object> map);

	int countSparesNoDevice();
	
	int save(DeviceSpareDO deviceSpare);

	int saveSparesPart(Long[] spreID,Long deviceID,Integer warehouseId);

	int  removeSpareChilden(Long[]ids,Long deviceId);
	
	int update(DeviceSpareDO deviceSpare);

	int countRelatedSpareParts(Map<String, Object> map);
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<StockDO> getSpartPartsCount(Long[] ids);

}
