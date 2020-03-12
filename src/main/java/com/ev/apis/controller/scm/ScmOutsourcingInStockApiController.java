package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.MathUtils;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.domain.StockInDO;
import com.ev.scm.service.ProcessingChargeItemService;
import com.ev.scm.service.StockInItemService;
import com.ev.scm.service.StockInService;
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
 * @Date 2020-2-12 15:37
 **/
@Api(value = "/", tags="委外入库")
@RestController
public class ScmOutsourcingInStockApiController {

    @Autowired
    private ProcessingChargeItemService processingChargeItemService;
    @Autowired
    private StockInService stockInService;
    @Autowired
    private StockInItemService stockInItemService;

    @EvApiByToken(value = "/apis/scm/outsourcingInStock/saveAndChange", method = RequestMethod.POST, apiTitle = "新增/修改—委外入库")
    @ApiOperation("新增/修改—委外入库")
    @Transactional(rollbackFor = Exception.class)
    public R getMenus(StockInDO stockInDO,
                      @ApiParam(value = "产品/物料明细行[\n" +
                              "{\"materielId\":2,\n" +
                              "\"id\":\"明细主键（修改时必传）\",\n" +
                              "\"batch\":\"批次\",\n" +
                              "\"count\":数量,\n" +
                              "\"unitPrice\":单价,\n" +
                              "\"amount\":金额,\n" +
                              "\"warehouse\":仓库,\n" +
                              "\"warehLocation\":库位,\n" +
                              "\"cost\":成本,\n" +
                              "\"expense\":费用,\n" +
                              "\"sourceType\":源单类型,\n" +
                              "\"sourceCode\":\"原单编码\"\n" +
                              "\"sourceId\":\"原单id(追溯用，必传)\"\n" +
                              "\"qrcodeId\":\"二维码主键id\"\n" +
                              "}\n" +
                              "]", required = true) @RequestParam(value = "bodyDetail", defaultValue = "") String bodyDetail,
                      @ApiParam(value = "删除的明细id") @RequestParam(value = "itemIds", required = false) Long[] itemIds) {

        R result = stockInService.checkSourceCountsOfOutSourcing(bodyDetail,stockInDO.getId());
        if(Objects.isNull(result)){
            return stockInService.addAndChangeInStockType(stockInDO, ConstantForGYL.OUTSOURCING_INSTOCK,bodyDetail,itemIds);
        }else{
            return result;
        }
    }

    @EvApiByToken(value = "/apis/scm/outsourcingInStock/auditStatusChange", method = RequestMethod.POST, apiTitle = "审核--委外入库")
    @ApiOperation("审核--委外入库")
    @Transactional(rollbackFor = Exception.class)
    public R changeAuditStatus(@ApiParam(value = "委外入库主表主键", required = true) @RequestParam(value = "inHeadId") Long inHeadId) {
        Long auditor= ShiroUtils.getUserId();
        return stockInService.auditAllTypeInStock(inHeadId, auditor ,ConstantForGYL.OUTSOURCING_INSTOCK);
    }

    @EvApiByToken(value = "/apis/scm/outsourcingInStock/reverseAuditChange", method = RequestMethod.POST, apiTitle = "反审核--委外入库")
    @ApiOperation("反审核--委外入库")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(@ApiParam(value = "委外入库主表主键", required = true) @RequestParam(value = "inHeadId", defaultValue = "") Long inHeadId) {

        //TODO    此处设置校验，如果材料费用大于0，则不允许反审核(避免核算出错)
        return stockInService.disAuditInStock(inHeadId, ConstantForGYL.OUTSOURCING_INSTOCK);
    }

    @EvApiByToken(value = "/apis/scm/outsourcingInStock/deletAllStock", method = RequestMethod.POST, apiTitle = "删除--委外入库")
    @ApiOperation("删除--委外入库")
    @Transactional(rollbackFor = Exception.class)
    public R remoceOtherWaitAuite(@ApiParam(value = "委外入库主表主键", required = true) @RequestParam(value = "inHeadIds") Long[] inHeadIds) {

        return  stockInService.deleBatch(inHeadIds);
    }

    @EvApiByToken(value = "/apis/scm/outsourcingInStock/list", method = RequestMethod.POST, apiTitle = "委外入库列表/查询/高级查询")
    @ApiOperation("委外入库列表/查询/高级查询")
    public R otherHeadDetailList(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                                 @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                                 @ApiParam(value = "单据编号") @RequestParam(value = "inheadCode", defaultValue = "", required = false) String inheadCode,
                                 @ApiParam(value = "供应商（模糊）") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
                                 @ApiParam(value = "物料名（模糊）") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                                 @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
                                 @ApiParam(value = "入库起始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                                 @ApiParam(value = "入库截止时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                                 @ApiParam(value = "规格型号") @RequestParam(value = "materielSpecification", defaultValue = "", required = false) String materielSpecification,
                                 @ApiParam(value = "制单人id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
                                 @ApiParam(value = "制单人名字") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
                                 @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String  createTime,
                                 @ApiParam(value = "制单起始日期") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String  createStartTime,
                                 @ApiParam(value = "制单结束日期") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String  createEndTime) {
        Map<String, Object> resulst = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        params.put("inheadCode", inheadCode);
        params.put("sourceCompanyName", clientName);
        params.put("materielName", materielName);
        params.put("auditSign", auditSign);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielSpecification", materielSpecification);
        params.put("createBy", createBy);
        params.put("createByName", createByName);
        params.put("createTime", createTime);
        params.put("storageType", ConstantForGYL.OUTSOURCING_INSTOCK);
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("createStartTime", createStartTime);
        params.put("createEndTime", createEndTime);

        Map<String, Object> totalForMap = stockInService.countForMap(params);
        List<Map<String, Object>> detailList = stockInService.listForMap(params);

        if (!detailList.isEmpty()) {
            Map<String, Object> dsRet = new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",(Integer.parseInt(totalForMap.get("count").toString()) + pagesize - 1) / pagesize);
            dsRet.put("totalRows",Integer.parseInt(totalForMap.get("count").toString()));

            dsRet.put("toatalCount",totalForMap.get("toatalCount"));
            dsRet.put("toatalAmount",totalForMap.get("toatalAmount"));
            dsRet.put("datas",detailList);
            resulst.put("data", dsRet);
        }
        return R.ok(resulst);
    }


    @EvApiByToken(value = "/apis/scm/outsourcingInStock/listOfHead", method = RequestMethod.POST, apiTitle = "入库主信息列表")
    @ApiOperation("入库主信息列表")
    public R otherHeadList(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                                 @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                                 @ApiParam(value = "物料名（模糊）") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                                 @ApiParam(value = "供应商id") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
                                 @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
                                 @ApiParam(value = "入库类型：委外传276；采购传260") @RequestParam(value = "storageType", defaultValue = "", required = false) Long storageType,
                                 @ApiParam(value = "制单起始日期") @RequestParam(value = "createStartTime", defaultValue = "", required = false) String  createStartTime,
                                 @ApiParam(value = "制单结束日期") @RequestParam(value = "createEndTime", defaultValue = "", required = false) String  createEndTime) {
        Map<String, Object> resulst = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("materielName", materielName);
        params.put("auditSign", auditSign);
        params.put("storageType", storageType);
        params.put("createStartTime", createStartTime);
        params.put("createEndTime", createEndTime);
        params.put("supplierId", supplierId);

        int count = stockInService.countForHead(params);
        List<Map<String, Object>> detailList = stockInService.listForHead(params);

        if (!detailList.isEmpty()) {
            Map<String,Object>  maps= new HashMap<>();
            maps.put("sourceType",storageType);

            for(int i=0;i<detailList.size();i++){
                Map<String,Object>mapDo= detailList.get(i);
                maps.put("sourceId",mapDo.get("stockInItemId"));
                //采购退货
                BigDecimal inCountOfCharge= processingChargeItemService.getCountBySource(maps);
                BigDecimal countByOutSource = inCountOfCharge == null ? BigDecimal.ZERO : inCountOfCharge;

                BigDecimal counts = MathUtils.getBigDecimal(mapDo.get("count")).subtract(countByOutSource);

                if (counts.compareTo(BigDecimal.ZERO) <= 0) {
                    mapDo.put("quoteCount",0);
                }else{
                    mapDo.put("quoteCount",count);
                }
            }
            List<Map<String, Object>> quoteList = detailList
                    .stream()
                    .filter(stringObjectMap -> MathUtils.getBigDecimal(stringObjectMap.get("quoteCount")).compareTo(BigDecimal.ZERO)>0)
                    .collect(Collectors.toList());
            List<Map<String, Object>> quoteLists= PageUtils.startPage(quoteList, pageno, pagesize);
            Map<String, Object> dsRet = new HashMap<>();
            dsRet.put("pageno",pageno);
            dsRet.put("pagesize",pagesize);
            dsRet.put("totalPages",(quoteLists.size() + pagesize - 1) / pagesize);
            dsRet.put("totalRows",quoteLists.size());
            dsRet.put("datas",quoteLists);
            resulst.put("data", dsRet);
        }
        return R.ok(resulst);
    }


    @EvApiByToken(value = "/apis/scm/outsourcingInStock/getDtailOfOtherIn", method = RequestMethod.POST, apiTitle = "详情--委外入库")
    @ApiOperation("详情--委外入库")
    public R getDetail(@ApiParam(value = "委外入库主表主键", required = true) @RequestParam(value = "inHeadIds") Long inHeadIds) {

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("id",inHeadIds);
        Map<String, Object> deatilOfhead = stockInService.deatilOfhead(params);
        List<Map<String, Object>> deatilOfBody = stockInItemService.deatilOfBody(params);
        params.clear();
        Map<String,Object>  map= new HashMap<>();
        if(Objects.nonNull(deatilOfhead)){
            map.put("deatilOfhead",deatilOfhead);
            map.put("deatilOfBody",deatilOfBody);
            params.put("data",map);
        }
        return R.ok(params);
    }


    @ResponseBody
    @EvApiByToken(value = "/apis/scm/exportExcel/outsourcingInStockGetOut", method = RequestMethod.GET, apiTitle = "导出委外入库")
    @ApiOperation("导出委外入库")
    public void exportExcel(
            @ApiParam(value = "单据编号") @RequestParam(value = "inheadCode", defaultValue = "", required = false) String inheadCode,
            @ApiParam(value = "客户（模糊）") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
            @ApiParam(value = "物料名（模糊）") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "退货起始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "退货截止时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "规格型号") @RequestParam(value = "materielSpecification", defaultValue = "", required = false) String materielSpecification,
            @ApiParam(value = "制单人id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单人名字") @RequestParam(value = "createByName", defaultValue = "", required = false) Long createByName,
            @ApiParam(value = "制单日期") @RequestParam(value = "createTime", defaultValue = "", required = false) String  createTime,
            HttpServletRequest request,
            HttpServletResponse response,
            ModelMap modelMap) {

        Map<String, Object> params = new HashMap<>();

        params.put("inheadCode", inheadCode);
        params.put("clientName", clientName);
        params.put("materielName", materielName);
        params.put("auditSign", auditSign);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("materielSpecification", materielSpecification);
        params.put("createBy", createBy);
        params.put("createByName", createByName);
        params.put("createTime", createTime);
        params.put("storageType", ConstantForGYL.SALES_RETURN);
        List<Map<String, Object>> list =  stockInService.listForMap(params);

        ClassPathResource classPathResource = new ClassPathResource("poi/scm_outsourcing_in_stock.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "委外入库");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }











}
