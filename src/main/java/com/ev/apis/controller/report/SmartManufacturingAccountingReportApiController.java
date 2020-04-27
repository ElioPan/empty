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
import com.ev.report.service.SmartManufacturingAccountingReportService;
import com.ev.report.vo.CommonVO;
import com.ev.report.vo.PieceRateVO;
import com.ev.report.vo.ProcessReportVO;
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
    @Autowired
    private QualityManagementAccountingReportService qualityReportService;

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
        params.put("materielSerialNo", materielSerialNo);
        // 非计划状态下的单据
        params.put("status", ConstantForMES.PLAN);
        // 统计合计数量
        List<Map<String, Object>> totalData = reportService.productionPlanList(params);

        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = reportService.productionPlanList(params);
        int total = reportService.productionPlanCount(params);
        if (data.size() > 0) {
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionPlan = reportService.productionPlan(data, false);
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> totalProductionPlan = reportService.productionPlan(totalData, true);
            results.put("total", totalProductionPlan.getRight());
            results.put("data", new DsResultResponse(pageno, pagesize, total, productionPlan.getLeft()));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/exportExcel/productionPlan", method = RequestMethod.GET, apiTitle = "生产计划跟踪（导出）")
    @ApiOperation("生产计划跟踪（导出）")
    public void productionPlan(
            @ApiParam(value = "计划单号") @RequestParam(value = "planCode", defaultValue = "", required = false) String planCode,
            @ApiParam(value = "物料编号") @RequestParam(value = "materielSerialNo", defaultValue = "", required = false) String materielSerialNo,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("planCode", planCode);
        params.put("materielId", materielId);
        params.put("deptId", deptId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielSerialNo", materielSerialNo);
        // 非计划状态下的单据
        params.put("status", ConstantForMES.PLAN);

        List<Map<String, Object>> data = reportService.productionPlanList(params);
        if (data.size() > 0) {
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> productionPlan = reportService.productionPlan(data, false);

            ClassPathResource classPathResource = new ClassPathResource("poi/report_smartManufacturing_productionPlan.xlsx");
            Map<String,Object> map = Maps.newHashMap();
            map.put("list",  productionPlan.getLeft());
            TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
            modelMap.put(TemplateExcelConstants.FILE_NAME, "生产计划跟踪");
            modelMap.put(TemplateExcelConstants.PARAMS, result);
            modelMap.put(TemplateExcelConstants.MAP_DATA, map);
            PoiBaseView.render(modelMap, request, response,
                    TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
        }
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
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processPlan = reportService.processPlan(data, false);
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> totalProcessPlan = reportService.processPlan(totalData, true);
            results.put("total", totalProcessPlan.getRight());
            results.put("data", new DsResultResponse(pageno, pagesize, total, processPlan.getLeft()));
        }
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/processPlan", method = RequestMethod.GET, apiTitle = "工序计划跟踪(导出)")
    @ApiOperation("工序计划跟踪(导出)")
    public void processPlan(
            @ApiParam(value = "计划单号") @RequestParam(value = "planCode", defaultValue = "", required = false) String planCode,
            @ApiParam(value = "物料编号") @RequestParam(value = "materielSerialNo", defaultValue = "", required = false) String materielSerialNo,
            @ApiParam(value = "物料ID") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap
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

        List<Map<String, Object>> data = reportService.processPlanList(params);
        if (data.size() > 0) {
            Pair<List<Map<String, Object>>, Map<String, BigDecimal>> processPlan = reportService.processPlan(data, false);

            ClassPathResource classPathResource = new ClassPathResource("poi/report_smartManufacturing_processPlan.xlsx");
            Map<String,Object> map = Maps.newHashMap();
            map.put("list",  processPlan.getLeft());
            TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
            modelMap.put(TemplateExcelConstants.FILE_NAME, "工序计划跟踪");
            modelMap.put(TemplateExcelConstants.PARAMS, result);
            modelMap.put(TemplateExcelConstants.MAP_DATA, map);
            PoiBaseView.render(modelMap, request, response,
                    TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
        }
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/pieceRate", method = RequestMethod.GET, apiTitle = "计件工资统计(导出)")
    @ApiOperation("计件工资统计(导出)")
    public void pieceRateGroup(
            CommonVO commonVO,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap
    ) {
        Pair<List<PieceRateVO>, BigDecimal> pieceRateGroup =
                reportService.pieceRateGroup(commonVO);
        if (pieceRateGroup == null) {
            return;
        }

        ClassPathResource classPathResource = new ClassPathResource("poi/report_smartManufacturing_pieceRate.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list",  pieceRateGroup.getLeft());
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "计件工资统计");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @EvApiByToken(value = "/apis/smartManufacturing/pieceRate", method = RequestMethod.POST, apiTitle = "计件工资统计")
    @ApiOperation("计件工资统计")
    public R pieceRateGroup(
            CommonVO commonVO
    ) {
        Pair<List<PieceRateVO>, BigDecimal> pieceRateGroup =
                reportService.pieceRateGroup(commonVO);
        if (pieceRateGroup == null) {
            return R.ok();
        }
        Map<String, Object> results = Maps.newHashMap();
        results.put("data", pieceRateGroup.getLeft());
        results.put("total", pieceRateGroup.getRight());
        return R.ok(results);

    }

    @EvApiByToken(value = "/apis/smartManufacturing/productionBatch", method = RequestMethod.POST, apiTitle = "生产批次跟踪")
    @ApiOperation("生产批次跟踪")
    public R productionBatch(
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
        storageTypes.add(ConstantForGYL.YDGOODS_WAREHOUSE);
        params.put("storageTypes", storageTypes);

        params.put("isQuality", 0);
        params.put("isPlan", 1);

        // 采购入库&来料检验&生产领料&生产计划
        List<Map<String, Object>> data = qualityReportService.qualityTraceabilityList(params);
        List<Map<String, Object>> inspectionList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.CPJY.toString()))
                .collect(Collectors.toList());
        List<Map<String, Object>> planList = data.stream()
                .filter(e -> Objects.equals(e.get("typeId").toString(), ConstantForMES.SCJH.toString()))
                .collect(Collectors.toList());
        data.removeAll(inspectionList);
        data.removeAll(planList);

        List<HashMap<String, Object>> inspectionLists= Lists.newArrayList();
        List<HashMap<String, Object>> planLists= Lists.newArrayList();
        Map<String,String> batchForSourceCode = Maps.newHashMap();
        for (Map<String, Object> datum : data) {

            if (Objects.equals(datum.get("typeId").toString(),ConstantForGYL.YDGOODS_WAREHOUSE.toString()) ) {
                String batchP = datum.get("batch").toString();
                if(batchForSourceCode.containsKey(batch)){
                    continue;
                }

                String sourceCode = datum.get("sourceCode").toString();
                List<HashMap<String, Object>> s = inspectionList
                        .stream()
                        .filter(e -> Objects.equals(e.get("sourceCode"), sourceCode))
                        .map(Maps::newHashMap)
                        .peek(e->e.put("batch",batchP))
                        .collect(Collectors.toList());
                List<HashMap<String, Object>> p = planList
                        .stream()
                        .filter(e -> Objects.equals(e.get("code"), sourceCode))
                        .map(Maps::newHashMap)
                        .peek(e->e.put("batch",batchP))
                        .collect(Collectors.toList());
                inspectionLists.addAll(s);
                planLists.addAll(p);
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
        data.addAll(planLists);
        List<Map<String, Object>> collect = data
                .stream()
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("times").toString())))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.get("sortNo").toString())))
                .sorted(Comparator.comparing(e -> e.get("batch").toString()))
                .collect(Collectors.toList());

        for (Map<String, Object> stringObjectMap : collect) {
            String typeId = stringObjectMap.get("typeId").toString();
            if(Objects.equals(typeId,ConstantForMES.LLJY.toString())||(Objects.equals(typeId,ConstantForMES.SCJH.toString()))){
                stringObjectMap.remove("batch");
            }
        }

        Map<String, Object> results = Maps.newHashMap();
        if (data.size() > 0) {
            results.put("data", collect);
        }
        return R.ok(results);
    }

//    @EvApiByToken(value = "/apis/smartManufacturing/productionBatch/item", method = RequestMethod.POST, apiTitle = "生产批次跟踪（详细列表生产入库，生产领料单）")
//    @ApiOperation("生产批次跟踪（详细列表生产入库，生产领料单）")
//    public R productionBatchItem(
//            @ApiParam(value = "生产计划ID", required = true) @RequestParam(value = "id", defaultValue = "") Long id
//    ) {
//        return reportService.productionBatch(id);
//    }



    @EvApiByToken(value = "/apis/smartManufacturing/processOutput", method = RequestMethod.POST, apiTitle = "工序产量跟踪")
    @ApiOperation("工序产量跟踪")
    public R processOutput(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "操作工") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "工序Id") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
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
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        Map<String, Object> results = Maps.newHashMap();
        List<ProcessReportVO> data = reportService.processOutputList(params);
        Map<String,Object> totalMap = reportService.processOutputCount(params);
        int total = Integer.parseInt(totalMap.getOrDefault("total",0).toString());
        if (data.size() > 0) {
            results.put("total",totalMap);
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(results);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/processOutput", method = RequestMethod.GET, apiTitle = "工序产量跟踪（导出）")
    @ApiOperation("工序产量跟踪(导出)")
    public void processOutput(
            @ApiParam(value = "生产部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "操作工") @RequestParam(value = "userId", defaultValue = "", required = false) Long userId,
            @ApiParam(value = "工序Id") @RequestParam(value = "processId", defaultValue = "", required = false) Long processId,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap
    ) {
        // 查询列表数据
        Map<String, Object> params = Maps.newHashMap();

        params.put("deptId", deptId);
        params.put("userId", userId);
        params.put("processId", processId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<ProcessReportVO> data = reportService.processOutputList(params);
        if (data.size() > 0) {
            ClassPathResource classPathResource = new ClassPathResource("poi/report_smartManufacturing_processOutPut.xlsx");
            Map<String,Object> map = Maps.newHashMap();
            map.put("list",  data);
            TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
            modelMap.put(TemplateExcelConstants.FILE_NAME, "工序产量跟踪");
            modelMap.put(TemplateExcelConstants.PARAMS, result);
            modelMap.put(TemplateExcelConstants.MAP_DATA, map);
            PoiBaseView.render(modelMap, request, response,
                    TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
        }
    }
}
