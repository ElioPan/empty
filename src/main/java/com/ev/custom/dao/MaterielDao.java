package com.ev.custom.dao;

import com.ev.custom.domain.MaterielDO;
import com.ev.scm.domain.StockDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 物料
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
@Mapper
public interface MaterielDao {

	MaterielDO get(Integer id);
	
	List<MaterielDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterielDO materiel);
	
	int update(MaterielDO materiel);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int countForMap(Map<String, Object> params);

	int checkSave(MaterielDO materiel);
	/************************************/
	
	List<StockDO> stockList(List<Long>stockIds);

	int checkUse(Integer id);
	
	List<Map<String,Object>> stockCount(Map<String,Object>map);
	
	List<Map<String,Object>> stockListForMap(Map<String, Object> map);
	
	int stockCountForMap(Map<String, Object> map);

    List<String> getAllCode();

    void batchSave(List<MaterielDO> materielDOs);
}
