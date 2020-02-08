package com.ev.custom.dao;

import com.ev.custom.domain.DeviceArgDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 设备参数表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-15 12:54:05
 */
@Mapper
public interface DeviceArgDao {

	DeviceArgDO get(Long id);
	
	List<DeviceArgDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(DeviceArgDO deviceArg);
	
	int update(DeviceArgDO deviceArg);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String,Object>> lisForMap(Map<String,Object> map);


	int  countForMap(Map<String,Object> map);
}
