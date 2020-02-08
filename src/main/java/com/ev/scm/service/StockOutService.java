package com.ev.scm.service;

import com.ev.custom.domain.DictionaryDO;
import com.ev.framework.utils.R;
import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockItemDO;
import com.ev.scm.domain.StockOutDO;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 出库主表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 16:55:49
 */
public interface StockOutService {

    StockOutDO get(Long id);

    List<StockOutDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    List<Map<String, Object>> listForMap(Map<String, Object> map);

    List<Map<String, Object>> listApi(Map<String, Object> map);

    int countForMap(Map<String, Object> map);

    int countApi(Map<String, Object> map);

    int save(StockOutDO stockOutDO);

    int update(StockOutDO stockOutDO);

    R add(StockOutDO stockOutDO, String item, DictionaryDO storageType);

    Map<String, Object> getDetail(Long id);

    void reverseAudit(Long id, Long outType);

    boolean checkInsert(List<Map<String, Object>> item, List<Long> stockIds);

    List<Long> getStockIds(String stockId);

    List<Long> getAllStockIds(List<Map<String, Object>> params);

    void removeStockDetail(Long id, Long outType);

    int batchRemove(Long[] ids, Long outType);

    int remove(Long id, Long outType);

    R audit(Long id, Long outType);

    void insertStockInfo(Long id, Long outType);

    StockItemDO saveStockDetailInfo(StockItemDO stockDetail, Long stockOutId, Long stockOutItemId, StockDO stockDO, BigDecimal change, Long storageType,
                                    Long sourceType);

    Pair<List<StockItemDO>, List<StockDO>> saveStockDetail(Long stockOutId, Long stockOutItemId, String stockId, BigDecimal proCount,
                                                             Long sourceType, Long storageType, List<StockDO> stockList);

    R edit(StockOutDO stockOutDO, String item, Long storageType, Long[] itemIds);

    int batchUpdateStockDO(List<StockDO> batchUpdateStockDO);

    int batchInsertStockDetailDO(List<StockItemDO> batchInsertStockDetailDO);

    void batchSaveStockInfo(List<Pair<List<StockItemDO>, List<StockDO>>> stockInfos);

    R batchDelete(Long[] ids, Long outType);

    R reverseAuditForR(Long id, Long outType);
}
