package com.ev.custom.service.impl;

import com.ev.custom.dao.ProductDao;
import com.ev.custom.domain.ProductDO;
import com.ev.custom.service.ProductService;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.StockDO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	
	@Override
	public ProductDO get(Long id){
		return productDao.get(id);
	}
	
	@Override
	public List<ProductDO> list(Map<String, Object> map){
		return productDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return productDao.count(map);
	}
	
	@Override
	public int save(ProductDO product){
		int code=getBySerialno(product);
		return code>0?productDao.save(product):code;
	}
	
	@Override
	public int update(ProductDO product){
		if (StringUtils.isNoneBlank(product.getSerialno())) {
			ProductDO productDO=get(product.getId());
			if (productDO.getSerialno().equals(product.getSerialno())) {
				return productDao.update(product);
			}
			if (getBySerialno(product)<0) {
				return -1;
			}
		}
		return productDao.update(product);
	}
	
	@Override
	public int remove(Long id){
		return productDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return productDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return productDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return productDao.countForMap(map);
	}

	@Override
	public int getBySerialno(ProductDO product) {
		ProductDO serialno = this.productDao.getBySerialno(product.getSerialno());
		if(serialno!=null){
			return -1;
		}
		return 1;
	}

	@Override
	public List<Map<String, Object>> stockListForMap(Map<String, Object> map) {
		return productDao.stockListForMap(map);
	}

	@Override
	public int stockCountForMap(Map<String, Object> map) {
		return productDao.stockCountForMap(map);
	}

	@Override
	public List<StockDO> stockList(List<Long> stockIds) {
		return productDao.stockList(stockIds);
	}

	@Override
	public int checkDelete(Long productId) {
		return productDao.checkDelete(productId);
	}
	
	@Override
	public Map<String,Object> add(ProductDO product){
		Map<String, Object> result = Maps.newHashMap();
		if (getBySerialno(product)<0) {
			result.put("error", "该编号已存在");
			return result;
		}
		if (productDao.save(product)>0) {
			result.put("id", product.getId());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> stockCount(Map<String, Object> map) {
		return productDao.stockCount(map);
	}
	
}
