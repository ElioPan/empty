package com.ev.mes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.ProductionFeedingDetailDao;
import com.ev.mes.domain.ProductionFeedingDetailDO;
import com.ev.mes.service.ProductionFeedingDetailService;



@Service
public class ProductionFeedingDetailServiceImpl implements ProductionFeedingDetailService {
	@Autowired
	private ProductionFeedingDetailDao productionFeedingDetailDao;
	
	@Override
	public ProductionFeedingDetailDO get(Long id){
		return productionFeedingDetailDao.get(id);
	}
	
	@Override
	public List<ProductionFeedingDetailDO> list(Map<String, Object> map){
		return productionFeedingDetailDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return productionFeedingDetailDao.count(map);
	}
	
	@Override
	public int save(ProductionFeedingDetailDO productionFeedingDetail){
		return productionFeedingDetailDao.save(productionFeedingDetail);
	}
	
	@Override
	public int update(ProductionFeedingDetailDO productionFeedingDetail){
		return productionFeedingDetailDao.update(productionFeedingDetail);
	}
	
	@Override
	public int remove(Long id){
		return productionFeedingDetailDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return productionFeedingDetailDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return productionFeedingDetailDao.listForMap(params);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return productionFeedingDetailDao.countForMap(params);
	}

	@Override
	public void removeByHeadId(Long id) {
		productionFeedingDetailDao.removeByHeadId(id);
	}

	@Override
	public String getCountByOutsourcing(Map<String, Object> params) {
		return productionFeedingDetailDao.getCountByOutsourcing(params);
	}

}
