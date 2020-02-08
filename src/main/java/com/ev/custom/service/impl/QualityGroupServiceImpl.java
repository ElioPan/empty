package com.ev.custom.service.impl;

import com.ev.custom.dao.QualityGroupDao;
import com.ev.custom.domain.QualityGroupDO;
import com.ev.custom.service.QualityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QualityGroupServiceImpl implements QualityGroupService {
	@Autowired
	private QualityGroupDao qualityGroupDao;

	@Override
	public QualityGroupDO get(Long id) {
		return qualityGroupDao.get(id);
	}

	@Override
	public List<QualityGroupDO> list(Map<String, Object> map) {
		return qualityGroupDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return qualityGroupDao.count(map);
	}

	@Override
	public int save(QualityGroupDO qualityGroup) {
		return qualityGroupDao.save(qualityGroup);
	}

	@Override
	public int update(QualityGroupDO qualityGroup) {
		return qualityGroupDao.update(qualityGroup);
	}

	@Override
	public int remove(Long id) {
		return qualityGroupDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return qualityGroupDao.batchRemove(ids);
	}

	@Override
	public void deleteList(Long[] groupIds) {
		if (groupIds != null && groupIds.length > 0) {
			for (Long id : groupIds) {
				remove(id);
			}
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return qualityGroupDao.listForMap(map);
	}

}
