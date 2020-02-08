package com.ev.scm.service;

import com.ev.scm.domain.SalescontractItemDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-10 10:18:10
 */
public interface SalescontractItemService {
	
	SalescontractItemDO get(Long sid);
	
	List<SalescontractItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalescontractItemDO salescontractItem);
	
	int update(SalescontractItemDO salescontractItem);
	
	int remove(Long sid);
	
	int batchRemove(Long[] sids);
}
