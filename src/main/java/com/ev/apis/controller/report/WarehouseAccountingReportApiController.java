package com.ev.apis.controller.report;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @EvApiByToken(value = "/apis/warehouse/inOutSummary", method = RequestMethod.POST, apiTitle = "仓库收发汇总")
    @ApiOperation("仓库收发汇总")
    public R inOutSummary(
            @ApiParam(value = "显示期间小计", required = true) @RequestParam(value = "showPeriodTotal", defaultValue = "1") int showPeriodTotalInt,
            @ApiParam(value = "显示物料小计", required = true) @RequestParam(value = "showMaterielTotal", defaultValue = "1") int showMaterielTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {
        Pair<List<Map<String, Object>>, Map<String, Object>> inOutSummaryResult =
                reportService.inOutSummaryResult(showPeriodTotalInt
                        , showMaterielTotalInt
                        , showItemInt
                        , startTime
                        , endTime
                        , materielType
                        , materielId);
        if (inOutSummaryResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", inOutSummaryResult.getLeft());
        results.put("total", inOutSummaryResult.getRight());
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/inOutStockItem", method = RequestMethod.POST, apiTitle = "仓库收发明细")
    @ApiOperation("仓库收发明细")
    public R inOutStockItem(
            @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "") Long materielId) {

        Pair<List<InOutStockItemVO>, Map<String, Object>> inOutStockItemResult =
                reportService.inOutStockItemResult(
                         startTime
                        , endTime
                        , materielType
                        , materielId);
        if (inOutStockItemResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("total", inOutStockItemResult.getLeft());
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/inStockItem", method = RequestMethod.POST, apiTitle = "仓库入库明细")
    @ApiOperation("仓库入库明细")
    public R inStockItem(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {
        Pair<List<StockInItemVO>, Map<String, Object>> inStockItemResult =
                reportService.inStockItemResult(
                        showTotalInt
                        ,showItemInt
                        ,startTime
                        , endTime
                        , materielType
                        , materielId);
        if (inStockItemResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", inStockItemResult.getLeft());
        results.put("total", inStockItemResult.getRight());
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/outStockItem", method = RequestMethod.POST, apiTitle = "仓库出库明细")
    @ApiOperation("仓库出库明细")
    public R outStockItem(@ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
                          @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
                          @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
                          @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
                          @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                          @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {
        Pair<List<StockOutItemVO>, Map<String, Object>> outStockItemResult =
                reportService.outStockItemResult(
                        showTotalInt
                        ,showItemInt
                        ,startTime
                        , endTime
                        , materielType
                        , materielId);
        if (outStockItemResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", outStockItemResult.getLeft());
        results.put("total", outStockItemResult.getRight());
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/safetyStockWarning", method = RequestMethod.POST, apiTitle = "安全库存预警")
    @ApiOperation("安全库存预警")
    public R safetyStockWarning(
//            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
//        params.put("endTime", endTime);
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> data = reportService.stockList(params);
        if (data.size() > 0) {
            results.put("total", data);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/warehouse/pickingSummary", method = RequestMethod.POST, apiTitle = "生产领料汇总表")
    @ApiOperation("生产领料汇总表")
    public R pickingSummary(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId) {
        Pair<List<Map<String,Object>>, Map<String, Object>> pickingSummaryResult =
                reportService.pickingSummaryResult(
                        showTotalInt
                        ,showItemInt
                        ,startTime
                        , endTime
                        , deptId
                );
        if (pickingSummaryResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", pickingSummaryResult.getLeft());
        results.put("total", pickingSummaryResult.getRight());
        return R.ok(results);
    }


}
