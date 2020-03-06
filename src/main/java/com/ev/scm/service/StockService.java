package com.ev.scm.service;

import com.ev.framework.utils.R;
import com.ev.scm.domain.StockDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库存表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 13:36:43
 */
public interface StockService {

    StockDO get(Long id);

    List<StockDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(StockDO stock);

    int update(StockDO stock);

    int remove(Long id);

    int batchRemove(Long[] ids);

    int batchSave(List<StockDO> stockDOs);

    List<Map<String, Object>> listForMap(Map<String, Object> results);

    Map<String, Object> countForMap(Map<String, Object> results);

    int batchUpdate(List<StockDO> batchUpdate);

    R importExcel(MultipartFile file);

    R saveStockStartTime(String yearAndMonth);

    R getStartTime();

    R initStock(String stockList, Long[] itemIds);

    R startList();

    R endInitial();

    R stockOutAccountingTime();

    R checkTime(String period);

    R stockOutAccounting(String period);

    R stockOutAccountingCheck(String period);

    R checkEndingCarryOver(String period);

    R endingCarryOver(String period);

    R endingClose(String period);

    Date getPeriodTime();
}
