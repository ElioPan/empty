package com.ev.report.service.impl;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            Map<String, Map<String, BigDecimal>> initialCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("initialCount")),BigDecimal::add)
                    ));

            // 期初金额
            Map<String, Map<String, BigDecimal>> initialAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("initialAmount")),BigDecimal::add)
                    ));
            // 本期入库数量
            Map<String, Map<String, BigDecimal>> inCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("inCount")))
                    ));
            // 入库金额
            Map<String, Map<String, BigDecimal>> inAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("inAmount"))))
                    );
            // 本期出库数量
            Map<String, Map<String, BigDecimal>> outCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("outCount"))))
                    );
            // 出库金额
            Map<String, Map<String, BigDecimal>> outAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("outAmount"))))
                    );
            // 结存数量
            Map<String, Map<String, BigDecimal>> finalCountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("finalCount"))))
                    );
            // 结存金额
            Map<String, Map<String, BigDecimal>> finalAmountMap = stockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> MathUtils.getBigDecimal(v.get("finalAmount"))))
                    );

            Map<String, Object> map;
            Map<String, BigDecimal> initialCountForPeriod;
            Map<String, BigDecimal> initialAmountForPeriod;
            Map<String, BigDecimal> inCountForPeriod;
            Map<String, BigDecimal> inAmountForPeriod;
            Map<String, BigDecimal> outCountForPeriod;
            Map<String, BigDecimal> outAmountForPeriod;
            Map<String, BigDecimal> finalCountForPeriod;
            Map<String, BigDecimal> finalAmountForPeriod;

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
                        map.put("period", periodParam.substring(0,7));
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
                    map.put("period", periodParam.substring(0,7) + ConstantForReport.TOTAL_SUFFIX);
                    map.put("times", periodMap.get(periodParam));
                    map.put("initialCount", initialCountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("initialAmount", initialAmountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("inCount", inCountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("inAmount", inAmountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("outCount", outCountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("outAmount", outAmountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("finalCount", finalCountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
                    map.put("finalAmount", finalAmountForPeriod
                            .values()
                            .stream()
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
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
        Map<String, BigDecimal> initialCountMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> MathUtils.getBigDecimal(v.get("initialCount"))));

        Map<String, BigDecimal> initialAmountMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> MathUtils.getBigDecimal(v.get("initialAmount"))));

        Map<String, String> initialPeriodMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> v.get("period").toString().substring(0, 7)));

        Map<String, BigDecimal> initialUnitPriceMap = Maps.newHashMap();
        for (String k : initialAmountMap.keySet()) {
            BigDecimal amount = initialAmountMap.get(k);
            BigDecimal count = initialCountMap.get(k);
            if (count.compareTo(BigDecimal.ZERO) == 0) {
                initialUnitPriceMap.put(k, BigDecimal.ZERO);
            } else {
                initialUnitPriceMap.put(k, amount.divide(count, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP));
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
        BigDecimal initialCount;
        BigDecimal initialAmount;
        // 计算
        BigDecimal newInitialCount;
        BigDecimal newInitialAmount;
        BigDecimal newInitialUnitPrice;
        // 出入库
        BigDecimal count;
        BigDecimal amount;
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
                    newInitialCount = initialCount.subtract(count) ;
                    newInitialAmount = initialAmount.subtract(amount) ;
                    groupByStockVO.setOutCount(count);
                    groupByStockVO.setOutAmount(amount);
                } else {
                    newInitialCount = initialCount.add(count) ;
                    newInitialAmount = initialAmount .add(amount) ;
                    groupByStockVO.setInCount(count);
                    groupByStockVO.setInAmount(amount);
                }
                groupByStockVO.setUnitPrice(unitPrice);
                groupByStockVO.setBalanceCount(newInitialCount);
                groupByStockVO.setBalanceAmount(newInitialAmount);
                newInitialUnitPrice = newInitialAmount.divide(newInitialCount, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
                groupByStockVO.setBalanceUnitPrice(newInitialUnitPrice);

            }
            InOutStockItemVO inOutStockItem = groupByStockVOS.get(groupByStockVOS.size() - 1);
            inOutStockItemVO = new InOutStockItemVO();
            BeanUtils.copyProperties(inOutStockItem, inOutStockItemVO);
            inOutStockItemVO.setStorageTypeName("本期合计");
            // 日期
            inOutStockItemVO.setInOutTime(DatesUtil.getSupportBeginDayOfMonthToDate(inOutStockItemVO.getInOutTime()));

            // 收入数量
            BigDecimal totalInCount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getCount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setInCount(totalInCount);
            // 收入金额合计
            BigDecimal totalInAmount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setInAmount(totalInAmount);
            // 合计收入单价
            BigDecimal totalInUnitPrice = totalInAmount.divide(totalInCount, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
            inOutStockItemVO.setInUnitPrice(totalInUnitPrice);
            // 发出数量
            BigDecimal totalOutCount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getCount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setOutCount(totalOutCount);
            // 发出金额合计
            BigDecimal totalOutAmount = groupByStockVOS
                    .stream()
                    .map(InOutStockItemVO::getAmount)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setOutAmount(totalOutAmount);
            // 合计发出单价
            BigDecimal totalOutUnitPrice = totalOutAmount.divide(totalOutCount, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
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
            Map<Long, BigDecimal> countMap = StockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockOutItemVO::getMaterielId
                            , StockOutItemVO::getCount
                            , BigDecimal::add));
            // 出库金额
            Map<Long, BigDecimal> amountMap = StockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockOutItemVO::getMaterielId
                            , StockOutItemVO::getAmount
                            , BigDecimal::add));

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


    @Override
    public void  processingExport(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String fileName, List<Map<String, Object>> dateList, String chineseName){
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, chineseName);
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }





}
