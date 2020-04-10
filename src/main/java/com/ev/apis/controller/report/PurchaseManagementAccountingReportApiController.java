package com.ev.apis.controller.report;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.PurchaseManagementAccountingReportService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 采购管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-31 09:51:41
 */
@Api(value = "/", tags = "采购管理报表分析API")
@RestController
public class PurchaseManagementAccountingReportApiController {
    @Autowired
    private PurchaseManagementAccountingReportService reportService;

    @EvApiByToken(value = "/apis/purchaseManagement/tracking", method = RequestMethod.POST, apiTitle = "采购全程跟踪")
    @ApiOperation("采购全程跟踪")
    public R tracking(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料类型") @RequestParam(value = "materielType", defaultValue = "", required = false) Long materielType,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "要显示详情 1是0否", required = true) @RequestParam(value = "showItem", defaultValue ="1") Integer showItem,
            @ApiParam(value = "要显示小计 1是0否", required = true) @RequestParam(value = "showUser", defaultValue = "1") Integer showUser,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
//        params.put("offset", (pageno - 1) * pagesize);
//        params.put("limit", pagesize);
        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("materielType", materielType);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("auditSign", ConstantForMES.OK_AUDITED);

       return  reportService.disposeTracking(params,showItem, showUser);
    }

    @EvApiByToken(value = "/apis/purchaseManagement/debtDue", method = RequestMethod.POST, apiTitle = "采购到期债务(供应商小计)")
    @ApiOperation("采购到期债务(供应商小计)")
    public R debtDue(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
//            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商Id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "要显示详情 1是0否", required = true) @RequestParam(value = "showItem", defaultValue ="1") Integer showItem,
            @ApiParam(value = "要显示小计 1是0否", required = true) @RequestParam(value = "showUser", defaultValue = "1") Integer showUser,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {

        Map<String, Object> params = Maps.newHashMap();
//        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);
        return  reportService.disposeDebtDue(params,showItem, showUser);

    }



    @EvApiByToken(value = "/apis/purchaseManagement/balance", method = RequestMethod.POST, apiTitle = "采购合同余额(供应商小计)")
    @ApiOperation("采购合同余额(供应商小计)")
    public R balance(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "要显示详情 1是0否", required = true) @RequestParam(value = "showItem", defaultValue ="1") Integer showItem,
            @ApiParam(value = "要显示小计 1是0否", required = true) @RequestParam(value = "showUser", defaultValue = "1") Integer showUser,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("supplierName", StringUtils.sqlLike(supplierName));
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("endTime", endTime);

        return reportService.disposeBalance(params,showItem,showUser);
    }


    @EvApiByToken(value = "/apis/purchaseManagement/priceAnalysis", method = RequestMethod.POST, apiTitle = "采购价格分析")
    @ApiOperation("采购价格分析")
    public R priceAnalysis(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("supplierId", supplierId);
        params.put("deptId", deptId);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("auditSign", ConstantForMES.OK_AUDITED);

        return reportService.disposePriceAnalysis(params,pageno,pagesize);
    }


//    @EvApiByToken(value = "/apis/purchaseManagement/balance/item", method = RequestMethod.POST, apiTitle = "采购合同余额(详细)")
//    @ApiOperation("采购合同余额(详细)")
//    public R balanceItem(
//            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
//            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
//            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("supplierId", supplierId);
//        params.put("deptId", deptId);
//        params.put("userId", userId);
//        params.put("endTime", endTime);
//
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
//        if (balanceLists.size() > 0) {
//            results.put("data", balanceLists);
//        }
//        return R.ok(results);
//    }

//    @EvApiByToken(value = "/apis/purchaseManagement/debtDue/item", method = RequestMethod.POST, apiTitle = "采购到期债务(详细)")
//    @ApiOperation("采购到期债务(详细)")
//    public R debtDueItem(
//            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
//            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
//            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("supplierId", supplierId);
//        params.put("deptId", deptId);
//        params.put("userId", userId);
//        params.put("endTime", endTime);
//
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> debtDueLists = reportService.debtDueList(params);
//        if (debtDueLists.size() > 0) {
//            results.put("data", debtDueLists);
//        }
//        return R.ok(results);
//    }

//    @EvApiByToken(value = "/apis/purchaseManagement/balance/item", method = RequestMethod.POST, apiTitle = "采购合同余额(详细)")
//    @ApiOperation("采购合同余额(详细)")
//    public R balanceItem(
//            @ApiParam(value = "供应商ID", required = true) @RequestParam(value = "supplierId", defaultValue = "") Long supplierId,
//            @ApiParam(value = "部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
//            @ApiParam(value = "采购员") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
//            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
//    ) {
//        // 查询列表数据
//        Map<String, Object> params = Maps.newHashMap();
//
//        params.put("supplierId", supplierId);
//        params.put("deptId", deptId);
//        params.put("userId", userId);
//        params.put("endTime", endTime);
//
//        Map<String, Object> results = Maps.newHashMap();
//        List<Map<String, Object>> balanceLists = reportService.balanceList(params);
//        if (balanceLists.size() > 0) {
//            results.put("data", balanceLists);
//        }
//        return R.ok(results);
//    }




}
