package com.ev.report.service.impl;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.BeanUtils;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.MathUtils;
import com.ev.report.dao.WarehouseAccountingReportDao;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;
import com.ev.scm.service.StockAnalysisService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
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
    private StockAnalysisService stockAnalysisService;
    @Autowired
    private DictionaryService dictionaryService;


    @Override
    public List<StockOutItemVO> stockOutItem(Map<String, Object> params) {
        return reportDao.stockOutItem(params);
    }

    @Override
    public List<StockInItemVO> stockInItem(Map<String, Object> params) {
        return reportDao.stockInItem(params);
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

            // 获取期间对应的秒数
            Map<String, Long> periodMap = stockList
                    .stream()
                    .collect(Collectors.toMap(k -> k.get("period").toString(),
                            v -> Long.parseLong(v.get("times").toString()), (v1, v2) -> v1));

            // 获取最大的一个物料ID
//            long maxMaterielId = Long.parseLong(materielIdMap
//                    .keySet()
//                    .stream()
//                    .max(Comparator.comparing(Long::parseLong))
//                    .orElse("0")
//            );
            long maxMaterielId = stockList
                    .stream()
                    .map(v->Long.parseLong(v.get("materielId").toString()))
                    .max(Comparator.comparing(v->v))
                    .orElse(0L);
            //  计算数量
            ArrayList<Map<String, Object>> cloneStockList = BeanUtils.clone((ArrayList<Map<String, Object>>) stockList);
            Map<String, Map<String, Map<String, Object>>> reduceMap = cloneStockList
                    .stream()
                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
                                    , v -> v,
                                    (v1, v2) -> {
                                        v1.put("initialCount", MathUtils.getBigDecimal(v1.get("initialCount")).add(MathUtils.getBigDecimal(v2.get("initialCount"))));
                                        v1.put("initialAmount", MathUtils.getBigDecimal(v1.get("initialAmount")).add(MathUtils.getBigDecimal(v2.get("initialAmount"))));
                                        v1.put("inCount", MathUtils.getBigDecimal(v1.get("inCount")).add(MathUtils.getBigDecimal(v2.get("inCount"))));
                                        v1.put("inAmount", MathUtils.getBigDecimal(v1.get("inAmount")).add(MathUtils.getBigDecimal(v2.get("inAmount"))));
                                        v1.put("outCount", MathUtils.getBigDecimal(v1.get("outCount")).add(MathUtils.getBigDecimal(v2.get("outCount"))));
                                        v1.put("outAmount", MathUtils.getBigDecimal(v1.get("outAmount")).add(MathUtils.getBigDecimal(v2.get("outAmount"))));
                                        v1.put("finalCount", MathUtils.getBigDecimal(v1.get("finalCount")).add(MathUtils.getBigDecimal(v2.get("finalCount"))));
                                        v1.put("finalAmount", MathUtils.getBigDecimal(v1.get("finalAmount")).add(MathUtils.getBigDecimal(v2.get("finalAmount"))));
                                        return v1;
                                    }
                            )));

//            // 期初数量
//            Map<String, Map<String, BigDecimal>> initialCountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("initialCount")),BigDecimal::add)
//                    ));
//
//            // 期初金额
//            Map<String, Map<String, BigDecimal>> initialAmountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("initialAmount")),BigDecimal::add)
//                    ));
//            // 本期入库数量
//            Map<String, Map<String, BigDecimal>> inCountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("inCount")),BigDecimal::add))
//                    );
//            // 入库金额
//            Map<String, Map<String, BigDecimal>> inAmountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("inAmount")),BigDecimal::add))
//                    );
//            // 本期出库数量
//            Map<String, Map<String, BigDecimal>> outCountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("outCount")),BigDecimal::add))
//                    );
//            // 出库金额
//            Map<String, Map<String, BigDecimal>> outAmountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("outAmount")),BigDecimal::add))
//                    );
//            // 结存数量
//            Map<String, Map<String, BigDecimal>> finalCountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("finalCount")),BigDecimal::add))
//                    );
//            // 结存金额
//            Map<String, Map<String, BigDecimal>> finalAmountMap = stockList
//                    .stream()
//                    .collect(Collectors.groupingBy(k1 -> k1.get("period").toString()
//                            , Collectors.toMap(k2 -> k2.get("materielId").toString()
//                                    , v -> MathUtils.getBigDecimal(v.get("finalAmount")),BigDecimal::add))
//                    );

            Map<String, Object> map;
//            Map<String, BigDecimal> initialCountForPeriod;
//            Map<String, BigDecimal> initialAmountForPeriod;
//            Map<String, BigDecimal> inCountForPeriod;
//            Map<String, BigDecimal> inAmountForPeriod;
//            Map<String, BigDecimal> outCountForPeriod;
//            Map<String, BigDecimal> outAmountForPeriod;
//            Map<String, BigDecimal> finalCountForPeriod;
//            Map<String, BigDecimal> finalAmountForPeriod;
            Map<String, Map<String, Object>> reduceCount;
            for (String periodParam : periodList) {
//                initialCountForPeriod = initialCountMap.get(periodParam);
//                initialAmountForPeriod = initialAmountMap.get(periodParam);
//                inCountForPeriod = inCountMap.get(periodParam);
//                inAmountForPeriod = inAmountMap.get(periodParam);
//                outCountForPeriod = outCountMap.get(periodParam);
//                outAmountForPeriod = outAmountMap.get(periodParam);
//                finalCountForPeriod = finalCountMap.get(periodParam);
//                finalAmountForPeriod = finalAmountMap.get(periodParam);
                reduceCount = reduceMap.get(periodParam);
                if (showMaterielTotal) {
                    for (String materielIdParam : reduceCount.keySet()) {
                        map = BeanUtils.clone((HashMap<String,Object>)reduceCount.get(materielIdParam));
//                      map = Maps.newHashMap(reduceCount.get(materielIdParam));
                        map.remove("batch");
                        map.put("materielId", materielIdParam);
                        // 标记颜色 1 为中间状态
                        map.put("sign", 1);
                        // 排序号
                        map.put("sortNo", 1);
                        map.put("period", periodParam.substring(0,7));
                        map.put("times", periodMap.get(periodParam));
                        map.put("materielName", map.getOrDefault("materielName","") + ConstantForReport.TOTAL_SUFFIX);
//                        map.put("initialCount", initialCountForPeriod.get(materielIdParam));
//                        map.put("initialAmount", initialAmountForPeriod.get(materielIdParam));
//                        map.put("inCount", inCountForPeriod.get(materielIdParam));
//                        map.put("inAmount", inAmountForPeriod.get(materielIdParam));
//                        map.put("outCount", outCountForPeriod.get(materielIdParam));
//                        map.put("outAmount", outAmountForPeriod.get(materielIdParam));
//                        map.put("finalCount", finalCountForPeriod.get(materielIdParam));
//                        map.put("finalAmount", finalAmountForPeriod.get(materielIdParam));
                        showList.add(map);
                    }
                }

                Map<String, Object> periodTotalMap;
                if (showPeriodTotal) {
                    reduceCount =  BeanUtils.clone((HashMap<String, Map<String, Object>>)reduceCount);

                    periodTotalMap = reduceCount
                            .values()
                            .stream()
                            .reduce((v1, v2) -> {
                                v1.put("initialCount", MathUtils.getBigDecimal(v1.get("initialCount")).add(MathUtils.getBigDecimal(v2.get("initialCount"))));
                                v1.put("initialAmount", MathUtils.getBigDecimal(v1.get("initialAmount")).add(MathUtils.getBigDecimal(v2.get("initialAmount"))));
                                v1.put("inCount", MathUtils.getBigDecimal(v1.get("inCount")).add(MathUtils.getBigDecimal(v2.get("inCount"))));
                                v1.put("inAmount", MathUtils.getBigDecimal(v1.get("inAmount")).add(MathUtils.getBigDecimal(v2.get("inAmount"))));
                                v1.put("outCount", MathUtils.getBigDecimal(v1.get("outCount")).add(MathUtils.getBigDecimal(v2.get("outCount"))));
                                v1.put("outAmount", MathUtils.getBigDecimal(v1.get("outAmount")).add(MathUtils.getBigDecimal(v2.get("outAmount"))));
                                v1.put("finalCount", MathUtils.getBigDecimal(v1.get("finalCount")).add(MathUtils.getBigDecimal(v2.get("finalCount"))));
                                v1.put("finalAmount", MathUtils.getBigDecimal(v1.get("finalAmount")).add(MathUtils.getBigDecimal(v2.get("finalAmount"))));
                                return v1;
                            }).orElse(Maps.newHashMap());
                    periodTotalMap.remove("batch");
                    periodTotalMap.remove("materielTypeName");
                    periodTotalMap.remove("materielSerialNo");
                    periodTotalMap.remove("materielName");
                    periodTotalMap.remove("specification");
                    periodTotalMap.remove("unitUomName");
                    periodTotalMap.put("materielId", maxMaterielId);
                    // 标记颜色 1 为中间状态
                    periodTotalMap.put("sign", ConstantForReport.COLOUR_END);
                    // 排序号
                    periodTotalMap.put("sortNo", 2);
                    periodTotalMap.put("period", periodParam.substring(0,7) + ConstantForReport.TOTAL_SUFFIX);
                    periodTotalMap.put("times", periodMap.get(periodParam));
//                    map.put("initialCount", initialCountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("initialAmount", initialAmountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("inCount", inCountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("inAmount", inAmountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("outCount", outCountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("outAmount", outAmountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("finalCount", finalCountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
//                    map.put("finalAmount", finalAmountForPeriod
//                            .values()
//                            .stream()
//                            .reduce(BigDecimal::add)
//                            .orElse(BigDecimal.ZERO));
                    showList.add(periodTotalMap);
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
        Map<String, Map<String,Object>> initialMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> v));
        /*Map<String, BigDecimal> initialCountMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> MathUtils.getBigDecimal(v.get("initialCount"))));

        Map<String, BigDecimal> initialAmountMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> MathUtils.getBigDecimal(v.get("initialAmount"))));

        Map<String, String> initialPeriodMap = initData.stream()
                .collect(Collectors.toMap(k -> k.get("period").toString()
                        , v -> v.get("period").toString()));*/
        List<InOutStockItemVO> totalList = Lists.newArrayList();
        InOutStockItemVO initInOutStockItemVO;
        for (String k : initialMap.keySet()) {
            Map<String, Object>  initMap = initialMap.get(k);
            BigDecimal amount = MathUtils.getBigDecimal(initMap.get("initialAmount"));
            BigDecimal count = MathUtils.getBigDecimal(initMap.get("initialCount"));
            BigDecimal unitPrice = count.compareTo(BigDecimal.ZERO) == 0?BigDecimal.ZERO:amount.divide(count, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);

            initInOutStockItemVO = new InOutStockItemVO();
            initInOutStockItemVO.setStorageTypeName("期初结存");
            initInOutStockItemVO.setBalanceAmount(amount);
            initInOutStockItemVO.setBalanceCount(count);
            initInOutStockItemVO.setBalanceUnitPrice(unitPrice);
            // 0 置顶
            initInOutStockItemVO.setSortNo(0);
            initInOutStockItemVO.setTimes(Long.parseLong(initMap.get("times").toString()));
            initInOutStockItemVO.setPeriod(k);
            totalList.add(initInOutStockItemVO);
        }
        Map<String, InOutStockItemVO> periodForVO = totalList
                .stream()
                .collect(Collectors.toMap(InOutStockItemVO::getPeriod, v -> v));


        List<InOutStockItemVO> inOutStockItemVOS = this.inOutStockItem(params);

        Map<String, List<InOutStockItemVO>> groupByPeriod = inOutStockItemVOS
                .stream()
                .collect(Collectors.groupingBy(InOutStockItemVO::getPeriod));
        List<InOutStockItemVO> groupByStockVOS;
        BigDecimal initialCount;
        BigDecimal initialAmount;
        // 计算
        BigDecimal newInitialUnitPrice;
        // 出入库
        BigDecimal count;
        BigDecimal amount;
        BigDecimal unitPrice;
        // 判断是否为出库
        boolean isOut;
        // 合计行
        InOutStockItemVO inOutStockItemVO;
        Map<String, Object>  initMap;
        for (String period : initialMap.keySet()) {
            initMap = initialMap.get(period);
            // 期初数量
            initialCount = MathUtils.getBigDecimal(initMap.get("initialCount"));
            // 期初金额
            initialAmount = MathUtils.getBigDecimal(initMap.get("initialAmount"));

            groupByStockVOS = groupByPeriod.get(period);
            if (groupByStockVOS !=null && groupByStockVOS.size()>0) {
                // 将数据按时间升序排列
                groupByStockVOS.sort(Comparator.comparing(InOutStockItemVO::getInOutTime));

                for (InOutStockItemVO groupByStockVO : groupByStockVOS) {
                    isOut = groupByStockVO.getType() == 1;
                    count = groupByStockVO.getCount();
                    amount = groupByStockVO.getAmount();
                    unitPrice = groupByStockVO.getUnitPrice();
                    if (isOut) {
                        initialCount = initialCount.subtract(count) ;
                        initialAmount = initialAmount.subtract(amount) ;
                        groupByStockVO.setOutUnitPrice(unitPrice);
                        groupByStockVO.setOutCount(count);
                        groupByStockVO.setOutAmount(amount);
                    } else {
                        initialCount = initialCount.add(count) ;
                        initialAmount = initialAmount .add(amount) ;
                        groupByStockVO.setInUnitPrice(unitPrice);
                        groupByStockVO.setInCount(count);
                        groupByStockVO.setInAmount(amount);
                    }
                    groupByStockVO.setBalanceCount(initialCount);
                    groupByStockVO.setBalanceAmount(initialAmount);
                    newInitialUnitPrice = initialAmount.divide(initialCount, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
                    groupByStockVO.setBalanceUnitPrice(newInitialUnitPrice);
                }
            }else {
                groupByStockVOS = Lists.newArrayList();
            }

            InOutStockItemVO inOutStockItem = groupByStockVOS.size()>0 ?groupByStockVOS.get(groupByStockVOS.size() - 1):periodForVO.get(period);
            inOutStockItemVO = new InOutStockItemVO();
            inOutStockItemVO.setStorageTypeName("本期合计");
            // 日期
            inOutStockItemVO.setPeriod(inOutStockItem.getPeriod());
            inOutStockItemVO.setTimes(inOutStockItem.getTimes());

            // 收入数量
            BigDecimal totalInCount = groupByStockVOS
                    .stream()
                    .map(e->e.getInCount()==null?BigDecimal.ZERO:e.getInCount())
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setInCount(totalInCount);
            // 收入金额合计
            BigDecimal totalInAmount = groupByStockVOS
                    .stream()
                    .map(e->e.getInAmount()==null?BigDecimal.ZERO:e.getInAmount())
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setInAmount(totalInAmount);
            // 合计收入单价
            BigDecimal totalInUnitPrice = totalInCount.compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:totalInAmount.divide(totalInCount, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
            inOutStockItemVO.setInUnitPrice(totalInUnitPrice);
            // 发出数量
            BigDecimal totalOutCount = groupByStockVOS
                    .stream()
                    .map(e->e.getOutCount()==null?BigDecimal.ZERO:e.getOutCount())
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setOutCount(totalOutCount);
            // 发出金额合计
            BigDecimal totalOutAmount = groupByStockVOS
                    .stream()
                    .map(e->e.getOutAmount()==null?BigDecimal.ZERO:e.getOutAmount())
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
            inOutStockItemVO.setOutAmount(totalOutAmount);
            // 合计发出单价
            BigDecimal totalOutUnitPrice = totalOutCount.compareTo(BigDecimal.ZERO)==0?BigDecimal.ZERO:totalOutAmount.divide(totalOutCount, Constant.BIGDECIMAL_ZERO, BigDecimal.ROUND_HALF_UP);
            inOutStockItemVO.setOutUnitPrice(totalOutUnitPrice);
            inOutStockItemVO.setSortNo(2);
            groupByStockVOS.add(inOutStockItemVO);
            totalList.addAll(groupByStockVOS);
        }


        List<InOutStockItemVO> collect = totalList
                .stream()
                .sorted(Comparator.comparing(InOutStockItemVO::getSortNo))
                .sorted(Comparator.comparing(InOutStockItemVO::getTimes))
                .collect(Collectors.toList());
        for (InOutStockItemVO outStockItemVO : collect) {
            int sortNo = outStockItemVO.getSortNo();
            if (sortNo == 2) {
                outStockItemVO.setInOutTime(null);
            }


        }

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
            Map<Long, StockInItemVO> materielMap = stockInItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockInItemVO::getMaterielId
                            , v -> v
                            , (v1, v2) -> v1));
            // 获取最大一个时间
            long maxTime = Objects.requireNonNull(DateFormatUtil.getDateByParttern(endTime==null?"2099-12-31":endTime, "yyyy-MM-dd")).getTime();

            maxTime = maxTime + (24 * 3600 * 1000);

            if (showTotal) {
                StockInItemVO stockInItemParam;
                StockInItemVO stockInItemVO;
                for (Long materiel : amountMap.keySet()) {
                    stockInItemParam = materielMap.get(materiel);
                    stockInItemVO = new StockInItemVO();
                    stockInItemVO.setMaterielId(materiel);
                    stockInItemVO.setCount(countMap.get(materiel));
                    stockInItemVO.setAmount(amountMap.get(materiel));
                    stockInItemVO.setInOutTimeParam(new Date(maxTime));
                    // 标记颜色
                    stockInItemVO.setSign(ConstantForReport.COLOUR_END);
                    stockInItemVO.setMaterielName(stockInItemParam.getMaterielName() + ConstantForReport.TOTAL_SUFFIX);
                    stockInItemVO.setSerialNo(stockInItemParam.getSerialNo());
                    stockInItemVO.setSpecification(stockInItemParam.getSpecification());
                    stockInItemVO.setUnitUomName(stockInItemParam.getUnitUomName());
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
                    .sorted(Comparator.comparing(StockInItemVO::getInOutTimeParam))
                    .sorted(Comparator.comparing(StockInItemVO::getMaterielId))
                    .collect(Collectors.toList());

            for (StockInItemVO stockInItemVO : collect) {
                long time = stockInItemVO.getInOutTimeParam().getTime();
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
            Map<Long, StockOutItemVO> materielMap = StockOutItemVOS
                    .stream()
                    .collect(Collectors.toMap(
                            StockOutItemVO::getMaterielId
                            , v -> v
                            , (v1, v2) -> v1));
            // 获取最大一个时间
            long maxTime = Objects.requireNonNull(DateFormatUtil.getDateByParttern(endTime==null?"2099-12-31":endTime, "yyyy-MM-dd")).getTime();

            maxTime = maxTime + (24 * 3600 * 1000);

            if (showTotal) {
                StockOutItemVO stockOutItemVO;
                StockOutItemVO stockOutItemParam;
                for (Long materiel : amountMap.keySet()) {
                    stockOutItemParam = materielMap.get(materiel);
                    stockOutItemVO = new StockOutItemVO();
                    stockOutItemVO.setCount(countMap.get(materiel));
                    stockOutItemVO.setAmount(amountMap.get(materiel));
                    stockOutItemVO.setOutTimeParam(new Date(maxTime));
                    // 标记颜色
                    stockOutItemVO.setSign(ConstantForReport.COLOUR_END);

                    stockOutItemVO.setMaterielName(stockOutItemParam.getMaterielName() + ConstantForReport.TOTAL_SUFFIX);
                    stockOutItemVO.setMaterielId(materiel);
                    stockOutItemVO.setSerialNo(stockOutItemParam.getSerialNo());
                    stockOutItemVO.setSpecification(stockOutItemParam.getSpecification());
                    stockOutItemVO.setUnitUomName(stockOutItemParam.getUnitUomName());
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
                    .sorted(Comparator.comparing(StockOutItemVO::getOutTimeParam))
                    .sorted(Comparator.comparing(StockOutItemVO::getMaterielId))
                    .collect(Collectors.toList());

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
