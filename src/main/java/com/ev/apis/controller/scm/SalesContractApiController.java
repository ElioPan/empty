package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.SalescontractDO;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.scm.service.ContractAlterationService;
import com.ev.scm.service.SalescontractService;
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
 * 销售合同控制器层
 * @author gumingjie
 *
 */
@RestController
@Api(value = "/",tags = "销售合同API")
public class SalesContractApiController {

	@Autowired
	private SalescontractService salescontractService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ContractAlterationService contractAlterationService;
	
	@EvApiByToken(value = "/apis/salesContractApi/addOrUpdate",method = RequestMethod.POST,apiTitle = "添加销售合同")
    @ApiOperation("添加/修改销售合同（修改传入id）")
	@Transactional(rollbackFor = Exception.class)
	public R addOrUpdate(SalescontractDO salesContract,
			@ApiParam(value = "添加/修改销售合同明细:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"materielId\":50,\n" +
                    "        \"count\":5,\n" +
                    "        \"taxAmount\":1300,\n" +
                    "        \"taxRate\":90,\n" +
                    "        \"unitPrice\":300,\n" +
                    "        \"taxUnitPrice\":1500,\n" +
                    "        \"amount\":2000,\n" +
                    "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":3,\n" +
                    "        \"materielId\":50,\n" +
                    "        \"count\":5,\n" +
                    "        \"taxAmount\":1300,\n" +
                    "        \"taxRate\":90,\n" +
                    "        \"unitPrice\":300,\n" +
                    "        \"taxUnitPrice\":1500,\n" +
                    "        \"amount\":2000,\n" +
                    "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    }\n" +
                    "]",
                    required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
			@ApiParam(value = "添加/修改销售合同收款条件:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"receivableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"receivableAmount\":500,\n" +
                    "        \"receivedAmount\":1300,\n" +
                    "        \"unpayAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":3,\n" +
                    "        \"receivableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"receivableAmount\":500,\n" +
                    "        \"receivedAmount\":1300,\n" +
                    "        \"unpayAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    }\n" +
                    "]",
                    required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
                              @ApiParam(value = "被删除的销售合同明细ID") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds,
                              @ApiParam(value = "被删除的销售合同条件ID") @RequestParam(value = "payIds", defaultValue = "", required = false) Long[] payIds){
		return salescontractService.addOrUpdateSalesContract(salesContract, bodyItem, bodyPay,itemIds,payIds);
	}
	
	@EvApiByToken(value = "/apis/salesContractApi/batchRemove",method = RequestMethod.POST,apiTitle = "删除销售合同")
    @ApiOperation("批量删除销售合同")
	@Transactional(rollbackFor = Exception.class)
	public R removeSalesContract(
			@ApiParam(value = "销售合同id数组",required = true) @RequestParam(value = "salesContractIds",defaultValue = "") Long[] salesContractIds) {
		return salescontractService.removeSalesContract(salesContractIds);
	}
	
	@EvApiByToken(value = "/apis/salesContractApi/edit",method = RequestMethod.POST,apiTitle = "变更销售合同")
    @ApiOperation("变更销售合同")
	@Transactional(rollbackFor = Exception.class)
	public R editSalesContract(
            @ApiParam(value = "销售合同id",required = true) @RequestParam(value = "id",defaultValue = "") Long salesContractId,
            @ApiParam(value = "销售合同明细:详情回传过去的JSONArray",
                    required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
            @ApiParam(value = "添加/修改销售合同收款条件:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"receivableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"receivableAmount\":500,\n" +
                    "        \"receivedAmount\":1300,\n" +
                    "        \"unpayAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":3,\n" +
                    "        \"receivableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"receivableAmount\":500,\n" +
                    "        \"receivedAmount\":1300,\n" +
                    "        \"unpayAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    }\n" +
                    "]",
                    required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
            @ApiParam(value = "被删除的销售合同条件ID") @RequestParam(value = "payIds", defaultValue = "", required = false) Long[] payIds
    ){
		return salescontractService.editSalesContract(salesContractId, bodyItem, bodyPay,payIds);
	}
	
	@EvApiByToken(value = "/apis/salesContractApi/salesContractList",method = RequestMethod.GET,apiTitle = "获取销售合同列表")
    @ApiOperation("获取销售合同列表")
	public R salesContractList(
	          @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
              @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			  @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
              @ApiParam(value = "产品名称/客户名称 模糊查询") @RequestParam(value = "fuzzyQuery",required = false) String fuzzyQuery,
              // 高级查询
			  @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
              @ApiParam(value = "合同开始时间") @RequestParam(value = "contractStartTime",defaultValue = "",required = false)  String contractStartTime,
              @ApiParam(value = "合同结束时间") @RequestParam(value = "contractEndTime",defaultValue = "",required = false)  String contractEndTime,
              @ApiParam(value = "客户名称") @RequestParam(value = "clientName",defaultValue = "",required = false)  String clientName,
              @ApiParam(value = "客户Id") @RequestParam(value = "clientId",defaultValue = "",required = false)  Long clientId,
              @ApiParam(value = "产品名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
              @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
              @ApiParam(value = "销售部门") @RequestParam(value = "salesPersonDept",defaultValue = "",required = false)  Long salesPersonDept,
              @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
              @ApiParam(value = "销售员") @RequestParam(value = "salesPerson",defaultValue = "",required = false)  Long salesPerson,
              @ApiParam(value = "制单人") @RequestParam(value = "createBy",defaultValue = "",required = false)  Long createBy,
              @ApiParam(value = "制单日期") @RequestParam(value = "createTime",defaultValue = "",required = false)  String createTime,

              @ApiParam(value = "产品编号/产品名称/合同编号/客户名称 模糊查询") @RequestParam(value = "fuzzyInquire",required = false) String fuzzyInquire,
              @ApiParam(value = "单据开始时间") @RequestParam(value = "createStartTime",defaultValue = "",required = false)  String createStartTime,
              @ApiParam(value = "单据结束时间") @RequestParam(value = "createEndTime",defaultValue = "",required = false)  String createEndTime,

              @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
			  @ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("fuzzyQuery", StringUtils.sqlLike(fuzzyQuery));
        // 高级查询
        map.put("contractType", contractType);
        map.put("contractStartTime", contractStartTime);
        map.put("contractEndTime", contractEndTime);
        map.put("clientName", StringUtils.sqlLike(clientName));
        map.put("clientId", clientId);
        map.put("materielName", StringUtils.sqlLike(materielName));
        map.put("specification",StringUtils.sqlLike(specification));
        map.put("salesPersonDept", salesPersonDept);
        map.put("auditSign", auditSign);
        map.put("salesPerson", salesPerson);
        map.put("createBy", createBy);
        map.put("createTime", createTime);
        // 导入关联单据列表
        map.put("fuzzyInquire",  StringUtils.sqlLike(fuzzyInquire));
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);

        map.put("closeStatus",closeStatus);
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        List<Map<String, Object>> data = salescontractService.listForMap(map);
        Map<String, Object> stringBigDecimalMap = salescontractService.countForMap(map);
        int total = Integer.parseInt(stringBigDecimalMap.getOrDefault("total",0).toString());
        Map<String, Object> result = Maps.newHashMap();

        if (data.size() > 0) {
            DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.XSHT.intValue());
            String thisSourceTypeName = dictionaryDO.getName();
            for (Map<String, Object> datum : data) {
                datum.put("thisSourceType", ConstantForGYL.XSHT);
                datum.put("thisSourceTypeName", thisSourceTypeName);
            }
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
            result.put("total", stringBigDecimalMap);
        }
        return R.ok(result);
	}

    @EvApiByToken(value = "/apis/salesContractApi/alterationList",method = RequestMethod.GET,apiTitle = "获取销售合同变更列表")
    @ApiOperation("获取销售合同变更列表")
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

        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);

        map.put("contractId", contractId);
        map.put("contractType", ConstantForGYL.XSHT);
        List<Map<String, Object>> data = contractAlterationService.listForMap(map);
        Map<String, Object> result = Maps.newHashMap();
        int total = contractAlterationService.countForMap(map);
        if (data.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(result);
    }
	
	@EvApiByToken(value = "/apis/salesContract/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "销售合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return salescontractService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/salesContract/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "销售合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return salescontractService.reverseAudit(id);
	}

    @EvApiByToken(value = "/apis/salesContract/close",method = RequestMethod.POST,apiTitle = "关闭接口")
    @ApiOperation("关闭接口")
    @Transactional(rollbackFor = Exception.class)
    public R close(@ApiParam(value = "销售合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return salescontractService.close(id);
    }

    @EvApiByToken(value = "/apis/salesContract/reverseClose",method = RequestMethod.POST,apiTitle = "反关闭接口")
    @ApiOperation("反关闭接口")
    @Transactional(rollbackFor = Exception.class)
    public R reverseClose(@ApiParam(value = "销售合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return salescontractService.reverseClose(id);
    }
	
	@EvApiByToken(value = "/apis/salesContractApi/detail",method = RequestMethod.GET,apiTitle = "获取销售合同详细信息")
	@ApiOperation("获取销售合同详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "salesContractId",defaultValue = "")  Long salesContractId) {
	    return  salescontractService.getDetail(salesContractId);
    }

    @EvApiByToken(value = "/apis/salesContractApi/alterationDetail",method = RequestMethod.GET,apiTitle = "获取销售合同详细信息")
    @ApiOperation("获取销售合同变更详细信息")
    public R alterationDetail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return  salescontractService.getAlterationDetail(id);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/salesContract", method = RequestMethod.GET, apiTitle = "导出合同")
    @ApiOperation("导出合同")
    public void exportExcel(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "产品名称/客户名称 模糊查询") @RequestParam(value = "fuzzyQuery",required = false) String fuzzyQuery,
            // 高级查询
            @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
            @ApiParam(value = "合同开始时间") @RequestParam(value = "contractStartTime",defaultValue = "",required = false)  String contractStartTime,
            @ApiParam(value = "合同结束时间") @RequestParam(value = "contractEndTime",defaultValue = "",required = false)  String contractEndTime,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName",defaultValue = "",required = false)  String clientName,
            @ApiParam(value = "产品名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
            @ApiParam(value = "销售部门") @RequestParam(value = "salesPersonDept",defaultValue = "",required = false)  Long salesPersonDept,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
            @ApiParam(value = "销售员") @RequestParam(value = "salesPerson",defaultValue = "",required = false)  Long salesPerson,
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
        // 高级查询
        map.put("contractType", contractType);
        map.put("contractStartTime", contractStartTime);
        map.put("contractEndTime", contractEndTime);
        map.put("clientName", StringUtils.sqlLike(clientName));
        map.put("materielName", StringUtils.sqlLike(materielName));
        map.put("specification",StringUtils.sqlLike( specification));
        map.put("salesPersonDept", salesPersonDept);
        map.put("auditSign", auditSign);
        map.put("salesPerson", salesPerson);
        map.put("createBy", createBy);
        map.put("createTime", createTime);

        map.put("closeStatus",closeStatus);
        List<Map<String, Object>> data = salescontractService.listForMap(map);
        ClassPathResource classPathResource = new ClassPathResource("poi/sales_contract.xlsx");
        Map<String,Object> param = Maps.newHashMap();
        param.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "销售合同");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, param);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
