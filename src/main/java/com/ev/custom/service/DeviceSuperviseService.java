package com.ev.custom.service;

import com.ev.custom.domain.DeviceSuperviseDO;

import java.util.List;
import java.util.Map;

/**
 * 设备安监管理
 *
 * @author ev-wang
 * @email 286600136@qq.com
 * @date 2019-08-12 17:05:50
 */
public interface DeviceSuperviseService {

	DeviceSuperviseDO get(Long id);

	List<DeviceSuperviseDO> list(Map<String, Object> map);
	List<Map<String,Object>> listPageMap(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(DeviceSuperviseDO deviceSupervise);

	int update(DeviceSuperviseDO deviceSupervise);

	int remove(Long id);

	int batchRemove(Long[] ids);
}
