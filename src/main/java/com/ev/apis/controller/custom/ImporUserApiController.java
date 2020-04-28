package com.ev.apis.controller.custom;

import com.ev.common.service.ImportUserService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author gumingjie
 * @date 2019/9/27
 */
@Api(value = "/", tags = "用户信息导入API（企业微信）")
@RestController
public class ImporUserApiController {
	@Autowired
	private ImportUserService importExcel;

	/**
	 * 导入
	 */
	@ResponseBody
	@EvApiByToken(value = "/apis/importExcel/user", method = RequestMethod.POST, apiTitle = "用户信息导入（企业微信）")
	@ApiOperation("用户信息导入（企业微信）")
	@Transactional(rollbackFor = Exception.class)
	public R readBomFile(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file) {
		return importExcel.importExcelForUser(file);
	}

}
