package com.ev.scm.dao;

import com.ev.scm.domain.PurchaseDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 采购申请表主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 13:11:44
 */
@Mapper
public interface PurchaseDao {

	PurchaseDO get(Long id);
	
	List<PurchaseDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseDO purchase);
	
	int update(PurchaseDO purchase);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int canDeletOfCount(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	Map<String, Object> detailOfPurchase(Map<String, Object> map);


}
