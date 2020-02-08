package com.ev.apis.controller.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.system.domain.AreaDO;
import com.ev.system.service.AreaService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author gumingjie
 * @date 2019/9/27
 */
@Api(value = "/", tags = "地区选择API")
@RestController
public class AreaApiController {
	@Autowired
	private AreaService areaService;

	@EvApiByToken(value = "/apis/area/list", method = RequestMethod.GET, apiTitle = "获取地区下拉列表信息")
	@ApiOperation("获取地区下拉列表信息")
	public R list(
			@ApiParam(value = "区域ID", required = true) @RequestParam(value = "cityId", defaultValue = "100000") Integer cityId) {
		Map<String, Object> results = Maps.newHashMap();
		List<AreaDO> areaList = areaService.listByParentId(cityId);
		List<AreaDO> datas = new ArrayList<>();
		if (areaList != null && areaList.size() > 0) {
			for (AreaDO areaDO : areaList) {
				if (areaDO.getLevelType() != 3) {
					areaDO.setZipCode(null);
				}
				datas.add(areaDO);
			}
		}
		results.put("data", datas);
		return R.ok(results);
	}
	@EvApiByToken(value = "/apis/area/detail", method = RequestMethod.GET, apiTitle = "获取地区信息")
	@ApiOperation("获取地区信息")
	public R detail(
			@ApiParam(value = "区域ID", required = true)Integer cityId) {
		Map<String, Object> results = Maps.newHashMap();
		AreaDO areaDO = areaService.get(cityId);
		if (areaDO!=null) {
			results.put("data", areaDO);
			return R.ok(results);
		}
		return R.error();
	}

}
