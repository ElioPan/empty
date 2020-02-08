package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.common.domain.MenuTree;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.MenuDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.MenuService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author yunian
 * @date 2018/8/6
 */
@Api(value = "/",tags = "系统管理API")
@RestController
public class SystemApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @EvApiByToken(value = "/apis/sys/getMenus",method = RequestMethod.POST)
    @ApiOperation("获取当前登陆人员的菜单权限")
    public R getMenus(@ApiParam(value = "PC/手机/平板(空：所有；0：PC端；1:手机端；2:平板端)",required = false) @RequestParam(value = "terminal",defaultValue = "") String terminal){
        UserDO user = ShiroUtils.getUser();
        Map<String,Object> results = Maps.newHashMap();
        List<MenuTree<MenuDO>> menus = menuService.listMenuTree(user.getUserId(),terminal);
        results.put("menus",menus);
        return R.ok(results);
    }
}
