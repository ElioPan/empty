package com.ev.custom.service;

import com.ev.custom.domain.DeviceArgDO;

import java.util.List;
import java.util.Map;

/**
 * 设备参数表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-15 12:54:05
 */
public interface DeviceArgService {
	
	DeviceArgDO get(Long id);
	
	List<DeviceArgDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DeviceArgDO deviceArg);
	
	int update(DeviceArgDO deviceArg);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object> addDeviceArg(DeviceArgDO deviceArg);

	Map<String,Object> deviceArgList(int pageno, int pagesize, Long deviceId);

	Map<String,Object> delDeviceArg(Long id);

	List<Map<String,Object>> lisForMap(Map<String,Object> map);

}
