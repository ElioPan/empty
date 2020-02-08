package com.ev.scm.dao;

import com.ev.scm.domain.ContractAlterationDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 合同(变更历史)
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-11 14:21:06
 */
@Mapper
public interface ContractAlterationDao {

	ContractAlterationDO get(Long id);
	
	List<ContractAlterationDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ContractAlterationDO contractAlteration);
	
	int update(ContractAlterationDO contractAlteration);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    int countForMap(Map<String, Object> map);
}
