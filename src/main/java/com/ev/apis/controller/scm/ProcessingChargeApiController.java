package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.ProcessingChargeDO;
import com.ev.scm.service.ProcessingChargeService;
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

/**
 * 加工费用控制器层
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "加工费用API")
public class ProcessingChargeApiController {

	@Autowired
	private ProcessingChargeService processingChargeService;

	@EvApiByToken(value = "/apis/processingCharge/addOrUpdate",method = RequestMethod.POST,apiTitle = "添加加工费用")
    @ApiOperation("添加/修改加工费用（修改传入id）")
	@Transactional(rollbackFor = Exception.class)
	public R addOrUpdateProcessingCharge(ProcessingChargeDO processingChargeDO,
			@ApiParam(value = "添加加工费用明细:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":1,\n" +
                    "        \"materielId\":50,\n" +
                    "        \"count\":5,\n" +
                    "        \"unitPrice\":300,\n" +
                    "        \"taxUnitPrice\":1500,\n" +
                    "        \"taxRate\":90,\n" +
                    "        \"amount\":2000,\n" +
                    "        \"taxes\":2000,\n" +
                    "        \"taxAmount\":1300,\n" +
                    "        \"sourceType\":187,\n" +
                    "        \"sourceCode\":\"XSTH202002110003\",\n" +
                    "        \"sourceId\":77\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"materielId\":50,\n" +
                    "        \"count\":5,\n" +
                    "        \"unitPrice\":300,\n" +
                    "        \"taxUnitPrice\":1500,\n" +
                    "        \"taxRate\":90,\n" +
                    "        \"amount\":2000,\n" +
                    "        \"taxes\":2000,\n" +
                    "        \"taxAmount\":1300,\n" +
                    "        \"sourceType\":187,\n" +
                    "        \"sourceCode\":\"XSTH202002110003\",\n" +
                    "        \"sourceId\":77\n" +
                    "    }\n" +
                    "]"
                    , required = true)
            @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
            @ApiParam(value = "被删除的销售合同明细ID") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds){
		return processingChargeService.addOrUpdateProcessingCharge(processingChargeDO, bodyItem, itemIds);
	}
	
	@EvApiByToken(value = "/apis/processingCharge/batchRemove",method = RequestMethod.POST,apiTitle = "删除加工费用")
    @ApiOperation("删除加工费用")
	@Transactional(rollbackFor = Exception.class)
	public R removePurchaseBill(
			@ApiParam(value = "根据id删除加工费用",required = true) @RequestParam(value = "ids",defaultValue = "") Long[] ids) {
		return processingChargeService.batchRemoveProcessingCharge(ids);
	}
	
	@EvApiByToken(value = "/apis/processingCharge/list",method = RequestMethod.GET,apiTitle = "获取加工费用列表/高级搜索")
    @ApiOperation("获取加工费用列表/高级搜索")
    public R list(
            @ApiParam(value = "发票号码") @RequestParam(value = "billCode",required = false) String billCode,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "产品名称") @RequestParam(value = "materielName",required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = Maps.newHashMap();
        map.put("billCode",billCode);
        map.put("supplierName",StringUtils.sqlLike(supplierName));
        map.put("materielName",StringUtils.sqlLike(materielName));
        map.put("startTime", startTime);
        map.put("endTime",endTime);

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        List<Map<String, Object>> data = processingChargeService.listForMap(map);
        Map<String, Object> countForMap = processingChargeService.countForMap(map);
        int total = Integer.parseInt(countForMap.getOrDefault("total",0).toString());
        Map<String, Object> results = Maps.newHashMap();
        if (data.size() > 0) {
            results.put("data", new DsResultResponse(pageno,pagesize,total,data));
            results.put("total", countForMap);
        }
        return R.ok(results);
	}
	
	@EvApiByToken(value = "/apis/processingCharge/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "加工费用Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return processingChargeService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/processingCharge/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "加工费用Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return processingChargeService.reverseAudit(id);
	}
	
	@EvApiByToken(value = "/apis/processingCharge/detail",method = RequestMethod.GET,apiTitle = "获取加工费用详细信息")
	@ApiOperation("获取加工费用详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
	    return  processingChargeService.getDetail(id);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/processingCharge", method = RequestMethod.GET, apiTitle = "导出加工费用")
    @ApiOperation("导出加工费用")
    public void exportExcel(
            @ApiParam(value = "票据编号") @RequestParam(value = "billCode",required = false) String billCode,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "产品名称") @RequestParam(value = "materielName",required = false) String materielName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,

            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String,Object> param = Maps.newHashMap();
        param.put("billCode",billCode);
        param.put("supplierName",StringUtils.sqlLike(supplierName));
        param.put("materielName",StringUtils.sqlLike(materielName));
        param.put("startTime", startTime);
        param.put("endTime",endTime);

        List<Map<String, Object>> data = processingChargeService.listForMap(param);
        ClassPathResource classPathResource = new ClassPathResource("poi/processing_charge.xlsx");
        Map<String,Object> map = Maps.newHashMap();
        map.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "加工费用");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }
}
