package com.ev.scm.service;

import com.ev.scm.domain.AllotItemDO;
import com.ev.scm.domain.StockDO;
import com.ev.framework.utils.R;
import com.ev.scm.domain.AllotDO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 调拨单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
public interface AllotService {

    AllotDO get(Long id);

    List<AllotDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(AllotDO allot);

    R add(AllotDO allot, String body) throws IOException, ClassNotFoundException;

    int update(AllotDO allot);

    int updateAll(AllotDO allot);

    int remove(Long id);

    int batchRemove(Long[] ids);

    StockDO saveNewStock(StockDO stockDO, BigDecimal change, String stockCode, Long warehLocation, Long warehouse);

    List<Map<String,Object>> listForMap(Map<String, Object> map);

    int countForMap(Map<String, Object> map);

    Map<String, Object> getDetail(Long id);

    R edit(AllotDO allotId, String body,Long[] itemIds);

    void saveStockInfo(Long id, Long storageType);

    R audit(Long id, Long storageType);

    R addByQrcodeId(AllotDO allot, List<AllotItemDO> bodys) throws IOException, ClassNotFoundException;

    R reverseAudit(Long id, Long db);

    boolean isQrcode(Long id);

    R batchRemoveByIds(Long[] ids);
}
