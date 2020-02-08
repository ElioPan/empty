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
import com.ev.custom.domain.BanktransferslipDO;
import com.ev.custom.service.BanktransferslipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 银行转账单控制器层
 * @author hepeng
 *
 */
@RestController
@Api(value = "/",tags = "银行转账单API")
public class BankTransferSlipApiController {

	@Autowired
	private BanktransferslipService banktransferslipService;
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/add",method = RequestMethod.POST,apiTitle = "添加银行转账单")
    @ApiOperation("添加银行转账单")
	@Transactional(rollbackFor = Exception.class)
	public R addBankTransferSlip(BanktransferslipDO banktransferslip,
			@ApiParam(value = "添加银行转账单:[{<br/>\"transferredAccount\":\"fdsdghfdfdfgdsg\",<br>\"transferAccount\":\"dsfdgfhgjdhghrfcgd\",<br/>\"money\":13000,\"settlementMethod\":229,\"settlementNumber\":\"170097\",<br/>\"remarks\":\"hhhhhhhhhhhhh\"},{<br/>\"transferredAccount\":\"fdsdghfdfdfgdsg\",<br>\"transferAccount\":\"dsfdgfhgjdhghrfcgd\",<br/>\"money\":13000,\"settlementMethod\":228,\"settlementNumber\":\"170097\",<br/>\"remarks\":\"hhhhhhhhhhhhh\"}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "", required = true) String bodyItem){
		return banktransferslipService.addBankTransferSlip(banktransferslip, bodyItem);	
	}
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/remove",method = RequestMethod.POST,apiTitle = "删除银行转账单")
    @ApiOperation("删除银行转账单")
	@Transactional(rollbackFor = Exception.class)
	public R removeBankTransferSlip(
			@ApiParam(value = "根据id删除银行转账单",required = true) @RequestParam(value = "id",defaultValue = "",required = true) Long id) {
		return banktransferslipService.removeBankTransferSlip(id);
	}
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/update",method = RequestMethod.POST,apiTitle = "修改银行转账单")
    @ApiOperation("修改银行转账单")
	@Transactional(rollbackFor = Exception.class)
	public R updateBankTransferSlip(BanktransferslipDO banktransferslip,Long[] deleteId,
			@ApiParam(value = "修改银行转账单:[{<br/>\"transferredAccount\":\"fdsdghfdfdfgdsg\",<br>\"transferAccount\":\"dsfdgfhgjdhghrfcgd\",<br/>\"money\":13000,\"settlementMethod\":228,\"settlementNumber\":\"170097\",<br/>\"remarks\":\"hhhhhhhhhhhhh\"},{<br/>\"transferredAccount\":\"fdsdghfdfdfgdsg\",<br>\"transferAccount\":\"dsfdgfhgjdhghrfcgd\",<br/>\"money\":13000,\"settlementMethod\":229,\"settlementNumber\":\"170097\",<br/>\"remarks\":\"hhhhhhhhhhhhh\"}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "", required = true) String bodyItem){
		return banktransferslipService.updateBankTransferSlip(banktransferslip, bodyItem,deleteId);
	}
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "付款单Id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){		
		return banktransferslipService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "付款单Id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){		
		return banktransferslipService.reverseAudit(id);
	}
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/listApi",method = RequestMethod.GET,apiTitle = "获取银行转单")
    @ApiOperation("获取银行转账单")
	public R listApi( @ApiParam(value = "开始时间",required = false) @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间",required = false) @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			@ApiParam(value = "单据编号",required = false) @RequestParam(value = "transferNum",required = false) String transferNum,
			@ApiParam(value = "经办人",required = false) @RequestParam(value = "handlePeople",defaultValue = "",required = false)  Long handlePeople,
			@ApiParam(value = "审核状态",required = false) @RequestParam(value = "auditStatus",required = false) Long auditStatus,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("transferNum",transferNum);
		map.put("handlePeople",handlePeople);
		map.put("auditStatus",auditStatus);
		map.put("pageno",pageno);
		map.put("pagesize",pagesize);
		return banktransferslipService.listApi(map);
	}
	
	@EvApiByToken(value = "/apis/BankTransferSlipApi/detail",method = RequestMethod.GET,apiTitle = "获取银行转单详细信息")
	@ApiOperation("获取银行转单详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
	    return  banktransferslipService.getdetail(id);
    }
}
