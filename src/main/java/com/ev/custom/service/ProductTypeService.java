package com.ev.custom.service;

import com.ev.custom.domain.ProductTypeDO;
import java.util.List;
import java.util.Map;

/**
 * 商品类型表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 08:48:26
 */
public interface ProductTypeService {
	
	ProductTypeDO get(Long id);
	
	int getByName(ProductTypeDO productType);
	
	List<ProductTypeDO> list(Map<String, Object> map);
	
	List<Map<String,Object>> listForMap(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ProductTypeDO productType);
	
	int update(ProductTypeDO productType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	int countForMap(Map<String, Object> map);

	Map<String,Object> add(ProductTypeDO productTypeDO);
}
