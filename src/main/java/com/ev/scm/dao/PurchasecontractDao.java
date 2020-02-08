package com.ev.scm.dao;

import com.ev.scm.domain.PurchasecontractDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购合同主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:54:44
 */
@Mapper
public interface PurchasecontractDao {

	PurchasecontractDO get(Long id);
	
	List<PurchasecontractDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchasecontractDO purchasecontract);
	
	int update(PurchasecontractDO purchasecontract);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);

	Map<String, Object> detailOfContract(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

}
