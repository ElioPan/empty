package com.ev.mes.service.impl;

import com.ev.mes.vo.FeedingDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.mes.dao.ProductionFeedingAlterationDao;
import com.ev.mes.domain.ProductionFeedingAlterationDO;
import com.ev.mes.service.ProductionFeedingAlterationService;



@Service
public class ProductionFeedingAlterationServiceImpl implements ProductionFeedingAlterationService {
	@Autowired
	private ProductionFeedingAlterationDao productionFeedingAlterationDao;
	
	@Override
	public ProductionFeedingAlterationDO get(Long id){
		return productionFeedingAlterationDao.get(id);
	}
	
	@Override
	public List<ProductionFeedingAlterationDO> list(Map<String, Object> map){
		return productionFeedingAlterationDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return productionFeedingAlterationDao.count(map);
	}
	
	@Override
	public int save(ProductionFeedingAlterationDO productionFeedingAlteration){
		return productionFeedingAlterationDao.save(productionFeedingAlteration);
	}
	
	@Override
	public int update(ProductionFeedingAlterationDO productionFeedingAlteration){
		return productionFeedingAlterationDao.update(productionFeedingAlteration);
	}

	@Override
	public int updateAll(ProductionFeedingAlterationDO productionFeedingAlteration){
		return productionFeedingAlterationDao.updateAll(productionFeedingAlteration);
	}
	
	@Override
	public int remove(Long id){
		return productionFeedingAlterationDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return productionFeedingAlterationDao.batchRemove(ids);
	}

	@Override
	public List<FeedingDetailVO> listForFeedingItem(Map<String, Object> params) {
		return productionFeedingAlterationDao.listForFeedingItem(params);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return productionFeedingAlterationDao.listForMap(params);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return productionFeedingAlterationDao.countForMap(params);
	}

}
