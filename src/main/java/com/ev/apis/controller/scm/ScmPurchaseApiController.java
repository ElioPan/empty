package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.scm.domain.PurchaseDO;
import com.ev.scm.service.PurchaseService;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author Kuzi
 * @Date 2020-1-9 14:51
 **/
@RestController
@Api(value = "/",tags = "采购申请")
public class ScmPurchaseApiController {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private DictionaryService dictionaryService;


    @EvApiByToken(value = "/apis/scm/purchase/addAndChange", method = RequestMethod.POST, apiTitle = "保存/修改 —采购申请单")
    @ApiOperation("保存/修改 —采购申请单")
    @Transactional(rollbackFor = Exception.class)
    public R addPurchaseProduct(PurchaseDO purchaseDO,
                                @ApiParam(value = "添加采购申请明细:[\n" +
                                        "{\n" +
                                        "\"id\":\"名细行主键(修改时必传)\",\n" +
                                        "\"materielId\":\"物料\",\n" +
                                        "\"count\":\"请购数量\",\n" +
                                        "\"unitPrice\":\"采购单价\",\n" +
                                        "\"amount\":\"采购金额\",\n" +
                                        "\"sourceType\":\"源单类型“,\n" +
                                        "\"sourceCode\":\"来源单号\",\n" +
                                        "\"sourceId\":\"来源单明细主键\"\n" +
                                        "}\n" +
                                        "]", required = true) @RequestParam(value = "item", defaultValue = "") String item,
                                @ApiParam(value = "删除的明细行id:") @RequestParam(value = "itemIds", required = false) Long[] deleItemIds) {

        R resutlt = purchaseService.checkSourceCounts(item, purchaseDO.getId());
        if(Objects.isNull(resutlt)){
            return purchaseService.addPurchase(purchaseDO, item, deleItemIds);
        }else{
            return resutlt;
        }
    }


    @EvApiByToken(value = "/apis/scm/purchase/detailOfPurchase", method = RequestMethod.POST, apiTitle = "详情—采购申请单")
    @ApiOperation("详情—采购申请单")
    public R detail(@ApiParam(value = "采购申请单id:", required = true) @RequestParam(value = "id") Long id) {
        return purchaseService.getdetail(id);
    }

    @EvApiByToken(value = "/apis/scm/purchase/audit", method = RequestMethod.POST, apiTitle = "审核—采购申请单")
    @ApiOperation("审核—采购申请单")
    @Transactional(rollbackFor = Exception.class)
    public R auditPurchase(@ApiParam(value = "采购申请单id:", required = true) @RequestParam(value = "id") Long id) {
        return purchaseService.audit(id);
    }


    @EvApiByToken(value = "/apis/scm/purchase/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核—采购申请单")
    @ApiOperation("反审核—采购申请单")
    @Transactional(rollbackFor = Exception.class)
    public R disAudit(@ApiParam(value = "采购申请单id:", required = true) @RequestParam(value = "id") Long id) {
        return purchaseService.rollBackAudit(id);
    }


    @EvApiByToken(value = "/apis/scm/purchase/delet", method = RequestMethod.POST, apiTitle = "删除—采购申请单")
    @ApiOperation("删除—采购申请单")
    @Transactional(rollbackFor = Exception.class)
    public R removePurchase(@ApiParam(value = "采购申请单id:", required = true) @RequestParam(value = "ids") Long[] ids) {
        return purchaseService.removePurchase(ids);
    }

    @EvApiByToken(value = "/apis/scm/purchase/listOfPurchase", method = RequestMethod.POST, apiTitle = "列表—采购申请单")
    @ApiOperation("列表—采购申请单")
    public R purchaseOflist(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "物料名称，规格型号,供应商名字") @RequestParam(value = "general", defaultValue = "", required = false) String general,
                            @ApiParam(value = "单据编号") @RequestParam(value = "purchaseCode", defaultValue = "", required = false) String purchaseCode,
                            @ApiParam(value = "开始日期(申请时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                            @ApiParam(value = "截止日期(申请时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
                            @ApiParam(value = "物料名称") @RequestParam(value = "materiel", defaultValue = "", required = false) Long materiel,
                            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
                            @ApiParam(value = "申请部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
                            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
                            @ApiParam(value = "申请人") @RequestParam(value = "applicant", defaultValue = "", required = false) Long applicant,
                            @ApiParam(value = "申请人名字") @RequestParam(value = "applicantName", defaultValue = "", required = false) String applicantName,
                            @ApiParam(value = "制单人") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
                            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime,
                            @ApiParam(value = "制单起始日期") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String  createStartTime,
                            @ApiParam(value = "制单结束日期") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String  createEndTime) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("general", general);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("purchaseCode", purchaseCode);
        params.put("supplierId", supplierId);
        params.put("materiel", materiel);
        params.put("specification", specification);
        params.put("deptId", deptId);
        params.put("auditSign", auditSign);
        params.put("applicant", applicant);
        params.put("createBy", createBy);
        params.put("createTime", createTime);
        params.put("applicantName", applicantName);
        params.put("createStartTime", createStartTime);
        params.put("createEndTime", createEndTime);

        List<Map<String, Object>> list = purchaseService.listForMap(params);
        Map<String, Object> countForMaps = purchaseService.countForMap(params);

        DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.PURCHASE.intValue());
        String thisSourceTypeName = dictionaryDO.getName();
        for (Map<String, Object> datum : list) {
            datum.put("thisSourceType", ConstantForGYL.PURCHASE);
            datum.put("thisSourceTypeName", thisSourceTypeName);
        }
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);
        if (!list.isEmpty()) {
            Map<String,Object> dsRet= new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",countForMaps.get("count"));
            dsRet.put("totalPages",(Integer.parseInt(countForMaps.get("count").toString()) + pagesize - 1) / pagesize);
            dsRet.put("totalCount",countForMaps.get("totalCount"));
            dsRet.put("totalAmount",countForMaps.get("totalAmount"));
            dsRet.put("datas",list);
            results.put("data", dsRet);
        }
        return R.ok(results);

    }

    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/purchaseGetOut", method = RequestMethod.GET, apiTitle = "导出采购申请")
    @ApiOperation("导出采购申请")
    public void exportExcel(
            @ApiParam(value = "单据编号，物料名称，规格型号") @RequestParam(value = "general", defaultValue = "", required = false) String general,
            @ApiParam(value = "开始日期(申请时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "截止日期(申请时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "物料名称") @RequestParam(value = "materiel", defaultValue = "", required = false) Long materiel,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "申请部门") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "申请人") @RequestParam(value = "applicant", defaultValue = "", required = false) Long applicant,
            @ApiParam(value = "申请人名字") @RequestParam(value = "applicantName", defaultValue = "", required = false) String applicantName,
            @ApiParam(value = "制单人") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String createTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {


        Map<String, Object> params = Maps.newHashMap();
        params.put("general", general);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("supplierId", supplierId);
        params.put("materiel", materiel);
        params.put("specification", specification);
        params.put("deptId", deptId);
        params.put("auditSign", auditSign);
        params.put("applicant", applicant);
        params.put("createBy", createBy);
        params.put("createTime", createTime);
        params.put("applicantName", applicantName);

        List<Map<String, Object>> list = purchaseService.listForMap(params);
        ClassPathResource classPathResource = new ClassPathResource("poi/scm_purchase.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "采购申请单");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }


    @EvApiByToken(value = "/apis/scm/purchase/PurchaseIntroduce", method = RequestMethod.POST, apiTitle = "导入列表—采购申请单")
    @ApiOperation("导入列表—采购申请单")
    public R purchaseOflistForIntroduce(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                            @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                            @ApiParam(value = "开始日期(申请时间)") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                            @ApiParam(value = "截止日期(申请时间)") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                            @ApiParam(value = "供应商") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
                            @ApiParam(value = "供应商名字模糊") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
                            @ApiParam(value = "制单起始日期") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String  createStartTime,
                            @ApiParam(value = "制单结束日期") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String  createEndTime
//                            @ApiParam(value = "采购合同主键id") @RequestParam(value = "purchaseContractId",required = false) Long purchaseContractId
    ) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("supplierId", supplierId);
        params.put("general", supplierName);
        params.put("createStartTime", createStartTime);
        params.put("createEndTime", createEndTime);
        params.put("auditSign", ConstantForGYL.OK_AUDITED);

        List<Map<String, Object>> list = purchaseService.listForMap(params);
        Map<String, Object> countForMaps = purchaseService.countForMap(params);

        DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.PURCHASE.intValue());
        String thisSourceTypeName = dictionaryDO.getName();
//        for (Map<String, Object> datum : list) {
//            datum.put("thisSourceType", ConstantForGYL.PURCHASE);
//            datum.put("thisSourceTypeName", thisSourceTypeName);
//        }
        Map<String, Object> results = Maps.newHashMapWithExpectedSize(2);

        if (!list.isEmpty()) {

            Map<String,Object>  map= new HashMap<>();
            map.put("sourceType",ConstantForGYL.PURCHASE);
//            map.put("contractId",purchaseContractId);
            for(int i=0;i<list.size();i++){
                Map<String, Object> mapDo =list.get(i);
                map.put("purchaseItemId",mapDo.get("purchaseItemId"));

                Map<String, Object> stringObjectMap = purchaseService.vailableQuantity(map);
                BigDecimal count=new BigDecimal(mapDo.get("count").toString());
                if(stringObjectMap!=null){
                    BigDecimal countOfVail=new BigDecimal(stringObjectMap.containsKey("count")?stringObjectMap.get("count").toString():"0");
                    mapDo.put("quoteCount",countOfVail);
                }else{
                    mapDo.put("quoteCount",count);
                }
                mapDo.put("thisSourceType", ConstantForGYL.PURCHASE);
                mapDo.put("thisSourceTypeName", thisSourceTypeName);
            }
            List<Map<String, Object>> quoteList = list
                    .stream()
                    .filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("quoteCount")).compareTo(BigDecimal.ZERO)>0)
                    .collect(Collectors.toList());

            List<Map<String, Object>> quoteLists = PageUtils.startPage(quoteList, pageno, pagesize);
            Map<String,Object> dsRet= new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalRows",quoteLists.size());
            dsRet.put("totalPages",(quoteLists.size() + pagesize - 1) / pagesize);
            dsRet.put("totalCount",countForMaps.get("totalCount"));
            dsRet.put("totalAmount",countForMaps.get("totalAmount"));
            dsRet.put("datas",quoteLists);
            results.put("data", dsRet);
        }
        return R.ok(results);
    }



}
