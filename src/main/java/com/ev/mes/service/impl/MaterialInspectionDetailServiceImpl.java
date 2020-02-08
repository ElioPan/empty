package com.ev.mes.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev.mes.dao.MaterialInspectionDetailDao;
import com.ev.mes.domain.MaterialInspectionDetailDO;
import com.ev.mes.service.MaterialInspectionDetailService;

@Service
public class MaterialInspectionDetailServiceImpl implements MaterialInspectionDetailService {
	@Autowired
	private MaterialInspectionDetailDao materialInspectionDetailDao;

	@Override
	public MaterialInspectionDetailDO get(Long id) {
		return materialInspectionDetailDao.get(id);
	}

	@Override
	public List<MaterialInspectionDetailDO> list(Map<String, Object> map) {
		return materialInspectionDetailDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return materialInspectionDetailDao.count(map);
	}

	@Override
	public int save(MaterialInspectionDetailDO materialInspectionDetail) {
		return materialInspectionDetailDao.save(materialInspectionDetail);
	}

	@Override
	public int update(MaterialInspectionDetailDO materialInspectionDetail) {
		return materialInspectionDetailDao.update(materialInspectionDetail);
	}

	@Override
	public int remove(Long id) {
		return materialInspectionDetailDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return materialInspectionDetailDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return materialInspectionDetailDao.listForMap(params);
	}

	@Override
	public int removeByHeadId(Long id) {
		return materialInspectionDetailDao.removeByHeadId(id);
	}

}
