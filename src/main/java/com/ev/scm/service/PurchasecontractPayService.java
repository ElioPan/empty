package com.ev.scm.service;

import com.ev.scm.domain.PurchasecontractPayDO;

import java.util.List;
import java.util.Map;

/**
 * 采购合同付款条件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:55:10
 */
public interface PurchasecontractPayService {
	
	PurchasecontractPayDO get(Long id);
	
	List<PurchasecontractPayDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchasecontractPayDO purchasecontractPay);
	
	int update(PurchasecontractPayDO purchasecontractPay);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchRemoveByContractId(Long[] ids);

	List<PurchasecontractPayDO> detailOfPay(Map<String, Object> map);

	Map<String, Object> totalOfPay(Map<String, Object> map);

	List<PurchasecontractPayDO> detailOfPayById(Map<String, Object> map);

}
