package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.PurchasecontractDO;
import com.ev.scm.service.PurchasecontractPayService;
import com.ev.scm.service.PurchasecontractService;
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
import java.util.Objects;

/**
 * @Author Kuzi
 * @Date 2020-1-14 15:05
 *
 **/
@Api(value = "/",tags="采购合同")
@RestController
public class ScmPurchaseContractApiController {

    @Autowired
    private PurchasecontractService purchasecontractService;
    @Autowired
    private PurchasecontractPayService purchasecontractPayService;
    @Autowired
    private DictionaryService dictionaryService;

    @EvApiByToken(value = "/apis/scm/purchaseContract/addOrUpdate",method = RequestMethod.POST,apiTitle = "添加/修改—采购合同")
    @ApiOperation("添加/修改—采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R addOrUpdate(PurchasecontractDO purchasecontractDO,

                         @ApiParam(value = "添加/修改采购合同明细:[\n" +
                                 "    {\n" +
                                 "        \"id\":\"明细表主键（更新时必传）\",\n" +
                                 "        \"materielId\":\"商品/产品id\",\n" +
                                 "        \"isCheck\":\"是否检验（1是0否）\",\n" +
                                 "        \"isOvercharge\":\"是否允许超收（1是0否）\",\n" +
                                 "        \"proportion\":\"超收比例\",\n" +
                                 "        \"count\":\"采购数量\",\n" +
                                 "        \"taxAmount\":\"含税金额\",\n" +
                                 "        \"unitPrice\":\"不含税单价\",\n" +
                                 "        \"taxUnitPrice\":\"含税单价\",\n" +
                                 "        \"taxRate\":\"税率\",\n" +
                                 "        \"amount\":\"不含税金额\",\n" +
                                 "        \"taxes\":\"税额\",\n" +
                                 "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"sourceId\":\"原单主键\",\n" +
                                 "        \"sourceType\":\"源单类型\",\n" +
                                 "        \"sourceCode\":\"来源单号\"\n" +
                                 "    }\n" +
                                 "]",
                                 required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
                         @ApiParam(value = "添加/修改采购合同付款条件:[\n" +
                                 "{\n" +
                                 "\"id\":\"付款条件主键（更新时必传）\",\n" +
                                 "\"dueDate\":\"应付日期\",\n" +
                                 "\"payAmount\":\"应付金额\",\n" +
                                 "\"amountPaid\":\"已付金额\",\n" +
                                 "\"unpayAmount\":\"未付金额\",\n" +
                                 "\"remarks\":\"这是备注\"\n" +
                                 "}\n" +
                                 "]",
                                 required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
                         @ApiParam(value = "删除的合同明细ID") @RequestParam(value = "itemIds", required = false) Long[] itemIds,
                         @ApiParam(value = "删除的合同付款条件ID") @RequestParam(value = "payIds", required = false) Long[] payIds){

        String result = purchasecontractService.checkSourceCounts(bodyItem);
        if(Objects.equals("ok",result)){
            return purchasecontractService.addOrChangePurchasecontract(purchasecontractDO, bodyItem, bodyPay,itemIds,payIds);
        }else{
            return R.error(result);
        }

    }


    @EvApiByToken(value = "/apis/scm/purchaseContract/detail",method = RequestMethod.GET,apiTitle = "详情——采购合同")
    @ApiOperation("详情——采购合同")
    public R getDetail(@ApiParam(value = "合同主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return  purchasecontractService.getDetailOfContract(id);
    }


    @EvApiByToken(value = "/apis/scm/purchaseContract/contractList",method = RequestMethod.GET,apiTitle = "列表—采购合同")
    @ApiOperation("列表—采购合同")
    public R contractOfList(
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "供应商名称/物料名称 模糊查询") @RequestParam(value = "fuzzyQuery",required = false) String fuzzyQuery,
            @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
            @ApiParam(value = "制单人") @RequestParam(value = "createBy",defaultValue = "",required = false)  Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime",defaultValue = "",required = false)  String createTime,
            @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
            @ApiParam(value = "制单起始日期") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String  createStartTime,
            @ApiParam(value = "制单结束日期") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String  createEndTime){

        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("fuzzyQuery", StringUtils.sqlLike(fuzzyQuery));
        map.put("contractType", contractType);
        // 高级查询
        map.put("supplierName", supplierName);
        map.put("materielName", StringUtils.sqlLike(materielName));
        map.put("specification",specification);
        map.put("auditSign", auditSign);
        map.put("createBy", createBy);
        map.put("createTime", createTime);
        map.put("closeStatus",closeStatus);
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);

        List<Map<String, Object>> data = purchasecontractService.listForMap(map);
        Map<String, Object> totalMap = purchasecontractService.countForMap(map);
        DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.CGHT.intValue());
        String thisSourceTypeName = dictionaryDO.getName();
        for (Map<String, Object> datum : data) {
            datum.put("thisSourceType", ConstantForGYL.CGHT);
            datum.put("thisSourceTypeName", thisSourceTypeName);
        }
        Map<String, Object> result = Maps.newHashMap();
        if (data.size() > 0) {
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("datas",data);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",Integer.parseInt(totalMap.get("count").toString()));
            dsRet.put("totalPages",((Integer.parseInt(totalMap.get("count").toString()) + pagesize - 1) / pagesize));
            dsRet.put("totalAmount",totalMap.get("totalAmount"));
            dsRet.put("totalTaxAmount",totalMap.get("totalTaxAmount"));
            dsRet.put("totalTaxRate",totalMap.get("totalTaxRate"));
            result.put("data", dsRet);
    }
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/scm/purchaseContract/change",method = RequestMethod.POST,apiTitle = "变更—采购合同")
    @ApiOperation("变更—采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R editSalesContract(
            @ApiParam(value = "采购合同id",required = true) @RequestParam(value = "id",defaultValue = "") Long id,
            @ApiParam(value = "采购合同明细:详情回传过去的JSONArray",required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
            @ApiParam(value = "添加/修改采购合同收款条件:[\n" +
                    "{\n" +
                    "\"id\":\"付款条件主键（更新时必传）\",\n" +
                    "\"dueDate\":\"应付日期\",\n" +
                    "\"payAmount\":\"应付金额\",\n" +
                    "\"amountPaid\":\"已付金额\",\n" +
                    "\"unpayAmount\":\"未付金额\",\n" +
                    "\"remarks\":\"这是备注\"\n" +
                    "}\n" +
                    "]", required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
            @ApiParam(value = "被删除的付款条件ID") @RequestParam(value = "payIds", required = false) Long[] payIds
    ){
        return purchasecontractService.editPurchaseContract(id, bodyItem, bodyPay,payIds);
    }


    @EvApiByToken(value = "/apis/scm/purchaseContract/alterationList",method = RequestMethod.GET,apiTitle = "变更记录—采购合同")
    @ApiOperation("变更记录—采购合同")
    public R alterationList(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "变更人") @RequestParam(value = "createByName",defaultValue = "",required = false)  String createByName,

            @ApiParam(value = "合同Id") @RequestParam(value = "contractId",required = false) Long contractId,
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("createByName", StringUtils.sqlLike(createByName));
        map.put("pageno",pageno);
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        map.put("contractId", contractId);
        map.put("contractType", ConstantForGYL.CGHT);
        return purchasecontractService.alterationList(map);
    }

    @EvApiByToken(value = "/apis/scm/purchaseContract/alterationDetail",method = RequestMethod.GET,apiTitle = "变更详情—采购合同")
    @ApiOperation("变更详情—采购合同")
    public R alterationDetail(@ApiParam(value = "变更记录主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return  purchasecontractService.getAlterationDetail(id);
    }


    @EvApiByToken(value = "/apis/scm/purchaseContract/audit",method = RequestMethod.POST,apiTitle = "审核——采购合同")
    @ApiOperation("审核——采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R audit(@ApiParam(value = "采购合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return purchasecontractService.audit(id);
    }

    @EvApiByToken(value = "/apis/scm/purchaseContract/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核——采购合同")
    @ApiOperation("反审核——采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(@ApiParam(value = "采购合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return purchasecontractService.disAudit(id);
    }

    @EvApiByToken(value = "/apis/scm/purchaseContract/closeOfConcatract",method = RequestMethod.POST,apiTitle = "关闭——采购合同")
    @ApiOperation("关闭——采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R close(@ApiParam(value = "采购合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return purchasecontractService.close(id);
    }

    @EvApiByToken(value = "/apis/scm/purchaseContract/disCloseOfConcatract",method = RequestMethod.POST,apiTitle = "反关闭——采购合同")
    @ApiOperation("反关闭——采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R reverseClose(@ApiParam(value = "采购合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return purchasecontractService.disClose(id);
    }

    @EvApiByToken(value = "/apis/scm/purchaseContract/delet",method = RequestMethod.POST,apiTitle = "删除——采购合同")
    @ApiOperation("删除——采购合同")
    @Transactional(rollbackFor = Exception.class)
    public R removeContracts(@ApiParam(value = "销售合同id数组",required = true) @RequestParam(value = "ids") Long[] ids) {
        return purchasecontractService.removeContract(ids);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/purchaseContractGetOut", method = RequestMethod.GET, apiTitle = "导出采购合同")
    @ApiOperation("导出采购合同")
    public void exportExcel(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "供应商名称/物料名称 模糊查询") @RequestParam(value = "fuzzyQuery",required = false) String fuzzyQuery,
            @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
            @ApiParam(value = "制单人") @RequestParam(value = "createBy",defaultValue = "",required = false)  Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime",defaultValue = "",required = false)  String createTime,
            @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("fuzzyQuery", StringUtils.sqlLike(fuzzyQuery));
        map.put("contractType", contractType);
        // 高级查询
        map.put("supplierName", StringUtils.sqlLike(supplierName));
        map.put("materielName", StringUtils.sqlLike(materielName));
        map.put("specification", specification);
        map.put("auditSign", auditSign);
        map.put("createBy", createBy);
        map.put("createTime", createTime);
        map.put("closeStatus", closeStatus);

        List<Map<String, Object>> data = purchasecontractService.listForMap(map);

        ClassPathResource classPathResource = new ClassPathResource("poi/scm_purchase_contract.xlsx");
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("list", data);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "采购合同");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, maps);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }



    @EvApiByToken(value = "/apis/scm/purchaseContractPay/contractPayList",method = RequestMethod.POST,apiTitle = "付款列表—采购合同付款")
    @ApiOperation("付款列表—采购合同付款")
    public R contractOfList(
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
            @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
            @ApiParam(value = "制单起始日期") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String  createStartTime,
            @ApiParam(value = "制单结束日期") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String  createEndTime){

        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        map.put("supplierName", supplierName);
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);
        map.put("closeStatus", closeStatus);
        map.put("auditSign",auditSign);

        List<Map<String, Object>> data = purchasecontractPayService.listOfPay(map);
        Map<String, Object> totalMap= purchasecontractPayService.countListOfPay(map);

        DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.CGHT.intValue());
        String thisSourceTypeName = dictionaryDO.getName();
        for (Map<String, Object> datum : data) {
            datum.put("thisSourceType", ConstantForGYL.CGHT);
            datum.put("thisSourceTypeName", thisSourceTypeName);
        }
        Map<String, Object> result = Maps.newHashMap();
        if (data.size() > 0) {
            Map<String,Object>  dsRet= new HashMap<>();
            dsRet.put("datas",data);
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",Integer.parseInt(totalMap.get("count").toString()));
            dsRet.put("totalPages",((Integer.parseInt(totalMap.get("count").toString()) + pagesize - 1) / pagesize));
            dsRet.put("totalPayAmount",totalMap.get("totalPayAmount"));
            dsRet.put("totalAmountPaid",totalMap.get("totalAmountPaid"));
            dsRet.put("totalUnpayAmount",totalMap.get("totalUnpayAmount"));
            result.put("data", dsRet);
        }
        return R.ok(result);
    }

}
