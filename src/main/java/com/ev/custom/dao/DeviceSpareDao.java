package com.ev.custom.dao;

import com.ev.custom.domain.DeviceSpareDO;
import com.ev.scm.domain.StockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备-备件中间表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-15 08:55:07
 */
@Mapper
public interface DeviceSpareDao {

	DeviceSpareDO get(Long id);
	
	List<DeviceSpareDO> list(Map<String, Object> map);

	List<Map<String, Object>> getRelatedSpareParts(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	int countSparesNoDevice();
	
	int save(DeviceSpareDO deviceSpare);
	
	int update(DeviceSpareDO deviceSpare);
	
	int remove(Long id);

	public int removeDeviceSpare(@Param(value = "list")List<Long> list ,@Param(value = "id")Long id );

	int batchRemove(Long[] ids);

	int countRelatedSpareParts(Map<String, Object> map);

	List<StockDO> getSpartPartsCount(Long[] ids);


}
