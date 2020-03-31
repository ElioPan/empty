package com.ev.apis.controller.report;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.DatesUtil;
import com.ev.framework.utils.R;
import com.ev.report.service.WarehouseAccountingReportService;
import com.ev.report.vo.InOutStockItemVO;
import com.ev.report.vo.StockInItemVO;
import com.ev.report.vo.StockOutItemVO;
import com.ev.scm.service.StockAnalysisService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
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
    @Autowired
    private StockAnalysisService stockAnalysisService;

    @EvApiByToken(value = "/apis/warehouse/inOutSummary", method = RequestMethod.POST, apiTitle = "仓库收发汇总")
    @ApiOperation("仓库收发汇总")
    public R inOutSummary(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                          @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                          @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                          @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                          @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("period", period);
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
                            @ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
                            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "") Long materielId) {

        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("materielId", materielId);
        params.put("period", period);
        List<Map<String, Object>> initData = stockAnalysisService.listForMap(params);
        double initialCount = initData
                .stream()
                .mapToDouble(e -> Double.parseDouble(e.get("initialCount").toString()))
                .reduce(Double::sum)
                .orElse(0.0d);
        double initialAmount = initData
                .stream()
                .mapToDouble(e -> Double.parseDouble(e.get("initialAmount").toString()))
                .reduce(Double::sum)
                .orElse(0.0d);
        double initialUnitPrice = 0.0d;
        if (initialCount != 0.0d) {
            initialUnitPrice = initialAmount/initialCount;
        }
        InOutStockItemVO initInOutStockItemVO = new InOutStockItemVO();
        initInOutStockItemVO.setStorageTypeName("期初结存");
        initInOutStockItemVO.setBalanceAmount(BigDecimal.valueOf(initialAmount));
        initInOutStockItemVO.setBalanceCount(BigDecimal.valueOf(initialCount));
        initInOutStockItemVO.setBalanceUnitPrice(BigDecimal.valueOf(initialUnitPrice));

        Date periodTime = DateFormatUtil.getDateByParttern(period);
        params.put("startTime",DatesUtil.getSupportBeginDayOfMonth(periodTime));
        params.put("endTime",  DatesUtil.getSupportEndDayOfMonth(periodTime));
        params.put("materielType", materielType);
        List<InOutStockItemVO> inOutStockItemVOS = reportService.inOutStockItem(params);
        inOutStockItemVOS.add(0,initInOutStockItemVO);




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
