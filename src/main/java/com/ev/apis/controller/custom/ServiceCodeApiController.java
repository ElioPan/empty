package com.ev.apis.controller.custom;

import java.util.Map;
import java.util.Objects;

import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ev.framework.annotation.EvApi;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.common.service.ReceiveService;
import com.ev.framework.utils.R;
import com.ev.system.domain.CompanyDO;
import com.ev.system.service.CompanyService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author AirOrangeWorkSpace
 *
 */

@Api(value = "/", tags = "服务码")
@RestController
public class ServiceCodeApiController {
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	/**
	 * 保存
	 * 
	 */
	@ApiIgnore
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/serviceCode/save", method = RequestMethod.POST, apiTitle = "保存服务码")
	@ApiOperation("保存服务码")
	public R save(
			@ApiParam(value = "服务码") @RequestParam(value = "serviceCode", defaultValue = "", required = false) String serviceCode)
			throws Exception {
		if (!receiveService.is(serviceCode)) {
			return R.error(messageSourceHandler.getMessage("license.save.error",null));
		}
        receiveService.saveServiceCodeForRedis(serviceCode);
        receiveService.saveServiceCodeForMysql(serviceCode);
		return R.ok();
	}
	
	@ApiIgnore
	@EvApiByToken(value = "/apis/serviceCode/getServiceCode", method = RequestMethod.POST, apiTitle = "获取服务码")
	@ApiOperation("获取服务码")
	public R getServiceCode() {
		String serviceCodeByRedis =  receiveService.getServiceCodeByRedis();
		if (Objects.isNull(serviceCodeByRedis)) {
			return R.error(messageSourceHandler.getMessage("license.get.error",null));
		}
		Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
		result.put("serviceCode", serviceCodeByRedis);
		return R.ok(result);
	}
	
	@EvApi(value = "/apis/serviceCode/activateServiceCode", method = RequestMethod.POST, apiTitle = "激活服务码")
	@ApiOperation("激活服务码")
	public R activateServiceCode(@ApiParam(value = "导入服务码",required = true) @RequestParam(value = "importServiceCode",defaultValue = "") MultipartFile  importServiceCode) {
		CompanyDO companyDO = new CompanyDO();
		companyDO.setId(1L);
		return companyService.importServiceCode(companyDO, importServiceCode);
		 
	}
}
