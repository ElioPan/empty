package com.ev.scm.dao;

import com.ev.scm.domain.PurchaseItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 采购申请明细表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 13:12:11
 */
@Mapper
public interface PurchaseItemDao {

	PurchaseItemDO get(Long id);
	
	List<PurchaseItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PurchaseItemDO purchaseItem);
	
	int update(PurchaseItemDO purchaseItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int batchRemoveByPurcahseId(Long[] ids);

	List<Map<String, Object>>detailOfItem(Map<String, Object> map);

	Map<String, Object>aggregate(Map<String, Object> map);

	BigDecimal getInCountOfPurchase(Map<String, Object> map);
}
