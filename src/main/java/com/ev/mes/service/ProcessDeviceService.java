package com.ev.mes.service;

import com.ev.mes.domain.ProcessDeviceDO;

import java.util.List;
import java.util.Map;

/**
 * 工序生产设备
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:04
 */
public interface ProcessDeviceService {
	
	ProcessDeviceDO get(Long id);
	
	List<ProcessDeviceDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessDeviceDO processDevice);
	
	int update(ProcessDeviceDO processDevice);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
