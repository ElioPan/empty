package com.ev.apis.controller.custom;

import com.ev.custom.domain.PaymentformDO;
import com.ev.custom.service.PaymentformService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 付款单控制器层
 * @author hepeng
 *
 */
@RestController
@Api(value = "/",tags = "付款单API")
public class PaymentformApiController {


	@Autowired
	private PaymentformService paymentformService;

	@EvApiByToken(value = "/apis/PaymentformApi/add",method = RequestMethod.POST,apiTitle = "添加付款单")
    @ApiOperation("添加付款单")
	@Transactional(rollbackFor = Exception.class)
	public R addPaymentform(PaymentformDO paymentform,
			@ApiParam(value = "添加付款单明细:[{<br/>\"documentDate\":\"\",\"dueDate\":\"\",<br/>\"payMoney\":13000,\"amountPaid\":6500,\"thisPay\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":229,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceNum\":\"123\",\"sourceType\":1},{<br/>\"documentDate\":\"\",\"dueDate\":\"\",<br/>\"payMoney\":13000,\"amountPaid\":6500,\"thisPay\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":228,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceNum\":\"123\",\"sourceType\":1}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem){
		return paymentformService.addPaymentform(paymentform, bodyItem);	
	}
	
	@EvApiByToken(value = "/apis/PaymentformApi/remove",method = RequestMethod.POST,apiTitle = "删除付款单")
    @ApiOperation("删除付款单")
	@Transactional(rollbackFor = Exception.class)
	public R removePaymentform(
			@ApiParam(value = "根据id删除付款单",required = true) @RequestParam(value = "id",defaultValue = "") Long id) {
		return paymentformService.removePaymentform(id);
	}
	
	@EvApiByToken(value = "/apis/PaymentformApi/update",method = RequestMethod.POST,apiTitle = "修改付款单")
    @ApiOperation("修改付款单")
	@Transactional(rollbackFor = Exception.class)
	public R updatePaymentform(PaymentformDO paymentform,Long[] deleteId,
			@ApiParam(value = "修改付款单明细:[{<br/>\"documentDate\":\"\",\"dueDate\":\"\",<br/>\"payMoney\":13000,\"amountPaid\":6500,\"thisPay\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":229,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceNum\":\"123\",\"sourceType\":1},{<br/>\"documentDate\":\"\",\"dueDate\":\"\",<br/>\"payMoney\":13000,\"amountPaid\":6500,\"thisPay\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":228,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceNum\":\"123\",\"sourceType\":1}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "") String bodyItem){
		return paymentformService.updatePaymentform(paymentform, bodyItem,deleteId);
	}
	
	@EvApiByToken(value = "/apis/PaymentformApi/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "付款单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return paymentformService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/PaymentformApi/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "付款单Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id){
		return paymentformService.reverseAudit(id);
	}
	
	@EvApiByToken(value = "/apis/PaymentformApi/listApi",method = RequestMethod.GET,apiTitle = "获取付款单")
    @ApiOperation("获取付款单")
	public R listApi( @ApiParam(value = "开始时间") @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			@ApiParam(value = "付款单号") @RequestParam(value = "payNum",required = false) String payNum,
			@ApiParam(value = "供应商") @RequestParam(value = "company",defaultValue = "",required = false) Long company,
			@ApiParam(value = "业务源单号") @RequestParam(value = "sourceNum",required = false) String sourceNum,
			@ApiParam(value = "付款人") @RequestParam(value = "drawee",required = false) String drawee,
			@ApiParam(value = "付款帐号") @RequestParam(value = "payAccount",required = false) String payAccount,
			@ApiParam(value = "结算方式") @RequestParam(value = "settlementMethod",required = false) Long settlementMethod,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = new HashMap<>();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("payNum",payNum);
		map.put("company",company);
		map.put("sourceNum",sourceNum);
		map.put("drawee",drawee);
		map.put("payAccount",payAccount);
		map.put("settlementMethod",settlementMethod);
		map.put("pageno",pageno);
		map.put("pagesize",pagesize);
		return paymentformService.listApi(map);
	}
	
	@EvApiByToken(value = "/apis/PaymentformApi/detail",method = RequestMethod.GET,apiTitle = "获取付款单详细信息")
	@ApiOperation("获取付款单详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
	    return  paymentformService.getdetail(id);
    }
}
