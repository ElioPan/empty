package com.ev.mes.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev.mes.dao.ProductionPlanAlterationDao;
import com.ev.mes.domain.ProductionPlanAlterationDO;
import com.ev.mes.service.ProductionPlanAlterationService;

@Service
public class ProductionPlanAlterationServiceImpl implements ProductionPlanAlterationService {
	@Autowired
	private ProductionPlanAlterationDao productionPlanAlterationDao;

	@Override
	public ProductionPlanAlterationDO get(Long id) {
		return productionPlanAlterationDao.get(id);
	}

	@Override
	public List<ProductionPlanAlterationDO> list(Map<String, Object> map) {
		return productionPlanAlterationDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return productionPlanAlterationDao.count(map);
	}

	@Override
	public int save(ProductionPlanAlterationDO productionPlanAlteration) {
		return productionPlanAlterationDao.save(productionPlanAlteration);
	}

	@Override
	public int update(ProductionPlanAlterationDO productionPlanAlteration) {
		return productionPlanAlterationDao.update(productionPlanAlteration);
	}

	@Override
	public int remove(Long id) {
		return productionPlanAlterationDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return productionPlanAlterationDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return productionPlanAlterationDao.listForMap(params);
	}

	@Override
	public int countForMap(Map<String, Object> params) {
		return productionPlanAlterationDao.countForMap(params);
	}

	@Override
	public void removeByPlanId(Long id) {
		productionPlanAlterationDao.removeByPlanId(id);
		
	}

	@Override
	public void batchRemoveByPlanId(Long[] ids) {
		productionPlanAlterationDao.batchRemoveByPlanId(ids);
		
	}

}
