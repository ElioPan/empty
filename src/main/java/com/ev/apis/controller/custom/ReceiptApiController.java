package com.ev.apis.controller.custom;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.custom.domain.ReceiptDO;
import com.ev.custom.service.ReceiptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 收款单控制器类
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "收款单API")
public class ReceiptApiController {

	@Autowired
	private ReceiptService receiptService;
	
	@EvApiByToken(value = "/apis/ReceiptApi/add",method = RequestMethod.POST,apiTitle = "添加收款单")
    @ApiOperation("添加收款单")
	@Transactional(rollbackFor = Exception.class)
	public R addSalesBill(ReceiptDO receiptDO,
			@ApiParam(value = "添加收款单明细:[{<br/>\"documentDate\":\"\",\"receivableDate\":\"\",<br/>\"receivableMoney\":13000,\"receivedMoney\":6500,\"thisReceivable\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":229,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceType\":1,\"sourceNum\":\"123\"},{<br/>\"documentDate\":\"\",\"receivableDate\":\"\",<br/>\"receivableMoney\":13000,\"receivedMoney\":6500,\"thisReceivable\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":228,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceType\":1,\"sourceNum\":\"123\"}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "", required = true) String bodyItem){
		return receiptService.addReceipt(receiptDO, bodyItem);	
	}
	
	@EvApiByToken(value = "/apis/ReceiptApi/remove",method = RequestMethod.POST,apiTitle = "删除收款单")
    @ApiOperation("删除收款单")
	@Transactional(rollbackFor = Exception.class)
	public R removeReceipt(
			@ApiParam(value = "根据id删除收款单",required = true) @RequestParam(value = "id",defaultValue = "",required = true) Long id) {
		return receiptService.removeReceipt(id);
	}
	
	@EvApiByToken(value = "/apis/ReceiptApi/update",method = RequestMethod.POST,apiTitle = "收款单")
    @ApiOperation("修改收款单")
	@Transactional(rollbackFor = Exception.class)
	public R updatesalesContract(ReceiptDO receiptDO,Long[] deleteId,
			@ApiParam(value = "修改收款单明细:[{<br/>\"documentDate\":\"\",\"receivableDate\":\"\",<br/>\"receivableMoney\":13000,\"receivedMoney\":6500,\"thisReceivable\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":229,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceType\":1,\"sourceNum\":\"123\"},{<br/>\"documentDate\":\"\",\"receivableDate\":\"\",<br/>\"receivableMoney\":13000,\"receivedMoney\":6500,\"thisReceivable\":1700,<br/>\"unpayMoney\":5000,\"payAccount\":\"123\",<br/>\"settlementMethod\":228,\"settlementNum\":\"qqqqwwwwwwe\",<br>\"remarks\":\"hhhhhhhhhhhhh\",<br/>\"sourceType\":1,\"sourceNum\":\"123\"}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "", required = true) String bodyItem){	
		return receiptService.updateReceipt(receiptDO, bodyItem,deleteId);
	}
	
	@EvApiByToken(value = "/apis/ReceiptApi/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "收款单Id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){		
		return receiptService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/ReceiptApi/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "收款单Id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){		
		return receiptService.reverseAudit(id);
	}
	
	@EvApiByToken(value = "/apis/ReceiptApi/listApi",method = RequestMethod.GET,apiTitle = "获取收款单")
    @ApiOperation("获取收款单")
	public R listApi( @ApiParam(value = "开始时间",required = false) @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间",required = false) @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			@ApiParam(value = "收款单号",required = false) @RequestParam(value = "receiptNum",required = false) String receiptNum,
			@ApiParam(value = "客户名称",required = false) @RequestParam(value = "username",defaultValue = "",required = false)  String username,
			@ApiParam(value = "业务源单号",required = false) @RequestParam(value = "sourceNum",required = false) String sourceNum,
			@ApiParam(value = "收款人",required = false) @RequestParam(value = "payee",required = false) String payee,
			@ApiParam(value = "收款帐号",required = false) @RequestParam(value = "payAccount",required = false) String payAccount,
			@ApiParam(value = "结算方式",required = false) @RequestParam(value = "settlementMethod",required = false) Long settlementMethod,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("receiptNum",receiptNum);
		map.put("username",username);
		map.put("sourceNum",sourceNum);
		map.put("payee",payee);
		map.put("payAccount",payAccount);
		map.put("settlementMethod",settlementMethod);
		map.put("pageno",pageno);
		map.put("pagesize",pagesize);
		return receiptService.listApi(map);
	}
	
	@EvApiByToken(value = "/apis/ReceiptApi/detail",method = RequestMethod.GET,apiTitle = "获取收款单详细信息")
	@ApiOperation("获取收款单详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
	    return  receiptService.getdetail(id);
    }
}
