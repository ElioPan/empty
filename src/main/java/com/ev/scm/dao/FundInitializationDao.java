package com.ev.scm.dao;

import com.ev.scm.domain.FundInitializationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 资金初始子表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 09:55:59
 */
@Mapper
public interface FundInitializationDao {

	FundInitializationDO get(Integer id);
	
	List<FundInitializationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(FundInitializationDO fundInitialization);
	
	int update(FundInitializationDO fundInitialization);

	int updateAll(FundInitializationDO fundInitialization);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);
}
