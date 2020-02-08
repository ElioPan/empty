package com.ev.scm.service;

import com.ev.scm.domain.SalescontractAlterationDO;

import java.util.List;
import java.util.Map;

/**
 * 销售合同(变更历史)
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 11:18:13
 */
public interface SalescontractAlterationService {
	
	SalescontractAlterationDO get(Long id);
	
	List<SalescontractAlterationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalescontractAlterationDO salescontractAlteration);
	
	int update(SalescontractAlterationDO salescontractAlteration);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
