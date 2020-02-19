package com.ev.scm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ev.scm.dao.StockOutItemDao;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.StockOutItemService;



@Service
public class StockOutItemServiceImpl implements StockOutItemService {
    @Autowired
    private StockOutItemDao stockOutItemDao;

    @Override
    public StockOutItemDO get(Long id){
        return stockOutItemDao.get(id);
    }

    @Override
    public List<StockOutItemDO> list(Map<String, Object> map){
        return stockOutItemDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map){
        return stockOutItemDao.count(map);
    }

    @Override
    public int save(StockOutItemDO stockOutItemDO){
        return stockOutItemDao.save(stockOutItemDO);
    }

    @Override
    public int update(StockOutItemDO stockOutItemDO){
        return stockOutItemDao.update(stockOutItemDO);
    }

    @Override
    public int remove(Long id){
        return stockOutItemDao.remove(id);
    }

    @Override
    public void removeByStockOutId(Long id){
        stockOutItemDao.removeByStockOutId(id);
    }

    @Override
    public int batchRemove(Long[] ids){
        return stockOutItemDao.batchRemove(ids);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return stockOutItemDao.listForMap(map);
    }

    @Override
    public StockOutItemDO insert(StockOutItemDO stockOutItemDO) {
        this.save(stockOutItemDO);
        return stockOutItemDO;
    }

    @Override
    public void batchRemoveByStockOutId(Long[] ids) {
        stockOutItemDao.batchRemoveByStockOutId(ids);
    }


    @Override
    public int batchInsert(List<StockOutItemDO> stockOutItemDOs) {
        return stockOutItemDao.batchInsert(stockOutItemDOs);
    }

    @Override
    public int batchUpdate(List<StockOutItemDO> stockOutItemDOs) {
        return  stockOutItemDao.batchUpdate(stockOutItemDOs);
    }

    @Override
    public BigDecimal getCountBySource(Map<String, Object> params) {
        return stockOutItemDao.getCountBySource(params);
    }

}
