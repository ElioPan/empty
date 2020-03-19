package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.SmartManufacturingAccountingReportService;
import com.ev.report.vo.CommonVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 智能制造报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-18 09:51:41
 */
@Api(value = "/", tags = "智能制造报表分析API")
@RestController
public class SmartManufacturingAccountingReportApiController {
    @Autowired
    private SmartManufacturingAccountingReportService reportService;

    @EvApiByToken(value = "/apis/smartManufacturing/productionPlan", method = RequestMethod.POST, apiTitle = "生产计划跟踪")
    @ApiOperation("生产计划跟踪")
    public R productionPlan(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "计划单号") @RequestParam(value = "planCode", defaultValue = "", required = false) String planCode,
            @ApiParam(value = "物料编号") @RequestParam(value = "materielSerialNo", defaultValue = "", required = false) String materielSerialNo,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("planCode", planCode);
        params.put("materielId", materielId);
        params.put("deptId", deptId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielSerialNo", StringUtils.sqlLike(materielSerialNo));
        // 统计合计数量
        List<Map<String, Object>> totalData = reportService.productionPlanList(params);

        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = reportService.productionPlanList(params);
        int total = reportService.productionPlanCount(params);
        if (data.size() > 0) {
            Pair<List<Map<String,Object>>,Map<String, BigDecimal>> productionPlan = reportService.processPlan(data,false);
            Pair<List<Map<String,Object>>,Map<String, BigDecimal>> totalProductionPlan = reportService.processPlan(totalData,true);
            results.put("total", totalProductionPlan.getRight());
            results.put("data", new DsResultResponse(pageno,pagesize,total,productionPlan.getLeft()));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/smartManufacturing/processPlan", method = RequestMethod.POST, apiTitle = "工序计划跟踪")
    @ApiOperation("工序计划跟踪")
    public R processPlan(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "计划单号") @RequestParam(value = "planCode", defaultValue = "", required = false) String planCode,
            @ApiParam(value = "物料编号") @RequestParam(value = "materielSerialNo", defaultValue = "", required = false) String materielSerialNo,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("planCode", planCode);
        params.put("materielId", materielId);
        params.put("deptId", deptId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielSerialNo", StringUtils.sqlLike(materielSerialNo));
        // 非计划状态下的单据
        params.put("status", ConstantForMES.PLAN);
        // 统计合计数量
        List<Map<String, Object>> totalData = reportService.processPlanList(params);

        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = reportService.processPlanList(params);
        int total = reportService.processPlanCount(params);
        if (data.size() > 0) {
            Pair<List<Map<String,Object>>,Map<String, BigDecimal>> productionPlan = reportService.processPlan(data,false);
            Pair<List<Map<String,Object>>,Map<String, BigDecimal>> totalProductionPlan = reportService.processPlan(totalData,true);
            results.put("total", totalProductionPlan.getRight());
            results.put("data", new DsResultResponse(pageno,pagesize,total,productionPlan.getLeft()));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/smartManufacturing/pieceRate", method = RequestMethod.POST, apiTitle = "计件工资统计")
    @ApiOperation("计件工资统计")
    public R pieceRate(
            CommonVO commonVO
    ) {
        // 查询列表数据
        return reportService.pieceRate(commonVO);
    }

    @EvApiByToken(value = "/apis/smartManufacturing/productionBatch", method = RequestMethod.POST, apiTitle = "生产批次跟踪")
    @ApiOperation("生产批次跟踪")
    public R productionBatch(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "计划单号") @RequestParam(value = "planCode", defaultValue = "", required = false) String planCode,
            @ApiParam(value = "物料编号") @RequestParam(value = "materielSerialNo", defaultValue = "", required = false) String materielSerialNo,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("planCode", planCode);
        params.put("materielId", materielId);
        params.put("deptId", deptId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielSerialNo", StringUtils.sqlLike(materielSerialNo));
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = reportService.productionBatchList(params);
        int total = reportService.productionBatchCount(params);
        if (data.size() > 0) {
            Pair<List<Map<String,Object>>,Map<String, BigDecimal>> productionBatch = reportService.productionBatch(data);
            results.put("total", productionBatch.getRight());
            results.put("data", new DsResultResponse(pageno,pagesize,total,productionBatch.getLeft()));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/smartManufacturing/processOutput", method = RequestMethod.POST, apiTitle = "工序产量跟踪")
    @ApiOperation("工序产量跟踪")
    public R processOutput(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "操作工") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "工序Id") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "工序名") @RequestParam(value = "processName", defaultValue = "", required = false) String processName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);

        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("processId", processId);
        params.put("processName", processName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = reportService.processOutputList(params);
        int total = reportService.processOutputCount(params);
        if (data.size() > 0) {
            Pair<List<Map<String,Object>>,Map<String, BigDecimal>> processOutput = reportService.processOutput(data);
            results.put("total", processOutput.getRight());
            results.put("data", new DsResultResponse(pageno,pagesize,total,processOutput.getLeft()));
        }
        return R.ok(results);
    }
}
