package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.framework.utils.StringUtils;
import com.ev.custom.dao.ProductTypeDao;
import com.ev.custom.domain.ProductTypeDO;
import com.ev.custom.service.ProductTypeService;
import com.google.common.collect.Maps;



@Service
public class ProductTypeServiceImpl implements ProductTypeService {
	@Autowired
	private ProductTypeDao productTypeDao;
	
	@Override
	public ProductTypeDO get(Long id){
		return productTypeDao.get(id);
	}
	
	@Override
	public List<ProductTypeDO> list(Map<String, Object> map){
		return productTypeDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return productTypeDao.count(map);
	}
	
	@Override
	public int save(ProductTypeDO productType){
		return productTypeDao.save(productType);
	}
	
	@Override
	public int update(ProductTypeDO productType){
		if (StringUtils.isNoneBlank(productType.getName())) {
			ProductTypeDO productTypeDO = productTypeDao.get(productType.getId());
			if (productTypeDO.getName().equals(productType.getName())) {
				return productTypeDao.update(productType);
			}
			if (getByName(productType)<0) {
				return -1;
			}
		}
		return productTypeDao.update(productType);
	}
	
	@Override
	public int remove(Long id){
		return productTypeDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return productTypeDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return productTypeDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return productTypeDao.countForMap(map);
	}

	@Override
	public int getByName(ProductTypeDO productType) {
		ProductTypeDO typeName = this.productTypeDao.getByName(productType.getName());
		if(typeName!=null){
			return -1;
		}
		return 1;
	}
	
	@Override
	public Map<String,Object> add(ProductTypeDO productType) {
		Map<String,Object> result = Maps.newHashMap();
		if (getByName(productType)<0) {
			result.put("error", "该类型已存在不能保存");
			return result;
		}
		int save = productTypeDao.save(productType);
		if (save>0) {
			result.put("ok", productType.getId());
		}
		return result;
	}
	
}
