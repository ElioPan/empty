package com.ev.scm.dao;

import com.ev.scm.domain.SalesbillItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-30 13:24:10
 */
@Mapper
public interface SalesbillItemDao {

    SalesbillItemDO  get(Long id);
	
	List<SalesbillItemDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);
	
	int save(SalesbillItemDO salesbillItem);
	
	int update(SalesbillItemDO salesbillItem);
	
	int remove(Long bid);
	
	int batchRemove(Long[] bids);

    int batchRemoveByBillId(Long[] ids);

    List<Map<String,Object>> listForMap(Map<String, Object> param);

    Map<String,Object> countForMap(Map<String, Object> param);

    BigDecimal getCountBySource(Map<String, Object> map);

	List<Map<String,Object>> countForMapGroupBySourceId(Long id);
}
