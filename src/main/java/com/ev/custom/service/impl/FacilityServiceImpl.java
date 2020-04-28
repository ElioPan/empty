package com.ev.custom.service.impl;

import com.ev.custom.dao.FacilityDao;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.service.FacilityService;
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
public class FacilityServiceImpl implements FacilityService {
    @Autowired
    private FacilityDao facilityDao;

    @Override
    public FacilityDO get(Long id) {
        return facilityDao.get(id);
    }

    @Override
    public List<FacilityDO> list(Map<String, Object> map) {
        return facilityDao.list(map);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return facilityDao.listForMap(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return facilityDao.count(map);
    }

    @Override
    public int save(FacilityDO facility) {
        Map<String, Object> param = Maps.newHashMap();
        facility.setSerialNo(this.setSerialNo());

        param.put("name", facility.getName());
        int count = this.count(param);
        if (count > 0) {
            return -1;
        }
        facility.setAuditSign(ConstantForMES.WAIT_AUDIT);
        return facilityDao.save(facility);
    }

    private String setSerialNo() {
        //获取编号
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
        param.put("offset", 0);
        param.put("limit", 1);
        List<FacilityDO> list = this.list(param);
        String taskNo = null;
        if (list.size() > 0) {
            taskNo = list.get(0).getSerialNo();
        }
        return DateFormatUtil.getWorkOrderno(Constant.CK, taskNo, 4);
    }

    @Override
    public int update(FacilityDO facility) {
        FacilityDO facilityDO = this.get(facility.getId());
        if (Objects.equals(facility.getName(), facilityDO.getName())) {
            return facilityDao.update(facility);
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("name", facility.getName());
        int count = this.count(param);
        if (count > 0) {
            return -1;
        }
        return facilityDao.update(facility);
    }

    @Override
    public int remove(Long id) {
        return facilityDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return facilityDao.batchRemove(ids);
    }

    @Override
    public int logicRemove(Long id) {
        FacilityDO facilityDO = new FacilityDO();
        facilityDO.setId(id);
        facilityDO.setDelFlag(1);
        return facilityDao.update(facilityDO);
    }

    @Override
    public int logicBatchRemove(Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += this.logicRemove(id);
        }
        return count;
    }

    @Override
    public int countForMap(Map<String, Object> params) {
        return facilityDao.countForMap(params);
    }

    @Override
    public int audit(Long id) {
        FacilityDO facilityDO = new FacilityDO();
        facilityDO.setId(id);
        facilityDO.setAuditor(ShiroUtils.getUserId());
        facilityDO.setAuditSign(ConstantForMES.OK_AUDITED);
        return facilityDao.update(facilityDO);
    }

    @Override
    public int reverseAudit(Long id) {
        FacilityDO facilityDO = new FacilityDO();
        facilityDO.setId(id);
        facilityDO.setAuditor(0L);
        facilityDO.setAuditSign(ConstantForMES.WAIT_AUDIT);
        return facilityDao.update(facilityDO);
    }

}
