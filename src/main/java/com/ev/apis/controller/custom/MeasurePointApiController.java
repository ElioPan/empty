package com.ev.apis.controller.custom;

import java.util.List;
import java.util.Map;

import com.ev.custom.domain.MeasurePointDO;
import com.ev.framework.annotation.EvApi;
import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.domain.MeasurePointTypeDO;
import com.ev.custom.domain.UomDO;
import com.ev.custom.service.MeasurePointService;
import com.ev.custom.service.MeasurePointTypeService;
import com.ev.custom.service.UomService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 测点维护
 * 
 * @author gumingjie
 * @email gumingjie.qi@gmail.com
 * @date 2019-10-22 09:51:41
 */

@Api(value = "/", tags = "测点维护API")
@RestController
public class MeasurePointApiController {
	@Autowired
	private MeasurePointService measurePointService;
	@Autowired
	private MeasurePointTypeService measurePointTypeService;
	@Autowired
	private UomService uomService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@EvApi(value = "/apis/measurePoint/list", method = RequestMethod.POST, apiTitle = "获取测点列表信息")
	@ApiOperation("获取测点列表信息")
	public R list(
			@ApiParam(value = "测点子类型") @RequestParam(value = "childType", defaultValue = "", required = false) Long childType,
			@ApiParam(value = "测点编码") @RequestParam(value = "serialNo", defaultValue = "", required = false) String serialNo,
			@ApiParam(value = "测点名称") @RequestParam(value = "name", defaultValue = "", required = false) String name,
			@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId) {
		// 查询列表数据
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
		params.put("childType", childType);
		params.put("serialNo", serialNo);
		params.put("name", name);
		params.put("deviceId", deviceId);
		Map<String, Object> results = Maps.newHashMapWithExpectedSize(1);
		List<Map<String, Object>> data = measurePointService.listForMap(params);
		int total = measurePointService.countForMap(params);
		if (data != null && data.size() > 0) {
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(data);
			dsRet.setTotalRows(total);
			results.put("data", dsRet);
		}
		return R.ok(results);
	}

	/**
	 * 保存
	 */
	@EvApiByToken(value = "/apis/measurePoint/save", method = RequestMethod.POST, apiTitle = "保存测点信息")
	@ApiOperation("保存测点信息")
	public R save(MeasurePointDO measurePoint,
			@ApiParam(value = "子类型", required = true) @RequestParam(value = "childTypeName", defaultValue = "") String childTypeName,
			@ApiParam(value = "单位", required = true) @RequestParam(value = "uomName", defaultValue = "") String uomName) {
		if (measurePointService.checkSave(measurePoint) > 0) {
			return R.error(messageSourceHandler.getMessage("point.device.isExist",null));
		}
		if (measurePointService.add(measurePoint, childTypeName, uomName) > 0) {
			Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
			result.put("id", measurePoint.getId());
			return R.ok(result);
		}
		return R.error();
	}
	
	/**
	 * 查看详情
	 */
	@EvApiByToken(value = "/apis/measurePoint/detail", method = RequestMethod.POST, apiTitle = "查看测点信息")
	@ApiOperation("查看测点信息")
	public R detail(@ApiParam(value = "测点Id", required = true) @RequestParam(value = "id", defaultValue = "") Long id) {
		Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
		params.put("id", id);
		params.put("offset", 0);
		params.put("limit", 1);
		List<Map<String, Object>> datas = measurePointService.listForMap(params);
		Map<String, Object> result = null;
		if (datas.size()>0) {
			result = datas.get(0);
		}
		return R.ok(result);
		
	}

	/**
	 * 修改
	 */
	@EvApiByToken(value = "/apis/measurePoint/update", method = RequestMethod.POST, apiTitle = "修改测点信息")
	@ApiOperation("修改测点信息")
	public R update(MeasurePointDO measurePoint,
					@ApiParam(value = "子类型", required = true) @RequestParam(value = "childTypeName", defaultValue = "") String childTypeName,
					@ApiParam(value = "单位", required = true) @RequestParam(value = "uomName", defaultValue = "") String uomName) {
		MeasurePointDO param = new MeasurePointDO();
		param.setDeviceId(measurePoint.getId());
		param.setSerialNo(measurePoint.getSerialNo());
		if (measurePointService.checkSave(measurePoint) > 0) {
			return R.error(messageSourceHandler.getMessage("point.device.isExist",null));
		}
		if (measurePointService.edit(measurePoint, childTypeName, uomName) > 0) {
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 *  批量修改
	 */
	@EvApiByToken(value = "/apis/measurePoint/batchUpdate", method = RequestMethod.POST, apiTitle = "修改测点信息")
	@ApiOperation("批量修改测点信息")
	public R batchUpdate(@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId,
			@ApiParam(value = "已修改测点数据数组:[{\"id\":1,\"serialNo\":\"测点ID序号\",\"name\":\"测点名称\",\"rwType\":151,<br/>\"childType\":1,\"uom\":1,\"useType\":154,\"isManualInput\":0,<br/>\"impower\":160,\"format\":163,\"sort_no\":0},<br/>{\"id\":2,\"serialNo\":\"测点ID序号\",\"name\":\"测点名称\",\"rwType\":151,<br/>\"childType\":1,\"uom\":1,\"useType\":154,\"isManualInput\":0,<br/>\"impower\":160,\"format\":163,\"sort_no\":1000},<br/>{\"id\":3,\"serialNo\":\"测点ID序号\",\"name\":\"测点名称\",<br/>\"rwType\":151,\"childType\":1,\"uom\":1,\"useType\":154,\"isManualInput\":0,<br/>\"impower\":160,\"format\":163,\"sort_no\":500}]", required = true) @RequestParam(value = "pointArray", defaultValue = "") String pointArray) {
		int batchEdit = measurePointService.batchEdit(pointArray,deviceId);
		if(batchEdit>0) {
			return R.ok();
		}
		if (batchEdit==-2) {
			return R.error(messageSourceHandler.getMessage("point.duplicate.name",null));
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@EvApiByToken(value = "/apis/measurePoint/remove", method = RequestMethod.POST, apiTitle = "删除测点信息")
	@ApiOperation("删除测点信息")
	public R remove(@ApiParam(value = "测点主键",required = true) @RequestParam(value="id",defaultValue = "") Long id) {
		if (measurePointService.logicRemove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 批量删除
	 */
	@EvApiByToken(value = "/apis/measurePoint/batchRemove", method = RequestMethod.POST, apiTitle = "批量删除测点信息")
	@ApiOperation("批量删除测点信息")
	public R batchRemove(@ApiParam(value = "测点主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids) {
		if (measurePointService.logicBatchRemove(ids) == ids.length) {
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 *  获取子类型下拉框
	 */
	@EvApiByToken(value = "/apis/measurePoint/childTypeList", method = RequestMethod.POST, apiTitle = "获取子类型下拉框")
	@ApiOperation("获取子类型下拉框")
	public R childTypeList() {
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		List<MeasurePointTypeDO> list = measurePointTypeService.list(Maps.newHashMapWithExpectedSize(0));
		result.put("data", list);
		return R.ok(result);
	}
	
	/**
	 * 删除 子类型数据
	 */
	@EvApiByToken(value = "/apis/measurePoint/removeChildType", method = RequestMethod.POST, apiTitle = "删除 子类型数据")
	@ApiOperation("删除 子类型数据")
	public R removeChildType(@ApiParam(value = "子类型Id",required = true) @RequestParam(value="id",defaultValue = "") Long id) {
		if (measurePointTypeService.remove(id.intValue()) > 0) {
			return R.ok();
		}
		return R.error();
	}
	
	
	/**
	 *  获取单位下拉框
	 */
	@EvApiByToken(value = "/apis/measurePoint/uomList", method = RequestMethod.POST, apiTitle = "获取单位下拉框")
	@ApiOperation("获取单位下拉框")
	public R uomList() {
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		List<UomDO> list = uomService.list(Maps.newHashMapWithExpectedSize(0));
		result.put("data", list);
		return R.ok(result);
	}
	
	/**
	 * 删除 单位数据
	 */
	@EvApiByToken(value = "/apis/measurePoint/uomRemove", method = RequestMethod.POST, apiTitle = "删除 单位数据")
	@ApiOperation("删除 单位数据")
	public R removeUom(@ApiParam(value = "单位Id",required = true) @RequestParam(value="id",defaultValue = "") Long id) {
		if (uomService.remove(id.intValue()) > 0) {
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 *  获取该设备的子类型列表
	 */
	@EvApiByToken(value = "/apis/measurePoint/childTypeListByDeviceId", method = RequestMethod.POST, apiTitle = "获取该设备的子类型列表")
	@ApiOperation("获取该设备的子类型列表")
	public R childTypeListByDeviceId(@ApiParam(value = "设备ID", required = true) @RequestParam(value = "deviceId", defaultValue = "") Long deviceId) {
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		Map<String,Object> param = Maps.newHashMapWithExpectedSize(1);
		param.put("deviceId", deviceId);
		List<MeasurePointTypeDO> list = measurePointTypeService.listByDeviceId(param);
		result.put("data", list);
		return R.ok(result);
	}
	
	
}
