package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchasecontractDO;

import java.util.List;
import java.util.Map;

/**
 * 采购合同主表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:54:44
 */
public interface PurchasecontractService {

	PurchasecontractDO get(Long id);

	List<PurchasecontractDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(PurchasecontractDO purchasecontract);

	int update(PurchasecontractDO purchasecontract);

	int remove(Long id);

	int batchRemove(Long[] ids);

	R addOrChangePurchasecontract(PurchasecontractDO purchasecontractDO, String bodyItem, String bodyPay, Long[]itemIds, Long[]payIds);

	R audit(Long id);

	R disAudit(Long id);

	R close(Long id);

	R disClose(Long id);

	R removeContract(Long[] contractIds);

	R getDetailOfContract(Long id);

	R editPurchaseContract(Long purchaseContractId, String bodyItem, String bodyPay, Long[] payIds);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	R alterationList(Map<String, Object> map);

	R getAlterationDetail(Long id);

	int wetherChangeContract(Map<String, Object> map);

}
