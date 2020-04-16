package com.ev.apis.controller.report;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/inOutSummaryGetOut", method = RequestMethod.GET, apiTitle = "导出——仓库收发汇总")
    @ApiOperation("导出——仓库收发汇总")
    public void exportExcelSummary(
            @ApiParam(value = "显示期间小计", required = true) @RequestParam(value = "showPeriodTotal", defaultValue = "1") int showPeriodTotalInt,
            @ApiParam(value = "显示物料小计", required = true) @RequestParam(value = "showMaterielTotal", defaultValue = "1") int showMaterielTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> inOutSummaryResult =
                reportService.inOutSummaryResult(showPeriodTotalInt
                        , showMaterielTotalInt
                        , showItemInt
                        , startTime
                        , endTime
                        , materielType
                        , materielId);
        List<Map<String, Object>> dateList;
        if(Objects.isNull(inOutSummaryResult)){
            return;
        }
        dateList=inOutSummaryResult.getLeft();
        String fileName="poi/report_warehouse_summary.xlsx";
        String fileChineseName="仓库收发汇总";
        reportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
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

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/inOutStockItemGetOut", method = RequestMethod.GET, apiTitle = "导出——仓库收发明细")
    @ApiOperation("导出——仓库收发明细")
    public void exportExcelItem(
            @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "") Long materielId,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<InOutStockItemVO>, Map<String, Object>> inOutStockItemResult =
                reportService.inOutStockItemResult(
                        startTime
                        , endTime
                        , materielType
                        , materielId);
        List<InOutStockItemVO> dateList;
        if (Objects.isNull(inOutStockItemResult)) {
            return;
        }
        dateList=inOutStockItemResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_warehouse_matierialItem.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "仓库收发汇总");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
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

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/inStockGetOut", method = RequestMethod.GET, apiTitle = "导出——仓库入库明细")
    @ApiOperation("导出——仓库入库明细")
    public void exportExcelInStock(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Pair<List<StockInItemVO>, Map<String, Object>> inStockItemResult =
                reportService.inStockItemResult(
                        showTotalInt
                        ,showItemInt
                        ,startTime
                        , endTime
                        , materielType
                        , materielId);
        List<StockInItemVO> dateList;
        if(Objects.isNull(inStockItemResult)){
            return;
        }
        dateList=inStockItemResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_warehouse_inStockItem.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "仓库入库明细");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
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

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/outStockGetOut", method = RequestMethod.GET, apiTitle = "导出——仓库出库明细")
    @ApiOperation("导出——仓库出库明细")
    public void exportExcelOutStock(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间", required = true) @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @ApiParam(value = "结束时间", required = true) @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Pair<List<StockOutItemVO>, Map<String, Object>> outStockItemResult =
                reportService.outStockItemResult(
                        showTotalInt
                        ,showItemInt
                        ,startTime
                        , endTime
                        , materielType
                        , materielId);
        List<StockOutItemVO> dateList ;
        if(Objects.isNull(outStockItemResult)){
            return ;
        }
        dateList=outStockItemResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_warehouse_outStockItem.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "仓库出库明细");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
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

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/safetyStockWarning", method = RequestMethod.GET, apiTitle = "导出——安全库存预警")
    @ApiOperation("导出——安全库存预警")
    public void exportExcelSafety(
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("materielId", materielId);
        params.put("materielType", materielType);
        List<Map<String, Object>> dateList = reportService.stockList(params);
        if(dateList.isEmpty()){
            return;
        }
        String fileName="poi/report_warehouse_safetyStockWarning.xlsx";
        String fileChineseName="安全库存预警";
        reportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
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

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/pickingSummary", method = RequestMethod.GET, apiTitle = "导出——生产领料汇总表")
    @ApiOperation("导出——生产领料汇总表")
    public void exportExcelPicking(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Pair<List<Map<String,Object>>, Map<String, Object>> pickingSummaryResult =
                reportService.pickingSummaryResult(
                        showTotalInt
                        ,showItemInt
                        ,startTime
                        , endTime
                        , deptId
                );
        List<Map<String,Object>> dateList ;
        if(Objects.isNull(pickingSummaryResult)){
                return ;
        }
        dateList=pickingSummaryResult.getLeft();
        String fileName="poi/report_warehouse_pickingSummary.xlsx";
        String fileChineseName="生产领料汇总表";
        reportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
    }





}
