package com.ev.scm.dao;

import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockItemDO;
import com.ev.scm.domain.StockOutDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 出库主表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 16:55:49
 */
@Mapper
public interface StockOutDao {

    StockOutDO get(Long id);

    List<StockOutDO> list(Map<String,Object> map);

    int save(StockOutDO stockOutDO);

    int update(StockOutDO stockOutDO);

    int remove(Long id);

    int batchRemove(Long[] ids);

    int count(Map<String,Object> map);

    /* 自定义*/
    List<Map<String,Object>> listApi(Map<String, Object> map);

    List<Map<String,Object>> listForMap(Map<String, Object> map);

    int countForMap(Map<String, Object> map);

    int countApi(Map<String, Object> map);

    int countOfStockId(List<Long>ids);

    int batchUpdateStockDO(List<StockDO> batchUpdateStockDO);

    int batchInsertStockDetailDO(List<StockItemDO> batchInsertStockDetailDO);

    int updateStockDetail(Map<String,Object>map);

    Map<String, Object> countTotal(Map<String, Object> params);

    int salesChildCount(Long id);
}
