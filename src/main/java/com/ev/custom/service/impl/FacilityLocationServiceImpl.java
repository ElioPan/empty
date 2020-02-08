package com.ev.custom.service.impl;

import com.ev.custom.dao.FacilityDao;
import com.ev.custom.dao.FacilityLocationDao;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.domain.PatrolPlanDO;
import com.ev.custom.service.FacilityLocationService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.ShiroUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FacilityLocationServiceImpl implements FacilityLocationService {
	@Autowired
	private FacilityLocationDao facilityLocationDao;
    @Autowired
    private FacilityDao facilityDao;

	@Override
	public FacilityLocationDO get(Integer id) {
		return facilityLocationDao.get(id);
	}

	@Override
	public List<FacilityLocationDO> list(Map<String, Object> map) {
		return facilityLocationDao.list(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return facilityLocationDao.listForMap(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return facilityLocationDao.count(map);
	}

	@Override
	public int save(FacilityLocationDO facilityLocation) {
		Map<String, Object> param = Maps.newHashMap();
		facilityLocation.setSerialNo(this.setSerialNo(facilityLocation.getFacilityId()));

		param.put("name", facilityLocation.getName());
		int count = this.count(param);
		if (count > 0) {
			return -1;
		}
		facilityLocation.setAuditSign(ConstantForMES.WAIT_AUDIT);
		return facilityLocationDao.save(facilityLocation);
	}


    @Override
	public int update(FacilityLocationDO facilityLocation) {
		FacilityLocationDO do1 = this.get(facilityLocation.getId());
		if (Objects.equals(facilityLocation.getName(), do1.getName())) {
			return facilityLocationDao.update(facilityLocation);
		}
		Map<String, Object> param = Maps.newHashMap();
		param.put("name", facilityLocation.getName());
		int count = this.count(param);
		if (count > 0) {
			return -1;
		}
		return facilityLocationDao.update(facilityLocation);
	}

	@Override
	public int remove(Integer id) {
		return facilityLocationDao.remove(id);
	}

	@Override
	public int batchRemove(Integer[] ids) {
		return facilityLocationDao.batchRemove(ids);
	}

	@Override
	public int logicRemove(Integer id) {
		FacilityLocationDO locationDO = new FacilityLocationDO();
		locationDO.setId(id);
		locationDO.setDelFlag(1);
		return facilityLocationDao.update(locationDO);
	}

	@Override
	public int logicBatchRemove(Integer[] ids) {
		int count = 0;
		for (Integer id : ids) {
			count += this.logicRemove(id);
		}
		return count;
	}

    @Override
    public int audit(Integer id) {
        FacilityLocationDO facilityLocationDO = new FacilityLocationDO();
        facilityLocationDO.setId(id);
        facilityLocationDO.setAuditor(ShiroUtils.getUserId());
        facilityLocationDO.setAuditSign(ConstantForMES.OK_AUDITED);
        return facilityLocationDao.update(facilityLocationDO);
    }

    @Override
    public int reverseAudit(Integer id) {
        FacilityLocationDO facilityLocationDO = new FacilityLocationDO();
        facilityLocationDO.setId(id);
        facilityLocationDO.setAuditor(0L);
        facilityLocationDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
        return facilityLocationDao.update(facilityLocationDO);
    }

    private String setSerialNo(Integer facilityId) {
        //获取编号
        Map<String,Object> param = Maps.newHashMapWithExpectedSize(3);
//        FacilityDO facilityDO = facilityDao.get(facilityId);
//        String maxNo = new StringBuilder(facilityDO.getSerialNo()).append("-").append(Constant.KW).toString();
        String maxNo = Constant.KW;
        param.put("maxNo", maxNo);
        param.put("offset", 0);
        param.put("limit", 1);
        List<FacilityLocationDO> list = this.list(param);
        String taskNo = null;
        if (list.size()>0) {
            taskNo = list.get(0).getSerialNo();
        }
        return  DateFormatUtil.getWorkOrderno(maxNo, taskNo,6);
    }

}
