package com.ev.apis.controller.report;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
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
            results.put("data",data);
        }
        return R.ok(results);
    }

}
