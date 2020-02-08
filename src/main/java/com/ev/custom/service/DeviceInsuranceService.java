package com.ev.custom.service;

import com.ev.custom.domain.DeviceInsuranceDO;

import java.util.List;
import java.util.Map;

/**
 * 设备保险管理
 * 
 * @author ev-wang
 * @email 286600136@qq.com
 * @date 2019-08-12 08:57:12
 */
public interface DeviceInsuranceService {
	
	DeviceInsuranceDO get(Long id);
	
	List<DeviceInsuranceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DeviceInsuranceDO deviceInsurance);
	
	int update(DeviceInsuranceDO deviceInsurance);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	Map<String,Object> getInturanDetail(Long deviceId);

	List<Map<String,Object>> listPageMap(Map<String, Object> map);

}
