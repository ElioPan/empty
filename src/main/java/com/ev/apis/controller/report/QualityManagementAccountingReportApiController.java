package com.ev.apis.controller.report;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.config.ConstantForMES;
import com.ev.framework.config.ConstantForReport;
import com.ev.framework.utils.BeanUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.report.service.QualityManagementAccountingReportService;
import com.google.common.collect.Lists;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 质量管理报表分析
 *
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2020-03-23 09:51:41
 */
@Api(value = "/", tags = "质量管理报表分析API")
@RestController
public class QualityManagementAccountingReportApiController {
    @Autowired
    private QualityManagementAccountingReportService reportService;

    @EvApiByToken(value = "/apis/qualityManagement/pieceRate/badPurchase", method = RequestMethod.POST, apiTitle = "采购不良分析")
    @ApiOperation("采购不良分析")
    public R badPurchase(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> badPurchaseResult =
                reportService.badPurchaseResult( showTotalInt
                        , showItemInt
                        , supplierId
                        , materielId
                        , startTime
                        , endTime
                );
        if (badPurchaseResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", badPurchaseResult.getLeft());
//        results.put("total", badPurchaseResult.getRight());
        return R.ok(results);

    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/badPurchase", method = RequestMethod.GET, apiTitle = "采购不良分析(导出)")
    @ApiOperation("采购不良分析(导出)")
    public void badPurchase(
            @ApiParam(value = "显示小计", required = true) @RequestParam(value = "showTotal", defaultValue = "1") int showTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap
    ) {
        Pair<List<Map<String, Object>>, Map<String, Object>> badPurchaseResult =
                reportService.badPurchaseResult( showTotalInt
                        , showItemInt
                        , supplierId
                        , materielId
                        , startTime
                        , endTime
                );
        if (badPurchaseResult == null) {
            return;
        }
        ClassPathResource classPathResource = new ClassPathResource("poi/report_quality_badPurchase.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", badPurchaseResult.getLeft());
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "采购不良分析");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/badProcess", method = RequestMethod.GET, apiTitle = "工序不良分析(导出)")
    @ApiOperation("工序不良分析（导出）")
    public void badProcessForDept(
            @ApiParam(value = "显示部门小计", required = true) @RequestParam(value = "showDeptTotal", defaultValue = "1") int showDeptTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "显示工序小计", required = true) @RequestParam(value = "showProcessTotal", defaultValue = "1") int showProcessTotalInt,

            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "工序ID") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap

    ) {
        Pair<List<Map<String, Object>>, BigDecimal> badProcessForDeptResult =
                reportService.badProcessForDeptResult( showDeptTotalInt
                        , showItemInt
                        , showProcessTotalInt
                        , deptId
                        , processId
                        , operator
                        , deviceId
                        , startTime
                        , endTime
                );
        if (badProcessForDeptResult == null) {
            return ;
        }
        ClassPathResource classPathResource = new ClassPathResource("poi/report_quality_badProcess.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", badProcessForDeptResult.getLeft());
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "工序不良分析");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);


    }

    @EvApiByToken(value = "/apis/qualityManagement/badProcess", method = RequestMethod.POST, apiTitle = "工序不良分析")
    @ApiOperation("工序不良分析")
    public R badProcessForDept(
            @ApiParam(value = "显示部门小计", required = true) @RequestParam(value = "showDeptTotal", defaultValue = "1") int showDeptTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "显示工序小计", required = true) @RequestParam(value = "showProcessTotal", defaultValue = "1") int showProcessTotalInt,

            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "工序ID") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "操作工ID") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "设备ID") @RequestParam(value = "deviceId", defaultValue = "", required = false) Long deviceId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, BigDecimal> badProcessForDeptResult =
                reportService.badProcessForDeptResult( showDeptTotalInt
                        , showItemInt
                        , showProcessTotalInt
                        , deptId
                        , processId
                        , operator
                        , deviceId
                        , startTime
                        , endTime
                );
        if (badProcessForDeptResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", badProcessForDeptResult.getLeft());
        results.put("total", badProcessForDeptResult.getRight());
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/badProduct", method = RequestMethod.GET, apiTitle = "产品不良分析(导出)")
    @ApiOperation("产品不良分析（导出）")
    public void badProductForDept(
            @ApiParam(value = "显示部门小计", required = true) @RequestParam(value = "showDeptTotal", defaultValue = "1") int showDeptTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "显示产品小计", required = true) @RequestParam(value = "showProductTotal", defaultValue = "1") int showProductTotalInt,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "产品ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap
    ) {
        Pair<List<Map<String, Object>>, BigDecimal> badProductForDeptResult =
                reportService.badProductForDeptResult( showDeptTotalInt
                        , showItemInt
                        , showProductTotalInt
                        , deptId
                        , materielId
                        , startTime
                        , endTime
                );
        if (badProductForDeptResult == null) {
            return;
        }

        ClassPathResource classPathResource = new ClassPathResource("poi/report_quality_badProduct.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", badProductForDeptResult.getLeft());
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "产品不良分析");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @EvApiByToken(value = "/apis/qualityManagement/badProduct", method = RequestMethod.POST, apiTitle = "产品不良分析")
    @ApiOperation("产品不良分析")
    public R badProductForDept(
            @ApiParam(value = "显示部门小计", required = true) @RequestParam(value = "showDeptTotal", defaultValue = "1") int showDeptTotalInt,
            @ApiParam(value = "显示详细", required = true) @RequestParam(value = "showItem", defaultValue = "1") int showItemInt,
            @ApiParam(value = "显示产品小计", required = true) @RequestParam(value = "showProductTotal", defaultValue = "1") int showProductTotalInt,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "产品ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        Pair<List<Map<String, Object>>, BigDecimal> badProductForDeptResult =
                reportService.badProductForDeptResult( showDeptTotalInt
                        , showItemInt
                        , showProductTotalInt
                        , deptId
                        , materielId
                        , startTime
                        , endTime
                );
        if (badProductForDeptResult == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", badProductForDeptResult.getLeft());
        results.put("total", badProductForDeptResult.getRight());
        return R.ok(results);
    }

    /**
     *  采购入库物料且有批次管理
     */
    @EvApiByToken(value = "/apis/qualityManagement/qualityTraceability", method = RequestMethod.POST, apiTitle = "质量追溯分析")
    @ApiOperation("质量追溯分析")
    public R qualityTraceability(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批号") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();


        params.put("batch", batch);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForGYL.OK_AUDITED);

        List<Long> storageTypes =  Lists.newArrayList();
        storageTypes.add(ConstantForGYL.PURCHASE_INSTOCK);
        storageTypes.add(ConstantForGYL.OUTSOURCING_INSTOCK);
        params.put("storageTypes", storageTypes);

        params.put("isQuality", 1);

        // 采购入库&来料检验&生产领料&生产计划
        List<Map<String, Object>> data = reportService.qualityTraceabilityList(params);
        List<Map<String, Object>> inspectionList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.LLJY.toString()))
                .collect(Collectors.toList());
        data.removeAll(inspectionList);

        Map<String,String> batchForSourceCode = Maps.newHashMap();
        List<HashMap<String, Object>> inspectionLists= Lists.newArrayList();
        List<String> emptyId = Lists.newArrayList();
        for (Map<String, Object> datum : data) {
            if (Objects.equals(datum.get("way").toString(),"in") ) {
                String batchP = datum.get("batch").toString();
                if(batchForSourceCode.containsKey(batchP)){
                    continue;
                }
                String sourceCode = datum.get("sourceCode").toString();
                List<HashMap<String, Object>> s = inspectionList
                        .stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            boolean isMatch = batch1==null||StringUtils.isEmpty(batch1.toString())||Objects.equals(batch1.toString(),batchP);
                            return Objects.equals(e.get("sourceCode"), sourceCode) && isMatch;
                        })
                        .map(Maps::newHashMap)
                        .collect(Collectors.toList());
                List<String> collect = s.stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            return batch1 == null || StringUtils.isEmpty(batch1.toString());
                        })
                        .map(e -> e.get("id").toString())
                        .collect(Collectors.toList());
                emptyId.addAll(collect);
                s.forEach(e->e.put("batch",batchP));

                inspectionLists.addAll(s);
                batchForSourceCode.put(batchP,sourceCode);
            }
        }

        ArrayList<Map<String, Object>> clone = BeanUtils.clone((ArrayList<Map<String, Object>>) data);
        Map<String, Map<String, Object>> batchGroup = clone.stream()
                .collect(Collectors.toMap(k -> k.get("batch").toString(), v -> v, (v1, v2) -> v1));
        for (Map<String, Object> value : batchGroup.values()) {
            value.remove("typeName");
            value.remove("id");
            value.remove("sourceCode");
            value.remove("code");
            value.remove("count");
            value.remove("facilityName");
            value.remove("way");
            value.remove("locationName");
            value.remove("sourceTypeName");
            value.remove("time");
            value.put("times",0);
            value.put("sign", ConstantForReport.COLOUR_END);
            value.put("sortNo",0);
        }
        data.addAll(batchGroup.values());
        data.addAll(inspectionLists);

        List<Map<String, Object>> collect = data
                .stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("times").toString())))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                .sorted(Comparator.comparing(e -> e.get("batch").toString()))
                .collect(Collectors.toList());

        if (emptyId.size() > 0) {
            for (Map<String, Object> stringObjectMap : collect) {
                if(emptyId.contains(stringObjectMap.get("id").toString())){
                    stringObjectMap.remove("batch");
                }
            }
        }

        Map<String, Object> results = Maps.newHashMap();
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, 0, collect));
        }
        return R.ok(results);
    }

    /**
     *  采购入库物料且有批次管理
     */
    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/qualityTraceability", method = RequestMethod.GET, apiTitle = "质量追溯分析(导出)")
    @ApiOperation("质量追溯分析（导出）")
    public void qualityTraceability(
            @ApiParam(value = "物料ID", required = true) @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批号", required = true) @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap

    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();


        params.put("batch", batch);
        params.put("materielId", materielId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        params.put("auditSign", ConstantForGYL.OK_AUDITED);

        List<Long> storageTypes =  Lists.newArrayList();
        storageTypes.add(ConstantForGYL.PURCHASE_INSTOCK);
        storageTypes.add(ConstantForGYL.OUTSOURCING_INSTOCK);
        params.put("storageTypes", storageTypes);

        params.put("isQuality", 1);

        // 采购入库&来料检验&生产领料&生产计划
        List<Map<String, Object>> data = reportService.qualityTraceabilityList(params);
        List<Map<String, Object>> inspectionList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.LLJY.toString()))
                .collect(Collectors.toList());
        data.removeAll(inspectionList);

        Map<String,String> batchForSourceCode = Maps.newHashMap();
        List<HashMap<String, Object>> inspectionLists= Lists.newArrayList();
        List<String> emptyId = Lists.newArrayList();
        for (Map<String, Object> datum : data) {
            if (Objects.equals(datum.get("way").toString(),"in") ) {
                String batchP = datum.get("batch").toString();
                if(batchForSourceCode.containsKey(batchP)){
                    continue;
                }
                String sourceCode = datum.get("sourceCode").toString();
                List<HashMap<String, Object>> s = inspectionList
                        .stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            boolean isMatch = batch1==null||StringUtils.isEmpty(batch1.toString())||Objects.equals(batch1.toString(),batchP);
                            return Objects.equals(e.get("sourceCode"), sourceCode) && isMatch;
                        })
                        .map(Maps::newHashMap)
                        .collect(Collectors.toList());
                List<String> collect = s.stream()
                        .filter(e -> {
                            Object batch1 = e.get("batch");
                            return batch1 == null || StringUtils.isEmpty(batch1.toString());
                        })
                        .map(e -> e.get("id").toString())
                        .collect(Collectors.toList());
                emptyId.addAll(collect);
                s.forEach(e->e.put("batch",batchP));

                inspectionLists.addAll(s);
                batchForSourceCode.put(batchP,sourceCode);
            }
        }

        ArrayList<Map<String, Object>> clone = BeanUtils.clone((ArrayList<Map<String, Object>>) data);
        Map<String, Map<String, Object>> batchGroup = clone.stream()
                .collect(Collectors.toMap(k -> k.get("batch").toString(), v -> v, (v1, v2) -> v1));
        for (Map<String, Object> value : batchGroup.values()) {
            value.remove("typeName");
            value.remove("id");
            value.remove("sourceCode");
            value.remove("code");
            value.remove("count");
            value.remove("facilityName");
            value.remove("way");
            value.remove("locationName");
            value.remove("sourceTypeName");
            value.remove("time");
            value.put("times",0);
            value.put("sign", ConstantForReport.COLOUR_END);
            value.put("sortNo",0);
        }
        data.addAll(batchGroup.values());
        data.addAll(inspectionLists);

        List<Map<String, Object>> collect = data
                .stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("times").toString())))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                .sorted(Comparator.comparing(e -> e.get("batch").toString()))
                .collect(Collectors.toList());

        for (Map<String, Object> stringObjectMap : collect) {
            if(emptyId.contains(stringObjectMap.get("id").toString())){
                stringObjectMap.remove("batch");
            }
        }
        if (data.size() > 0) {
            ClassPathResource classPathResource = new ClassPathResource("poi/report_quality_qualityTraceability.xlsx");
            Map<String,Object> map = Maps.newHashMap();
            map.put("list", collect);
            TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
            modelMap.put(TemplateExcelConstants.FILE_NAME, "质量追溯分析");
            modelMap.put(TemplateExcelConstants.PARAMS, result);
            modelMap.put(TemplateExcelConstants.MAP_DATA, map);
            PoiBaseView.render(modelMap, request, response,
                    TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
        }

    }

}
