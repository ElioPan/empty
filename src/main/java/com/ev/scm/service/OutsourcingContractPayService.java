package com.ev.scm.service;

import com.ev.scm.domain.OutsourcingContractPayDO;

import java.util.List;
import java.util.Map;

/**
 * 委外合同付款条件
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
public interface OutsourcingContractPayService {
	
	OutsourcingContractPayDO get(Long id);
	
	List<OutsourcingContractPayDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OutsourcingContractPayDO outsourcingContractPay);
	
	int update(OutsourcingContractPayDO outsourcingContractPay);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
