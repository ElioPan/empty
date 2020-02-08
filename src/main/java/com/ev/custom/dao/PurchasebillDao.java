package com.ev.custom.dao;

import com.ev.custom.domain.PurchasebillDO;

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
public interface PurchasebillDao {

	Map<String,Object> get(Long id);
	
	List<PurchasebillDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(PurchasebillDO purchasebill);
	
	int update(PurchasebillDO purchasebill);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<Map<String,Object>> listApi(Map<String,Object> map);
	
	int countApi(Map<String,Object> map);
	
	int audit(Long id);
	
	int reverseAudit(Long id);
}
