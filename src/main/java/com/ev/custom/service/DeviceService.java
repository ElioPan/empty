package com.ev.custom.service;

import com.ev.framework.utils.R;
import com.ev.custom.domain.DeviceDO;

import java.util.List;
import java.util.Map;

/**
 * 设备表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-09 14:35:29
 */
public interface DeviceService {
	
	DeviceDO get(Long id);
	
	List<DeviceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	int parentCount();
	
	int save(DeviceDO device);
	
	int update(DeviceDO device);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    R apiDelete(Long[] deviceId);

    Map<String,Object> getDeviceQRCode(Long deviceId);

	Map<String,Object> listApi(int pageno,int pagesize,String name);

	List<Map<String,Object>> childListDev(Map<String, Object> map);

	List<Map<String,Object>> deviceHaveNoChildDevice();

	List<Map<String,Object>> childrenDevice(Map<String, Object> map);

	int  updateParentId(Long id,Long[] ids);

	int  desvinculao(Long[] ids);

	List<Map<String,Object>> deviceTypeIdName();

    Map<String,Object> getDeviceDetail(Long deviceId);

    Map<String,Object> getDeviceMap();

    Map<String,Object> getDevicesByUser(Long userId);

	Map<String,Object> oneDeviceDetail(Map<String,Object> map);

	List<Map<String,Object>> advancedQueryLists(Map<String,Object> map);

	int advancedCounts(Map<String,Object> map);

	List<Map<String,Object>> getAllDevices();

	List<DeviceDO>  listOfDeviceDetail(Map<String, Object> map);

	R countOfAllDevice();

	int countOfChildList(Map<String,Object> map);


    List<Map<String, Object>> listForMap(Map<String, Object> param);

    List<String> getAllCode();

    void batchSave(List<DeviceDO> deviceDOs);
}
