package com.ev.mes.service;

import com.ev.mes.domain.ProcessCheckDO;

import java.util.List;
import java.util.Map;

/**
 * 工序检验项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 10:17:22
 */
public interface ProcessCheckService {
	
	ProcessCheckDO get(Long id);
	
	List<ProcessCheckDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessCheckDO processCheck);
	
	int update(ProcessCheckDO processCheck);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> getDetailByProcessId(Map<String, Object> map);

	int removeBacthById(Map<String, Object> map);

	int removeBacthByforeignId(Map<String, Object> map);

}
