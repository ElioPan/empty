package com.ev.apis.controller.mes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.config.ConstantForGYL;
import com.ev.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.service.MaterielService;
import com.ev.mes.domain.ProductionFeedingDO;
import com.ev.mes.service.ProductionFeedingDetailService;
import com.ev.mes.service.ProductionFeedingService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 生产投料管理
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-11-22 09:51:41
 */

@Api(value = "/", tags = "生产投料API")
@RestController
public class ProductionFeedingApiController {
	@Autowired
	private ProductionFeedingService productionFeedingService;
	@Autowired
	private ProductionFeedingDetailService productionFeedingDetailService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private MaterielService materielService;

	/**
	 * 获取生产投料父项列表(上表)
	 * 说明：
	 * 本表数据由生产计划单关联对应的生产投料单、生产领料单、生产报废单、即时库存等数据生成。上表数据选中下表动态显示相应的数据   
	 *
	 * @date 2019-11-28
	 * @author gumingjie
	 *
	 */
	@EvApiByToken(value = "/apis/productionFeeding/list", method = RequestMethod.POST, apiTitle = "生产投料列表")
	@ApiOperation("生产投料父项产品列表")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "生产计划单号") @RequestParam(value = "productionPlanNo", defaultValue = "", required = false) String productionPlanNo,
			@ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
			@ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
//			@ApiParam(value = "生产投料名称", required = false) @RequestParam(value = "productionFeedingName", defaultValue = "", required = false) String productionFeedingName,
			@ApiParam(value = "产品代码") @RequestParam(value = "serialNo", defaultValue = "", required = false) String serialNo,
			@ApiParam(value = "状态ID") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
            @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
            @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(10);
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = this.productionFeedingService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }
		params.put("productionPlanNo", productionPlanNo);
		params.put("deptId", deptId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		params.put("serialNo", serialNo);
		params.put("status", status);

		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		List<Map<String, Object>> data = productionFeedingService.listForMap(params);
		int total = productionFeedingService.countForMap(params);
		if (data != null && data.size() > 0) {
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(data);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			results.put("data", dsRet);
		}
		return R.ok(results);
	}
	
	/**
	 * 获取生产投料子项列表(下表)
	 * 说明：
	 * 本表数据由生产计划单关联对应的生产投料单、生产领料单、生产报废单、即时库存等数据生成。上表数据选中下表动态显示相应的数据   
	 *
	 * @date 2019-11-28
	 * @author gumingjie
	 *
	 */
	@EvApiByToken(value = "/apis/productionFeeding/childList", method = RequestMethod.POST, apiTitle = "生产投料列表")
	@ApiOperation("生产投料子项目列表")
	public R childList(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
            @ApiParam(value = "生产投料单号") @RequestParam(value = "planNo", defaultValue = "", required = false) String planNo,
			@ApiParam(value = "状态ID") @RequestParam(value = "auditSign", defaultValue = "", required = false) Integer auditSign,
            @ApiParam(value = "物料名称") @RequestParam(value = "materialsName", defaultValue = "", required = false) String materialsName,
            @ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
			@ApiParam(value = "父项产品ID") @RequestParam(value = "headId", defaultValue = "", required = false) Long headId) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMap();

        params.put("planNo", planNo);
        params.put("materialsName", materialsName);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
		params.put("headId", headId);
		params.put("auditSign", auditSign);

		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		List<Map<String, Object>> data = productionFeedingDetailService.listForMap(params);
		DictionaryDO dictionaryDO = dictionaryService.get(ConstantForGYL.SCTLD.intValue());
		String thisSourceTypeName = dictionaryDO.getName();

		// 获取实时库存
		List<Map<String, Object>> stockListForMap = materielService.stockListForMap(Maps.newHashMap());
		int total = productionFeedingDetailService.countForMap(params);
		if (data.size() > 0) {
			// quoteCount  可领数量
			for (Map<String, Object> map : data) {
				map.put("thisSourceType", ConstantForGYL.SCTLD);
				map.put("thisSourceTypeName", thisSourceTypeName);
				if (stockListForMap.size() > 0) {
					double availableCount = 0.0d;
					for (Map<String, Object> stockList : stockListForMap) {
						if (Objects.equals(stockList.get("materielId").toString(), map.get("materielId").toString())) {
							// 如果没有批次要求则查出所有该商品的可用数量累计
							if (!map.containsKey("batch")) {
								availableCount += Double.parseDouble(stockList.get("availableCount").toString());
								continue;
							}
							// 若制定了批次则将这一批次的可用数量查出记为实时数量
							if (Objects.equals(stockList.get("batch").toString(), map.get("batchNo").toString())) {
								availableCount += Double.parseDouble(stockList.get("availableCount").toString());
							}

						}
					}
					map.put("availableCount", availableCount);
				}
			}

			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(data);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((total + pagesize - 1) / pagesize);
			results.put("data", dsRet);
		}
		return R.ok(results);
	}

	/**
	 * 审核生产投料
	 * 限制条件1.已审核不能重复审核2.不是单据审核人不能审核
	 * 将单据状态修改为审核状态，锁定单据不能修改
	 *
	 * @date 2019-11-28 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionFeeding/audit", method = RequestMethod.POST, apiTitle = "审核生产投料单")
	@ApiOperation("审核生产投料单")
	public R audit(
			@ApiParam(value = "生产投料单主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionFeedingService.audit(id);
	}

	/**
	 * 反审核生产投料
	 * 审核生产投料
	 * 限制条件
	 * 1.待审核不能反审核
	 * 2.不是单据审核人不能反审核
	 * 3.有关联单据在不能反审核（即详细列表里领料数量存在或报废数量存在）
	 * 将单据状态修改为审核状态，锁定单据不能修改
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionFeeding/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核生产投料")
	@ApiOperation("反审核生产投料")
	public R reverseAudit(
			@ApiParam(value = "生产投料主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return productionFeedingService.reverseAudit(id);
	}

	/**
	 * 保存生产投料信息（单独生成的生产投料计划）
	 * 1.目前不能反选生产计划单
	 * 
	 * @param feedingDO 主表数据
	 * @param childArray 子表数据
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionFeeding/save", method = RequestMethod.POST, apiTitle = "保存生产投料信息")
	@ApiOperation("保存生产投料信息（单独生成的生产投料计划）")
	public R save(ProductionFeedingDO feedingDO, @ApiParam(value = "子项物料例："+ 
			"[\r\n" + 
			"    {\r\n" + 
			"        \"materielId\":15,\r\n" + 
			"        \"batchNo\":\"批号001\",\r\n" + 
			"        \"planFeeding\":1000\r\n" + 
			"    },\r\n" + 
			"    {\r\n" + 
			"        \"materielId\":16,\r\n" + 
			"        \"batchNo\":\"批号002\",\r\n" + 
			"        \"planFeeding\":9000\r\n" + 
			"    }\r\n" + 
			"]"
			, required = true) @RequestParam(value = "childArray", defaultValue = "") String childArray) {
		return productionFeedingService.add(feedingDO, childArray);
	}

	/**
	 * 查看生产投料单详情
	 *
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@EvApiByToken(value = "/apis/productionFeeding/detail", method = RequestMethod.POST, apiTitle = "查看生产投料信息")
	@ApiOperation("查看生产投料信息")
	public R detail(
			@ApiParam(value = "生产投料Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(productionFeedingService.getDetailInfo(id));
	}

	/**
	 * 修改生产投料
	 * 限制条件:
	 * 1.已审核单据不能进行反审核
	 * @param feedingDO 需要修改的生产投料信息
	 * @param childArray 生产投料单需要领的物料
	 * 
	 * @date 2019-11-29 
	 * @author gumingjie
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/productionFeeding/update", method = RequestMethod.POST, apiTitle = "修改生产投料")
	@ApiOperation("修改生产投料")
	public R update(ProductionFeedingDO feedingDO, @ApiParam(value = "生产投料单需要领的物料数组例："+
			 "[\r\n" + 
			 "    {\r\n" + 
			 "        \"id\":1,\r\n" + 
			 "        \"materielId\":1,\r\n" + 
			 "        \"batchNo\":\"批号001\",\r\n" + 
			 "        \"planFeeding\":1000\r\n" + 
			 "    },\r\n" + 
			 "    {\r\n" + 
			 "        \"id\":2,\r\n" + 
			 "        \"materielId\":2,\r\n" + 
			 "        \"batchNo\":\"批号002\",\r\n" + 
			 "        \"planFeeding\":9000\r\n" + 
			 "    },\r\n" + 
			 "    {\r\n" + 
			 "        \"materielId\":1,\r\n" + 
			 "        \"batchNo\":\"批号004\",\r\n" + 
			 "        \"planFeeding\":9000\r\n" + 
			 "    }\r\n" + 
			 "]"
    ) @RequestParam(value = "childArray", defaultValue = "", required = false) String childArray,
			@ApiParam(value = "被删除的子项目ID") @RequestParam(value = "ids", defaultValue = "", required = false) Long[] ids) {
		return productionFeedingService.edit(feedingDO, childArray, ids);
	}

//	/**
//	 * 删除（原型无此按钮，暂将功能隐藏）
//	 *
//	 * @date 2019-11-29
//	 * @author gumingjie
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	@EvApiByToken(value = "/apis/productionFeeding/remove", method = RequestMethod.POST, apiTitle = "删除生产投料信息")
//	@ApiOperation("删除生产投料信息")
//	public R remove(
//			@ApiParam(value = "生产投料主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
//		if (!productionFeedingService.isNonAudit(id)) {
//			return R.error("单据已提交，不能删除");
//		}
//		if (productionFeedingService.removeHeadAndBody(id) > 0) {
//			return R.ok();
//		}
//		return R.error();
//	}

//	/**
//	 * 批量删除(原型无此按钮，暂将功能隐藏)
//	 *
//	 * @date 2019-11-29
//	 * @author gumingjie
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	@EvApiByToken(value = "/apis/productionFeeding/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除生产投料信息")
//	@ApiOperation("批量删除生产投料信息")
//	public R remove(
//			@ApiParam(value = "单据主键数组,例 1,2,3,4 ", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
//		for (Long id : ids) {
//			if (!productionFeedingService.isNonAudit(id)) {
//				return R.error("单据已提交，不能删除");
//			}
//		}
//		productionFeedingService.batchRemoveHeadAndBody(ids);
//		return R.ok();
//	}


//	/**
//	 * 与供应链系统关联暂未做此功能
//	 * 与 导入来源订单 导入生产投料单 生产领料用
//	 */
//	@EvApiByToken(value = "/apis/productionFeeding/getFeedingListForOutStock", method = RequestMethod.POST, apiTitle = "导入生产投料单的列表（生产领用使用）")
//	@ApiOperation("导入生产投料单的列表（生产领用使用）")
//	public R getFeedingListForOutStock(
//			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1", required = true) int pageno,
//			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20", required = true) int pagesize,
//			@ApiParam(value = "开始时间", required = false) @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
//			@ApiParam(value = "结束时间", required = false) @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
//			@ApiParam(value = "模糊查询(工单号、产品编号或名称或型号、或原料名称、编号)", required = false) @RequestParam(value = "fuzzySearch", defaultValue = "", required = false) String fuzzySearch) {
//		Map<String, Object> params = Maps.newHashMapWithExpectedSize(10);
//
//		params.put("startTime", startTime);
//		params.put("endTime", endTime);
//		params.put("fuzzySearch", fuzzySearch);
//
//		params.put("offset", (pageno - 1) * pagesize);
//		params.put("limit", pagesize);
//		return R.ok();
//	}

}
