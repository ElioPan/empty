package com.ev.apis.controller.report;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.report.service.SalesManagementAccountingReportService;
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
 * 销售管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-27 09:51:41
 */
@Api(value = "/", tags = "销售管理报表分析API")
@RestController
public class SalesManagementAccountingReportApiController {
    @Autowired
    private SalesManagementAccountingReportService reportService;

    @EvApiByToken(value = "/apis/salesManagement/tracking", method = RequestMethod.POST, apiTitle = "销售全程跟踪")
    @ApiOperation("销售全程跟踪")
    public R tracking(
            @ApiParam(value = "客户") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode", defaultValue = "", required = false) String contractCode,
            @ApiParam(value = "合同Id") @RequestParam(value = "contractId", defaultValue = "", required = false) Long contractId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime

    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult =
                reportService.trackingResult(clientId
                        , contractCode
                        , contractId
                        , deptId
                        , userId
                        , showItemInt
                        , showTotalInt
                        , startTime
                        , endTime
                );
        if (trackingResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", trackingResult.getLeft());
        results.put("total", trackingResult.getRight());
        return R.ok(results);
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/salesTrackingGetOut", method = RequestMethod.GET, apiTitle = "导出——销售全程跟踪")
    @ApiOperation("导出——销售全程跟踪")
    public void exportExcel(
            @ApiParam(value = "客户") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode", defaultValue = "", required = false) String contractCode,
            @ApiParam(value = "合同Id") @RequestParam(value = "contractId", defaultValue = "", required = false) Long contractId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult =
                reportService.trackingResult(clientId
                        , contractCode
                        , contractId
                        , deptId
                        , userId
                        , showItemInt
                        , showTotalInt
                        , startTime
                        , endTime
                );
        List<Map<String, Object>> dateList  ;
        if(Objects.isNull(trackingResult)){
            return ;
        }
        dateList = trackingResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_sales_tracking.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售全程跟踪");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }


    @EvApiByToken(value = "/apis/salesManagement/debtDue", method = RequestMethod.POST, apiTitle = "销售到期债务")
    @ApiOperation("销售到期债务")
    public R debtDue(
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult =
                reportService.debtDueResult(clientId
                        , showTotalInt
                        , showItemInt
                        , deptId
                        , userId
                        , endTime
                );
        if (debtDueResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", debtDueResult.getLeft());
        results.put("total", debtDueResult.getRight());
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/salesDebtDueGetOut", method = RequestMethod.GET, apiTitle = "导出——销售到期债务")
    @ApiOperation("导出——销售到期债务")
    public void exportExcelDebtDue(
            @ApiParam(value = "客户Id") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult =
                reportService.debtDueResult(clientId
                        , showTotalInt
                        , showItemInt
                        , deptId
                        , userId
                        , endTime
                );
        List<Map<String, Object>> dateList  ;
        if(Objects.isNull(debtDueResult)){
            return ;
        }
        dateList = debtDueResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_sales_debtdue.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售到期债务");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @EvApiByToken(value = "/apis/salesManagement/summary", method = RequestMethod.POST, apiTitle = "销售汇总统计")
    @ApiOperation("销售汇总统计")
    public R summary(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:1客户，2部门，3销售员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> summaryResult =
                reportService.summaryResult(
                        showTotalInt
                        , showItemInt
                        , clientName
                        , clientId
                        , deptId
                        , userId
                        , startTime
                        , endTime
                        , type
                );
        if (summaryResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", summaryResult.getLeft());
        results.put("total", summaryResult.getRight());
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/salesSummaryGetOut", method = RequestMethod.GET, apiTitle = "导出——销售汇总统计")
    @ApiOperation("导出——销售汇总统计")
    public void exportExcelSummary(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:1客户，2部门，3销售员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> summaryResult =
                reportService.summaryResult(
                        showTotalInt
                        , showItemInt
                        , clientName
                        , clientId
                        , deptId
                        , userId
                        , startTime
                        , endTime
                        , type
                );

        List<Map<String, Object>> dateList  ;
        if(Objects.isNull(summaryResult)){
            return ;
        }
        dateList = summaryResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_sales_summary.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售汇总统计");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }


    @EvApiByToken(value = "/apis/salesManagement/balance", method = RequestMethod.POST, apiTitle = "销售合同余额")
    @ApiOperation("销售合同余额")
    public R balance(
            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "销售员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult =
                reportService.balanceResult(
                        clientName
                        , clientId
                        , showTotalInt
                        , showItemInt
                        , deptId
                        , userId
                        , endTime
                );
        if (balanceResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", balanceResult.getLeft());
        results.put("total", balanceResult.getRight());
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/salesBalanceGetOut", method = RequestMethod.GET, apiTitle = "导出——销售合同余额")
    @ApiOperation("导出——销售合同余额")
    public void exportExcelBalance(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "客户") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "业务员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "分类汇总条件:1客户，2部门，3销售员", required = true) @RequestParam(value = "type", defaultValue = "1") Integer type,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult =
                reportService.balanceResult(
                        clientName
                        , clientId
                        , showTotalInt
                        , showItemInt
                        , deptId
                        , userId
                        , endTime
                );
        List<Map<String, Object>> dateList  ;
        if(Objects.isNull(balanceResult)){
            return ;
        }
        dateList = balanceResult.getLeft();
        ClassPathResource classPathResource = new ClassPathResource("poi/report_sales_balance.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", dateList);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售合同余额");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
