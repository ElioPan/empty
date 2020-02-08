package com.ev.apis.controller.custom;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ev.custom.domain.GatewayDO;
import com.ev.custom.service.GatewayService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;

/**
 * 网关信息
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-10-21 17:23:38
 */

@Api(value = "/", tags = "网关维护API")
@RestController
public class GatewayApiController {
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@EvApiByToken(value = "/apis/gateway/list", method = RequestMethod.POST, apiTitle = "获取网关列表信息")
	@ApiOperation("获取网关列表信息")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "网关序列号、网关名称") @RequestParam(value = "fuzzySearch", required = false) String fuzzySearch) {
		// 查询列表数据
		Map<String, Object> results = Maps.newHashMap();
		Map<String, Object> query = Maps.newHashMapWithExpectedSize(4);
		query.put("offset", (pageno - 1) * pagesize);
		query.put("limit", pagesize);
		query.put("fuzzySearch", fuzzySearch);
		List<Map<String, Object>> data = gatewayService.listForMap(query);
		int total = gatewayService.count(query);
		if (data.size() > 0) {
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
	 * 查看网关详情
	 */
	@EvApiByToken(value = "/apis/gateway/detail", method = RequestMethod.POST, apiTitle = "查看网关详情")
	@ApiOperation("查看网关详情")
	public R detail(@ApiParam(value = "网关Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		Map<String, Object> query = Maps.newHashMapWithExpectedSize(1);
		query.put("id", id);
		Map<String, Object> result = null;
		List<Map<String, Object>> results = gatewayService.listForMap(query);
		if (results.size() > 0) {
			result = gatewayService.listForMap(query).get(0);
		}
		return R.ok(result);
	}

	/**
	 * 保存
	 */
	@EvApiByToken(value = "/apis/gateway/save", method = RequestMethod.POST, apiTitle = "保存网关信息")
	@ApiOperation("保存网关信息")
	public R save(GatewayDO gateway) {
		int checkSave = gatewayService.checkSave(gateway.getSerialNo());
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		if (checkSave > 0) {
			return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
		}
		if (gatewayService.save(gateway) > 0) {
			result.put("id", gateway.getId());
			return R.ok(result);
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@EvApiByToken(value = "/apis/gateway/update", method = RequestMethod.POST, apiTitle = "修改网关信息")
	@ApiOperation("修改网关信息")
	public R update(GatewayDO gateway) {
		String serialNo = gateway.getSerialNo();
		GatewayDO gatewayDO = gatewayService.get(gateway.getId());
		if (Objects.equals(serialNo, gatewayDO.getSerialNo())) {
			if (gatewayService.update(gateway) > 0) {
				return R.ok();
			}
			return R.error();
		}
		int checkSave = gatewayService.checkSave(serialNo);
		if (checkSave > 0) {
			return R.error(messageSourceHandler.getMessage("common.duplicate.serialNo",null));
		}
		if (gatewayService.update(gateway) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@EvApiByToken(value = "/apis/gateway/remove", method = RequestMethod.DELETE, apiTitle = "删除网关信息")
	@ApiOperation("删除网关信息")
	public R remove(@ApiParam(value = "网关Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (gatewayService.logicRemove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 批量删除
	 */
	@EvApiByToken(value = "/apis/gateway/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除网关信息")
	@ApiOperation("批量删除网关信息")
	public R batchRemove(
			@ApiParam(value = "网关Id数组", required = true) @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		if (gatewayService.logicBatchRemove(ids) == ids.length) {
			return R.ok();
		}
		return R.error();
	}

}
