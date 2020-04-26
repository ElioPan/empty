package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.OutsourcingContractItemDO;

import java.util.List;
import java.util.Map;

/**
 * 委外合同明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:38
 */
public interface OutsourcingContractItemService {
	
	OutsourcingContractItemDO get(Long id);
	
	List<OutsourcingContractItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OutsourcingContractItemDO outsourcingContractItem);
	
	int update(OutsourcingContractItemDO outsourcingContractItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	R disposeCloseLine(Long[] ids , String closeReason);

}
