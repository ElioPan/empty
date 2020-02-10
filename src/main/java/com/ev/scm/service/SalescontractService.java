package com.ev.scm.service;

import com.ev.scm.domain.SalescontractDO;
import com.ev.framework.utils.R;
import com.ev.scm.domain.SalescontractItemDO;
import com.ev.scm.domain.SalescontractPayDO;
import com.ev.scm.vo.ContractItemVO;
import com.ev.scm.vo.ContractPayVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-10 10:18:09
 */
public interface SalescontractService {
	
	SalescontractDO get(Long id);
	
	List<SalescontractDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalescontractDO salescontract);
	
	int update(SalescontractDO salescontract);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	R addOrUpdateSalesContract(SalescontractDO salesContract, String bodyItem, String bodyPay, Long[] itemIds, Long[] payIds);

	R removeSalesContract(Long[] salesContractIds);

	R audit(Long id);
	
	R reverseAudit(Long id);
	
	R getDetail(Long salesContractId);

    R editSalesContract(Long salesContractId, String bodyItem, String bodyPay, Long[] payIds);

    R close(Long id);

    R reverseClose(Long id);

    R getAlterationDetail(Long id);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    Map<String, Object> countForMap(Map<String, Object> map);

    List<ContractPayVO> getContractPayVOS(String bodyPay, Long[] payIds, List<SalescontractPayDO> salesContractPayList);

    List<ContractItemVO> getContractItemVOS(String bodyItem, List<SalescontractItemDO> salesContractItemList);
}
