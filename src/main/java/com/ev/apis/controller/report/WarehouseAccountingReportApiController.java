package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.scm.service.StockAnalysisService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓库管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-31 09:51:41
 */
@Api(value = "/", tags = "仓库管理报表分析API")
@RestController
public class WarehouseAccountingReportApiController {
    @Autowired
    private WarehouseAccountingReportService reportService;
    @Autowired
    private StockAnalysisService stockAnalysisService;

    @EvApiByToken(value = "/apis/warehouse/inOutSummary", method = RequestMethod.POST, apiTitle = "仓库收发汇总")
    @ApiOperation("仓库收发汇总")
    public R inOutSummary(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                          @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                          @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                          @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                          @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                          @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (data.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/inOutStockItem", method = RequestMethod.POST, apiTitle = "仓库收发明细")
    @ApiOperation("仓库收发明细")
    public R inOutStockItem(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
                            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
                            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "") Long materielId) {


        Map<String, Object> params = Maps.newHashMap();
        Date startPeriod = DateFormatUtil.getDateByParttern(startTime);
        Date endPeriod = DateFormatUtil.getDateByParttern(endTime);
        if (materielId == null || endPeriod == null || startPeriod == null) {
            return R.ok();
        }
        params.put("materielType", materielType);
        params.put("materielId", materielId);
        Date startTimeParam = DateFormatUtil.getDateByParttern(startTime);
        Date endTimeParam = DateFormatUtil.getDateByParttern(endTime);

        params.put("startTime",DatesUtil.getSupportBeginDayOfMonth(startTimeParam));
        params.put("endTime",  DatesUtil.getSupportEndDayOfMonth(endTimeParam));

        List<Map<String, Object>> initData = stockAnalysisService.listForMap(params);
        Map<String, Double> initialCountMap = initData.stream()
                .collect(Collectors.groupingBy(k -> k.get("period").toString()
                        , Collectors.summingDouble(v -> Double.parseDouble(v.get("initialCount").toString()))));

        Map<String, Double> initialAmountMap = initData.stream()
                .collect(Collectors.groupingBy(k -> k.get("period").toString()
                        , Collectors.summingDouble(v -> Double.parseDouble(v.get("initialAmount").toString()))));

        Map<String, String> initialPeriodMap = initData.stream()
                .collect(Collectors.toMap(k->k.get("period").toString()
                        ,v->v.get("period").toString().substring(0,7)));

        Map<String,BigDecimal> initialUnitPriceMap = Maps.newHashMap();
        for (String k : initialAmountMap.keySet()) {
            Double amount = initialAmountMap.get(k);
            Double count = initialCountMap.get(k);
            if(count==0.0d){
                initialUnitPriceMap.put(k,BigDecimal.ZERO);
            }else {
                initialUnitPriceMap.put(k, BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(count), Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP));
            }

        }
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
            initInOutStockItemVO.setInOutTime(DateFormatUtil.getDateByParttern(k));

        }

        List<InOutStockItemVO> inOutStockItemVOS = reportService.inOutStockItem(params);

        Map<String, List<InOutStockItemVO>> groupByPeriod = inOutStockItemVOS
                .stream()
                .collect(Collectors.groupingBy(InOutStockItemVO::getPeriod));
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
                    groupByStockVO.setUnitPrice(unitPrice);
                }else{
                    newInitialCount = initialCount + count;
                    newInitialAmount = initialAmount + amount;
                    groupByStockVO.setInCount(count);
                    groupByStockVO.setInAmount(amount);
                    groupByStockVO.setUnitPrice(unitPrice);
                }
                groupByStockVO.setBalanceCount(newInitialCount);
                groupByStockVO.setBalanceAmount(newInitialAmount);
                newInitialUnitPrice = BigDecimal.valueOf(newInitialAmount).divide(BigDecimal.valueOf(newInitialCount), Constant.BIGDECIMAL_ZERO,BigDecimal.ROUND_HALF_UP);
                groupByStockVO.setBalanceUnitPrice(newInitialUnitPrice);

            }
            InOutStockItemVO inOutStockItem = groupByStockVOS.get(groupByStockVOS.size() - 1);
            inOutStockItemVO = new InOutStockItemVO();
            BeanUtils.copyProperties(inOutStockItemVO,inOutStockItem);
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



        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (inOutStockItemVOS.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, inOutStockItemVOS));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/inStockItem", method = RequestMethod.POST, apiTitle = "仓库入库明细")
    @ApiOperation("仓库入库明细")
    public R inStockItem(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                         @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                         @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("period", period);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (data.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/outStockItem", method = RequestMethod.POST, apiTitle = "仓库出库明细")
    @ApiOperation("仓库出库明细")
    public R outStockItem(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                          @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                          @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                          @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                          @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("period", period);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (data.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/safetyStockWarning", method = RequestMethod.POST, apiTitle = "安全库存预警")
    @ApiOperation("仓库出库明细")
    public R safetyStockWarning(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                                @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                                @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                                @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                                @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("period", period);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (data.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/pickingSummary", method = RequestMethod.POST, apiTitle = "生产领料汇总表(汇总)")
    @ApiOperation("生产领料汇总表（汇总）")
    public R pickingSummary(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("period", period);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (data.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/pickingItem", method = RequestMethod.POST, apiTitle = "生产领料汇总表(详细)")
    @ApiOperation("生产领料汇总表（详细）")
    public R pickingItem(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                         @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                         @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                         @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                         @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("period", period);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = stockAnalysisService.listForMap(params);
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        int total = Integer.parseInt(map.get("total").toString());
        if (data.size() > 0) {
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }


}
