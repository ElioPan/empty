package com.ev.custom.dao;

import com.ev.custom.domain.PurchasebillItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-17 13:06:48
 */
@Mapper
public interface PurchasebillItemDao {

	List<Map<String,Object>> get(Long bid);
	
	List<PurchasebillItemDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PurchasebillItemDO purchasebillItem);
	
	int update(PurchasebillItemDO purchasebillItem);
	
	int remove(Long bid);
	
	int batchRemove(Long[] bids);
	
	int removeByBid(Long bid);
}
