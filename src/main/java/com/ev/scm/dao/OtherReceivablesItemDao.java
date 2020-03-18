package com.ev.scm.dao;

import com.ev.scm.domain.OtherReceivablesItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 其他应收应付明细
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 16:27:57
 */
@Mapper
public interface OtherReceivablesItemDao {

	OtherReceivablesItemDO get(Long id);
	
	List<OtherReceivablesItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OtherReceivablesItemDO otherReceivablesItem);
	
	int update(OtherReceivablesItemDO otherReceivablesItem);

	int updateAll(OtherReceivablesItemDO otherReceivablesItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
