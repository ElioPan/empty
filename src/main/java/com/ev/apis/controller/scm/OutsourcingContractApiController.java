package com.ev.apis.controller.scm;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.scm.domain.OutsourcingContractDO;
import com.ev.scm.service.ContractAlterationService;
import com.ev.scm.service. OutsourcingContractService;
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
import java.util.List;
import java.util.Map;

/**
 * 委外合同控制器层
 * @author gumingjie
 *
 */
@RestController
@Api(value = "/",tags = "委外合同API")
public class OutsourcingContractApiController {

	@Autowired
	private  OutsourcingContractService  outsourcingContractService;
    @Autowired
    private ContractAlterationService contractAlterationService;
	
	@EvApiByToken(value = "/apis/outsourcingContract/addOrUpdate",method = RequestMethod.POST,apiTitle = "添加委外合同")
    @ApiOperation("添加/修改委外合同（修改传入id）")
	@Transactional(rollbackFor = Exception.class)
	public R addOrUpdate(OutsourcingContractDO outsourcingContract,
                         @ApiParam(value = "添加/修改销售合同明细:" +
                                 "[\n" +
                                 "    {\n" +
                                 "        \"id\":1,\n" +
                                 "        \"materielId\":50,\n" +
                                 "        \"isCheck\":1,\n" +
                                 "        \"bomId\":15,\n" +
                                 "        \"isQuota\":15,\n" +
                                 "        \"count\":5,\n" +
                                 "        \"taxUnitPrice\":1500,\n" +
                                 "        \"taxRate\":90,\n" +
                                 "        \"taxAmount\":1300,\n" +
                                 "        \"unitPrice\":300,\n" +
                                 "        \"amount\":2000,\n" +
                                 "        \"taxes\":1300,\n" +
                                 "        \"sourceId\":2,\n" +
                                 "        \"sourceType\":89,\n" +
                                 "        \"sourceCode\":\"XSHT20190720001\",\n" +
                                 "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    },\n" +
                                 "    {\n" +
                                 "        \"id\":2,\n" +
                                 "        \"materielId\":50,\n" +
                                 "        \"isCheck\":1,\n" +
                                 "        \"bomId\":15,\n" +
                                 "        \"isQuota\":15,\n" +
                                 "        \"count\":5,\n" +
                                 "        \"taxUnitPrice\":1500,\n" +
                                 "        \"taxRate\":90,\n" +
                                 "        \"taxAmount\":1300,\n" +
                                 "        \"unitPrice\":300,\n" +
                                 "        \"amount\":2000,\n" +
                                 "        \"taxes\":1300,\n" +
                                 "        \"sourceId\":2,\n" +
                                 "        \"sourceType\":89,\n" +
                                 "        \"sourceCode\":\"XSHT20190720001\",\n" +
                                 "        \"deliveryDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    }\n" +
                                 "]",
                                 required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
                         @ApiParam(value = "添加/修改销售合同收款条件:" +
                                 "[\n" +
                                 "    {\n" +
                                 "        \"id\":2,\n" +
                                 "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"payableAmount\":500,\n" +
                                 "        \"paidAmount\":1300,\n" +
                                 "        \"unpaidAmount\":90,\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    },\n" +
                                 "    {\n" +
                                 "        \"id\":2,\n" +
                                 "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                                 "        \"payableAmount\":500,\n" +
                                 "        \"paidAmount\":1300,\n" +
                                 "        \"unpaidAmount\":90,\n" +
                                 "        \"remarks\":\"这是备注\"\n" +
                                 "    }\n" +
                                 "]",
                                 required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
                              @ApiParam(value = "被删除的委外合同明细ID") @RequestParam(value = "itemIds", defaultValue = "", required = false) Long[] itemIds,
                              @ApiParam(value = "被删除的委外合同条件ID") @RequestParam(value = "payIds", defaultValue = "", required = false) Long[] payIds){
		return  outsourcingContractService.addOrUpdateOutsourcingContract(outsourcingContract, bodyItem, bodyPay,itemIds,payIds);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/batchRemove",method = RequestMethod.POST,apiTitle = "删除委外合同")
    @ApiOperation("批量删除委外合同")
	@Transactional(rollbackFor = Exception.class)
	public R removeOutsourcingContract(
			@ApiParam(value = "委外合同id数组",required = true) @RequestParam(value = "outsourcingContractIds",defaultValue = "") Long[] outsourcingContractIds) {
		return  outsourcingContractService.removeOutsourcingContract(outsourcingContractIds);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/edit",method = RequestMethod.POST,apiTitle = "变更委外合同")
    @ApiOperation("变更委外合同")
	@Transactional(rollbackFor = Exception.class)
	public R editOutsourcingContract(
            @ApiParam(value = "委外合同id",required = true) @RequestParam(value = "id",defaultValue = "") Long outsourcingContractId,
            @ApiParam(value = "委外合同明细:详情回传过去的JSONArray",
                    required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem,
            @ApiParam(value = "添加/修改委外合同付款条件:" +
                    "[\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"payableAmount\":500,\n" +
                    "        \"paidAmount\":1300,\n" +
                    "        \"unpaidAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\":2,\n" +
                    "        \"payableDate\":\"2020-01-08 00:00:00\",\n" +
                    "        \"payableAmount\":500,\n" +
                    "        \"paidAmount\":1300,\n" +
                    "        \"unpaidAmount\":90,\n" +
                    "        \"remarks\":\"这是备注\"\n" +
                    "    }\n" +
                    "]",
                    required = true) @RequestParam(value = "bodyPay", defaultValue = "") String bodyPay,
            @ApiParam(value = "被删除的委外合同条件ID") @RequestParam(value = "payIds", defaultValue = "", required = false) Long[] payIds
    ){
		return  outsourcingContractService.editOutsourcingContract(outsourcingContractId, bodyItem, bodyPay,payIds);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/outsourcingContractList",method = RequestMethod.GET,apiTitle = "获取委外合同列表")
    @ApiOperation("获取委外合同列表")
	public R outsourcingContractList(
	          @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
              @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			  @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
              @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
              @ApiParam(value = "物料名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
              // 高级查询
              @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
              @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
              @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
              @ApiParam(value = "制单人") @RequestParam(value = "createByName",defaultValue = "",required = false)  String createByName,
            // 导入关联单据列表
              @ApiParam(value = "物料编号/物料规格型号/物料名称/合同编号/客户名称 模糊查询") @RequestParam(value = "fuzzyInquire",required = false) String fuzzyInquire,
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
        map.put("supplierName", StringUtils.sqlLike(supplierName));
        map.put("materielName", StringUtils.sqlLike(materielName));
        // 高级查询
        map.put("contractType", contractType);
        map.put("specification",StringUtils.sqlLike(specification));
        map.put("auditSign", auditSign);
        map.put("createByName", StringUtils.sqlLike(createByName));
        // 导入关联单据列表
        map.put("fuzzyInquire",  StringUtils.sqlLike(fuzzyInquire));
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);

        map.put("closeStatus",closeStatus);
        map.put("offset",(pageno-1)*pagesize);
        map.put("limit",pagesize);
        List<Map<String, Object>> data =  outsourcingContractService.listForMap(map);
        Map<String, BigDecimal> stringBigDecimalMap = outsourcingContractService.countForMap(map);
        int total = stringBigDecimalMap.getOrDefault("total", BigDecimal.ZERO).intValue();
        Map<String, Object> result = Maps.newHashMap();
        if (data.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
            result.put("total", stringBigDecimalMap);
        }
        return R.ok(result);
	}

    @EvApiByToken(value = "/apis/outsourcingContract/alterationList",method = RequestMethod.GET,apiTitle = "获取委外合同变更列表")
    @ApiOperation("获取委外合同变更列表")
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
        map.put("contractType", ConstantForGYL.WWHT);
        List<Map<String, Object>> data = contractAlterationService.listForMap(map);
        Map<String, Object> result = Maps.newHashMap();
        int total = contractAlterationService.countForMap(map);
        if (data.size() > 0) {
            result.put("data", new DsResultResponse(pageno,pagesize,total,data));
        }
        return R.ok(result);
    }
	
	@EvApiByToken(value = "/apis/outsourcingContract/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return  outsourcingContractService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/outsourcingContract/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return  outsourcingContractService.reverseAudit(id);
	}

    @EvApiByToken(value = "/apis/outsourcingContract/close",method = RequestMethod.POST,apiTitle = "关闭接口")
    @ApiOperation("关闭接口")
    @Transactional(rollbackFor = Exception.class)
    public R close(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return  outsourcingContractService.close(id);
    }

    @EvApiByToken(value = "/apis/outsourcingContract/reverseClose",method = RequestMethod.POST,apiTitle = "反关闭接口")
    @ApiOperation("反关闭接口")
    @Transactional(rollbackFor = Exception.class)
    public R reverseClose(@ApiParam(value = "委外合同Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
        return  outsourcingContractService.reverseClose(id);
    }
	
	@EvApiByToken(value = "/apis/outsourcingContract/detail",method = RequestMethod.GET,apiTitle = "获取委外合同详细信息")
	@ApiOperation("获取委外合同详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "outsourcingContractId",defaultValue = "")  Long outsourcingContractId) {
	    return   outsourcingContractService.getDetail(outsourcingContractId);
    }

    @EvApiByToken(value = "/apis/outsourcingContract/alterationDetail",method = RequestMethod.GET,apiTitle = "获取委外合同详细信息")
    @ApiOperation("获取委外合同变更详细信息")
    public R alterationDetail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "")  Long id) {
        return   outsourcingContractService.getAlterationDetail(id);
    }

    @ResponseBody
    @EvApiByToken(value = "/apis/exportExcel/outsourcingContract", method = RequestMethod.GET, apiTitle = "导出合同")
    @ApiOperation("导出合同")
    public void exportExcel(
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
            @ApiParam(value = "合同编号") @RequestParam(value = "contractCode",required = false) String contractCode,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName",defaultValue = "",required = false)  String supplierName,
            @ApiParam(value = "物料名称") @RequestParam(value = "materielName",defaultValue = "",required = false)  String materielName,
            // 高级查询
            @ApiParam(value = "合同类型") @RequestParam(value = "contractType",required = false) Long contractType,
            @ApiParam(value = "规格型号") @RequestParam(value = "specification",required = false) String specification,
            @ApiParam(value = "审核状态") @RequestParam(value = "auditSign",required = false) Long auditSign,
            @ApiParam(value = "制单人") @RequestParam(value = "createByName",defaultValue = "",required = false)  String createByName,
            // 导入关联单据列表
            @ApiParam(value = "物料编号/物料规格型号/物料名称/合同编号/客户名称 模糊查询") @RequestParam(value = "fuzzyInquire",required = false) String fuzzyInquire,
            @ApiParam(value = "单据开始时间") @RequestParam(value = "createStartTime",defaultValue = "",required = false)  String createStartTime,
            @ApiParam(value = "单据结束时间") @RequestParam(value = "createEndTime",defaultValue = "",required = false)  String createEndTime,

            @ApiParam(value = "关闭状态/0未关/1关闭") @RequestParam(value = "closeStatus",defaultValue = "",required = false)  Long closeStatus,
            HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        Map<String, Object> map = Maps.newHashMap();
        // 列表查询
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("contractCode", contractCode);
        map.put("supplierName", StringUtils.sqlLike(supplierName));
        map.put("materielName", StringUtils.sqlLike(materielName));
        // 高级查询
        map.put("contractType", contractType);
        map.put("specification",StringUtils.sqlLike(specification));
        map.put("auditSign", auditSign);
        map.put("createByName", StringUtils.sqlLike(createByName));
        // 导入关联单据列表
        map.put("fuzzyInquire",  StringUtils.sqlLike(fuzzyInquire));
        map.put("createStartTime", createStartTime);
        map.put("createEndTime", createEndTime);

        map.put("closeStatus",closeStatus);

        List<Map<String, Object>> data =  outsourcingContractService.listForMap(map);
        ClassPathResource classPathResource = new ClassPathResource("poi/outsourcing_contract.xlsx");
        Map<String,Object> param = Maps.newHashMap();
        param.put("list", data);
        TemplateExportParams params = new TemplateExportParams(classPathResource.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, "委外合同");
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, param);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

}
