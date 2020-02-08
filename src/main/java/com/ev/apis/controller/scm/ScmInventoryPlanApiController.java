package com.ev.apis.controller.scm;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.domain.InventoryPlanDO;
import com.ev.scm.service.InventoryPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                    "\"id\":(明细主键（修改时必传）)," +
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
            @ApiParam(value = "单据编号/盘点人") @RequestParam(value = "false", defaultValue = "", required = false) String code,
            @ApiParam(value = "计划时间Start") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "计划时间End") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime) {

        Map<String, Object> resuls = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("code", code);
        params.put("checkers", code);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
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


    @EvApiByToken(value = "/apis/scm/inventoryPlan/saveChangeResult", method = RequestMethod.POST, apiTitle = "保存盘点结果")
    @ApiOperation("保存盘点结果")
    public R saveEditResuls(
            InventoryPlanDO checkHeadDO,
            @ApiParam(value = "盘点产品明细行：[{" +
                    "\"id\":1," +
                    "\"materielId\":2," +
                    "\"stockId\":2," +
                    "\"warehouse\":2," +
                    "\"warehLocation\":2," +
                    "\"batch\":\"20191225001\"," +
                    "\"systemCount\":50," +
                    "\"checkCount\":49," +
                    "\"profitLoss\":-1}" +
                    "]") @RequestParam(value = "checkBodyDO") String checkBodys) {
        return inventoryPlanService.savePlanDetail(checkHeadDO,checkBodys);
    }


    @EvApiByToken(value = "/apis/scm/inventoryPlan/buildWinStock", method = RequestMethod.POST, apiTitle = "生成盘盈单")
    @ApiOperation("生成盘盈单")
    public R buildInStockByCheck(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {
        return inventoryPlanService.buildLossStock(planId);
    }

    @EvApiByToken(value = "/apis/scm/inventoryPlan/buildLossStock", method = RequestMethod.POST, apiTitle = "生成盘亏单")
    @ApiOperation("生成盘亏单")
    public R buildLossStockByCheck(
            @ApiParam(value = "盘点方案id", required = true) @RequestParam(value = "planId") Long planId) {
        return inventoryPlanService.buildLossStock( planId );
    }






}
