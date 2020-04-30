package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.common.controller.BaseController;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.ev.system.domain.RoleDO;
import com.ev.system.service.RoleService;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "/", tags = "角色管理")
public class RoleApiController extends BaseController {
	@Autowired
	RoleService roleService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/role/list",method = RequestMethod.GET,apiTitle = "获取用户列表信息")
    @ApiOperation("获取用户列表信息")
    public R list() {
		Map<String,Object> results = Maps.newHashMap();
		results.put("datas", this.roleService.list());
		return R.ok(results);
	}

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/role/add",method = RequestMethod.POST,apiTitle = "添加角色")
    @ApiOperation("添加角色")
    public R save(@ApiParam(value = "用户信息",required = true)RoleDO role,
                  @ApiParam(value = "自定义权限的部门ID") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
		}
		boolean checkSave =roleService.checkSave(role);
		if(!checkSave){
			return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
		if (roleService.insert(role,ids) > 0) {
			return R.ok();
		} else {
			return R.error();
		}
	}

	@EvApiByToken(value = "/apis/role/detail",method = RequestMethod.POST,apiTitle = "角色明细")
	@ApiOperation("角色明细")
	public R detail(Long id) {
		Map<String,Object> results = new HashMap<>();
		RoleDO roleDO = roleService.get(id);
		results.put("role",roleDO);
		return R.ok(results);
	}


    @EvApiByToken(value = "/apis/role/update",method = RequestMethod.POST,apiTitle = "编辑角色")
    @ApiOperation("编辑角色")
    R update(@ApiParam(value = "用户信息",required = true)RoleDO role,
            @ApiParam(value = "自定义权限的部门ID") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
		}
		boolean checkSave =roleService.checkSave(role);
		if(!checkSave){
			return R.error(messageSourceHandler.getMessage("common.duplicate.names",null));
		}
		if (roleService.edit(role,ids) > 0) {
			return R.ok();
		} else {
			return R.error();
		}
	}

    @EvApiByToken(value = "/apis/role/remove",method = RequestMethod.POST,apiTitle = "删除角色")
    @ApiOperation("删除角色")
    R remove(@ApiParam(value = "删除角色ID",required = true) @RequestParam(value = "id",defaultValue = "",required = true)Long id) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
		}
        int remove = roleService.remove(id);
        if (remove == -1){
            return R.error(messageSourceHandler.getMessage("basicInfo.userUse.delete.disabled",null));
        }
        if (remove > 0) {
            return R.ok();
        }
        return R.error();
	}

    @EvApiByToken(value = "/apis/role/batchRemove",method = RequestMethod.POST,apiTitle = "批量删除角色")
    @ApiOperation("批量删除角色")
    R batchRemove(@ApiParam(value = "删除角色ID数组",required = true)@RequestParam(value = "ids",defaultValue = "",required = true) Long[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
		}
		int r = roleService.batchremove(ids);
        if (r == -1){
            return R.error(messageSourceHandler.getMessage("basicInfo.userUse.delete.disabled",null));
        }
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}
   
}
