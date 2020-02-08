package com.ev.mes.dao;

import com.ev.mes.domain.ProcessDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工序配置
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:45:29
 */
@Mapper
public interface ProcessDao {

	ProcessDO get(Long id);
	
	List<ProcessDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProcessDO process);
	
	int update(ProcessDO process);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countListForMap(Map<String, Object> map);

	int deletOfProcess(Map<String, Object> map);



}
