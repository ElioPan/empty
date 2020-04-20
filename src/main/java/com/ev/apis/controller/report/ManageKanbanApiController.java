package com.ev.apis.controller.report;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.R;
import com.ev.report.service.ManageKanbanService;
import com.ev.report.service.SmartManufacturingAccountingReportService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理看板
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-04-16 09:51:41
 */
@Api(value = "/", tags = "管理看板API")
@RestController
public class ManageKanbanApiController {
    @Autowired
    private SmartManufacturingAccountingReportService smartReportService;
    @Autowired
    private ManageKanbanService kanbanService;

    @EvApiByToken(value = "/apis/ManageKanban/workshop", method = RequestMethod.POST, apiTitle = "车间WIP看板")
    @ApiOperation("车间WIP看板")
    public R inOutSummary() {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        // 非计划状态下的单据
        List<Integer> status = Lists.newArrayList();
        status.add(ConstantForMES.ISSUED);
        status.add(ConstantForMES.PUT_UP);
        params.put("statusList", status);
        // 统计合计数量
        List<Map<String, Object>> totalData = smartReportService.productionPlanList(params);

        Map<String, Object> results = Maps.newHashMap();
        if (totalData.size() > 0) {
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionPlan = smartReportService.productionPlan(totalData, false);
            results.put("data", productionPlan.getLeft());
        }
        return R.ok(results);

    }


    @EvApiByToken(value = "/apis/ManageKanban/process", method = RequestMethod.POST, apiTitle = "工序WIP看板")
    @ApiOperation("工序WIP看板")
    public R inOutStockItem() {

        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        // 非计划状态下的单据
        List<Integer> status = Lists.newArrayList();
        status.add(ConstantForMES.ISSUED);
        status.add(ConstantForMES.PUT_UP);
        params.put("statusList", status);

        // 统计合计数量
        List<Map<String, Object>> totalData = smartReportService.processPlanList(params);
        Map<String, Object> results = Maps.newHashMap();
        if (totalData.size() > 0) {
            BigDecimal max = BigDecimal.valueOf(100L);
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processPlan = smartReportService.processPlan(totalData, false);
            List<Map<String, Object>> collect = processPlan
                    .getLeft()
                    .stream()
                    .filter(e -> {
                        BigDecimal bigDecimal = MathUtils.getBigDecimal(e.get("completionRate"));
                        return bigDecimal.compareTo(BigDecimal.ZERO) > 0 && bigDecimal.compareTo(max) < 0;
                    })
                    .collect(Collectors.toList());
            if (collect.size() > 0) {
                results.put("data", collect);
            }
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/ManageKanban/materielDelivery", method = RequestMethod.POST, apiTitle = "物料配送看板")
    @ApiOperation("物料配送看板")
    public R inStockItem() {
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = kanbanService.feedingList();
        if (data.size() > 0) {
            results.put("data", data);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/ManageKanban/productionStatistics", method = RequestMethod.POST, apiTitle = "生产运营看板-产量统计")
    @ApiOperation("生产运营看板-产量统计")
    public R productionStatistics() {
        Map<String, Object> result = Maps.newHashMap();
        // 获取当前年
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        String monthToString = String.format("%0" + 2 + "d", month);

        Map<String, Object> param = Maps.newHashMap();
        param.put("year", year);
        param.put("auditSign", ConstantForMES.OK_AUDITED);
        param.put("storageType", ConstantForGYL.YDGOODS_WAREHOUSE);
        List<Map<String, Object>> yearData = kanbanService.getProductionStatistics(param);
        // 年产量
        BigDecimal yearCount = yearData
                .stream()
                .map(v -> MathUtils.getBigDecimal(v.get("count")))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        result.put("yearCount", yearCount);

        param.remove("year");
        param.put("month", year + "-" + monthToString);
        List<Map<String, Object>> monthData = kanbanService.getProductionStatistics(param);
        // 月产量
        BigDecimal monthCount = monthData
                .stream()
                .map(v -> MathUtils.getBigDecimal(v.get("count")))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        result.put("monthCount", monthCount);

        // 获取最近十天
        List<Date> dateTen = DatesUtil.getDateBefore(now.getTime(), ConstantForReport.SHOW_COUNT.intValue());
        List<String> dateTenToString = dateTen.stream()
                .map(e -> DateFormatUtil.getFormateDate(e, DateFormatUtil.DATE_PATTERN_YMD))
                .collect(Collectors.toList());

        // 获取最近10天
        param.remove("month");
        param.put("day", true);
        param.put("startTime", dateTenToString.get(0));
        param.put("endTime", dateTenToString.get(dateTenToString.size() - 1));
        List<Map<String, Object>> productionStatistics = kanbanService.getProductionStatistics(param);
        Map<String, Object> timeToCount = productionStatistics
                .stream()
                .collect(Collectors.toMap(k -> k.get("time").toString(), v -> v.get("count").toString()));
        Map<String, Object> tenDayMap = Maps.newHashMap();
        for (String date : dateTenToString) {
            tenDayMap.put(date, timeToCount.getOrDefault(date, 0));
        }
        result.put("tenDayCount", tenDayMap);

        // 周产量 本周
        param.put("startTime", DateFormatUtil.getFormateDate(DatesUtil.getBeginDayOfWeek(), DateFormatUtil.DATE_PATTERN_YMD));
        param.put("endTime", DateFormatUtil.getFormateDate(DatesUtil.getEndDayOfWeek(), DateFormatUtil.DATE_PATTERN_YMD));
        List<Map<String, Object>> weekData = kanbanService.getProductionStatistics(param);
        BigDecimal weekCount = weekData
                .stream()
                .map(v -> MathUtils.getBigDecimal(v.get("count")))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        result.put("weekCount", weekCount);

        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/ManageKanban/yieldStatisticsByProduct", method = RequestMethod.POST, apiTitle = "生产运营看板-良率统计(产品最高TOP10)")
    @ApiOperation("生产运营看板-良率统计(产品最高TOP10)")
    public R yieldStatisticsByProduct() {
        // 获取最近7天
        List<Date> dateSeven = DatesUtil.getDateBefore(new Date(), 7);
        List<String> dateTenToString = dateSeven.stream()
                .map(e -> DateFormatUtil.getFormateDate(e, DateFormatUtil.DATE_PATTERN_YMD))
                .collect(Collectors.toList());

        Map<String, Object> param = Maps.newHashMap();
        param.put("status", ConstantForMES.OK_AUDITED);
        param.put("inspectionType", ConstantForMES.CPJY);
        param.put("startTime", dateTenToString.get(0));
        param.put("endTime", dateTenToString.get(dateTenToString.size() - 1));
//        List<Map<String, Object>> data = kanbanService.getProductionStatistics(param);
//        // 获取一周内产量最高的十个物料
//        List<Object> materielIdList = data
//                .stream()
//                .limit(ConstantForReport.SHOW_COUNT)
//                .map(e -> e.get("materielId"))
//                .collect(Collectors.toList());

        List<Map<String, Object>> badData = kanbanService.badProductList(param);
        List<Map<String, Object>> yieldRate = badData
                .stream()
                .limit(ConstantForReport.SHOW_COUNT)
                .peek(e -> e.put("qualifiedRate", BigDecimal.ONE.subtract(MathUtils.getBigDecimal(e.get("unqualifiedRate")))))
                .collect(Collectors.toList());
        Map<String, Object> results = Maps.newHashMap();
        results.put("yieldRate", yieldRate);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/ManageKanban/yieldStatisticsByDefectiveRate", method = RequestMethod.POST, apiTitle = "生产运营看板-良率统计(不良率最高TOP10)")
    @ApiOperation("生产运营看板-良率统计(不良率最高TOP10)")
    public R yieldStatisticsByDefectiveRate() {
        // 获取最近7天
        List<Date> dateSeven = DatesUtil.getDateBefore(new Date(), 7);
        List<String> dateTenToString = dateSeven.stream()
                .map(e -> DateFormatUtil.getFormateDate(e, DateFormatUtil.DATE_PATTERN_YMD))
                .collect(Collectors.toList());

        Map<String, Object> param = Maps.newHashMap();
        param.put("status", ConstantForMES.OK_AUDITED);
        param.put("inspectionType", ConstantForMES.CPJY);
        param.put("startTime", dateTenToString.get(0));
        param.put("endTime", dateTenToString.get(dateTenToString.size() - 1));
        param.put("unqualifiedRate", true);

        List<Map<String, Object>> badData = kanbanService.badProductList(param);
        List<Map<String, Object>> yieldRate = badData
                .stream()
                .limit(ConstantForReport.SHOW_COUNT)
                .collect(Collectors.toList());
        Map<String, Object> results = Maps.newHashMap();
        results.put("yieldRate", yieldRate);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/ManageKanban/badReasonsCount", method = RequestMethod.POST, apiTitle = "生产运营看板-良率统计(不良数量TOP10)")
    @ApiOperation("生产运营看板-不良原因(不良数量TOP10)")
    public R badReasonsCount() {
        Map<String, Object> param = Maps.newHashMap();
        param.put("status", ConstantForMES.OK_AUDITED);
        param.put("inspectionType", ConstantForMES.CPJY);
        param.put("unqualifiedCount", true);
        param.put("poor", true);
        List<Map<String, Object>> badData = kanbanService.badProductList(param);
        List<Map<String, Object>> badDataTop = badData
                .stream()
                .limit(ConstantForReport.SHOW_COUNT)
                .collect(Collectors.toList());
        Map<String, Object> results = Maps.newHashMap();
        results.put("badData", badDataTop);
        return R.ok(results);
    }

}
