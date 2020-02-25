package com.ev.scm.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.framework.utils.ListUtils;
import com.ev.scm.domain.*;
import com.ev.custom.service.MaterielService;
import com.ev.scm.service.StockItemService;
import com.ev.scm.service.StockService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.dao.AllotDao;
import com.ev.scm.dao.StockOutDao;
import com.ev.scm.service.AllotItemService;
import com.ev.scm.service.AllotService;
import com.ev.scm.service.StockOutService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AllotServiceImpl implements AllotService {
    @Autowired
    private AllotDao allotDao;
    @Autowired
    private StockOutDao stockOutDao;
    @Autowired
    private AllotItemService allotItemService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockOutService stockOutService;
    @Autowired
    private MaterielService materielService;
    @Autowired
    private StockItemService stockDetailService;
    @Autowired
    private QrcodeServiceImpl qrcodeService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @Override
    public AllotDO get(Long id) {
        return allotDao.get(id);
    }

    @Override
    public List<AllotDO> list(Map<String, Object> map) {
        return allotDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return allotDao.count(map);
    }

    @Override
    public int save(AllotDO allot) {
        //获取编号
        String maxNo = DateFormatUtil.getWorkOrderno(ConstantForGYL.DBDJ_PREFIX);
        Map<String, Object> param = Maps.newHashMapWithExpectedSize(3);
        param.put("maxNo", maxNo);
        param.put("offset", 0);
        param.put("limit", 1);
        List<AllotDO> list = this.list(param);
        String taskNo = null;
        if (list.size() > 0) {
            taskNo = list.get(0).getAllotCode();
        }
        allot.setAllotCode(DateFormatUtil.getWorkOrderno(maxNo, taskNo));
        return allotDao.save(allot);
    }

    @Override
    public R add(AllotDO allot, String body) throws IOException, ClassNotFoundException {
        Map<String, Object> result = Maps.newHashMap();
        // 先验证是否能调拨
        List<AllotItemDO> bodys = JSON.parseArray(body, AllotItemDO.class);
        // 是否为二维码出库
        if (bodys.get(0).getQrcodeId() != null) {
            return this.addByQrcodeId(allot,bodys);
        }

        // 将JSON 解析出来以库存ID 数组 和 数量作为验证条件
        List<Map<String, Object>> params = stockListParam(bodys);
        // 获取库存明细表中保存的库位id数组
        List<Long> stockIds = stockOutService.getAllStockIds(params);
        if (stockOutService.checkInsert(params, stockIds)) {
            return R.error(messageSourceHandler.getMessage("stock.count.error", null));
        }

        // 库存无误执行下面保存操作
        List<StockDO> stockList = materielService.stockList(stockIds);
        // 设置调拨单据号
        Long singleTypeId = ConstantForGYL.DBDJ;
        Long storageTypeId = ConstantForGYL.DB;
        // 设置审核状态为待审核
        allot.setAuditSign(ConstantForGYL.WAIT_AUDIT);
        // 保存主表数据
        int count = this.save(allot);
        Long allotId = allot.getId();
        result.put("id", allotId);
        // 保存调拨产品
        if (count > 0) {
            List<AllotItemDO> allotItemDOs = new ArrayList<>();
            for (AllotItemDO obj : bodys) {
                obj.setAllotId(allotId);
                // 保存调拨明细表
                allotItemDOs.add(obj);
            }
            int saveAllotDetail = allotItemService.batchInsert(allotItemDOs);
            if (saveAllotDetail == allotItemDOs.size()) {
                List<Pair<List<StockItemDO>, List<StockDO>>> stockInfos = new ArrayList<>();
                for (AllotItemDO allotItemDO : allotItemDOs) {
                    BigDecimal proCount = allotItemDO.getCount();
                    Pair<List<StockItemDO>, List<StockDO>> pair = stockOutService.saveStockDetail(allotId,
                            allotItemDO.getId(), allotItemDO.getStockId(), proCount, singleTypeId, storageTypeId,
                            stockList);
                    stockInfos.add(pair);
                }
                stockOutService.batchSaveStockInfo(stockInfos);
                return R.ok(result);
            }
        }
        return R.error();
    }

    @Override
    public R addByQrcodeId(AllotDO allot, List<AllotItemDO> bodys) throws IOException, ClassNotFoundException {
        // 检查二维码中的数量还足不足
        Map<String,Object> params ;
        for (AllotItemDO itemDO : bodys) {
            params = Maps.newHashMap();
            params.put("id",itemDO.getQrcodeId());
            params.put("remainCount",itemDO.getCount());
            List<QrcodeDO> errorList = qrcodeService.list(params);
            if (errorList.size() > 0) {
                return R.error(messageSourceHandler.getMessage("scm.stock.code.outError", null));
            }
        }
        // 设置调拨单据号
        Long storageTypeId = ConstantForGYL.DB;
        // 设置审核状态为待审核
        allot.setAuditSign(ConstantForGYL.OK_AUDITED);
        allot.setAuditTime(new Date());
        allot.setAuditor(ShiroUtils.getUserId());
        // 保存主表数据
        int count = this.save(allot);
        Long allotId = allot.getId();
        // 保存调拨产品
        List<String> collect = bodys.stream().map(AllotItemDO::getStockId).collect(Collectors.toList());
        Map<String,Object> map = Maps.newHashMap();
        map.put("stockId",collect);
        List<Map<String, Object>> stockListForMap = materielService.stockListForMap(map);

        List<Long>stockIds = Lists.newArrayList();
        for (String s : collect) {
            stockIds.add(Long.parseLong(s));
        }
        List<StockDO> stockList = materielService.stockList(stockIds);
        // 保存调拨产品
        if (count > 0) {
            List<StockDO> insertNewStockList = ListUtils.deepCopy(stockList);
            // 修改原有库存数据
            List<StockItemDO> insertStockItemDOList = Lists.newArrayList();
            StockItemDO stockItemDO;
            for (StockDO stockDO : stockList) {

                stockItemDO = new StockItemDO();
                stockItemDO.setStockId(stockDO.getId());
                stockItemDO.setUnitPrice(stockDO.getUnitPrice());
                stockItemDO.setCount(stockDO.getCount().multiply(new BigDecimal(-1)));
                stockItemDO.setInOutType(storageTypeId);
                stockItemDO.setSourceType(storageTypeId);
                stockItemDO.setHandleSign(1L);
                insertStockItemDOList.add(stockItemDO);

                stockDO.setCount(BigDecimal.ZERO);
                stockDO.setAvailableCount(BigDecimal.ZERO);
                stockDO.setQrcodeId(0L);
            }

            // 保存调拨后产品的库存数据
            stockService.batchSave(insertNewStockList);
            for (StockDO stockDO : insertNewStockList) {
                stockItemDO = new StockItemDO();
                stockItemDO.setStockId(stockDO.getId());
                stockItemDO.setUnitPrice(stockDO.getUnitPrice());
                stockItemDO.setCount(stockDO.getCount());
                stockItemDO.setInOutType(storageTypeId);
                stockItemDO.setSourceType(storageTypeId);
                stockItemDO.setHandleSign(1L);
                insertStockItemDOList.add(stockItemDO);
            }

            qrcodeService.transferHandler(insertNewStockList,bodys);
            stockService.batchUpdate(stockList);
            stockOutService.batchInsertStockDetailDO(insertStockItemDOList);


            List<AllotItemDO> insertAllotItemDOs = Lists.newArrayList();
            AllotItemDO allotItemDO;
            for (Map<String, Object> stringObjectMap : stockListForMap) {
                allotItemDO = new AllotItemDO();
                String stockId = stringObjectMap.get("id").toString();
                for (AllotItemDO obj : bodys) {
                    // 保存调拨明细表
                    if (Arrays.asList(stockId.split(",")).contains(obj.getStockId())) {
                        if (allotItemDO.getQrcodeId()!=null) {
                            allotItemDO.setCount(allotItemDO.getCount().add(obj.getCount()));
                            continue;
                        }
                        BeanUtils.copyProperties(obj,allotItemDO);
                        allotItemDO.setQrcodeId(obj.getQrcodeId());
                    }

                }
                allotItemDO.setAllotId(allotId);
                allotItemDO.setStockId(stockId);
                insertAllotItemDOs.add(allotItemDO);
            }

            allotItemService.batchInsert(insertAllotItemDOs);
            return R.ok();
        }
        return R.error();
    }

    private List<Map<String, Object>> stockListParam(List<AllotItemDO> bodys) {
        List<Map<String, Object>> params = new ArrayList<>();
        Map<String, Object> param;
        for (AllotItemDO allotItemDO : bodys) {
            param = Maps.newHashMapWithExpectedSize(2);
            param.put("id", allotItemDO.getStockId());
            param.put("outCount", allotItemDO.getCount());
            params.add(param);
        }
        return params;
    }

    @Override
    public R audit(Long id, Long storageType) {
        AllotDO allot = get(id);
        Long auditSignId = allot.getAuditSign();
        int count;
        // 若是待审核状态设置审核状态为已审核
        if (Objects.equals(auditSignId, ConstantForGYL.WAIT_AUDIT)) {
            allot.setAuditSign(ConstantForGYL.OK_AUDITED);
            // 设置审核时间
            allot.setAuditTime(new Date());
            // 设置审核人
            allot.setAuditor(ShiroUtils.getUserId());
            count = update(allot);
            // 保存库存明细
            this.saveStockInfo(id, storageType);
            if (count > 0) {
                return R.ok();
            }
        }
        return R.error(messageSourceHandler.getMessage("common.duplicate.approved", null));
    }

    @Override
    public boolean isQrcode(Long id) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("allotId",id);
        List<AllotItemDO> list = allotItemService.list(map);
        if (list.size() > 0) {
            return list.get(0).getQrcodeId() != null;
        }
        return true;
    }

    @Override
    public R reverseAudit(Long id, Long storageType) {
        if (this.isQrcode(id)) {
            return R.error(messageSourceHandler.getMessage("scm.stock.code.operateError", null));
        }
        AllotDO allot = get(id);
        Long auditSignId = allot.getAuditSign();
        int count;
        // 若是已审核状态设置审核状态为待审核
        if (Objects.equals(auditSignId, ConstantForGYL.OK_AUDITED)) {
            // 检测调拨产品是否已出库
            if (this.isOut(id)) {
                return R.error(messageSourceHandler.getMessage("allot.audit.material.isOut", null));
            }
            // 操作库存数据
            stockOutService.reverseAudit(id, storageType);
            // 修改调拨单据状态
            allot.setAuditSign(ConstantForGYL.WAIT_AUDIT);
            allot.setAuditor(0L);
            count = this.update(allot);
            if (count > 0) {
                return R.ok();
            }
        }
        return R.error(messageSourceHandler.getMessage("common.massge.faildRollBackAudit", null));
    }

    private boolean isOut(Long id) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("inheadId", id);
        param.put("inOutType", ConstantForGYL.DB);

        // 获取所有的库存明细变化
        List<StockItemDO> stockDetailDO = stockDetailService.list(param);
        // 校验调拨后新添的数据是否已出库
        List<Long> allot = Lists.newArrayList();
        for (StockItemDO stockDetail : stockDetailDO) {
            if (stockDetail.getCount().compareTo(BigDecimal.ZERO) > 0) {
                allot.add(stockDetail.getStockId());
            }
        }
        if (allot.size() > 0) {
            int countOfStockId = stockOutDao.countOfStockId(allot);
            return countOfStockId != allot.size();
        }
        return false;
    }

    @Override
    public void saveStockInfo(Long id, Long storageType) {
        Long sourceType = ConstantForGYL.DBDJ;
        String stockCode = ConstantForGYL.DB_PREFIX + DateFormatUtil.getWorkOrderno();
        Map<String, Object> param = Maps.newHashMap();
        param.put("allotId", id);
        List<AllotItemDO> list = allotItemService.list(param);
        // 将库存明细表数据设置成生效状态并在库存表中减去相应数量
        stockOutService.insertStockInfo(id, storageType);
        List<StockItemDO> stockDetailDOs = new ArrayList<>();
        StockItemDO stockDetailDO;
        for (AllotItemDO allotItemDO : list) {
            stockDetailDO = new StockItemDO();
            // 获取要更改库存ID
            Long stockId = allotItemDO.getStockId().contains(",")
                    ? Long.parseLong(allotItemDO.getStockId().split(",")[0])
                    : Long.parseLong(allotItemDO.getStockId());
            // 获得新的仓库与库位
            Long newLocation = allotItemDO.getInLocation();
            Long warehouse = allotItemDO.getInFacility();
            // 保存旧的产品数据到新的仓位
            StockDO newStock = this.saveNewStock(stockService.get(stockId), allotItemDO.getCount(), stockCode, newLocation, warehouse);
            if (newStock == null) {
                continue;
            }
            this.stockOutService.saveStockDetailInfo(stockDetailDO, allotItemDO.getAllotId(), allotItemDO.getId(), newStock,
                    allotItemDO.getCount(), storageType, sourceType);
            stockDetailDOs.add(stockDetailDO);
        }
        stockOutService.batchInsertStockDetailDO(stockDetailDOs);

    }

    @Override
    public StockDO saveNewStock(StockDO stockDO, BigDecimal change, String stockCode, Long location,
                                Long warehouse) {
        StockDO newStock = new StockDO();
        newStock.setEnteringTime(new Date());
        newStock.setMaterielId(stockDO.getMaterielId());
        newStock.setCode(stockCode);
        newStock.setBatch(stockDO.getBatch());
        newStock.setAvailableCount(change);
        newStock.setCount(change);
        newStock.setUnitPrice(stockDO.getUnitPrice());
        newStock.setAmount(stockDO.getUnitPrice().multiply(change));
        newStock.setSourceCompany(stockDO.getSourceCompany());
        newStock.setWarehouse(warehouse);
        newStock.setWarehLocation(location);
        int count = stockService.save(newStock);
        return count > 0 ? newStock : null;
    }

    @Override
    public Map<String, Object> getDetail(Long id) {
        Map<String, Object> results = Maps.newHashMap();
        results.put("allot", allotDao.getDetail(id));
        List<Map<String, Object>> item = allotItemService.getDetail(id);
        if (item.size() > 0) {
            Map<String,Object> params;
            for (Map<String, Object> map : item) {
                params = Maps.newHashMap();
                params.put("materielId",map.get("materielId"));
                params.put("batch",map.get("batch"));
                params.put("locationId",map.get("outLocation"));
                params.put("facilityId",map.get("facId"));
                params.put("isPC",1);
                List<Map<String, Object>> stockListForMap = materielService.stockListForMap(params);
                if (stockListForMap.size() > 0) {
                    map.put("availableCount",stockListForMap.get(0).get("availableCount"));
                }
            }
        }

        results.put("item", item);
        return results;
    }

    @Override
    public R edit(AllotDO allotDO, String body, Long[] itemIds) {
        Long id = allotDO.getId();
        if (this.get(id).getAuditSign().equals(ConstantForGYL.OK_AUDITED)) {
            return R.error(messageSourceHandler.getMessage("common.approved.update.disabled", null));
        }
        if (itemIds.length > 0) {
            allotItemService.batchRemove(itemIds);
        }

        Long storageType = ConstantForGYL.DB;
        // 修改前先删除库存记录
        stockOutService.removeStockDetail(id, storageType);
        // 修改出库产品
        List<AllotItemDO> itemDOS = JSON.parseArray(body, AllotItemDO.class);
        // 将JSON 解析出来以库存ID 数组 和 数量作为验证条件
        List<Map<String, Object>> stockListParam = stockListParam(itemDOS);
        List<Long> stockIds = stockOutService.getAllStockIds(stockListParam);
        if (stockOutService.checkInsert(stockListParam, stockIds)) {
            return R.error(messageSourceHandler.getMessage("allot.stock.error", null));
        }
        // 修改主表数据
        int count = this.allotDao.update(allotDO);
        // 库存无误执行下面修改操作
        if (count > 0) {
            Long singleType = ConstantForGYL.DBDJ;
            List<StockDO> stockList = materielService.stockList(stockIds);
            List<AllotItemDO> AllotItemDOs = new ArrayList<>();
            List<Pair<List<StockItemDO>, List<StockDO>>> stockInfos = new ArrayList<>();
            for (AllotItemDO obj : itemDOS) {
                // 如果传入数据id为null 则该数据已在表中存在执行保存，若有值则修改
                obj.setAllotId(id);
                if (obj.getId() == null) {
                    allotItemService.save(obj);
                    // 库存数量
                    BigDecimal proCount = obj.getCount();
                    // 保存库存明细表数据
                    Pair<List<StockItemDO>, List<StockDO>> pair = stockOutService.saveStockDetail(id, obj.getId(),
                            obj.getStockId(), proCount, singleType, storageType, stockList);
                    stockInfos.add(pair);
                    continue;
                }
                BigDecimal proCount = obj.getCount();
                // 修改出库明细表数据
                AllotItemDOs.add(obj);
                Pair<List<StockItemDO>, List<StockDO>> pair = stockOutService.saveStockDetail(id, obj.getId(),
                        obj.getStockId(), proCount, singleType, storageType, stockList);
                stockInfos.add(pair);
            }
            if (allotItemService.batchUpdate(AllotItemDOs) > 0) {
                stockOutService.batchSaveStockInfo(stockInfos);
                return R.ok();
            }
        }
        return R.error();

    }

    @Override
    public int update(AllotDO allot) {
        return allotDao.update(allot);
    }

    @Override
    public int remove(Long id) {
        this.stockOutService.removeStockDetail(id, ConstantForGYL.DB);
        return allotDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        for (Long id : ids) {
            this.stockOutService.removeStockDetail(id, ConstantForGYL.DB);
        }
        return allotDao.batchRemove(ids);
    }

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> map) {
        return allotDao.listForMap(map);
    }

    @Override
    public int countForMap(Map<String, Object> map) {
        return allotDao.countForMap(map);
    }

}
