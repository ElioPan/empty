package com.ev.apis.controller.report;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.R;
import com.ev.report.service.OutsourcedManagementAccountingReportService;
import com.ev.report.service.WarehouseAccountingReportService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 委外管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-27 09:51:41
 */
@Api(value = "/", tags = "委外管理报表分析API")
@RestController
public class OutsourcedManagementAccountingReportApiController {
    @Autowired
    private OutsourcedManagementAccountingReportService reportService;
    @Autowired
    private WarehouseAccountingReportService wareReportService;


    @EvApiByToken(value = "/apis/outsourcedManagement/tracking", method = RequestMethod.POST, apiTitle = "委外全程跟踪")
    @ApiOperation("委外全程跟踪")
    public R tracking(
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult =
                reportService.trackingResult(supplierName
                        , materielType
                        , deptId
                        , userId
                        , showTotalInt
                        , showItemInt
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
    @EvApiByToken(value = "/apis/exportExcel/outsourcedTrackingGetOut", method = RequestMethod.GET, apiTitle = "导出——委外全程跟踪")
    @ApiOperation("导出——委外全程跟踪")
    public void exportExcelTracking(
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> trackingResult =
                reportService.trackingResult(supplierName
                        , materielType
                        , deptId
                        , userId
                        , showTotalInt
                        , showItemInt
                        , startTime
                        , endTime
                );
        List<Map<String, Object>> dateList;
        if(Objects.isNull(trackingResult)){
            return;
        }
        dateList=trackingResult.getLeft();
        String fileName="poi/report_outsourced_tracking.xlsx";
        String fileChineseName="委外全程跟踪";
        wareReportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/debtDue", method = RequestMethod.POST, apiTitle = "委外到期债务")
    @ApiOperation("委外到期债务")
    public R debtDue(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult =
                reportService.debtDueResult(
                        showTotalInt
                        , showItemInt
                        , supplierId
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
    @EvApiByToken(value = "/apis/exportExcel/outsourcedDebtDueGetOut", method = RequestMethod.GET, apiTitle = "导出——委外到期债务")
    @ApiOperation("导出——委外到期债务")
    public void exportExcelDebtDue(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> debtDueResult =
                reportService.debtDueResult(
                        showTotalInt
                        , showItemInt
                        , supplierId
                        , deptId
                        , userId
                        , endTime
                );
        List<Map<String, Object>> dateList;
        if(Objects.isNull(debtDueResult)){
            return;
        }
        dateList=debtDueResult.getLeft();
        String fileName="poi/report_outsourced_debtDue.xlsx";
        String fileChineseName="委外到期债务";
        wareReportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/balance", method = RequestMethod.POST, apiTitle = "委外合同余额")
    @ApiOperation("委外合同余额")
    public R balance(
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult =
                reportService.balanceResult(
                        supplierId
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
    @EvApiByToken(value = "/apis/exportExcel/outsourcedBalanceGetOut", method = RequestMethod.GET, apiTitle = "导出——委外合同余额")
    @ApiOperation("导出——委外合同余额")
    public void exportExcelBalance(
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Pair<List<Map<String, Object>>, Map<String, Object>> balanceResult =
                reportService.balanceResult(
                        supplierId
                        , showTotalInt
                        , showItemInt
                        , deptId
                        , userId
                        , endTime
                );
        List<Map<String, Object>> dateList;
        if(Objects.isNull(balanceResult)){
            return;
        }
        dateList=balanceResult.getLeft();
        String fileName="poi/report_outsourced_balance.xlsx";
        String fileChineseName="委外合同余额";
        wareReportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
    }

    @EvApiByToken(value = "/apis/outsourcedManagement/inOutReconciliation", method = RequestMethod.POST, apiTitle = "委外收发对账")
    @ApiOperation("委外收发对账")
    public R inOutReconciliation(
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);

        params.put("outboundType", ConstantForGYL.WWCK);
        params.put("auditSign", ConstantForMES.OK_AUDITED);
        Map<String, Object> results = Maps.newHashMap();

        // 获取委外合同列表
        List<Map<String, Object>> inOutReconciliationList = reportService.inOutReconciliationList(params);
        if (inOutReconciliationList.size() > 0) {
            results.put("data", inOutReconciliationList);
        }
        return R.ok(results);
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/outsourcedReconciliationGetOut", method = RequestMethod.GET, apiTitle = "导出——委外收发对账")
    @ApiOperation("导出——委外收发对账")
    public void exportExcelReconciliation(
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        params.put("outboundType", ConstantForGYL.WWCK);
        params.put("auditSign", ConstantForMES.OK_AUDITED);
        // 获取委外合同列表
        List<Map<String, Object>> dateList = reportService.inOutReconciliationList(params);
        if(dateList.isEmpty()){
            return;
        }
        String fileName="poi/report_outsourced_reconciliation.xlsx";
        String fileChineseName="委外收发对账";
        wareReportService.processingExport(request,response,modelMap,fileName,dateList,fileChineseName);
    }

}
