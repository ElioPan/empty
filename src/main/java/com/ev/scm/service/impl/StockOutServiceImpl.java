package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.MaterielService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.*;
import com.ev.scm.dao.StockOutDao;
import com.ev.scm.domain.StockDO;
import com.ev.scm.domain.StockItemDO;
import com.ev.scm.domain.StockOutDO;
import com.ev.scm.domain.StockOutItemDO;
import com.ev.scm.service.StockItemService;
import com.ev.scm.service.StockOutItemService;
import com.ev.scm.service.StockOutService;
import com.ev.scm.service.StockService;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class StockOutServiceImpl implements StockOutService {
    @Autowired
    private StockOutDao stockOutDao;
    @Autowired
    private StockOutItemService stockOutItemService;
    @Autowired
    private StockService stockService;
    @Autowired
    private MaterielService materielService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private StockItemService stockItemService;

    @Override
    public StockOutDO get(Long id) {
        return stockOutDao.get(id);
    }

    @Override
    public List<StockOutDO> list(Map<String, Object> map) {
        return stockOutDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return stockOutDao.count(map);
    }

    @Override
    public int save(StockOutDO stockOutDO) {
        return stockOutDao.save(stockOutDO);
    }

    @Override
    public int update(StockOutDO stockOutDO) {
        return stockOutDao.update(stockOutDO);
    }

    @Override
    public int remove(Long id, Long outType) {
        this.stockOutItemService.removeByStockOutId(id);
        this.removeStockDetail(id, outType);
        return this.stockOutDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids, Long outType) {
        int count = stockOutDao.batchRemove(ids);
        stockOutItemService.batchRemoveByStockOutId(ids);
        // 只有在待审核状态下才可以删除库存明细表数据
        for (Long id : ids) {
            this.removeStockDetail(id, outType);
        }
        return count;
    }

    @Override
    public List<Long> getAllStockIds(List<Map<String, Object>> params) {
        // 获取库存明细表中保存的库位id数组
        List<Long> stockIds = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        int log = 0;
        for (Map<String, Object> stockId : params) {
            log++;
            buffer.append(stockId.get("id"));
            if (log == params.size()) {
                break;
            }
            buffer.append(",");
        }
        String stockIdString = new String(buffer);
        if (stockIdString.contains(",")) {
            String[] stockStrings = stockIdString.split(",");
            for (String string : stockStrings) {
                stockIds.add(Long.parseLong(string));
            }
        }
        if (!stockIdString.contains(",")) {
            stockIds.add(Long.parseLong(stockIdString));
        }
        return stockIds;
    }


    @Override
    public R add(StockOutDO stockOutDO, String item, DictionaryDO storageType) {
        Map<String, Object> result = Maps.newHashMap();
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);

        // 将JSON 解析出来以库存ID 数组 和 数量作为验证条件
        List<Map<String, Object>> params = this.stockListParam(itemDOs);
        // 获取库存明细表中保存的库位id数组
        List<Long> stockIds = getAllStockIds(params);
        if (checkInsert(params, stockIds)) {
            return R.error(messageSourceHandler.getMessage("stock.count.error", null));
        }

        // 库存无误执行下面保存操作
        List<StockDO> stockList = materielService.stockList(stockIds);
        // 设置出库类型
        long storageTypeId = storageType.getId().longValue();
        stockOutDO.setOutboundType(storageTypeId);
        // 设置审核状态为待审核
        stockOutDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
        // 设置出库单据号
        String value = storageType.getValue();

        //获取编号
        String maxNo = DateFormatUtil.getWorkOrderno(value);
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
        param.put("maxNo", maxNo);
        param.put("offset", 0);
        param.put("limit", 1);
        List<StockOutDO> list = this.list(param);
        String taskNo = null;
        if (list.size() > 0) {
            taskNo = list.get(0).getOutCode();
        }
        stockOutDO.setOutCode(DateFormatUtil.getWorkOrderno(maxNo, taskNo));

        // 保存主表数据
        int count = this.stockOutDao.save(stockOutDO);
        Long stockOutId = stockOutDO.getId();
        result.put("id", stockOutId);

        if (count > 0) {
            // 保存出库产品
            List<StockOutItemDO> saveItemDOs = Lists.newArrayList();
            for (StockOutItemDO obj : itemDOs) {
                obj.setOutId(stockOutDO.getId());
                saveItemDOs.add(obj);
            }
            // 批量更新子表数据
            int saveItemCount = stockOutItemService.batchInsert(saveItemDOs);
            // 子表数据保存成功保存库存信息
            if (saveItemCount == saveItemDOs.size()) {
                List<Pair<List<StockItemDO>, List<StockDO>>> stockInfos = Lists.newArrayList();
                for (StockOutItemDO itemDO : itemDOs) {
                    BigDecimal proCount = itemDO.getCount();
                    // 保存库存明细数据并设置为未生效
                    Pair<List<StockItemDO>, List<StockDO>> saveStockDetail = this.saveStockDetail(stockOutId, itemDO.getId(),
                            itemDO.getStockId(), proCount, itemDO.getSourceType(), storageTypeId,
                            stockList);
                    if (saveStockDetail != null) {
                        stockInfos.add(saveStockDetail);
                    }
                }
                // 保存库存明细
                this.batchSaveStockInfo(stockInfos);
                return R.ok(result);
            }
        }
        return R.error();
    }

    @Override
    public void batchSaveStockInfo(List<Pair<List<StockItemDO>, List<StockDO>>> stockInfos) {
        if (stockInfos.size() > 0) {
            List<StockDO> batchUpdateStockDO = new ArrayList<>();
            List<StockItemDO> batchInsertStockDetailDO = new ArrayList<>();
            for (Pair<List<StockItemDO>, List<StockDO>> stockInfo : stockInfos) {
                batchUpdateStockDO.addAll(stockInfo.getRight());
                batchInsertStockDetailDO.addAll(stockInfo.getLeft());
            }
            this.batchUpdateStockDO(batchUpdateStockDO);
            this.batchInsertStockDetailDO(batchInsertStockDetailDO);
        }
    }

    @Override
    public R batchDelete(Long[] ids, Long outType) {
        for (Long id : ids) {
            if (Objects.equal(this.get(id).getAuditSign(), ConstantForGYL.OK_AUDITED)) {
                return R.error(messageSourceHandler.getMessage("common.approvedOrChild.delete.disabled", null));
            }
        }
        return this.batchRemove(ids, outType) > 0 ? R.ok() : R.error();
    }

    @Override
    public R reverseAuditForR(Long id, Long outType) {
        StockOutDO stockOutDO = get(id);
        Long auditSignId = stockOutDO.getAuditSign();
        int count = 0;
        // 若是已审核状态设置审核状态为反审核
        if (auditSignId.equals(ConstantForGYL.OK_AUDITED)) {
            stockOutDO.setAuditSign(ConstantForGYL.WAIT_AUDIT);
            // 设置审核人
            stockOutDO.setAuditor(0L);
            // 修改出库单据状态
            count = update(stockOutDO);
            // 操作库存数据
            this.reverseAudit(id, outType);
        }
        return count > 0 ? R.ok() : R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
    }

    @Override
    public Map<String, Object> countTotal(Map<String, Object> params) {
        return stockOutDao.countTotal(params);
    }

    @Override
    public int childCount(Long id) {
        return stockOutDao.childCount(id);
    }

    @Override
    public void batchInsertStockDetailDO(List<StockItemDO> batchInsertStockDetailDO) {
        stockOutDao.batchInsertStockDetailDO(batchInsertStockDetailDO);
    }

    @Override
    public void batchUpdateStockDO(List<StockDO> batchUpdateStockDO) {
        stockOutDao.batchUpdateStockDO(batchUpdateStockDO);
    }

    private List<Map<String, Object>> stockListParam(List<StockOutItemDO> itemDOs) {
        List<Map<String, Object>> params = new ArrayList<>();
        Map<String, Object> param;
        for (StockOutItemDO itemDO : itemDOs) {
            param = Maps.newHashMapWithExpectedSize(2);
            param.put("id", itemDO.getStockId());
            param.put("outCount", itemDO.getCount());
            params.add(param);
        }
        return params;
    }

    @Override
    public boolean checkInsert(List<Map<String, Object>> params, List<Long> stockIds) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("stockId", stockIds);
        // 获取即将出库产品实际数量
        List<Map<String, Object>> stockListForMap = materielService.stockCount(param);
        // 将相同库存ID的数据当作一类数据统计
        Map<String,BigDecimal> stringBigDecimalMap = Maps.newHashMap();
        for (Map<String, Object> map : params) {
            String stockId = map.get("id").toString();
            if (stringBigDecimalMap.containsKey(stockId)) {
                stringBigDecimalMap.put(stockId,stringBigDecimalMap.get(stockId).add(MathUtils.getBigDecimal(map.get("outCount"))));
                continue;
            }
            stringBigDecimalMap.put(stockId,MathUtils.getBigDecimal(map.get("outCount")));
        }

        for (Map<String, Object> stockCount : stockListForMap) {
            String stockId = stockCount.get("id").toString();
            if (stringBigDecimalMap.containsKey(stockId)) {
                if (MathUtils.getBigDecimal(stockCount.get("availableCount"))
                        .compareTo(stringBigDecimalMap.get(stockId)) < 0) {
                    return true;
                }
            }

//            for (Map<String, Object> outCount : params) {
//                if (outCount.get("id").toString().equals(stockCount.get("id").toString())) {
//                    if (MathUtils.getBigDecimal(stockCount.get("availableCount"))
//                            .compareTo(MathUtils.getBigDecimal(outCount.get("outCount"))) < 0) {
//                        return true;
//                    }
//                }
//            }
        }
        return false;
    }

    @Override
    public R audit(Long id, Long outType) {
        StockOutDO stockOutDO = get(id);
        Long auditSignId = stockOutDO.getAuditSign();
        int count = 0;
        // 若是待审核状态设置审核状态为已审核
        if (auditSignId.equals(ConstantForGYL.WAIT_AUDIT)) {
            stockOutDO.setAuditSign(ConstantForGYL.OK_AUDITED);
            // 设置审核时间
            stockOutDO.setAuditTime(new Date());
            // 设置审核人
            stockOutDO.setAuditor(ShiroUtils.getUserId());
            count = update(stockOutDO);
            // 保存库存明细
            this.insertStockInfo(id, outType);
        }
        return count > 0 ? R.ok() : R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
    }

    @Override
    public void insertStockInfo(Long id, Long outType) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("inheadId", id);
        param.put("inOutType", outType);
        stockOutDao.updateStockDetail(param);

        // 获取所有的库存明细变化
        List<StockItemDO> stockDetailDO = stockItemService.list(param);
        if (stockDetailDO.size() > 0) {
            // 批量更新库存明细数据集合
            List<StockDO> stockDOs = Lists.newArrayList();
            List<Long> stockIds = Lists.newArrayList();
            for (StockItemDO stockDetail : stockDetailDO) {
                // 修改库存表里数据
                stockIds.add(stockDetail.getStockId());
            }
            List<StockDO> stockList = materielService.stockList(stockIds);
            for (StockDO stockDO : stockList) {
                for (StockItemDO stockDetail : stockDetailDO) {
                    if (stockDetail.getStockId().equals(stockDO.getId())) {
                        stockDO.setCount(stockDO.getCount().add(stockDetail.getCount()));
                        stockDOs.add(stockDO);
                    }
                }
            }
            batchUpdateStockDO(stockDOs);
        }
    }

    @Override
    public Pair<List<StockItemDO>, List<StockDO>> saveStockDetail(Long stockOutId, Long stockOutItemId, String stockId, BigDecimal proCount,
                                                                    Long sourceType, Long storageType, List<StockDO> stockList) {
        // 返回结果集
        List<Long> stockIds = getStockIds(stockId);
        // 批量保存集合
        List<StockItemDO> stockDetailDOs = Lists.newArrayList();
        // 批量修改集合
        List<StockDO> stockDOs = Lists.newArrayList();
        StockItemDO detailDO;
        for (StockDO stockDO : stockList) {
            if (stockIds.contains(stockDO.getId())) {
                detailDO = new StockItemDO();
                // 库存可用数量
                BigDecimal count = stockDO.getAvailableCount();
                // 库存产品数量大于或等于出库明细表数量
                if (count.compareTo(proCount) > 0 || count.compareTo(proCount) == 0) {
                    // 修改库存表可用数量
                    stockDO.setAvailableCount(count.subtract(proCount));
                    // 将需要更改库存数据保存进集合中批量更改
                    stockDOs.add(stockDO);
                    // 添加库存明细信息
                    this.saveStockDetailInfo(detailDO, stockOutId, stockOutItemId, stockDO,
                            BigDecimal.ZERO.subtract(proCount), storageType, sourceType);
                    // 将需要更改库存明细数据保存进集合中批量保存
                    stockDetailDOs.add(detailDO);
                    // 将需要更改的数据保存入集合中
                    return Pair.of(stockDetailDOs, stockDOs);
                }
                // 若库存产品数量小于出库明细数量
                if (count.compareTo(proCount) < 0) {
                    if (count.compareTo(BigDecimal.ZERO) > 0) {
                        // 设置该库存的可用数量为0
                        stockDO.setAvailableCount(BigDecimal.ZERO);
                        // 添加库存明细信息
                        // 将需要更改库存数据保存进集合中批量更改
                        stockDOs.add(stockDO);
                        this.saveStockDetailInfo(detailDO, stockOutId, stockOutItemId, stockDO,
                                BigDecimal.ZERO.subtract(count), storageType, sourceType);
                        // 将需要更改库存明细数据保存进集合中批量保存
                        stockDetailDOs.add(detailDO);
                        proCount = proCount.subtract(count);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Long> getStockIds(String stockId) {
        List<Long> stockIds = new ArrayList<>();
        if (stockId.contains(",")) {
            String[] split = stockId.split(",");
            for (String string : split) {
                stockIds.add(Long.parseLong(string));
            }
        }
        if (!stockId.contains(",")) {
            stockIds.add(Long.parseLong(stockId));
        }
        return stockIds;
    }

    @Override
    public void saveStockDetailInfo(StockItemDO stockDetail, Long stockOutId, Long stockOutItemId, StockDO stockDO, BigDecimal change,
                                    Long storageType, Long sourceType) {
        stockDetail.setInbodyId(stockOutItemId);
        stockDetail.setStockId(stockDO.getId());
        stockDetail.setInheadId(stockOutId);
        stockDetail.setUnitPrice(stockDO.getUnitPrice());
        stockDetail.setCount(change);
        stockDetail.setInOutType(storageType);
        stockDetail.setSourceType(sourceType);
        // 设置为未生效状态
        if (change.compareTo(BigDecimal.ZERO) > 0) {
            stockDetail.setHandleSign(1L);
        }
        if (change.compareTo(BigDecimal.ZERO) < 0) {
            stockDetail.setHandleSign(0L);
        }
    }

    @Override
    public void reverseAudit(Long id, Long outType) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("inheadId", id);
        param.put("inOutType", outType);

        // 获取所有的库存明细变化
        List<StockItemDO> stockDetailDO = stockItemService.list(param);

        List<Long> removeStockIds = Lists.newArrayList();
        List<Long> stockIds = Lists.newArrayList();
        List<StockDO> batchUpdateStockDO = Lists.newArrayList();
        for (StockItemDO stockDetail : stockDetailDO) {
            BigDecimal stockDetailCount = stockDetail.getCount();
            if (stockDetailCount.compareTo(BigDecimal.ZERO) > 0) {
                removeStockIds.add(stockDetail.getStockId());
            }
            // 若是库存明细表中数量为负数则是出库操作，则进行修改操作将已经出库的操作撤回
            if (stockDetailCount.compareTo(BigDecimal.ZERO) < 0) {
                stockIds.add(stockDetail.getStockId());
            }
        }
        if (removeStockIds.size() > 0) {
            stockService.batchRemove(removeStockIds.toArray(new Long[0]));
        }
        List<StockDO> stockList = materielService.stockList(stockIds);
        for (StockItemDO stockDetail : stockDetailDO) {
            BigDecimal detailCount = stockDetail.getCount();
            if (detailCount.compareTo(BigDecimal.ZERO) < 0) {
                for (StockDO stockDO : stockList) {
                    if (stockDetail.getStockId().equals(stockDO.getId())) {
                        stockDO.setCount(stockDO.getCount().add(detailCount.multiply(new BigDecimal(-1))));
                        stockDO.setAvailableCount(
                                stockDO.getAvailableCount().add(detailCount.multiply(new BigDecimal(-1))));
                        batchUpdateStockDO.add(stockDO);
                        break;
                    }
                }
            }
        }
        stockOutDao.batchUpdateStockDO(batchUpdateStockDO);
        // 删除库存明细表里的数据
        stockItemService.removeByInheadId(outType, id);
    }

    @Override
    public void removeStockDetail(Long id, Long outType) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("inheadId", id);
        param.put("inOutType", outType);
        // 获取所有的库存明细变化
        List<StockItemDO> stockDetailDO = stockItemService.list(param);
        if (stockDetailDO.size() == 0) {
            return;
        }
        List<Long> stockIds = Lists.newArrayList();
        for (StockItemDO stockDetail : stockDetailDO) {
            // 若是库存明细表中数量为负数则是出库操作，则进行修改操作将已经出库的操作撤回
            if (stockDetail.getCount().compareTo(BigDecimal.ZERO) < 0) {
                stockIds.add(stockDetail.getStockId());
            }
        }
        List<StockDO> stockList = materielService.stockList(stockIds);
        List<StockDO> batchUpdateStockDO = Lists.newArrayList();
        for (StockItemDO stockDetail : stockDetailDO) {
            BigDecimal detailCount = stockDetail.getCount();
            if (detailCount.compareTo(BigDecimal.ZERO) < 0) {
                for (StockDO stockDO : stockList) {
                    if (stockDetail.getStockId().equals(stockDO.getId())) {
                        stockDO.setAvailableCount(
                                stockDO.getAvailableCount().add(detailCount.multiply(new BigDecimal(-1))));
                        batchUpdateStockDO.add(stockDO);
                    }
                }
            }
        }
        stockOutDao.batchUpdateStockDO(batchUpdateStockDO);
        this.stockItemService.removeByInheadId(outType, id);
    }

    @Override
    public R edit(StockOutDO stockOutDO, String item, Long storageType,Long [] itemIds) {
        StockOutDO stockOut = this.get(stockOutDO.getId());
        if (Objects.equal(stockOut.getAuditSign(),ConstantForGYL.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
        }
        // 修改前先删除库存记录
        this.removeStockDetail(stockOutDO.getId(), storageType);
        // 修改出库产品
        List<StockOutItemDO> itemDOs = JSON.parseArray(item, StockOutItemDO.class);
        List<Map<String, Object>> stockListParam = this.stockListParam(itemDOs);
        List<Long> stockIds = getAllStockIds(stockListParam);
        if (checkInsert(stockListParam, stockIds)) {
            return R.error(messageSourceHandler.getMessage("stock.count.error", null));
        }

        int count = this.stockOutDao.update(stockOutDO);
        if (count > 0) {
            if(itemIds.length>0){
                stockOutItemService.batchRemove(itemIds);
            }

            List<StockOutItemDO> updateItemDOs = new ArrayList<>();
            // 库存无误执行下面修改操作
            List<StockDO> stockList = materielService.stockList(stockIds);
            List<Pair<List<StockItemDO>, List<StockDO>>> stockInfos = Lists.newArrayList();
            for (StockOutItemDO itemDO : itemDOs) {
                Long stockOutDOId = stockOutDO.getId();
                // 如果传入数据不为null 则该数据已在表中存在执行保存，若有值则修改
                if (itemDO.getId() == null) {
                    itemDO.setOutId(stockOutDOId);
                    stockOutItemService.save(itemDO);
                    // 库存数量
                    BigDecimal proCount = itemDO.getCount();
                    // 保存库存明细表数据
                    Pair<List<StockItemDO>, List<StockDO>> saveStockDetail = this.saveStockDetail(stockOutDOId, itemDO.getId(),
                            itemDO.getStockId(), proCount, itemDO.getSourceType(), storageType, stockList);
                    stockInfos.add(saveStockDetail);
                    continue;
                }
                BigDecimal proCount = itemDO.getCount();
                // 修改出库明细表数据
                updateItemDOs.add(itemDO);
                // 重新保存库存明细表数据
                Pair<List<StockItemDO>, List<StockDO>> saveStockDetail = this.saveStockDetail(stockOutDOId, itemDO.getId(), itemDO.getStockId(),
                        proCount, itemDO.getSourceType(), storageType, stockList);
                stockInfos.add(saveStockDetail);
            }
            int updateItemDOCount = stockOutItemService.batchUpdate(updateItemDOs);
            if (updateItemDOCount > 0) {
                this.batchSaveStockInfo(stockInfos);
                return R.ok();
            }
        }
        return R.error();
    }

    @Override
    public Map<String, Object> getDetail(Long id) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", id);
        List<Map<String, Object>> maps = this.listForMap(param);
        Map<String, Object> stockOut = maps.size() > 0 ? maps.get(0) : null;
        results.put("stockOut", stockOut);

        List<Map<String, Object>> stockOutItem = stockOutItemService.listForMap(param);
        results.put("stockOutItem", stockOutItem);
        if (stockOutItem.size() > 0) {
            Map<String,Object> params;
            for (Map<String, Object> map : stockOutItem) {
                params = Maps.newHashMap();
                params.put("materielId",map.get("materielId"));
                params.put("batch",map.get("batch"));
                params.put("locationId",map.get("locationId"));
                List<Map<String, Object>> stockListForMap = materielService.stockListForMap(params);
                if (stockListForMap.size() > 0) {
                    map.put("availableCount",stockListForMap.get(0).get("availableCount"));
                }
            }
        }
        return results;
    }

    @Override
    public List<Map<String, Object>> listApi(Map<String, Object> map) {
        return this.stockOutDao.listApi(map);
    }

    @Override
    public int countApi(Map<String, Object> map) {
        return this.stockOutDao.countApi(map);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return this.stockOutDao.listForMap(map);
    }

    @Override
    public int countForMap(Map<String, Object> map) {
        return this.stockOutDao.countForMap(map);
    }
}
