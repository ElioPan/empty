package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.SalesbillDO;
import com.ev.scm.service.SalesbillService;
import com.ev.scm.service.SalescontractService;
import com.ev.scm.service.StockOutService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 销售票据控制器层
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "销售票据API")
public class SalesBillApiController {

	@Autowired
	private SalesbillService salesbillService;
    @Autowired
    private SalescontractService salescontractService;
    @Autowired
    private StockOutService stockOutService;
	
	@EvApiByToken(value = "/apis/salesBill/addOrUpdate",method = RequestMethod.POST,apiTitle = "添加销售票据")
    @ApiOperation("添加/修改销售票据（修改传入id）")
	@Transactional(rollbackFor = Exception.class)
	public R addOrUpdateSalesBill(SalesbillDO salesBillDO,
			@ApiParam(value = "添加销售票据明细:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"materielId\":50,\n" +
                    "        \"count\":5,\n" +
                    "        \"unitPrice\":300,\n" +
                    "        \"taxUnitPrice\":1500,\n" +
                    "        \"taxRate\":90,\n" +
                    "        \"amount\":2000,\n" +
                    "        \"taxes\":2000,\n" +
                    "        \"taxAmount\":1300,\n" +
                    "        \"sourceType\":130,\n" +
                    "        \"sourceCode\":\"销售合同||销售出库单号\",\n" +
                    "        \"sourceId\":1300\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 2,\n" +
                    "        \"materielId\":50,\n" +
                    "        \"count\":5,\n" +
                    "        \"unitPrice\":300,\n" +
                    "        \"taxUnitPrice\":1500,\n" +
                    "        \"taxRate\":90,\n" +
                    "        \"amount\":2000,\n" +
                    "        \"taxes\":2000,\n" +
                    "        \"taxAmount\":1300,\n" +
                    "        \"sourceType\":130,\n" +
                    "        \"sourceCode\":\"销售合同||销售出库单号\",\n" +
                    "        \"sourceId\":1300\n" +
                    "    }\n" +
                    "]"
                    , required = true)
            @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
            @ApiParam(value = "被删除的销售合同明细ID") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds){
		return salesbillService.addOrUpdateSalesBill(salesBillDO, bodyItem, itemIds);
	}
	
	@EvApiByToken(value = "/apis/salesBill/batchRemove",method = RequestMethod.POST,apiTitle = "删除销售票据")
    @ApiOperation("删除销售票据")
	@Transactional(rollbackFor = Exception.class)
	public R removePurchaseBill(
			@ApiParam(value = "根据id删除销售票据",required = true) @RequestParam(value = "ids",defaultValue = "") Long[] ids) {
		return salesbillService.batchRemoveSalesBill(ids);
	}
	
	@EvApiByToken(value = "/apis/salesBill/list",method = RequestMethod.GET,apiTitle = "获取销售票据列表/高级搜索")
    @ApiOperation("获取销售票据列表/高级搜索")
    public R list(
            @ApiParam(value = "票据编号") @RequestParam(value = "billCode",required = false) String billCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName",defaultValue = "",required = false)  String clientName,
            @ApiParam(value = "产品名称") @RequestParam(value = "materielName",required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            /*高级查询*/
//            @ApiParam(value = "票据类型") @RequestParam(value = "billType",required = false) Long billType,
//			@ApiParam(value = "规格型号") @RequestParam(value = "model",required = false) String model,
//			@ApiParam(value = "审核状态") @RequestParam(value = "auditStatus",required = false) Long auditStatus,
//			@ApiParam(value = "制单人") @RequestParam(value = "createBy",required = false) Long createBy,
//			@ApiParam(value = "制单日期") @RequestParam(value = "createTime",required = false) Date createTime,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = Maps.newHashMap();
        map.put("billCode",billCode);
        map.put("clientName",StringUtils.sqlLike(clientName));
        map.put("materielName",StringUtils.sqlLike(materielName));
        map.put("startTime", startTime);
        map.put("endTime",endTime);

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        List<Map<String, Object>> data = salesbillService.listForMap(map);
        int total = salesbillService.countForMap(map);
        Map<String, Object> results = Maps.newHashMap();
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(results);
	}
	
	@EvApiByToken(value = "/apis/salesBill/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "销售票据Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return salesbillService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/salesBill/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "销售票据Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return salesbillService.reverseAudit(id);
	}
	
	@EvApiByToken(value = "/apis/salesBill/detail",method = RequestMethod.GET,apiTitle = "获取销售票据详细信息")
	@ApiOperation("获取销售票据详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
	    return  salesbillService.getDetail(id);
    }

    @EvApiByToken(value = "/apis/salesBill/importList",method = RequestMethod.GET,apiTitle = "销售发票的导入关联单据查询")
    @ApiOperation("销售发票的导入关联单据查询")
    public R importList(
            @ApiParam(value = "源单类型") @RequestParam(value = "sourceType",required = false) Long sourceType,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "产品编号/产品名称/合同编号/客户名称 模糊查询") @RequestParam(value = "fuzzyInquire",required = false) String fuzzyInquire,
            @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String,Object> map = Maps.newHashMap();

        map.put("fuzzyInquire", fuzzyInquire);
        map.put("startTime", startTime);
        map.put("endTime",endTime);

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        if (Objects.equals(sourceType,ConstantForGYL.XSCK)) {
            // 销售出库
            // 出库类型
            map.put("outboundType", ConstantForGYL.XSCK);
            Map<String, Object> results = Maps.newHashMap();
            List<Map<String, Object>> data = this.stockOutService.listApi(map);
            for (Map<String, Object> datum : data) {
                datum.put("thisSourceType",ConstantForGYL.XSCK);
                datum.put("thisSourceTypeName","销售出库");
            }
            int total = this.stockOutService.countApi(map);
            if (data.size() > 0) {
                results.put("data", new DsResultResponse(pageno,pagesize,total,data));
            }
            return R.ok(results);
        }
        // 销售合同
        List<Map<String, Object>> data = salescontractService.listForMap(map);
        for (Map<String, Object> datum : data) {
            datum.put("thisSourceType",ConstantForGYL.XSHT);
            datum.put("thisSourceTypeName","销售合同");
        }
        Map<String, Object> stringBigDecimalMap = salescontractService.countForMap(map);
        int total = Integer.parseInt(stringBigDecimalMap.getOrDefault("total",0).toString());
        Map<String, Object> result = Maps.newHashMap();
        if (data.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
            result.put("total", stringBigDecimalMap);
        }
        return R.ok(result);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/salesBill/exportExcel", method = RequestMethod.GET, apiTitle = "导出销售票据")
    @ApiOperation("导出销售票据")
    public void exportExcel(
            @ApiParam(value = "票据编号") @RequestParam(value = "billCode",required = false) String billCode,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName",defaultValue = "",required = false)  String clientName,
            @ApiParam(value = "产品名称") @RequestParam(value = "materielName",required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,

            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("billCode",billCode);
        param.put("clientName",StringUtils.sqlLike(clientName));
        param.put("materielName",StringUtils.sqlLike(materielName));
        param.put("startTime", startTime);
        param.put("endTime",endTime);

        List<Map<String, Object>> data = salesbillService.listForMap(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/sales_bill.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售票据");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }
}
