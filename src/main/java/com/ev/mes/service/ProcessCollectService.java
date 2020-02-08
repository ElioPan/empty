package com.ev.mes.service;

import com.ev.mes.domain.ProcessCollectDO;

import java.util.List;
import java.util.Map;

/**
 * 工序采集项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:46:52
 */
public interface ProcessCollectService {
	
	ProcessCollectDO get(Long id);
	
	List<ProcessCollectDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessCollectDO processCollect);
	
	int update(ProcessCollectDO processCollect);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
