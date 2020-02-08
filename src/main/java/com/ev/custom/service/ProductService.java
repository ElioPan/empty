package com.ev.custom.service;

import com.ev.custom.domain.ProductDO;
import com.ev.scm.domain.StockDO;

import java.util.List;
import java.util.Map;

/**
 * 产品表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 08:49:45
 */
public interface ProductService {
	
	ProductDO get(Long id);
	
	List<ProductDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProductDO product);
	
	int update(ProductDO product);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int countForMap(Map<String, Object> map);
	
	List<Map<String,Object>> stockListForMap(Map<String, Object> map);
	
	int stockCountForMap(Map<String, Object> map);
	
	int getBySerialno(ProductDO product);
	
	List<StockDO> stockList(List<Long>stockIds);

	int checkDelete(Long productId);

	Map<String, Object> add(ProductDO product);
	
	List<Map<String,Object>> stockCount(Map<String,Object>map);
}
