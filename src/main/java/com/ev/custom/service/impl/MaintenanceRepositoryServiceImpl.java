package com.ev.custom.service.impl;

import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.custom.dao.MaintenanceRepositoryDao;
import com.ev.custom.domain.MaintenanceRepositoryDO;
import com.ev.custom.service.MaintenanceRepositoryService;


@Service
public class MaintenanceRepositoryServiceImpl implements MaintenanceRepositoryService {
    @Autowired
    private MaintenanceRepositoryDao maintenanceRepositoryDao;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @Override
    public MaintenanceRepositoryDO get(Long id) {
        return maintenanceRepositoryDao.get(id);
    }

    @Override
    public List<MaintenanceRepositoryDO> list(Map<String, Object> map) {
        return maintenanceRepositoryDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return maintenanceRepositoryDao.count(map);
    }

    @Override
    public int save(MaintenanceRepositoryDO maintenanceRepository) {
        return maintenanceRepositoryDao.save(maintenanceRepository);
    }

    @Override
    public int update(MaintenanceRepositoryDO maintenanceRepository) {
        return maintenanceRepositoryDao.update(maintenanceRepository);
    }

    @Override
    public int remove(Long id) {
        return maintenanceRepositoryDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return maintenanceRepositoryDao.batchRemove(ids);
    }

    @Override
    public R addOrUpdate(MaintenanceRepositoryDO maintenanceRepositoryDO) {
        if (this.checkDuplicate(maintenanceRepositoryDO)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo", null));
        }
        Map<String, Object> result = Maps.newHashMap();
        Long id = maintenanceRepositoryDO.getId();
        if (id == null) {
            this.save(maintenanceRepositoryDO);
            id = maintenanceRepositoryDO.getId();
        } else {
            this.update(maintenanceRepositoryDO);
        }
        result.put("id", id);
        return R.ok(result);
    }


    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> params) {
        return maintenanceRepositoryDao.listForMap(params);
    }

    @Override
    public R detail(Long id) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("id",id);
        map.put("offset", 0);
        map.put("limit", 1);
        List<Map<String, Object>> maps = maintenanceRepositoryDao.listForMap(map);
        if (maps.size() > 0) {
            Map<String,Object> result = Maps.newHashMap();
            result.put("detail",maps.get(0));
            return R.ok(result);
        }
        return R.ok();
    }

    @Override
    public R batchDelete(Long[] ids) {
        this.batchRemove(ids);
        return R.ok();
    }

    @Override
    public boolean checkDuplicate(MaintenanceRepositoryDO maintenanceRepositoryDO) {
        return maintenanceRepositoryDao.checkDuplicate(maintenanceRepositoryDO) > 0;
    }

}
