package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ev.scm.dao.AllotItemDao;
import com.ev.scm.domain.AllotItemDO;
import com.ev.scm.service.AllotItemService;



@Service
public class AllotItemServiceImpl implements AllotItemService {
    @Autowired
    private AllotItemDao allotDetailDao;

    @Override
    public AllotItemDO get(Long id){
        return allotDetailDao.get(id);
    }

    @Override
    public List<AllotItemDO> list(Map<String, Object> map){
        return allotDetailDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map){
        return allotDetailDao.count(map);
    }

    @Override
    public int save(AllotItemDO allotDetail){
        return allotDetailDao.save(allotDetail);
    }

    @Override
    public int update(AllotItemDO allotDetail){
        return allotDetailDao.update(allotDetail);
    }

    @Override
    public int remove(Long id){
        return allotDetailDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids){
        return allotDetailDao.batchRemove(ids);
    }

    @Override
    public List<Map<String, Object>> getDetail(Long id) {
        return allotDetailDao.getDetail(id);
    }

    @Override
    public void batchRemoveByAllotId(Long[] ids) {
        allotDetailDao.batchRemoveByAllotId(ids);
    }

    @Override
    public int batchInsert(List<AllotItemDO> allotDetailDOs) {
        return allotDetailDao.batchInsert(allotDetailDOs);
    }

    @Override
    public int batchUpdate(List<AllotItemDO> allotDetailDOs) {
        return allotDetailDao.batchUpdate(allotDetailDOs);
    }

}
