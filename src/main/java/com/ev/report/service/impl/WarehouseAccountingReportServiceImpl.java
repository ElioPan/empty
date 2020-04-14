package com.ev.report.service.impl;

import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.MathUtils;
import com.ev.report.dao.SmartManufacturingAccountingReportDao;
import com.ev.report.dao.WarehouseAccountingReportDao;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;
import com.ev.scm.service.StockAnalysisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehouseAccountingReportServiceImpl implements WarehouseAccountingReportService {
    @Autowired
    private WarehouseAccountingReportDao reportDao;
    @Autowired
    private SmartManufacturingAccountingReportDao smartManufacturingReportDao;
    @Autowired
    private StockAnalysisService stockAnalysisService;
    @Autowired
    private DictionaryService dictionaryService;


    @Override
    public List<StockOutItemVO> stockOutItem(Map<String, Object> params) {
        return smartManufacturingReportDao.stockOutItem(params);
    }

    @Override
    public List<StockInItemVO> stockInItem(Map<String, Object> params) {
        return smartManufacturingReportDao.stockInItem(params);
    }

    @Override
    public List<InOutStockItemVO> inOutStockItem(Map<String, Object> params) {
        return reportDao.inOutStockItem(params);
    }

    @Override
    public List<Map<String, Object>> stockList(Map<String, Object> params) {
        return reportDao.stockList(params);
    }

    @Override
    public List<Map<String, Object>> pickingSummary(Map<String, Object> params) {
        return reportDao.pickingSummary(params);
    }

    @Override
    public Pair<List<Map<String, Object>>, Map<String, Object>> inOutSummaryResult(int showPeriodTotalInt, int showMaterielTotalInt, int showItemInt, String startTime, String endTime, Long materielType, Long materielId) {

        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showMaterielTotal = showMaterielTotalInt == 1;
        boolean showPeriodTotal = showPeriodTotalInt == 1;
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> stockList = stockAnalysisService.listForMap(params);
        Map<String, Object> totalResult = stockAnalysisService.countForTotal(params);
        if (stockList.size() > 0) {
            List<Map<String, Object>> showList = Lists.newArrayList();

            List<String> periodList = stockList
                    .stream()
                    .map(e -> e.get("period").toString())
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, String> materielIdMap = stockList
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("materielId").toString(),
                            v -> v.get("materielName").toString(), (v1, v2) -> v1));

            // 获取期间对应的秒数
            Map<String, Long> periodMap = stockList
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("period").toString(),
                            v -> Long.parseLong(v.get("times").toString()), (v1, v2) -> v1));

            // 获取最大的一个物料ID
            long maxMaterielId = Long.parseLong(materielIdMap
                    .keySet()
                    .stream()
                    .max(Comparator.comparing(Long::parseLong))
                    .orElse("0")
            );


            // 期初数量
            Map<String, Map<String, Double>> initialCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("initialCount").toString()))))
                    );

            // 期初金额
            Map<String, Map<String, Double>> initialAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("initialAmount").toString()))))
                    );
            // 本期入库数量
            Map<String, Map<String, Double>> inCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("inCount").toString()))))
                    );
            // 入库金额
            Map<String, Map<String, Double>> inAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("inAmount").toString()))))
                    );
            // 本期出库数量
            Map<String, Map<String, Double>> outCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("outCount").toString()))))
                    );
            // 出库金额
            Map<String, Map<String, Double>> outAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("outAmount").toString()))))
                    );
            // 结存数量
            Map<String, Map<String, Double>> finalCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("finalCount").toString()))))
                    );
            // 结存金额
            Map<String, Map<String, Double>> finalAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.groupingBy(k2 -> k2.get("materielId").toString()
                                    , Collectors.summingDouble(v -> Double.parseDouble(v.get("finalAmount").toString()))))
                    );

            Map<String, Object> map;
            Map<String, Double> initialCountForPeriod;
            Map<String, Double> initialAmountForPeriod;
            Map<String, Double> inCountForPeriod;
            Map<String, Double> inAmountForPeriod;
            Map<String, Double> outCountForPeriod;
            Map<String, Double> outAmountForPeriod;
            Map<String, Double> finalCountForPeriod;
            Map<String, Double> finalAmountForPeriod;

            for (String periodParam : periodList) {
                initialCountForPeriod = initialCountMap.get(periodParam);
                initialAmountForPeriod = initialAmountMap.get(periodParam);
                inCountForPeriod = inCountMap.get(periodParam);
                inAmountForPeriod = inAmountMap.get(periodParam);
                outCountForPeriod = outCountMap.get(periodParam);
                outAmountForPeriod = outAmountMap.get(periodParam);
                finalCountForPeriod = finalCountMap.get(periodParam);
                finalAmountForPeriod = finalAmountMap.get(periodParam);

                if (showMaterielTotal) {
                    for (String materielIdParam : initialCountForPeriod.keySet()) {
                        map = Maps.newHashMap();
                        map.put("materielId", materielIdParam);
                        // 标记颜色 1 为中间状态
                        map.put("sign", 1);
                        // 排序号
                        map.put("sortNo", 1);
                        map.put("times", periodMap.get(periodParam));
                        map.put("materielName", materielIdMap.get(materielIdParam) + ConstantForReport.TOTAL_SUFFIX);
                        map.put("initialCount", initialCountForPeriod.get(materielIdParam));
                        map.put("initialAmount", initialAmountForPeriod.get(materielIdParam));
                        map.put("inCount", inCountForPeriod.get(materielIdParam));
                        map.put("inAmount", inAmountForPeriod.get(materielIdParam));
                        map.put("outCount", outCountForPeriod.get(materielIdParam));
                        map.put("outAmount", outAmountForPeriod.get(materielIdParam));
                        map.put("finalCount", finalCountForPeriod.get(materielIdParam));
                        map.put("finalAmount", finalAmountForPeriod.get(materielIdParam));
                        showList.add(map);
                    }
                }

                if (showPeriodTotal) {
                    map = Maps.newHashMap();
                    map.put("materielId", maxMaterielId);
                    // 标记颜色 1 为中间状态
                    map.put("sign", ConstantForReport.COLOUR_END);
                    // 排序号
                    map.put("sortNo", 2);
                    map.put("period", periodParam + ConstantForReport.TOTAL_SUFFIX);
                    map.put("times", periodMap.get(periodParam));
                    map.put("initialCount", initialCountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("initialAmount", initialAmountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("inCount", inCountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("inAmount", inAmountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("outCount", outCountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("outAmount", outAmountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("finalCount", finalCountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    map.put("finalAmount", finalAmountForPeriod
                            .values()
                            .stream()
                            .reduce(Double::sum)
                            .orElse(0.0d));
                    showList.add(map);
                }

            }


            if (showItem) {
                showList.addAll(stockList);
            }

            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("materielId").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("times").toString())))
                    .collect(Collectors.toList());

            return Pair.of(collect,totalResult);

        }
        return null;
    }

    @Override
    public Pair<List<InOutStockItemVO>, Map<String, Object>> inOutStockItemResult(String startTime, String endTime, Long materielType, Long materielId) {
        Map<String, Object> params = Maps.newHashMap();
        Date startPeriod = DateFormatUtil.getDateByParttern(startTime, "yyyy-MM-dd");
        Date endPeriod = DateFormatUtil.getDateByParttern(endTime, "yyyy-MM-dd");
        if (materielId == null || endPeriod == null || startPeriod == null) {
            return null;
        }
        params.put("materielType", materielType);
        params.put("materielId", materielId);

        params.put("startTime", DatesUtil.getSupportBeginDayOfMonth(startPeriod));
        params.put("endTime", DatesUtil.getSupportEndDayOfMonth(endPeriod));

        List<Map<String, Object>> initData = stockAnalysisService.listForMapGroupMateriel(params);
        Map<String, Double> initialCountMap = initData.stream()
                .collect(Collectors.groupingBy(k -> k.get("period").toString()
                        , Collectors.summingDouble(v -> Double.parseDouble(v.get("initialCount").toString()))));

        Map<String, Double> initialAmountMap = initData.stream()
                .collect(Collectors.groupingBy(k -> k.get("period").toString()
                        , Collectors.summingDouble(v -> Double.parseDouble(v.get("initialAmount").toString()))));

        Map<String, String> initialPeriodMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> v.get("period").toString().substring(0, 7)));

        Map<String, BigDecimal> initialUnitPriceMap = Maps.newHashMap();
        for (String k : initialAmountMap.keySet()) {
            Double amount = initialAmountMap.get(k);
            Double count = initialCountMap.get(k);
            if (count == 0.0d) {
                initialUnitPriceMap.put(k, BigDecimal.ZERO);
            } else {
                initialUnitPriceMap.put(k, BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(count), Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP));
            }

        }
        List<InOutStockItemVO> totalList = Lists.newArrayList();

        InOutStockItemVO initInOutStockItemVO;
        for (String k : initialUnitPriceMap.keySet()) {
            initInOutStockItemVO = new InOutStockItemVO();
            initInOutStockItemVO.setStorageTypeName("期初结存");
            initInOutStockItemVO.setBalanceAmount(initialAmountMap.get(k));
            initInOutStockItemVO.setBalanceCount(initialCountMap.get(k));
            initInOutStockItemVO.setBalanceUnitPrice(initialUnitPriceMap.get(k));
            // 0 置顶
            initInOutStockItemVO.setSortNo(0);
            initInOutStockItemVO.setPeriod(initialPeriodMap.get(k));
            initInOutStockItemVO.setInOutTime(DateFormatUtil.getDateByParttern(k, "yyyy-MM-dd"));
            totalList.add(initInOutStockItemVO);
        }

        List<InOutStockItemVO> inOutStockItemVOS = this.inOutStockItem(params);

        Map<String, List<InOutStockItemVO>> groupByPeriod = inOutStockItemVOS
                .stream()
                .collect(Collectors.groupingBy(e -> e.getPeriod() + "-01"));
        List<InOutStockItemVO> groupByStockVOS;
        Double initialCount;
        Double initialAmount;
        // 计算
        double newInitialCount;
        double newInitialAmount;
        BigDecimal newInitialUnitPrice;
        // 出入库
        Double count;
        Double amount;
        BigDecimal unitPrice;
        // 判断是否为出库
        boolean isOut;
        // 合计行
        InOutStockItemVO inOutStockItemVO;

        for (String period : groupByPeriod.keySet()) {
            groupByStockVOS = groupByPeriod.get(period);
            // 期初数量
            initialCount = initialCountMap.get(period);
            // 期初金额
            initialAmount = initialAmountMap.get(period);
            // 将数据按时间升序排列
            groupByStockVOS.sort(Comparator.comparing(InOutStockItemVO::getInOutTime));

            for (InOutStockItemVO groupByStockVO : groupByStockVOS) {
                isOut = groupByStockVO.getType() == 1;
                count = groupByStockVO.getCount();
                amount = groupByStockVO.getAmount();
                unitPrice = groupByStockVO.getUnitPrice();
                if (isOut) {
                    newInitialCount = initialCount - count;
                    newInitialAmount = initialAmount - amount;
                    groupByStockVO.setOutCount(count);
                    groupByStockVO.setOutAmount(amount);
                } else {
                    newInitialCount = initialCount + count;
                    newInitialAmount = initialAmount + amount;
                    groupByStockVO.setInCount(count);
                    groupByStockVO.setInAmount(amount);
                }
                groupByStockVO.setUnitPrice(unitPrice);
                groupByStockVO.setBalanceCount(newInitialCount);
                groupByStockVO.setBalanceAmount(newInitialAmount);
                newInitialUnitPrice = BigDecimal.valueOf(newInitialAmount).divide(BigDecimal.valueOf(newInitialCount), Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
                groupByStockVO.setBalanceUnitPrice(newInitialUnitPrice);

            }
            InOutStockItemVO inOutStockItem = groupByStockVOS.get(groupByStockVOS.size() - 1);
            inOutStockItemVO = new InOutStockItemVO();
            BeanUtils.copyProperties(inOutStockItem, inOutStockItemVO);
            inOutStockItemVO.setStorageTypeName("本期合计");
            // 日期
            inOutStockItemVO.setInOutTime(DatesUtil.getSupportBeginDayOfMonthToDate(inOutStockItemVO.getInOutTime()));

            // 收入数量
            Double totalInCount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getCount)
                    .reduce(Double::sum)
                    .orElse(0.0d);
            inOutStockItemVO.setInCount(totalInCount);
            // 收入金额合计
            Double totalInAmount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getAmount)
                    .reduce(Double::sum)
                    .orElse(0.0d);
            inOutStockItemVO.setInAmount(totalInAmount);
            // 合计收入单价
            BigDecimal totalInUnitPrice = BigDecimal.valueOf(totalInAmount).divide(BigDecimal.valueOf(totalInCount), Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
            inOutStockItemVO.setInUnitPrice(totalInUnitPrice);
            // 发出数量
            Double totalOutCount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getCount)
                    .reduce(Double::sum)
                    .orElse(0.0d);
            inOutStockItemVO.setOutCount(totalOutCount);
            // 发出金额合计
            Double totalOutAmount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getAmount)
                    .reduce(Double::sum)
                    .orElse(0.0d);
            inOutStockItemVO.setOutAmount(totalOutAmount);
            // 合计发出单价
            BigDecimal totalOutUnitPrice = BigDecimal.valueOf(totalOutAmount).divide(BigDecimal.valueOf(totalOutCount), Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
            inOutStockItemVO.setOutUnitPrice(totalOutUnitPrice);
            inOutStockItemVO.setSortNo(2);
            groupByStockVOS.add(inOutStockItemVO);

        }

        for (List<InOutStockItemVO> value : groupByPeriod.values()) {
            totalList.addAll(value);
        }
        List<InOutStockItemVO> collect = totalList
                .stream()
                .sorted(Comparator.comparing(InOutStockItemVO::getSortNo))
                .sorted(Comparator.comparing(InOutStockItemVO::getInOutTime))
                .collect(Collectors.toList());


        if (collect.size() > 0) {
           return Pair.of(collect,Maps.newHashMap());
        }
        return null;
    }

    @Override
    public Pair<List<StockInItemVO>, Map<String, Object>> inStockItemResult(int showTotalInt, int showItemInt, String startTime, String endTime, Long materielType, Long materielId) {
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        params.put("auditSign", ConstantForMES.OK_AUDITED);

        List<StockInItemVO> stockInItemVOS = this.stockInItem(params);
        if (stockInItemVOS.size() > 0) {

            List<StockInItemVO> showList = Lists.newArrayList();

            // 入库数量
            Map<Long, BigDecimal> countMap = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockInItemVO::getMaterielId
                            , StockInItemVO::getCount
                            , BigDecimal::add));
            // 入库金额
            Map<Long, BigDecimal> amountMap = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockInItemVO::getMaterielId
                            , StockInItemVO::getAmount
                            , BigDecimal::add));

            // 物料名
            Map<Long, String> materielMap = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockInItemVO::getMaterielId
                            , StockInItemVO::getMaterielName
                            , (v1, v2) -> v1));
            // 获取最大一个时间
            long maxTime = Objects.requireNonNull(DateFormatUtil.getDateByParttern(endTime, "yyyy-MM-dd")).getTime();

            maxTime = maxTime + (24 * 3600 * 1000);

            if (showTotal) {
                StockInItemVO stockInItemVO;
                for (Long materiel : amountMap.keySet()) {
                    stockInItemVO = new StockInItemVO();
                    stockInItemVO.setMaterielId(materiel);
                    stockInItemVO.setCount(countMap.get(materiel));
                    stockInItemVO.setAmount(amountMap.get(materiel));
                    stockInItemVO.setInOutTime(new Date(maxTime));
                    // 标记颜色
                    stockInItemVO.setSign(ConstantForReport.COLOUR_END);
                    stockInItemVO.setMaterielName(materielMap.get(materiel) + ConstantForReport.TOTAL_SUFFIX);
                    // 排序号
                    stockInItemVO.setSortNo(1);

                    showList.add(stockInItemVO);
                }
            }
            if (showItem) {
                showList.addAll(stockInItemVOS);
            }
            List<StockInItemVO> collect = showList.stream()
                    .sorted(Comparator.comparing(StockInItemVO::getSortNo))
                    .sorted(Comparator.comparing(StockInItemVO::getInOutTime))
                    .sorted(Comparator.comparing(StockInItemVO::getMaterielId))
                    .collect(Collectors.toList());

            for (StockInItemVO stockInItemVO : collect) {
                long time = stockInItemVO.getInOutTime().getTime();
                if (time == maxTime) {
                    stockInItemVO.setInOutTime(null);
                }
            }

            // 合计项
            BigDecimal totalCount = countMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalAmount = amountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);

            Map<String, Object> totalResult = Maps.newHashMap();
            totalResult.put("totalCount", totalCount);
            totalResult.put("totalAmount", totalAmount);

            return Pair.of(collect,totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<StockOutItemVO>, Map<String, Object>> outStockItemResult(int showTotalInt, int showItemInt, String startTime, String endTime, Long materielType, Long materielId) {

        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;

        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<StockOutItemVO> StockOutItemVOS = this.stockOutItem(params);

        if (StockOutItemVOS.size() > 0) {

            List<StockOutItemVO> showList = Lists.newArrayList();

            // 出库数量
            Map<Long, Double> countMap = StockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockOutItemVO::getMaterielId
                            , StockOutItemVO::getCount
                            , Double::sum));
            // 出库金额
            Map<Long, Double> amountMap = StockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockOutItemVO::getMaterielId
                            , StockOutItemVO::getAmount
                            , Double::sum));

            // 物料名
            Map<Long, String> materielMap = StockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockOutItemVO::getMaterielId
                            , StockOutItemVO::getMaterielName
                            , (v1, v2) -> v1));
            // 获取最大一个时间
            long maxTime = Objects.requireNonNull(DateFormatUtil.getDateByParttern(endTime, "yyyy-MM-dd")).getTime();

            maxTime = maxTime + (24 * 3600 * 1000);

            if (showTotal) {
                StockOutItemVO stockOutItemVO;
                for (Long materiel : amountMap.keySet()) {
                    stockOutItemVO = new StockOutItemVO();
                    stockOutItemVO.setCount(countMap.get(materiel));
                    stockOutItemVO.setAmount(amountMap.get(materiel));
                    stockOutItemVO.setOutTime(new Date(maxTime));
                    // 标记颜色
                    stockOutItemVO.setSign(ConstantForReport.COLOUR_END);

                    stockOutItemVO.setMaterielName(materielMap.get(materiel) + ConstantForReport.TOTAL_SUFFIX);
                    stockOutItemVO.setMaterielId(materiel);
                    // 排序号
                    stockOutItemVO.setSortNo(1);

                    showList.add(stockOutItemVO);
                }
            }
            if (showItem) {
                showList.addAll(StockOutItemVOS);
            }
            List<StockOutItemVO> collect = showList.stream()
                    .sorted(Comparator.comparing(StockOutItemVO::getSortNo))
                    .sorted(Comparator.comparing(StockOutItemVO::getOutTime))
                    .sorted(Comparator.comparing(StockOutItemVO::getMaterielId))
                    .collect(Collectors.toList());

            for (StockOutItemVO StockOutItemVO : collect) {
                long time = StockOutItemVO.getOutTime().getTime();
                if (time == maxTime) {
                    StockOutItemVO.setOutTime(null);
                }
            }

            // 合计项
            Double totalCount = countMap
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d);
            Double totalAmount = amountMap
                    .values()
                    .stream()
                    .reduce(Double::sum)
                    .orElse(0.0d);

            Map<String, Object> totalResult = Maps.newHashMap();
            totalResult.put("totalCount", totalCount);
            totalResult.put("totalAmount", totalAmount);
            return Pair.of(collect,totalResult);
        }
        return null;
    }

    @Override
    public Pair<List<Map<String,Object>>, Map<String, Object>> pickingSummaryResult(int showTotalInt, int showItemInt, String startTime, String endTime, Long deptId) {
        Map<String, Object> params = Maps.newHashMap();
        boolean showItem = showItemInt == 1;
        boolean showTotal = showTotalInt == 1;
        params.put("deptId", deptId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<DictionaryDO> feedingTypes = dictionaryService.listByType(ConstantForMES.FEEDING);
        List<Integer> sourceTypes = feedingTypes
                .stream()
                .map(DictionaryDO::getId)
                .collect(Collectors.toList());
        params.put("sourceTypes", sourceTypes);
        List<Map<String, Object>> pickingSummary = this.pickingSummary(params);
        if (pickingSummary.size() > 0) {
            List<Map<String, Object>> showList = Lists.newArrayList();
            // 出库数量
            Map<String, BigDecimal> countMap = pickingSummary
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("productId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("count"))
                            , BigDecimal::add));
            // 出库金额
            Map<String, BigDecimal> amountMap = pickingSummary
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("productId").toString()
                            , v -> MathUtils.getBigDecimal(v.get("amount"))
                            , BigDecimal::add));
            // 产品名
            Map<String, String> productMap = pickingSummary
                    .stream()
                    .collect(Collectors.toMap(
                            k -> k.get("productId").toString()
                            , v -> v.get("productName").toString()
                            , (v1, v2) -> v1));
            if (showTotal) {
                Map<String, Object> map;
                for (String productId : amountMap.keySet()) {
                    map = Maps.newHashMap();
                    map.put("productId", productId);
                    map.put("productName", productMap.get(productId) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("count", countMap.get(productId));
                    map.put("amount", amountMap.get(productId));
                    map.put("sortNo", 1);
                    map.put("sign", ConstantForReport.COLOUR_END);
                    showList.add(map);
                }
            }
            if (showItem) {
                showList.addAll(pickingSummary);
            }
            List<Map<String, Object>> collect = showList.stream()
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                    .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("productId").toString())))
                    .collect(Collectors.toList());

            Map<String, Object> totalResult = Maps.newHashMap();
            BigDecimal totalCount = countMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            BigDecimal totalAmount = amountMap
                    .values()
                    .stream()
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            totalResult.put("totalCount", totalCount);
            totalResult.put("totalAmount", totalAmount);
            return Pair.of(collect,totalResult);
        }
        return null;
    }
}
