package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.OutsourcingContractDO;
import com.ev.scm.domain.OutsourcingContractItemDO;
import com.ev.scm.domain.OutsourcingContractPayDO;
import com.ev.scm.vo.ContractItemVO;
import com.ev.scm.vo.ContractPayVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 委外合同
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:38
 */
public interface OutsourcingContractService {
	
	OutsourcingContractDO get(Long id);
	
	List<OutsourcingContractDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OutsourcingContractDO outsourcingContract);
	
	int update(OutsourcingContractDO outsourcingContract);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    R addOrUpdateOutsourcingContract(OutsourcingContractDO outsourcingContract, String bodyItem, String bodyPay, Long[] itemIds, Long[] payIds);

    R removeOutsourcingContract(Long[] outsourcingContractIds);

    R editOutsourcingContract( OutsourcingContractDO outsourcingContract, String bodyItem, String bodyPay, Long[] payIds);

    List<ContractPayVO> getContractPayVOS(Long outsourcingContractId,String bodyPay, Long[] payIds, List<OutsourcingContractPayDO> outsourcingContractPayList);

    List<ContractItemVO> getContractItemVOS(String bodyItem, List<OutsourcingContractItemDO> outsourcingContractItemList);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    Map<String, Object> countForMap(Map<String, Object> map);

    R audit(Long id);

    R reverseAudit(Long id);

    R close(Long id);

    R reverseClose(Long id);

    R getDetail(Long outsourcingContractId);

    R getAlterationDetail(Long id);

    BigDecimal getCountBySource(Map<String, Object> map);

    R checkSourceNumber(String bodyItem,Long id);
}
