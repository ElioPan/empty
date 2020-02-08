package com.ev.scm.dao;


import java.util.List;
import java.util.Map;

import com.ev.scm.domain.SalescontractAlterationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售合同(变更历史)
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 11:18:13
 */
@Mapper
public interface SalescontractAlterationDao {

	SalescontractAlterationDO get(Long id);
	
	List<SalescontractAlterationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalescontractAlterationDO salescontractAlteration);
	
	int update(SalescontractAlterationDO salescontractAlteration);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
