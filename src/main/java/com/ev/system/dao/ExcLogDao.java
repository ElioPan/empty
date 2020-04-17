package com.ev.system.dao;

import com.ev.system.domain.ExcLogDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author zd
 * @email 911435330@qq.com
 * @date 2020-04-09 14:33:09
 */
@Mapper
public interface ExcLogDao {

	ExcLogDO get(String excRequParam);
	
	List<ExcLogDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ExcLogDO excLog);
	
	int update(ExcLogDO excLog);

	int updateAll(ExcLogDO excLog);
	
	int remove(String exc_requ_param);
	
	int batchRemove(String[] excRequParams);
}
