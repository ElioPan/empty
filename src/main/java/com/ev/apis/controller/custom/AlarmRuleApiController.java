package com.ev.apis.controller.custom;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.domain.AlarmRuleDO;
import com.ev.custom.service.AlarmRuleService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 报警规则
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-10-23 09:51:41
 */

@Api(value = "/", tags = "报警规则API")
@RestController
public class AlarmRuleApiController {
	@Autowired
	private AlarmRuleService alarmRuleService;

	@EvApiByToken(value = "/apis/alarmRule/list", method = RequestMethod.POST, apiTitle = "报警规则列表")
	@ApiOperation("报警规则列表")
	public R list(
			@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
			@ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
			@ApiParam(value = "告警名称") @RequestParam(value = "name", defaultValue = "", required = false) String name,
			@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(6);
		params.put("name", name);
		params.put("deviceId", deviceId);
		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		List<Map<String, Object>> data = alarmRuleService.listForMap(params);
		int total = alarmRuleService.countForMap(params);
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
	 * 保存
	 */
	@EvApiByToken(value = "/apis/alarmRule/save", method = RequestMethod.POST, apiTitle = "保存报警规则信息")
	@ApiOperation("保存报警规则信息")
	public R save(AlarmRuleDO alarmRule,
			@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId,
			@ApiParam(value = "测点数组", required = true) @RequestParam(value = "pointArray", defaultValue = "") Long[] pointArray) {
		if (alarmRuleService.add(alarmRule, pointArray, deviceId) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 查看详情
	 */
	@EvApiByToken(value = "/apis/alarmRule/detail", method = RequestMethod.POST, apiTitle = "查看报警规则信息")
	@ApiOperation("查看报警规则信息")
	public R detail(
			@ApiParam(value = "报警规则Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(alarmRuleService.getDetailInfo(id));
	}

	/**
	 * 修改单个报警规则
	 */
	@EvApiByToken(value = "/apis/alarmRule/update", method = RequestMethod.POST, apiTitle = "修改单个报警规则")
	@ApiOperation("修改单个报警规则")
	public R update(AlarmRuleDO alarmRule) {
		if (alarmRuleService.update(alarmRule) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@EvApiByToken(value = "/apis/alarmRule/remove", method = RequestMethod.POST, apiTitle = "删除报警规则信息")
	@ApiOperation("删除报警规则信息")
	public R remove(@ApiParam(value = "规则主键", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		if (alarmRuleService.logicRemove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 批量删除
	 */
	@EvApiByToken(value = "/apis/alarmRule/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除报警规则信息")
	@ApiOperation("批量删除报警规则信息")
	public R remove(
			@ApiParam(value = "规则主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		if (alarmRuleService.logicBatchRemove(ids) == ids.length) {
			return R.ok();
		}
		return R.error();
	}

//	/**
//	 * 修改
//	 */
//	@EvApiByToken(value = "/apis/alarmRule/update", method = RequestMethod.POST, apiTitle = "修改报警规则信息")
//	@ApiOperation("修改报警规则信息")
//	public R update(AlarmRuleDO alarmRule,
//			@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId,
//			@ApiParam(value = "测点数组", required = true) @RequestParam(value = "pointArray", defaultValue = "", required = true) Long[] pointArray) {
//		if (alarmRuleService.edit(alarmRule, pointArray, deviceId) > 0) {
//			return R.ok();
//		}
//		return R.error();
//	}

	/**
	 * 查看详情
	 */
//	@EvApiByToken(value = "/apis/alarmRule/detail", method = RequestMethod.POST, apiTitle = "查看报警规则信息")
//	@ApiOperation("查看报警规则信息")
	public R detail(
			@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId,
			@ApiParam(value = "报警规则Id(传入值为返回的groupId字段)", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		return R.ok(alarmRuleService.getDetailInfo(id, deviceId));
	}

//	/**
//	 * 批量修改
//	 */
//	@EvApiByToken(value = "/apis/alarmRule/batchUpdate", method = RequestMethod.POST, apiTitle = "批量修改报警规则信息")
//	@ApiOperation("批量修改报警规则信息")
//	public R batchUpdate(
//			@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "", required = true) Long deviceId,
//			@ApiParam(value = "已修改报警规则数据数组:[{\"id\":1,\"name\":\"A相电流\",\"alarmType\":158,\"alarmLevel\":165,<br/>\"triggerMode\":1,\"triggerTime\":30,\"continueTime\":20,\"alarmWay\":\"123\",\"sortNo\":0},<br/>{\"id\":2,\"name\":\"B相电流\",\"alarmType\":158,\"alarmLevel\":165,<br/>\"triggerMode\":1,\"triggerTime\":30,\"continueTime\":20,\"alarmWay\":\"123\",\"sortNo\":10000000}]", required = true) @RequestParam(value = "alarmRuleArray", defaultValue = "", required = true) String alarmRuleArray) {
//		if (alarmRuleService.batchEdit(alarmRuleArray, deviceId) > 0) {
//			return R.ok();
//		}
//		return R.ok();
//	}

}
