package com.ev.apis.controller.custom;

import com.ev.common.domain.MenuTree;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.MenuDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.DeptService;
import com.ev.system.service.MenuService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yunian
 * @date 2018/6/17
 */
@Api(value = "/",tags = "通用API")
@RestController
public class CommonApiController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;


    @EvApiByToken(value = "/apis/common/getDepts",method = RequestMethod.POST,apiTitle = "部门下拉选项数据")
    @ApiOperation("部门下拉选项数据")
    public R getDepts(@ApiParam(value = "部门主键", required = false) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
            @ApiParam(value = "是否包含下级部门", required = false) @RequestParam(value = "isContainChildren", defaultValue = "", required = false) Integer isContainChildren){
        Map<String,Object> results = Maps.newHashMap();
        results = this.deptService.getDepts(deptId, isContainChildren);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/common/getUsers",method = RequestMethod.POST,apiTitle = "人员下拉选项数据")
    @ApiOperation("人员下拉选项数据")
    public R getUsers(@ApiParam(value = "部门主键", required = false) @RequestParam(value = "deptId", defaultValue = "", required = false) Long deptId,
                      @ApiParam(value = "是否包含下级部门", required = false) @RequestParam(value = "isContainChildren", defaultValue = "", required = false) Integer isContainChildren){
        Map<String,Object> results = Maps.newHashMap();
        results = this.userService.getUsers(deptId, isContainChildren);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/common/getPermissions",method = RequestMethod.POST,apiTitle = "获取当前登录人的操作权限")
    @ApiOperation("获取当前登录人的操作权限")
    public R getPermissions(){
        Map<String,Object> results = Maps.newHashMap();
        UserDO user = ShiroUtils.getUser();
        Set<String> permSet = menuService.listPerms(user.getUserId());
        results.put("permSet",permSet);
        return R.ok(results);
    }
}
