package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.scm.domain.StockInDO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Kuzi
 * @Date 2020-1-22 13:50
 *
 **/
@Api(value = "/", tags = "生产退料入库")
@RestController
public class ScmReturnMaterieInStockApiController {

	@Autowired
	private StockInService stockInService;
	@Autowired
	private StockInItemService stockInItemService;


    @EvApiByToken(value = "/apis/scm/returnMaterielnStock/saveAndChange", method = RequestMethod.POST, apiTitle = "新增/修改—生产退料入库")
    @ApiOperation("新增/修改—生产退料入库")
    @Transactional(rollbackFor = Exception.class)
    public R getMenus(StockInDO stockInDO,
                      @ApiParam(value = "产品/物料明细行[{" +
                              "\"id\":\"明细主键（修改时必传）\",\n" +
                              "\"materielId\":1," +
                              "\"batch\":\"A001\"," +
                              "\"count\":11," +
                              "\"unitPrice\":1003000," +
                              "\"amount\":1003000," +
                              "\"warehouse\":1," +
                              "\"warehLocation\":2," +
                              "\"returnReason\":\"不好用\"," +
                              "\"sourceType\":源 单 类 型,\n" +
                              "\"sourceCode\":\"SCLY20190720001\"," +
                              "\"sourceId\":1}"+
                              "\"qrcodeId\":\"二维码主键id\"\n" +
                              "]", required = true) @RequestParam(value = "bodyDetail", defaultValue = "") String bodyDetail,
                      @ApiParam(value = "删除的明细id") @RequestParam(value = "itemIds", required = false) Long[] itemIds) {

        R result = stockInService.checkSourceCountsOfReturnMateriel(bodyDetail,stockInDO.getId());
        if(Objects.isNull(result)){
            return stockInService.addAndChangeInStockType(stockInDO,ConstantForGYL.TLRK,bodyDetail,itemIds);
        }else{
            return result;
        }
    }


    @EvApiByToken(value = "/apis/scm/returnMaterielnStock/auditStatusChange", method = RequestMethod.POST, apiTitle = "审核--生产退料入库")
    @ApiOperation("审核--生产退料入库")
    @Transactional(rollbackFor = Exception.class)
    public R changeAuditStatus(@ApiParam(value = "主表主键", required = true) @RequestParam(value = "inHeadId", defaultValue = "") Long inHeadId) {
        Long auditor= ShiroUtils.getUserId();
        return stockInService.auditAllTypeInStock(inHeadId, auditor,ConstantForGYL.TLRK);
    }

    @EvApiByToken(value = "/apis/scm/returnMaterielnStock/reverseAuditChange", method = RequestMethod.POST, apiTitle = "反审核--生产退料入库")
    @ApiOperation("反审核--生产退料入库")
    @Transactional(rollbackFor = Exception.class)
    public R reverseAudit(@ApiParam(value = "主表主键", required = true) @RequestParam(value = "inHeadId", defaultValue = "") Long inHeadId) {

        return stockInService.disAuditInStock(inHeadId, ConstantForGYL.TLRK);
    }

    @EvApiByToken(value = "/apis/scm/returnMaterielnStock/deletAllStock", method = RequestMethod.POST, apiTitle = "删除--生产退料入库")
    @ApiOperation("删除--生产退料入库")
    @Transactional(rollbackFor = Exception.class)
    public R remoceOtherWaitAuite(@ApiParam(value = "主表主键", required = true) @RequestParam(value = "inHeadIds", defaultValue = "") Long[] inHeadIds) {

        return  stockInService.deleBatch(inHeadIds);
    }


    @EvApiByToken(value = "/apis/scm/returnMaterielnStock/list", method = RequestMethod.POST, apiTitle = "生产退料入库/查询/高级查询")
    @ApiOperation("生产退料入库/查询/高级查询")
    public R returnHeadDetailList(@ApiParam(value = "当前第几页") @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
                                 @ApiParam(value = "一页多少条") @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
                                  @ApiParam(value = "单据编号") @RequestParam(value = "inheadCode", defaultValue = "", required = false) String inheadCode,
                                  @ApiParam(value = "部门名字") @RequestParam(value = "deptName", defaultValue = "", required = false) String deptName,
                                  @ApiParam(value = "物料名（模糊）") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
                                  @ApiParam(value = "退料起始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
                                  @ApiParam(value = "退料截止时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
                                  @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
                                  @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
                                  @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
                                  @ApiParam(value = "退料操作员id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
                                  @ApiParam(value = "退料操作员名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
                                  @ApiParam(value = "制单人id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
                                 @ApiParam(value = "制单人名字") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
                                 @ApiParam(value = "制单时间") @RequestParam(value = "createTime", defaultValue = "", required = false) String  createTime  ) {
        Map<String, Object> resulst = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("offset", (pageno - 1) * pagesize);
        params.put("limit", pagesize);
        params.put("inheadCode", inheadCode);
        params.put("materielName",materielName );
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("deptName",deptName );
        params.put("materielSpecification",specification );
        params.put("batch", batch);
        params.put("auditSign",auditSign);
        params.put("operator", operator);
        params.put("operatorName",operatorName);
        params.put("createBy",createBy );
//        params.put("createByName", createByName);
        params.put("createTime", createTime);
        params.put("storageType", ConstantForGYL.TLRK);

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


    @EvApiByToken(value = "/apis/scm/returnMaterielnStock/getDtailOfOtherIn", method = RequestMethod.POST, apiTitle = "详情--生产退料入库")
    @ApiOperation("详情--生产退料入库")
    public R getDetail(@ApiParam(value = "主表主键", required = true) @RequestParam(value = "inHeadIds") Long inHeadIds) {

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
    @EvApiByToken(value = "/apis/scm/exportExcel/returnMaterielnStockGetOut", method = RequestMethod.GET, apiTitle = "导出生产退料入库")
    @ApiOperation("导出生产退料入库")
    public void exportExcel(
            @ApiParam(value = "单据编号") @RequestParam(value = "inheadCode", defaultValue = "", required = false) String inheadCode,
            @ApiParam(value = "部门名字") @RequestParam(value = "deptName", defaultValue = "", required = false) String deptName,
            @ApiParam(value = "物料名（模糊）") @RequestParam(value = "materielName", defaultValue = "", required = false) String materielName,
            @ApiParam(value = "退料起始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "退料截止时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification", defaultValue = "", required = false) String specification,
            @ApiParam(value = "批次") @RequestParam(value = "batch", defaultValue = "", required = false) String batch,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign", defaultValue = "", required = false) Long auditSign,
            @ApiParam(value = "退料操作员id") @RequestParam(value = "operator", defaultValue = "", required = false) Long operator,
            @ApiParam(value = "退料操作员名字") @RequestParam(value = "operatorName", defaultValue = "", required = false) String operatorName,
            @ApiParam(value = "制单人id") @RequestParam(value = "createBy", defaultValue = "", required = false) Long createBy,
            @ApiParam(value = "制单人名字") @RequestParam(value = "createByName", defaultValue = "", required = false) String createByName,
            @ApiParam(value = "制单时间") @RequestParam(value = "createTime", defaultValue = "", required = false) String  createTime,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {

        Map<String, Object> params = new HashMap<>();
        params.put("inheadCode", inheadCode);
        params.put("materielName",materielName );
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("deptName",deptName );
        params.put("materielSpecification",specification );
        params.put("batch", batch);
        params.put("auditSign",auditSign);
        params.put("operator", operator);
        params.put("operatorName",operatorName);
        params.put("createBy",createBy );
//        params.put("createByName", createByName);
        params.put("createTime", createTime);
        params.put("storageType", ConstantForGYL.TLRK);

        List<Map<String, Object>> list = stockInService.listForMap(params);
        ClassPathResource classPathResource = new ClassPathResource("poi/scm_return_materie_in_stock.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", list);
        TemplateExportParams result = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "生产退料入库");
        modelMap.put(TemplateExcelConstants.PARAMS, result);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}