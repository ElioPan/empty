package com.ev.scm.dao;

import com.ev.scm.domain.PurchasecontractItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购合同明细表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:54:59
 */
@Mapper
public interface PurchasecontractItemDao {

	PurchasecontractItemDO get(Long id);
	
	List<PurchasecontractItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchasecontractItemDO purchasecontractItem);
	
	int update(PurchasecontractItemDO purchasecontractItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchRemoveByContractId(Long[] ids);

	List<Map<String, Object>> detailOfItem (Map<String, Object> map);

	Map<String, Object> totalOfItem (Map<String, Object> map);


}
