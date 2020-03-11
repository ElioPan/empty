package com.ev.custom.service;

import com.ev.custom.domain.MaterielDO;
import com.ev.framework.utils.R;
import com.ev.scm.domain.StockDO;

import java.util.List;
import java.util.Map;

/**
 * 物料
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
public interface MaterielService {
	
	MaterielDO get(Integer id);
	
	List<MaterielDO> list(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MaterielDO materiel);
	
	int update(MaterielDO materiel);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int countForMap(Map<String, Object> params);

	Map<String, Object> getDetail(Integer id);

	int checkSave(MaterielDO materiel);

	/**
	 * 获取库存列表
	 */
	List<Map<String, Object>> stockListForMap(Map<String, Object> map);

	/**
	 *  获取库存数量
	 */
	int stockCountForMap(Map<String, Object> map);

	/**
	 * 获取库存产品列表
	 */
	List<StockDO> stockList(List<Long> stockIds);

	/**
	 * 验证能否删除
	 */
	int checkUse(Integer id);

	/**
	 * 获取库存数量
	 */
	List<Map<String, Object>> stockCount(Map<String, Object> map);

	/**
	 */
	R logicRemove(Integer id);

	/**
	 */
	R logicBatchRemove(Integer[] ids);

    int audit(Integer id);

    int reverseAudit(Integer id);

    List<String> getAllCode();

    void batchSave(List<MaterielDO> materielDOs);
}
