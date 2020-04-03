package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.service.MaterielService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.service.StockAnalysisService;
import com.ev.scm.service.StockService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by guMingJie on 2020-01-22.
 */


@Api(value = "/", tags = "库存API")
@RestController
public class StockApiController {

    @Autowired
    private StockService stockService;
    @Autowired
    private MaterielService materielService;
    @Autowired
    private StockAnalysisService stockAnalysisService;

    /*导入导出*/
    @ResponseBody
    @EvApiByToken(value = "/apis/importExcel/stock", method = RequestMethod.POST, apiTitle = "期初库存导入")
    @ApiOperation("期初库存导入")
    @Transactional(rollbackFor = Exception.class)
    public R readStockFile(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) {
        return stockService.importExcel(file);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/stock", method = RequestMethod.GET, apiTitle = "导出库存")
    @ApiOperation("导出库存")
    public void exportExcel(
            @ApiParam(value = "产品类型") @RequestParam(value = "productTypeId", defaultValue = "", required = false) Long productTypeId,
            @ApiParam(value = "产品编号或名称或型号查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
            @ApiParam(value = "仓库类型") @RequestParam(value = "facilityTypeId", defaultValue = "", required = false) Long facilityTypeId,

            @ApiParam(value = "产品Id") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("productTypeId", productTypeId);
        param.put("fuzzySearch", fuzzySearch);
        param.put("facilityTypeId", facilityTypeId);

        param.put("materielId", materielId);
        param.put("batch", batch);
        param.put("isPc", 1);
        // 获取实时库存
        List<Map<String, Object>> data = materielService.stockListForMap(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/stock.xlsx");
        Map<String, Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "库存");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    @EvApiByToken(value = "/apis/stock/list", method = RequestMethod.POST, apiTitle = "库存查询")
    @ApiOperation("库存查询")
    public R list(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "产品类型") @RequestParam(value = "productTypeId", defaultValue = "", required = false) Long productTypeId,
            @ApiParam(value = "产品编号或名称或型号查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
            @ApiParam(value = "仓库类型") @RequestParam(value = "facilityTypeId", defaultValue = "", required = false) Long facilityTypeId,
            @ApiParam(value = "仓库ID") @RequestParam(value = "facilityId", defaultValue = "", required = false) String facilityId,
            @ApiParam(value = "库位ID") @RequestParam(value = "locationId", defaultValue = "", required = false) String locationId,
            @ApiParam(value = "产品Id") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "是否为PC端") @RequestParam(value = "isPC", defaultValue = "1", required = false) Integer isPC

    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("productTypeId", productTypeId);
        params.put("fuzzySearch", fuzzySearch);
        params.put("facilityTypeId", facilityTypeId);
        params.put("facilityId", facilityId);

        params.put("locationId", locationId);
        params.put("materielId", materielId);
        params.put("batch", batch);
        params.put("isPC", isPC);
        List<Map<String, Object>> data = materielService.stockListForMap(params);
        int total = materielService.stockCountForMap(params);
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno, pagesize, total, data));
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/stock/stockGatherList", method = RequestMethod.POST, apiTitle = "库存汇总查询")
    @ApiOperation("库存汇总查询")
    public R stockGatherList(
            @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "产品类型") @RequestParam(value = "productTypeId", defaultValue = "", required = false) Long productTypeId,
            @ApiParam(value = "产品编号或名称或型号查询") @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch,
            @ApiParam(value = "仓库类型") @RequestParam(value = "facilityTypeId", defaultValue = "", required = false) Long facilityTypeId,
            @ApiParam(value = "仓库ID") @RequestParam(value = "facilityId", defaultValue = "", required = false) String facilityId,
            @ApiParam(value = "库位ID") @RequestParam(value = "locationId", defaultValue = "", required = false) String locationId,
            @ApiParam(value = "产品Id") @RequestParam(value = "materielId", defaultValue = "", required = false) Long materielId,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "是否为PC端") @RequestParam(value = "isPC", defaultValue = "1", required = false) Integer isPC

    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("productTypeId", productTypeId);
        params.put("fuzzySearch", fuzzySearch);
        params.put("facilityTypeId", facilityTypeId);
        params.put("facilityId", facilityId);

        params.put("locationId", locationId);
        params.put("materielId", materielId);
        params.put("batch", batch);
        params.put("isPC", isPC);
        List<Map<String, Object>> data = materielService.stockListForMap(params);
        if (data.size() > 0) {
            Map<Long,Map<String, Object>>  materielForStock = Maps.newHashMap();
            Map<String, Object> map;
            for (Map<String, Object> datum : data) {
                long materielIdForStock = Long.parseLong(datum.get("materielId").toString());
                if (materielForStock.containsKey(materielIdForStock)) {
                    map = materielForStock.get(materielIdForStock);
                    map.put("availableCount",MathUtils.getBigDecimal(map.get("availableCount")).add(MathUtils.getBigDecimal(datum.get("availableCount")))) ;
                    continue;
                }
                materielForStock.put(materielIdForStock,datum);
            }
            List<Map<String, Object>> stockList = Lists.newArrayList();
            stockList.addAll(materielForStock.values());
            List<Map<String, Object>> stockLists = PageUtils.startPage(stockList, pageno, pagesize);
            results.put("data", new DsResultResponse(pageno, pagesize, materielForStock.size(), stockLists));
        }
        return R.ok(results);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/start", method = RequestMethod.POST, apiTitle = "设置库存初始时间")
    @ApiOperation("设置库存初始时间")
    public R save(@ApiParam(value = "启用年月") @RequestParam(value = "yearAndMonth", defaultValue = "", required = false) String yearAndMonth) {
        return stockService.saveStockStartTime(yearAndMonth);
    }

    @EvApiByToken(value = "/apis/stock/startDetail", method = RequestMethod.POST, apiTitle = "查看库存初始时间")
    @ApiOperation("查看库存初始时间")
    public R detail() {
        return stockService.getStartTime();
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/save", method = RequestMethod.POST, apiTitle = "初始库存录入")
    @ApiOperation("初始库存录入")
    public R saveStock(@ApiParam(value = "出库明细:" +
            "[\n" +
            "    {\n" +
            "        \"materielId\":25,\n" +
            "        \"batch\":\"wh_ycl_0045\",\n" +
            "        \"warehouse\":3,\n" +
            "        \"warehLocation\":7,\n" +
            "        \"count\":2011,\n" +
            "        \"amount\":24000\n" +
            "    },\n" +
            "    {\n" +
            "        \"materielId\":25,\n" +
            "        \"batch\":\"wh_ycl_0034\",\n" +
            "        \"warehouse\":4,\n" +
            "        \"warehLocation\":8,\n" +
            "        \"count\":241,\n" +
            "        \"amount\":24000\n" +
            "    }\n" +
            "]"
            , required = true) @RequestParam(value = "item", defaultValue = "") String stockList,
                       @ApiParam(value = "明细数组") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds) {
        return stockService.initStock(stockList,itemIds);
    }

    @EvApiByToken(value = "/apis/stock/startList", method = RequestMethod.POST, apiTitle = "期初库存列表")
    @ApiOperation("期初库存列表")
    public R startList() {
        return stockService.startList();
    }


    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/endInitial", method = RequestMethod.POST, apiTitle = "结束初始化")
    @ApiOperation("结束初始化")
    public R endInitial() {
        return stockService.endInitial();
    }

    @EvApiByToken(value = "/apis/stock/stockAnalysisTime", method = RequestMethod.POST, apiTitle = "获取本次期间")
    @ApiOperation("获取本次期间")
    public R stockOutAccountingTime() {
        return stockService.stockOutAccountingTime();
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/stockOutAccounting", method = RequestMethod.POST, apiTitle = "出库核算")
    @ApiOperation("出库核算")
    public R stockOutAccounting(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        return stockService.stockOutAccounting(period);
    }

    @EvApiByToken(value = "/apis/stock/stockOutAccountingCheck", method = RequestMethod.POST, apiTitle = "检验出库核算（检查是否有单价为0的入库单存在）")
    @ApiOperation("检验出库核算（检查是否有单价为0的入库单存在）")
    public R stockOutAccountingCheck(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        return stockService.stockOutAccountingCheck(period);
    }

    @EvApiByToken(value = "/apis/stock/checkEndingCarryOver", method = RequestMethod.POST, apiTitle = "检验期末结转（检查是否有单价为0的入库单/出库单存在）")
    @ApiOperation("检验期末结转（检查是否有单价为0的入库单/出库单存在）")
    public R checkEndingCarryOver(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        return stockService.checkEndingCarryOver(period);

    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/endingCarryOver", method = RequestMethod.POST, apiTitle = "期末结转")
    @ApiOperation("期末结转")
    public R endingCarryOver(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        return stockService.endingCarryOver(period);
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/stock/endingClose", method = RequestMethod.POST, apiTitle = "期末关账")
    @ApiOperation("期末关账")
    public R endingClose(@ApiParam(value = "计算时间", required = true) @RequestParam(value = "period", defaultValue = "") String period) {
        return stockService.endingClose(period);
    }

    @EvApiByToken(value = "/apis/stock/analysis", method = RequestMethod.POST, apiTitle = "报表分析")
    @ApiOperation("报表分析")
    public R analysis(@ApiParam(value = "计算时间") @RequestParam(value = "period", defaultValue = "", required = false) String period,
                      @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                      @ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                      @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                      @ApiParam(value = "显示已关账期间（1是/0否）") @RequestParam(value = "isClose", defaultValue = "", required = false) Integer isClose
    ) {
        Map<String, Object> results = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
//        params.put("offset", (pageno - 1) * pagesize);
//        params.put("limit", pagesize);
        params.put("period", period);
        params.put("isClose", isClose);
        List<Map<String, Object>> data = Lists.newArrayList();
        // 分批认定 列表
        params.put("valuationMethod", ConstantForGYL.BATCH_FINDS);
        params.put("materielName", StringUtils.sqlLike(materielName));
        List<Map<String, Object>> batchData = stockAnalysisService.listForMap(params);
        if (batchData.size() > 0) {
            data.addAll(batchData);
        }
        // 加权平局 列表
        params.put("valuationMethod", ConstantForGYL.WEIGHTED_AVERAGE);
        List<Map<String, Object>> groupData = stockAnalysisService.listForMapGroupMateriel(params);
        if (groupData.size() > 0) {
            data.addAll(groupData);
        }
        Map<String, Object> map = stockAnalysisService.countForTotal(params);
        if (data.size() > 0) {
            List<Map<String, Object>> maps = PageUtils.startPage(data, pageno, pagesize);
            results.put("total", map);
            results.put("data", new DsResultResponse(pageno, pagesize, data.size(), maps));
        }
        return R.ok(results);
    }


}





