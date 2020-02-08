package com.ev.apis.controller.mes;

import com.ev.apis.model.DsResultResponse;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.mes.domain.MaterialInspectionDO;
import com.ev.mes.service.MaterialInspectionService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 物料检验管理
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-11-20 09:51:41
 */

@Api(value = "/", tags = "物料检验API")
@RestController
public class MaterialInspectionApiController {
	@Autowired
	private MaterialInspectionService materialInspectionService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

	@EvApiByToken(value = "/apis/materialInspection/list", method = RequestMethod.POST, apiTitle = "物料检验列表")
	@ApiOperation("物料检验列表")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "检验单类型(216、来料检验、217、产品检验、218、发货检验)", required = true) @RequestParam(value = "inspectionType", defaultValue = "") Integer inspectionType,
			@ApiParam(value = "开始时间") @RequestParam(value = "startTime", defaultValue = "", required = false) String startTime,
			@ApiParam(value = "结束时间") @RequestParam(value = "endTime", defaultValue = "", required = false) String endTime,
//			@ApiParam(value = "物料检验名称", required = false) @RequestParam(value = "materialInspectionName", defaultValue = "", required = false) String materialInspectionName,
			@ApiParam(value = "物料代码") @RequestParam(value = "serialNo", defaultValue = "", required = false) String serialNo,
			@ApiParam(value = "部门ID") @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
			@ApiParam(value = "供应商ID") @RequestParam(value = "supplierId", defaultValue = "", required = false) Long supplierId,
            @ApiParam(value = "供应商名称") @RequestParam(value = "supplierName", defaultValue = "", required = false) String supplierName,
            @ApiParam(value = "客户ID") @RequestParam(value = "clientId", defaultValue = "", required = false) Long clientId,
            @ApiParam(value = "客户名称") @RequestParam(value = "clientName", defaultValue = "", required = false) String clientName,
			@ApiParam(value = "状态ID") @RequestParam(value = "status", defaultValue = "", required = false) Integer status,
            @ApiParam(value = "需排序字段") @RequestParam(value = "sort",defaultValue = "",required = false)  String sort,
            @ApiParam(value = "升（asc）降(desc)序") @RequestParam(value = "order",defaultValue = "",required = false)  String order) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMap();
        // 自定义排序规则
        if (StringUtils.isNoneEmpty(sort)) {
            params.put("offset", 0);
            params.put("limit", 1);
            List<Map<String, Object>> maps = materialInspectionService.listForMap(params);
            if (maps.size() > 0 && maps.get(0).containsKey(sort)) {
                params.put("sort", sort);
                params.put("order", StringUtils.isOrder(order));
            }
        }

		params.put("inspectionType", inspectionType);
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("serialNo", serialNo);
		params.put("deptId", deptId);
		params.put("supplierId", supplierId);
        params.put("supplierName", supplierName);
		params.put("clientId", clientId);
        params.put("clientName", clientName);
		params.put("status", status);

		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		List<Map<String, Object>> data = materialInspectionService.listForMap(params);
		Map<String,Object> count = materialInspectionService.countForMap(params);
		results.put("count", count);
		int total = count.containsKey("total") ? Integer.parseInt(count.get("total").toString()) : 0;
		if ( data.size() > 0) {
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
	 * 审核物料检验
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/materialInspection/audit", method = RequestMethod.POST, apiTitle = "审核物料检验单")
	@ApiOperation("审核物料检验单")
	public R audit(
			@ApiParam(value = "物料检验单主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (materialInspectionService.isAudit(id)) {
            return R.error(messageSourceHandler.getMessage("common.duplicate.approved",null));
		}
//		if (!Objects.equals(ShiroUtils.getUserId(), materialInspectionService.get(id).getAuditor())) {
//            return R.error(messageSourceHandler.getMessage("common.approved.user",null));
//		}
		if (materialInspectionService.audit(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 反审核物料检验
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/materialInspection/reverseAudit", method = RequestMethod.POST, apiTitle = "反审核物料检验")
	@ApiOperation("反审核物料检验")
	public R reverseAudit(
			@ApiParam(value = "物料检验主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (!materialInspectionService.isAudit(id)) {
            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonWaitingAudit",null));
		}
//		if (!Objects.equals(ShiroUtils.getUserId(), materialInspectionService.get(id).getAuditor())) {
//            return R.error(messageSourceHandler.getMessage("receipt.reverseAudit.nonAuditor",null));
//		}
		if (materialInspectionService.reverseAudit(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 保存
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/materialInspection/save", method = RequestMethod.POST, apiTitle = "保存物料检验信息")
	@ApiOperation("保存物料检验信息")
	public R save(MaterialInspectionDO inspectionDO,
			@ApiParam(value = "检验项目数组例：" +
		    "[\r\n" + 
		    "    {\r\n" + 
		    "        \"projectId\":1,\r\n" + 
		    "        \"checkResult\":1,\r\n" + 
		    "        \"unit\":123,\r\n" + 
		    "        \"targetValue\":2.55,\r\n" + 
		    "        \"checkValue\":8.55,\r\n" + 
		    "        \"isMustCheck\":1,\r\n" + 
		    "        \"unqualifiedCount\":100,\r\n" + 
		    "        \"reasonId\":1,\r\n" + 
		    "        \"remarks\":\"这里是备注1\"\r\n" + 
		    "    },\r\n" + 
		    "    {\r\n" + 
		    "        \"projectId\":2,\r\n" + 
		    "        \"checkResult\":0,\r\n" + 
		    "        \"unit\":122,\r\n" + 
		    "        \"targetValue\":3.55,\r\n" + 
		    "        \"checkValue\":9.55,\r\n" + 
		    "        \"isMustCheck\":0,\r\n" + 
		    "        \"unqualifiedCount\":100,\r\n" + 
		    "        \"reasonId\":2,\r\n" + 
		    "        \"remarks\":\"这里是备注2\"\r\n" + 
		    "    }\r\n" + 
		    "]"
			, required = true) @RequestParam(value = "childArray", defaultValue = "") String childArray) {
		return materialInspectionService.add(inspectionDO, childArray);
	}

	/**
	 * 查看详情
	 */
	@EvApiByToken(value = "/apis/materialInspection/detail", method = RequestMethod.POST, apiTitle = "查看物料检验信息")
	@ApiOperation("查看物料检验信息")
	public R detail(
			@ApiParam(value = "物料检验Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(materialInspectionService.getDetailInfo(id));
	}

	/**
	 * 修改物料检验
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/materialInspection/update", method = RequestMethod.POST, apiTitle = "修改物料检验")
	@ApiOperation("修改物料检验")
	public R update(MaterialInspectionDO inspectionDO,
			@ApiParam(value = "检验项目数组例：" +
			"[\r\n" + 
			"    {\r\n" + 
			"        \"id\":1,\r\n" + 
			"        \"projectId\":1,\r\n" + 
			"        \"checkResult\":1,\r\n" + 
			"        \"unit\":123,\r\n" + 
			"        \"targetValue\":7.55,\r\n" + 
			"        \"checkValue\":20.55,\r\n" + 
			"        \"isMustCheck\":1,\r\n" + 
			"        \"unqualifiedCount\":100,\r\n" + 
			"        \"reasonId\":1,\r\n" + 
			"        \"remarks\":\"这里是备注1\"\r\n" + 
			"    },\r\n" + 
			"    {\r\n" + 
			"        \"id\":2,\r\n" + 
			"        \"projectId\":2,\r\n" + 
			"        \"checkResult\":0,\r\n" + 
			"        \"unit\":122,\r\n" + 
			"        \"targetValue\":8.55,\r\n" + 
			"        \"checkValue\":30.55,\r\n" + 
			"        \"isMustCheck\":0,\r\n" + 
			"        \"unqualifiedCount\":100,\r\n" + 
			"        \"reasonId\":2,\r\n" + 
			"        \"remarks\":\"这里是备注2\"\r\n" + 
			"    }\r\n" + 
			"]"
            ) @RequestParam(value = "childArray", defaultValue = "", required = false) String childArray,
			@ApiParam(value = "被删除的子项目ID") @RequestParam(value = "ids", defaultValue = "", required = false) Long[] ids
			) {
        return materialInspectionService.edit(inspectionDO, childArray, ids);
	}

	/**
	 * 删除
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/materialInspection/remove", method = RequestMethod.POST, apiTitle = "删除物料检验信息")
	@ApiOperation("删除物料检验信息")
	public R remove(
			@ApiParam(value = "物料检验主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		// TODO 关联引用不能删除未验证
		if (materialInspectionService.isAudit(id)) {
            return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
		}
		if (materialInspectionService.removeHeadAndBody(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 批量删除
	 */
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/materialInspection/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除物料检验信息")
	@ApiOperation("批量删除物料检验信息")
	public R remove(
			@ApiParam(value = "主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		for (Long id : ids) {
			// TODO 关联引用不能删除未验证
			if (materialInspectionService.isAudit(id)) {
                return R.error(messageSourceHandler.getMessage("common.submit.delete.disabled",null));
			}
		}
		materialInspectionService.batchRemoveHeadAndBody(ids);
		return R.ok();
	}

}
