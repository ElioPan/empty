package com.ev.scm.dao;

import com.ev.scm.domain.CommutationInitializationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 往来初始化
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 10:48:36
 */
@Mapper
public interface CommutationInitializationDao {

	CommutationInitializationDO get(Long id);
	
	List<CommutationInitializationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CommutationInitializationDO commutationInitialization);
	
	int update(CommutationInitializationDO commutationInitialization);

	int updateAll(CommutationInitializationDO commutationInitialization);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
