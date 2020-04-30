package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForDevice;
import com.ev.common.domain.Tree;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.MenuDO;
import com.ev.system.service.MenuService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/", tags = "菜单管理")
public class MenuApiController {
    @Autowired
    MenuService menuService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @EvApiByToken(value = "/apis/menu/list",method = RequestMethod.POST,apiTitle = "菜单列表")
    @ApiOperation("菜单列表")
    public R list(@ApiParam(value = "是否仅限菜单",required = false) @RequestParam(value = "isMenu",defaultValue = "1") Boolean isMenu,
                  @ApiParam(value = "PC/手机/平板(空：所有；0：PC端；1:手机端；2:平板端)",required = false) @RequestParam(value = "terminal",defaultValue = "") String terminal) {
        Map<String,Object> params = new HashMap<>();
        params.put("isMenu",isMenu);
        params.put("terminal",terminal);
        List<MenuDO> menus = menuService.list(params);
        Map<String,Object> results = new HashMap<>();
        results.put("menus",menus);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/menu/add",method = RequestMethod.POST,apiTitle = "新增菜单")
    @ApiOperation("新增菜单")
    public R add(@ApiParam(value = "菜单信息",required = true) MenuDO menu){
        try {
            menuService.save(menu);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apis/menu/detail",method = RequestMethod.POST,apiTitle = "菜单明细")
    @ApiOperation("菜单明细")
    public R detail(Long id) {
        Map<String,Object> results = new HashMap<>();
        MenuDO mdo = menuService.get(id);
        results.put("menu",mdo);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/menu/update",method = RequestMethod.POST,apiTitle = "更新菜单")
    @ApiOperation("更新菜单")
    public R update(@ApiParam(value = "菜单信息",required = true) MenuDO menu){
        try {
            menuService.update(menu);
            return R.ok();
        }catch (Exception ex){
            return R.error(ex.getMessage());
        }
    }

    @EvApiByToken(value = "/apis/menu/remove",method = RequestMethod.DELETE,apiTitle = "删除菜单信息")
    @ApiOperation("删除菜单信息")
    public R remove(@ApiParam(value = "菜单主键",required = true) @RequestParam(value="id",defaultValue = "") Long id){
        if(menuService.remove(id)>0){
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/menu/tree",method = RequestMethod.POST,apiTitle = "获取菜单树")
    @ApiOperation("获取菜单树")
    public R tree(@ApiParam(value = "PC/手机/平板(空：所有；0：PC端；1:手机端；2:平板端)",required = false) @RequestParam(value = "terminal",defaultValue = "") String terminal) {
        Map<String,Object> params = new HashMap<>();
        params.put("terminal",terminal);
        Map<String,Object> result = new HashMap<>();
        Tree<MenuDO>  tree = menuService.getTree(params);
        result.put("menus",tree);
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/menu/tree/{roleId}",method = RequestMethod.POST,apiTitle = "根据角色获取菜单权限")
    @ApiOperation("根据角色获取菜单权限")
    public R tree(@PathVariable("roleId") Long roleId,
                  @ApiParam(value = "PC/手机/平板(空：所有；0：PC端；1:手机端；2:平板端)",required = false) @RequestParam(value = "terminal",defaultValue = "") String terminal) {
        Map<String,Object> params = new HashMap<>();
        params.put("terminal",terminal);
        Map<String,Object> result = new HashMap<>();
        Tree<MenuDO> tree = menuService.getTree(roleId,params);
        result.put("menus",tree);
        return R.ok(result);
    }



    /**
     * 快捷菜单管理
     */
    @EvApiByToken(value = "/apis/shotcut/list",method = RequestMethod.POST)
    @ApiOperation("获取用户自定义的快捷菜单")
    public R getShortCuts(){
        ///根据key获取缓存中的用户名
        String username = ShiroUtils.getUser().getUsername();
        String shortCutString = redisTemplate.opsForValue().get(Constant.REDIS_SHORT_CUT_PREFIX+username);
        JSONArray shortCutArray = JSON.parseArray(shortCutString);
        Map<String,Object> results = Maps.newHashMap();
        results.put("shotCuts",shortCutArray);
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/shotcut/add",method = RequestMethod.POST)
    @ApiOperation("添加快捷菜单")
    public R addShortCuts(@ApiParam(value = "菜单主键数组",required = true, example = "[1,2,3,4]") @RequestParam(value="ids",defaultValue = "") Long[] ids){
        String username = ShiroUtils.getUser().getUsername();
        String shortCutString = redisTemplate.opsForValue().get(Constant.REDIS_SHORT_CUT_PREFIX+username);
        JSONArray shortCutArray = JSON.parseArray(shortCutString)==null?new JSONArray():JSON.parseArray(shortCutString);
        List<MenuDO> menuDoList = menuService.listByIds(ids);
        for(MenuDO menuDO : menuDoList){
            JSONObject shortCutObj = new JSONObject();
            shortCutObj.put("menuId",menuDO.getMenuId());
            shortCutObj.put("menuName",menuDO.getName());
            shortCutObj.put("icon",menuDO.getIcon());
            shortCutObj.put("routePath",menuDO.getRoutePath());
            shortCutArray.add(shortCutObj);
        }
        redisTemplate.opsForValue().set(Constant.REDIS_SHORT_CUT_PREFIX+username,shortCutArray.toJSONString());
        return R.ok();
    }
}
