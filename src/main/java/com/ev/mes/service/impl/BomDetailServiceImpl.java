package com.ev.mes.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev.mes.dao.BomDetailDao;
import com.ev.mes.domain.BomDetailDO;
import com.ev.mes.service.BomDetailService;

@Service
public class BomDetailServiceImpl implements BomDetailService {
	@Autowired
	private BomDetailDao bomDetailDao;

	@Override
	public BomDetailDO get(Long id) {
		return bomDetailDao.get(id);
	}

	@Override
	public List<BomDetailDO> list(Map<String, Object> map) {
		return bomDetailDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return bomDetailDao.count(map);
	}

	@Override
	public int save(BomDetailDO bomDetail) {
		return bomDetailDao.save(bomDetail);
	}

	@Override
	public int update(BomDetailDO bomDetail) {
		return bomDetailDao.update(bomDetail);
	}

	@Override
	public int remove(Long id) {
		return bomDetailDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return bomDetailDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> param) {
		return bomDetailDao.listForMap(param);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return bomDetailDao.countForMap(map);
	}

	@Override
	public int removeByHeadId(Long id) {
		return bomDetailDao.removeByHeadId(id);
	}

}
