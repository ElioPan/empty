package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.scm.domain.InventoryPlanDO;
import com.ev.scm.service.InventoryPlanItemService;
import com.ev.scm.service.InventoryPlanService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Kuzi
 * @Date 2019-09-19 16:07
 *
 **/

@EnableTransactionManagement
@Api(value="/",tags = "盘点方案")
@RestController
public class ScmInventoryPlanApiController {
    @Autowired
    private InventoryPlanService inventoryPlanService;
    @Autowired
    private InventoryPlanItemService inventoryPlanItemService;

    @EvApiByToken(value = "/apis/scm/inventoryPlan/choceProCount", method = RequestMethod.POST, apiTitle = "查询（“增加盘点方案”明细行商品）")
    @ApiOperation("查询（“增加盘点方案”明细行商品）")
    @Transactional(rollbackFor=Exception.class)
    public  R getMsgforProCount(
            @ApiParam(value = "仓库id(选择所有仓库id为空)",required = true) @RequestParam(value = "warehouse") Long warehouse,
            @ApiParam(value = "商品名称/编码：String") @RequestParam(value = "syntheticData", defaultValue = "") String syntheticData ) {

        return inventoryPlanService.getMaterielCount(warehouse,syntheticData);
    }


    @EvApiByToken(value = "/apis/scm/inventoryPlan/addCheckAndChange", method = RequestMethod.POST, apiTitle = "增加盘点方案")
    @ApiOperation("增加盘点方案")
    @Transactional(rollbackFor=Exception.class)
    public R addAndChangePlan(
            InventoryPlanDO checkHeadDO,
            @ApiParam(value = "盘点产品明细行：[{\"materielId\":2," +
                    "\"stockId\":2," +
                    "\"warehouse\":2," +
                    "\"warehLocation\":2," +
                    "\"batch\":\"20191225001\"," +
                    "\"systemCount\":50}" +
                    "]") @RequestParam(value = "checkBodys", defaultValue = "", required = false) String checkBodys,
            @ApiParam(value = "删除的明细行IDs") @RequestParam(value = "deleItemIds", defaultValue = "", required = false) Long[] deleItemIds) {

        return inventoryPlanService.addInventoryPlan(checkHeadDO,checkBodys,deleItemIds);
    }


    @EvApiByToken(value = "/apis/scm/inventoryPlan/deletPlan", method = RequestMethod.POST, apiTitle = "删除盘点方案")
    @ApiOperation("删除盘点方案")
    @Transactional(rollbackFor = Exception.class)
    public R deletPlanNow(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {

            return inventoryPlanService.deletBatchPlanNow(planId);
    }


    @EvApiByToken(value = "/apis/scm/inventoryPlan/istPlan", method = RequestMethod.POST, apiTitle = "盘点方案列表/查询")
    @ApiOperation("盘点方案列表/查询")
    public R getPlanList(
            @ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
            @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
            @ApiParam(value = "单据编号") @RequestParam(value = "code", defaultValue = "", required = false) String code,
            @ApiParam(value = "计划时间Start") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "计划时间End") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "手机选择盘点方案时必传（1）") @RequestParam(value = "QR", required = false) Integer QR) {

        Map<String, Object> resuls = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("code", code);
//        params.put("checkers", code);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("qr", QR);

        params.put("checkStatusOne", ConstantForGYL.EXECUTE_NON);
        params.put("checkStatusTwo", ConstantForGYL.EXECUTE_NOW);


        int counts = inventoryPlanService.countOfListByDates(params);
        List<Map<String, Object>> dateList = inventoryPlanService.listByDates(params);

        if(dateList.size()>0){
            DsResultResponse dsResultResponse =new DsResultResponse() ;
            dsResultResponse.setDatas(dateList);
            dsResultResponse.setPageno(pageno);
            dsResultResponse.setPagesize(pagesize);
            dsResultResponse.setTotalRows(counts);
            dsResultResponse.setTotalPages(((counts + pagesize - 1) / pagesize));

            resuls.put("data", dsResultResponse);
        }
        return R.ok(resuls);
    }


    @EvApiByToken(value = "/apis/scm/inventoryPlan/doPlan", method = RequestMethod.POST, apiTitle = "执行盘点/方案明细")
    @ApiOperation("执行盘点/方案明细")
    public R doPlanNow(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {

        return inventoryPlanService.doInventoryPlan(planId);
    }


    /**
     * 扫码盘点提交的时候，qrSign传1，
     * @param checkHeadDO
     * @param checkBodys
     * @return
     */
    @EvApiByToken(value = "/apis/scm/inventoryPlan/saveChangeResult", method = RequestMethod.POST, apiTitle = "保存盘点结果")
    @ApiOperation("PC端保存盘点结果")
    public R saveEditResuls(
            InventoryPlanDO checkHeadDO,
            @ApiParam(value = "盘点产品明细行：[{" +
                    "\"id\":1(PC盘点必传，)," +
                    "\"materielId\":2," +
                    "\"stockId\":库存（没有可不填）," +
                    "\"warehouse\":仓库id," +
                    "\"warehLocation\":库位id," +
                    "\"batch\":\"20191225001\"," +
                    "\"systemCount\":系统数量," +
                    "\"checkCount\":盘点数量," +
                    "\"profitLoss\":（盈亏数量 （赢+亏-））}" +
                    "]") @RequestParam(value = "checkBodyDO") String checkBodys) {
        return inventoryPlanService.savePlanDetail(checkHeadDO,checkBodys);
    }

    @EvApiByToken(value = "/apis/scm/inventoryPlan/phoneCheckResuls", method = RequestMethod.POST, apiTitle = "保存手机扫码盘点结果")
    @ApiOperation("保存手机扫码盘点结果")
    public R savePhoneCheckResuls(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId,
            @ApiParam(value = "盘点产品明细行：[{" +
                    "\"materielId\":2," +
                    "\"warehouse\":仓库id," +
                    "\"warehLocation\":库位id," +
                    "\"batch\":\"无批次不传\"," +
                    "\"checbatchkCount\":盘点数量," +
                    "\"qrId\":条码id}" +
                    "]") @RequestParam(value = "checkBodyDO") String checkBodys) {
        return inventoryPlanService.disposePhoneCheckResuls(planId,checkBodys);
    }

    @EvApiByToken(value = "/apis/scm/inventoryPlan/wetherCheckByQrId", method = RequestMethod.POST, apiTitle = "验证此条码是否已经盘点/是否在本次方案中")
    @ApiOperation("验证此条码是否已经盘点/是否在本次方案中")
    public R checkByQrId(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId,
            @ApiParam(value = "本次扫描的条码id", required = true) @RequestParam(value = "qrId") Long qrId,
            @ApiParam(value = "本次扫描的条码信息 [\n" +
                    "{\n" +
                    "\"materielId\":2,\n" +
                    "\"warehouse\":仓库id,\n" +
                    "\"warehLocation\":库位id,\n" +
                    "\"batch\":\"20191225001\"\n" +
                    "}\n" +
                    "]", required = true) @RequestParam(value = "qrMsg") String  qrMsg) {

        return inventoryPlanService.disposeCheckByQrId(planId,qrMsg,qrId);
    }



    @EvApiByToken(value = "/apis/scm/inventoryPlan/checkIsOver", method = RequestMethod.POST, apiTitle = "盘点结束")
    @ApiOperation("盘点结束")
    @Transactional(rollbackFor = Exception.class)
    public R planIsOver(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {
        return inventoryPlanService.disposePlanIsOver(planId);
    }


    @EvApiByToken(value = "/apis/scm/inventoryPlan/buildWinStock", method = RequestMethod.POST, apiTitle = "生成盘盈单")
    @ApiOperation("生成盘盈单")
    @Transactional(rollbackFor = Exception.class)
    public R buildInStockByCheck(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {
        return inventoryPlanService.buildWinStock(planId);
    }

    @EvApiByToken(value = "/apis/scm/inventoryPlan/buildLossStock", method = RequestMethod.POST, apiTitle = "生成盘亏单")
    @ApiOperation("生成盘亏单")
    @Transactional(rollbackFor = Exception.class)
    public R buildLossStockByCheck(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {
        return inventoryPlanService.buildLossStock( planId );
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/systemCountGetOut", method = RequestMethod.GET, apiTitle = "导出系统库存")
    @ApiOperation("导出系统库存")
    public void exportExcel(
            @ApiParam(value = "仓库id(选择所有仓库id为空)",required = true) @RequestParam(value = "warehouse") Long warehouse,
            @ApiParam(value = "商品名称/编码：String") @RequestParam(value = "syntheticData", defaultValue = "") String syntheticData,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String,Object> query = com.beust.jcommander.internal.Maps.newHashMap();
        query.put("warehouse",warehouse);
        query.put("name",syntheticData);

        List<Map<String, Object>> list = inventoryPlanItemService.getProMsgCount(query);

        ClassPathResource classPathResource = new ClassPathResource("poi/scm_inventory_plan.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "系统库存");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }







}
