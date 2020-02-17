package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchaseInvoiceDO;
import com.ev.scm.service.PurchaseInvoiceService;
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
 * @Date 2020-1-16 10:19
 *
 **/
@Api(value = "/",tags = "采购发票")
@RestController
public class ScmPurchaseInvoiceController {

    @Autowired
    private PurchaseInvoiceService purchaseInvoiceService;


    @EvApiByToken(value = "/apis/scm/Invoice/addOrChange",method = RequestMethod.POST,apiTitle = "添加/修改—采购发票")
    @ApiOperation("添加/修改—采购发票")
    @Transactional(rollbackFor = Exception.class)
    public R addOrChangeInvoice(PurchaseInvoiceDO purchaseInvoiceDO,
                                @ApiParam(value = "添加采购发票明细:[\n" +
                                          "{\n" +
                                          "\"id\":\"明细表主键（更新时必传）\",\n" +
                                          "\"materielId\":\"商品/产品id\",\n" +
                                          "\"count\":\"数量\",\n" +
                                          "\"unitPrice\":\"不含税单价\",\n" +
                                          "\"taxUnitPrice\":\"含税单价\",\n" +
                                          "\"taxRate\":\"税率\",\n" +
                                          "\"amount\":\"不含税金额\",\n" +
                                          "\"taxes\":\"税额\",\n" +
                                          "\"taxAmount\":\"价格合计\",\n" +
                                          "\"sourceId\":\"原单主键\",\n" +
                                          "\"sourceType\":\"源单类型\",\n" +
                                          "\"sourceCode\":\"来源单号(必填且写准确，不然审核测试会出错)\"\n" +
                                          "}\n" +
                                          "]\n", required = true)
                                  @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
                                @ApiParam(value = "被删除的明细ID") @RequestParam(value = "itemIds", required = false) Long[] itemIds){
        return purchaseInvoiceService.addAndChange(purchaseInvoiceDO, bodyItem, itemIds);
    }

    @EvApiByToken(value = "/apis/scm/Invoice/audit",method = RequestMethod.POST,apiTitle = "审核—采购发票")
    @ApiOperation("审核—采购发票")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "采购发票id:", required = true) @RequestParam(value = "id") Long id){
        return purchaseInvoiceService.audit(id);
    }

    @EvApiByToken(value = "/apis/scm/Invoice/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核—采购发票")
    @ApiOperation("反审核—采购发票")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "采购发票id:", required = true) @RequestParam(value = "id") Long id){
        return purchaseInvoiceService.rollBackAudit(id);
    }

    @EvApiByToken(value = "/apis/scm/Invoice/delet",method = RequestMethod.POST,apiTitle = "删除—采购发票")
    @ApiOperation("删除—采购发票")
    @Transactional(rollbackFor = Exception.class)
    public R removePurchase(@ApiParam(value = "采购发票id:", required = true) @RequestParam(value = "ids") Long[] ids){
        return purchaseInvoiceService.removePurchase(ids);
    }

    @EvApiByToken(value = "/apis/scm/Invoice/listOfInvoice", method = RequestMethod.POST, apiTitle = "列表—采购发票")
    @ApiOperation("列表—采购发票")
    public R purchaseOflist(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
                            @ApiParam(value = "物料名称") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                            @ApiParam(value = "发票号码") @RequestParam(value = "invoiceNum", defaultValue = "", required = false) String invoiceNum,
                            @ApiParam(value = "开始日期(开票时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                            @ApiParam(value = "截止日期(开票时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("supplierName", supplierName);
        params.put("materielName", materielName);
        params.put("invoiceNum", invoiceNum);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<Map<String, Object>> list = purchaseInvoiceService.listForMap(params);
        //数量中添加统计  数量  + 金额
        Map<String, Object> countForMaps = purchaseInvoiceService.countForMap(params);

        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        Map<String,Object> dsRet= new HashMap<>();
        if (!list.isEmpty()) {
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",(Integer.parseInt(countForMaps.get("count").toString()) + pagesize - 1) / pagesize);
            dsRet.put("totalRows", countForMaps.get("count"));
            dsRet.put("totalTaxes",countForMaps.get("totalTaxes"));
            dsRet.put("totalTaxAmount",countForMaps.get("totalTaxAmount"));
            dsRet.put("totalCount",countForMaps.get("totalCount"));
            dsRet.put("totalTaxes",countForMaps.get("totalTaxes"));
            dsRet.put("datas",list);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }


    @EvApiByToken(value = "/apis/scm/Invoice/detailOfInvoice",method = RequestMethod.POST,apiTitle = "详情—采购发票")
    @ApiOperation("详情—采购发票")
    @Transactional(rollbackFor = Exception.class)
    public R detailOfExpence(@ApiParam(value = "采购发票id:", required = true) @RequestParam(value = "id") Long id){
        return purchaseInvoiceService.getDetail(id);
    }



    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/purchaseInvoiceGetOut", method = RequestMethod.GET, apiTitle = "导出采购发票")
    @ApiOperation("导出采购发票")
    public void exportExcel(
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "supplierName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "开始日期(开票时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "截止日期(开票时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("supplierName", supplierName);
        params.put("materielName", materielName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<Map<String, Object>> list = purchaseInvoiceService.listForMap(params);

        ClassPathResource classPathResource = new ClassPathResource("poi/scm_purchase_invoice.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "采购发票");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }














}
