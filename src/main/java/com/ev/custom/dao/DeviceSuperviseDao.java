package com.ev.custom.dao;

import com.ev.custom.domain.DeviceSuperviseDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 设备安监管理
 * @author ev-wang
 * @email 286600136@qq.com
 * @date 2019-08-12 17:05:50
 */
@Mapper
public interface DeviceSuperviseDao {

	DeviceSuperviseDO get(Long id);

	List<DeviceSuperviseDO> list(Map<String,Object> map);
	List<Map<String,Object>> listPageMap(Map<String, Object> map);

	int count(Map<String,Object> map);

	int save(DeviceSuperviseDO deviceSupervise);

	int update(DeviceSuperviseDO deviceSupervise);

	int remove(Long id);

	int batchRemove(Long[] ids);
}
