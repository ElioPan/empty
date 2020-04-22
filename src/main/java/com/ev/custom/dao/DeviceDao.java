package com.ev.custom.dao;

import com.ev.custom.domain.DeviceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 设备表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-09 14:35:29
 */
@Mapper
public interface DeviceDao {

	DeviceDO get(Long id);
	
	List<DeviceDO> list(Map<String,Object> map);

	List<Map<String,Object>> childList(Map<String, Object> map);

	List<Map<String,Object>> deviceChildren(Map<String, Object> map);

	List<Map<String,Object>> deviceType();

	List<Map<String,Object>> haveNoChildDevice();

	List<Map<String,Object>> listByDeptId(Map<String, Object> map);

	int  updateParentId(DeviceDO device);
	
	int count(Map<String,Object> map);

	int countParent();
	
	int save(DeviceDO device);
	
	int update(DeviceDO device);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object> oneDeviceDetail(Map<String,Object> map);

	List<Map<String,Object>> advancedQueryLists(Map<String,Object> map);

	int advancedCounts(Map<String,Object> map);

	List<Map<String,Object>> getAllDevices();

	List<DeviceDO>  listOfDeviceDetail(Map<String, Object> map);

	int coumtOflistOfDevice(Map<String, Object> map);

	int deletOfDevices(Map<String,Object> map);

	int countOfoperation();

	int countOfDevice();

	int countOfClosingDown();

	int countOfChildList(Map<String,Object> map);

    List<Map<String, Object>> listForMap(Map<String, Object> param);

    List<String> getAllCode();

    int batchSave(List<DeviceDO> deviceDOs);

    List<String> getAllName();

	int countOfDeviceChildren(Map<String,Object> map);

	List<Map<String, Object>> countOfdeviceStatus();


}
