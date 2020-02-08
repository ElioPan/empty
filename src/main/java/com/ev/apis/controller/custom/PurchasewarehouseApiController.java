package com.ev.apis.controller.custom;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购入库控制器层
 * @author ABC
 *
 */
@RestController
@Api(value = "/",tags = "采购入库API")
public class PurchasewarehouseApiController {

//	@Autowired
//    private PropurchaseInheadService propurchaseInheadService;
//
//	@EvApiByToken(value = "/apis/purchaseWarehouseApi/add",method = RequestMethod.POST,apiTitle = "添加采购入库")
//    @ApiOperation("添加采购入库")
//	@Transactional(rollbackFor = Exception.class)
//	public R addPurchaseWarehouse(PropurchaseInheadDO propurchaseInheadDO,
//            @ApiParam(value = "其他入库明细行:格式String", required = true) @RequestParam(value = "propurchaseInBodyDO", defaultValue = "", required = true) String proInbodyList){
//		String code="RKDH"+DateFormatUtil.getWorkOrderno();
//        propurchaseInheadDO.setInheadCode(code);
//        propurchaseInheadDO.setStorageType(15L);    //入库类型  15其他入库
//		return propurchaseInheadService.addOtherIn(propurchaseInheadDO,proInbodyList);
//	}
//
//	@EvApiByToken(value = "/apis/purchaseWarehouseApi/remove",method = RequestMethod.POST,apiTitle = "删除采购入库")
//    @ApiOperation("删除采购入库")
//	@Transactional(rollbackFor = Exception.class)
//	public R removePurchaseWarehouse(
//			@ApiParam(value = "根据id删除采购入库",required = true) @RequestParam(value = "inHead_Id", defaultValue = "", required = true) Long inHeadId) {
//		return propurchaseInheadService.remoceOtherWaitAuite(inHeadId);
//	}
//
//	@EvApiByToken(value = "/apis/purchaseWarehouseApi/audit",method = RequestMethod.POST,apiTitle = "审核接口")
//    @ApiOperation("审核接口")
//	@Transactional(rollbackFor = Exception.class)
//	public R audit(@ApiParam(value = "采购入库主表主键", required = true) @RequestParam(value = "inHead_Id", defaultValue = "", required = true) Long inHeadId,
//            @ApiParam(value = "审核人主键", required = true) @RequestParam(value = "auditor", defaultValue = "", required = true) Long auditor){
//		return propurchaseInheadService.addAuditStatus(inHeadId, auditor);
//	}
//
//	@EvApiByToken(value = "/apis/purchaseWarehouseApi/reverseAudit",method = RequestMethod.POST,apiTitle = "反审核接口")
//    @ApiOperation("反审核接口")
//	@Transactional(rollbackFor = Exception.class)
//	public R reverseAudit(@ApiParam(value = "采购入库主表主键", required = true) @RequestParam(value = "inHead_Id", defaultValue = "", required = true) Long inHeadId){
//		return propurchaseInheadService.changeOtherAudit(inHeadId);
//	}
//
//	 @EvApiByToken(value = "/apis/purchaseWarehouseApi/edit", method = RequestMethod.POST, apiTitle = "修改采购入库")
//	 @ApiOperation("修改采购入库-10待审核状态下允许修改；11已审核")
//	 @Transactional(rollbackFor = Exception.class)
//	 public R editPurchaseWarehouse(PropurchaseInheadDO propurchaseInheadDO,
//	                               	@ApiParam(value = "产品明细行数据:格式String", required = true) @RequestParam(value = "proOldInBodyDO", defaultValue = "", required = true) String proOldInBodyDO,
//	                               	@ApiParam(value = "新增的明细行数据:String", required = false) @RequestParam(value = "proNewInBodyDO", defaultValue = "", required = false) String proNewInBodyDO,
//	                               	@ApiParam(value = "被删除明细行id：Long[]", required = true) @RequestParam(value = "inBodyIds", defaultValue = "", required = true) Long[] inBodyIds,
//	                               	@ApiParam(value = "主表主键", required = true) @RequestParam(value = "inHeadId", defaultValue = "", required = true) Long inHeadId) {
//	    	return propurchaseInheadService.editOtherStorages(propurchaseInheadDO, proOldInBodyDO, proNewInBodyDO, inBodyIds, inHeadId);
//	    }
//
//	 @EvApiByToken(value = "/apis/purchaseWarehouseApi/inOtherHeadList", method = RequestMethod.POST, apiTitle = "采购入库列表/查询")
//	 @ApiOperation("采购入库列表/查询")
//	 public R PurchaseWaehouseList(@ApiParam(value = "当前第几页", required = false) @RequestParam(value = "pageno", defaultValue = "1", required = false) int pageno,
//	                               @ApiParam(value = "一页多少条", required = false) @RequestParam(value = "pagesize", defaultValue = "20", required = false) int pagesize,
//	                               @ApiParam(value = "单据编号/公司", required = false) @RequestParam(value = "输入模糊信息", defaultValue = "", required = false) String inDatas,
//	                               @ApiParam(value = "入库起始时间", required = false) @RequestParam(value = "startTime", defaultValue = "", required = false) Date startTime,
//	                               @ApiParam(value = "入库截止时间", required = false) @RequestParam(value = "endTime", defaultValue = "", required = false) Date endTime) {
//	        Map<String, Object> params = new HashMap<String, Object>();
//	        params.put("sourceCompany", inDatas);
//	        params.put("offset", (pageno - 1) * pagesize);
//	        params.put("limit", pagesize);
//	        params.put("inheadCode", inDatas);
//	        params.put("startTime", startTime);
//	        params.put("endTime", endTime);
//	        params.put("endTime", endTime);
//	        params.put("storageType", 15L);
//	        params.put("pageno", pageno);
//	        params.put("pagesize",pagesize);
//	        return propurchaseInheadService.otherHeadDetailList(params);
//	 }
}