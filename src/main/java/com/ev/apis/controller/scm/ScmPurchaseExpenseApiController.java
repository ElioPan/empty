package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchaseExpenseDO;
import com.ev.scm.service.PurchaseExpenseService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Kuzi
 * @Date 2020-1-10 14:43
 *
 **/
@Api(value = "/",tags = "采购费用")
@RestController
public class ScmPurchaseExpenseApiController {
    @Autowired
    private PurchaseExpenseService purchaseExpenseService;

    @EvApiByToken(value = "/apis/scm/purchaseExpense/addAndChange",method = RequestMethod.POST,apiTitle = "保存/修改—采购费用")
    @ApiOperation("保存/修改—采购费用")
    @Transactional(rollbackFor = Exception.class)
    public R addPurchaseProduct(PurchaseExpenseDO purchaseExpenseDO,
                                @ApiParam(value = "添加采购申请明细:[\n" +
                                        "{\n" +
                                        "\"id\":\"明细行主键(修改时必传)\",\n" +
                                        "\"unit\":\"单位id\",\n" +
                                        "\"count\":\"数量\",\n" +
                                        "\"taxUnitPrice\":\"含税单价\",\n" +
                                        "\"unitPrice\":\"不含税单价\",\n" +
                                        "\"taxRate\":\"税率\",\n" +
                                        "\"amount\":\"不含税金额“,\n" +
                                        "\"taxes\":\"税额\",\n" +
                                        "\"taxAmount\":\"含税金额（价格合计）\"\n" +
                                        "}\n" +
                                        "]", required = true) @RequestParam(value = "item", defaultValue = "") String item,
                                @ApiParam(value = "删除的明细行id:") @RequestParam(value = "itemIds") Long[] deleItemIds){
        return purchaseExpenseService.addPurchase(purchaseExpenseDO,item,deleItemIds);
    }

    @EvApiByToken(value = "/apis/scm/purchaseExpense/audit",method = RequestMethod.POST,apiTitle = "审核—采购费用")
    @ApiOperation("审核—采购费用")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "采购费用id:", required = true) @RequestParam(value = "id") Long id){
        return purchaseExpenseService.audit(id);
    }

    @EvApiByToken(value = "/apis/scm/purchaseExpense/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核—采购费用")
    @ApiOperation("反审核—采购费用")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "采购费用id:", required = true) @RequestParam(value = "id") Long id){
        return purchaseExpenseService.rollBackAudit(id);
    }

    @EvApiByToken(value = "/apis/scm/purchaseExpense/delet",method = RequestMethod.POST,apiTitle = "删除—采购费用")
    @ApiOperation("删除—采购费用")
    @Transactional(rollbackFor = Exception.class)
    public R removePurchase(@ApiParam(value = "采购费用id:", required = true) @RequestParam(value = "ids") Long[] ids){
        return purchaseExpenseService.removePurchase(ids);
    }

    @EvApiByToken(value = "/apis/scm/purchaseExpense/listOfExpense", method = RequestMethod.POST, apiTitle = "列表—采购费用")
    @ApiOperation("列表—采购费用")
    public R purchaseOflist(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
                            @ApiParam(value = "开始日期(开票时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                            @ApiParam(value = "截止日期(开票时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("supplierName", supplierName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<Map<String, Object>> list = purchaseExpenseService.listForMap(params);
        //数量中添加统计  数量  + 金额
        Map<String, Object> countForMaps = purchaseExpenseService.countForMap(params);
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
        Map<String,Object>  dsRet= new HashMap<>();
        if (!list.isEmpty()) {
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",(Integer.parseInt(countForMaps.get("count").toString()) + pagesize - 1) / pagesize);
            dsRet.put("totalRows",countForMaps.get("count"));

            dsRet.put("totalCount",countForMaps.get("totalCount"));
            dsRet.put("totalAmount",countForMaps.get("totalAmount"));
            dsRet.put("totalTaxAmount",countForMaps.get("totalTaxAmount"));
            dsRet.put("totalTaxes",countForMaps.get("totalTaxes"));
            dsRet.put("datas",list);

            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/scm/purchaseExpense/detail",method = RequestMethod.POST,apiTitle = "详情—采购费用")
    @ApiOperation("详情—采购费用")
    @Transactional(rollbackFor = Exception.class)
    public R detailOfExpence(@ApiParam(value = "采购费用id:", required = true) @RequestParam(value = "id") Long id){
        return purchaseExpenseService.getDetail(id);
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/purchaseExpenseGetOut", method = RequestMethod.GET, apiTitle = "导出采购费用")
    @ApiOperation("导出采购费用")
    public void exportExcel(
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "开始日期(开票时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "截止日期(开票时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierName", supplierName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<Map<String, Object>> list = purchaseExpenseService.listForMap(params);

        ClassPathResource classPathResource = new ClassPathResource("poi/scm_purchase_expense.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "采购费用");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }





}
