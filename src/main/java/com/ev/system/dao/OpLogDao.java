package com.ev.system.dao;

import com.ev.system.domain.OpLogDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志表
 * @author zd
 * @email 911435330@qq.com
 * @date 2020-04-09 14:33:09
 */
@Mapper
public interface OpLogDao {

	OpLogDO get(String opModule);
	
	List<OpLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OpLogDO opLog);
	
	int update(OpLogDO opLog);

	int updateAll(OpLogDO opLog);
	
	int remove(String op_module);
	
	int batchRemove(String[] opModules);
}
