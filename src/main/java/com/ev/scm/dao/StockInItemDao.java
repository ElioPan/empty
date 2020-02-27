package com.ev.scm.dao;


import com.ev.scm.domain.StockInItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 产品入库明细表子表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:37:59
 */
@Mapper
public interface StockInItemDao {

	StockInItemDO get(Long id );
	
	List<StockInItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockInItemDO propurchaseInbody);
	
	int update(StockInItemDO propurchaseInbody);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String, Object>> getProByHeadId(Long id);

	int removeByInHeadId(Long[] id);

	
	//int removeByMap(Map<String, Object> map);

	List<Map<String, Object>> getBodyDetalByHeadId(Map<String, Object> map);

	int countOfList(Map<String, Object> map);

	List<Map<String, Object>> getlistForMap(Map<String, Object> map);

	List<Map<String, Object>> deatilOfBody(Map<String, Object> map);

	int updateExpense(Map<String, Object> map);

	List<Map<String, Object>> getListBystockInId(Map<String, Object> map);

	//totailAmount,totailCount
	Map<String, Object> getTotailCountAmount(Long id);

	int batchUpdate(List<StockInItemDO> stockInItemDOs);

	int batchSave(List<StockInItemDO> stockDOs);

	List<Map<String, Object>> getItemDetailById(Map<String, Object> map);

}
