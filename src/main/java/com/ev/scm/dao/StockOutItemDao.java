package com.ev.scm.dao;

import com.ev.scm.domain.StockItemDO;
import com.ev.scm.domain.StockOutItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 出库子表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 16:55:49
 */
@Mapper
public interface StockOutItemDao {

    StockOutItemDO get(Long id);

    List<StockOutItemDO> list(Map<String,Object> map);

    int count(Map<String,Object> map);

    int save(StockOutItemDO stockOutItemDO);

    int update(StockOutItemDO stockOutItemDO);

    int remove(Long id);

    int batchRemove(Long[] ids);

/*自定义*/
    int removeByStockOutId(Long id);

    List<Map<String,Object>> listForMap(Map<String,Object> map);

    int batchRemoveByStockOutId(Long[] ids);

    List<StockItemDO>stockList(Map<String,Object>map);

    int batchInsert(List<StockOutItemDO> stockOutItemDOs);

    int batchUpdate(List<StockOutItemDO> stockOutItemDOs);
}
