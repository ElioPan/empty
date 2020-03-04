package com.ev.scm.dao;

import com.ev.scm.domain.StockInDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 产品入库明细表主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:37:49
 */
@Mapper
public interface StockInDao {

	StockInDO get(Long id);
	
	List<StockInDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(StockInDO stockInDO);
	
	int update(StockInDO stockInDO);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	List<Map<String,Object>> getlistByIndata(Map<String, Object> map);


	List<Map<String,Object>> getHeadDetailById(Map<String, Object> map);

	int countOfList(Map<String, Object> map);

	int canDeletOfCount(Map<String, Object> map);

	Map<String, Object> countForMap(Map<String, Object> map);

	List<Map<String, Object>> listForMap(Map<String, Object> map);

	Map<String, Object> deatilOfhead(Map<String, Object> map);

	int wetherHaveQrSign(Map<String, Object> map);


	List<Map<String, Object>> getStockInDate(Map<String, Object> map);


	List<Map<String, Object>> listForHead(Map<String, Object> map);

	int countForHead(Map<String, Object> map);



}
