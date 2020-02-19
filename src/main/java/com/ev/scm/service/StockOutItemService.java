package com.ev.scm.service;

import com.ev.scm.domain.StockOutItemDO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 出库子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 16:55:49
 */
public interface StockOutItemService {

    StockOutItemDO get(Long id);

    List<StockOutItemDO> list(Map<String, Object> map);

    List<Map<String,Object>> listForMap(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(StockOutItemDO stockOutItemDO);

    StockOutItemDO insert(StockOutItemDO stockOutItemDO);

    int update(StockOutItemDO stockOutItemDO);

    int remove(Long id);

    void batchRemoveByStockOutId(Long[] ids);

    void removeByStockOutId(Long id);


    int batchRemove(Long[] ids);

    int batchInsert(List<StockOutItemDO> stockOutItemDOs);

    int batchUpdate(List<StockOutItemDO> stockOutItemDOs);

    BigDecimal getCountBySource(Map<String, Object> params);

}
