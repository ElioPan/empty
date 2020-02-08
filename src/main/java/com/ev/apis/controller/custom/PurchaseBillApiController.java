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
import com.ev.custom.domain.PurchasebillDO;
import com.ev.custom.service.PurchasebillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 采购票据控制器层
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "采购票据API")
public class PurchaseBillApiController {

	@Autowired
	private PurchasebillService purchasebillService;
	
	@EvApiByToken(value = "/apis/purchaseBillApi/add",method = RequestMethod.POST,apiTitle = "添加采购票据")
    @ApiOperation("添加采购票据")
	@Transactional(rollbackFor = Exception.class)
	public R addPurchaseBill(PurchasebillDO purchasebillDO,
			@ApiParam(value = "添加采购票据明细:[{<br/>\"productName\":\"xinsoft\",\"productId\":15,<br/>\"num\":5,<br/>\"price\":1300,\"money\":90,\"taxRate\":17,<br/>\"purchaseType\":1,\"sourceNum\":\"123\"},{<br/>\"productName\":\"xinsoft\",\"productId\":15,<br/>\"num\":5,<br/>\"price\":1300,\"money\":90,\"taxRate\":17,<br/>\"purchaseType\":1,\"sourceNum\":\"123\"}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "", required = true) String bodyItem){
		return purchasebillService.addPurchaseBill(purchasebillDO, bodyItem);	
	}
	
	@EvApiByToken(value = "/apis/purchaseBillApi/remove",method = RequestMethod.POST,apiTitle = "删除采购票据")
    @ApiOperation("删除采购票据")
	@Transactional(rollbackFor = Exception.class)
	public R removePurchaseBill(
			@ApiParam(value = "根据id删除采购票据",required = true) @RequestParam(value = "purchaseBillId",defaultValue = "",required = true) Long purchaseBillId) {
		return purchasebillService.removePurchaseBill(purchaseBillId);
	}
	
	@EvApiByToken(value = "/apis/purchaseBillApi/listApi",method = RequestMethod.GET,apiTitle = "获取采购票据列表/高级搜索")
    @ApiOperation("获取采购票据列表/高级搜索")
	public R listApi( @ApiParam(value = "开始时间",required = false) @RequestParam(value = "startTime",defaultValue = "",required = false)  String startTime,
            @ApiParam(value = "结束时间",required = false) @RequestParam(value = "endTime",defaultValue = "",required = false)  String endTime,
			@ApiParam(value = "票据编号",required = false) @RequestParam(value = "billNumber",required = false) String billNumber,
			@ApiParam(value = "供应商名称",required = false) @RequestParam(value = "company",defaultValue = "",required = false) Long company,
			@ApiParam(value = "产品名称",required = false) @RequestParam(value = "productName",required = false) String productName,
			@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("billNumber",billNumber);
		map.put("company",company);
		map.put("productName",productName);
		map.put("pageno",pageno);
		map.put("pagesize",pagesize);
		return purchasebillService.listApi(map);
	}
	
	@EvApiByToken(value = "/apis/purchaseBillApi/update",method = RequestMethod.POST,apiTitle = "修改采购票据")
    @ApiOperation("修改采购票据")
	@Transactional(rollbackFor = Exception.class)
	public R updatesalesContract(PurchasebillDO purchasebillDO,Long[] deleteId,
			@ApiParam(value = "修改采购票据明细:[{\"bid\":3,<br/>\"productName\":\"xinsoft\",\"productId\":15,<br/>\"num\":5,<br/>\"price\":1300,\"money\":90,\"taxRate\":17,<br/>\"purchaseType\":1,\"source_num\":\"mmmmmmmmm\",<br/>\"delFlag\":0},{\"bid\":3,<br/>\"productName\":\"xinsoft\",\"productId\":15,<br/>\"num\":5,<br/>\"price\":1300,\"money\":90,\"taxRate\":17,<br/>\"purchaseType\":1,\"source_num\":\"mmmmmmmmm\",<br/>\"delFlag\":0}]", required = true) @RequestParam(value = "bodyItem", defaultValue = "", required = true) String bodyItem){	
		return purchasebillService.updatePurchaseBill(purchasebillDO, bodyItem,deleteId);
	}
	
	@EvApiByToken(value = "/apis/purchaseBillApi/audit",method = RequestMethod.POST,apiTitle = "审核接口")
    @ApiOperation("审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R audit(@ApiParam(value = "采购票据Id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){		
		return purchasebillService.audit(id);
	}
	
	@EvApiByToken(value = "/apis/purchaseBillApi/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
    @ApiOperation("反审核接口")
	@Transactional(rollbackFor = Exception.class)
	public R reverseAudit(@ApiParam(value = "采购票据Id", required = true) @RequestParam(value = "id", defaultValue = "", required = true) Long id){		
		return purchasebillService.reverseAudit(id);
	}
	
	@EvApiByToken(value = "/apis/purchaseBillApi/detail",method = RequestMethod.GET,apiTitle = "获取采购票据详细信息")
	@ApiOperation("获取采购票据详细信息")
	public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
	    return  purchasebillService.getdetail(id);
    }
}
