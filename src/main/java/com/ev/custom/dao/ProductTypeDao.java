package com.ev.custom.dao;

import com.ev.custom.domain.ProductTypeDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 商品类型表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 08:48:26
 */
@Mapper
public interface ProductTypeDao {

	ProductTypeDO get(Long id);
	
	List<ProductTypeDO> list(Map<String,Object> map);
	
	List<Map<String,Object>> listForMap(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int countForMap(Map<String,Object> map);
	
	int save(ProductTypeDO productType);
	
	int update(ProductTypeDO productType);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	ProductTypeDO getByName(String code);
}
