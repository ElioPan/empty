package com.ev.apis.controller.custom;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.system.domain.CompanyDO;
import com.ev.system.service.CompanyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author gumingjie
 * @date 2019/9/26
 */
@Api(value = "/", tags = "公司简介API")
@RestController
public class CompanyApiController {
	@Autowired
	private CompanyService companyService;
	
	@Transactional(rollbackFor = Exception.class)
	@EvApiByToken(value = "/apis/company/update", method = RequestMethod.POST, apiTitle = "编辑公司简介")
	@ApiOperation("编辑公司简介")
	public R update(CompanyDO companyDO
			) {
		companyDO.setId(1L);
		int update = companyService.update(companyDO);
		if (update>0) {
			return R.ok();
		}
		return R.error();
	}
	@EvApiByToken(value = "/apis/company/detail", method = RequestMethod.POST, apiTitle = "查看公司简介")
	@ApiOperation("查看公司简介")
	public R detail() {
		Map<String, Object> map = companyService.get(1L);
		if (map!=null) {
			return 	R.ok(map);
		}
		return R.error();
	}

}
