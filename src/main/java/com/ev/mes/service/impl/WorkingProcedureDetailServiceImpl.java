package com.ev.mes.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ev.mes.dao.WorkingProcedureDetailDao;
import com.ev.mes.domain.WorkingProcedureDetailDO;
import com.ev.mes.service.WorkingProcedureDetailService;

@Service
public class WorkingProcedureDetailServiceImpl implements WorkingProcedureDetailService {
	@Autowired
	private WorkingProcedureDetailDao workingProcedureDetailDao;

	@Override
	public WorkingProcedureDetailDO get(Long id) {
		return workingProcedureDetailDao.get(id);
	}

	@Override
	public List<WorkingProcedureDetailDO> list(Map<String, Object> map) {
		return workingProcedureDetailDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return workingProcedureDetailDao.count(map);
	}

	@Override
	public int save(WorkingProcedureDetailDO workingProcedureDetail) {
		return workingProcedureDetailDao.save(workingProcedureDetail);
	}

	@Override
	public int update(WorkingProcedureDetailDO workingProcedureDetail) {
		return workingProcedureDetailDao.update(workingProcedureDetail);
	}

	@Override
	public int remove(Long id) {
		return workingProcedureDetailDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return workingProcedureDetailDao.batchRemove(ids);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> params) {
		return workingProcedureDetailDao.listForMap(params);
	}

	@Override
	public Map<String, Object> countForMap(Map<String, Object> params) {
		return workingProcedureDetailDao.countForMap(params);
	}

	@Override
	public List<Map<String, Object>> listByPlanId(Long id) {
		return workingProcedureDetailDao.listByPlanId(id);
	}

	@Override
	public void removeByHeadId(Long id) {
		workingProcedureDetailDao.removeByHeadId(id);
	}

	@Override
	public void updateByPlanId(WorkingProcedureDetailDO detailDO) {
		workingProcedureDetailDao.updateByPlanId(detailDO);
	}

	@Override
	public List<Map<String, Object>> getDetailByPlanId(Map<String ,Object> param) {
		return workingProcedureDetailDao.getDetailByPlanId(param);
	}

    @Override
    public List<Map<String, Object>> getDispatchItemlist(Long id) {
        return workingProcedureDetailDao.getDispatchItemlist(id);
    }

    @Override
    public Map<String, Object> listByDetailId(Long bodyId) {
        return workingProcedureDetailDao.listByDetailId(bodyId);
    }

}
