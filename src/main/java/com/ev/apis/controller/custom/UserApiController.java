package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.common.controller.BaseController;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.MD5Utils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.system.dao.UserMapper;
import com.ev.system.domain.UserDO;
import com.ev.system.service.RoleService;
import com.ev.system.service.UserService;
import com.ev.system.vo.UserVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/", tags = "用户管理")
public class UserApiController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;


    @Autowired
    private UserMapper userMapper;

    @EvApiByToken(value = "/apis/user/list",method = RequestMethod.GET,apiTitle = "获取用户列表信息")
    @ApiOperation("获取用户列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId",defaultValue = "",required = false)  String userId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName",defaultValue = "",required = false)  String userName,
                  @ApiParam(value = "姓名&&手机号码") @RequestParam(value = "query",defaultValue = "",required = false)  String query,
                  @ApiParam(value = "手机号码") @RequestParam(value = "mobile",defaultValue = "",required = false)  String mobile,
                  @ApiParam(value = "是否显示禁用") @RequestParam(value = "status",defaultValue = "",required = false)  String status,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId",defaultValue = "",required = false)  String deptId){
        Map<String,Object> results = Maps.newHashMap();
        List<UserDO> data= userMapper.selectList(null);
        results.put("datas",data);
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/user/add",method = RequestMethod.POST,apiTitle = "添加用户信息")
    @ApiOperation("添加用户信息")
    public R save(UserDO user) throws IOException, ParseException {
        //校验用户名是否重复
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("username",user.getUsername());
        if(userService.exit(params)){
            String[] args = {user.getUsername()};
            return R.error(messageSourceHandler.getMessage("basicInfo.user.isPresence",args));
        }

        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        if (userService.save(user) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/user/detail",method = RequestMethod.POST,apiTitle = "用户明细信息")
    @ApiOperation("显示用户明细信息")
    R detail(Long userId) {
        Map<String,Object> results = new HashMap<>();
        UserDO userDO = userService.get(userId);
        results.put("user",userDO);
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/user/update",method = RequestMethod.POST,apiTitle = "编辑用户信息")
    @ApiOperation("编辑用户信息")
    R update(UserDO user) throws IOException, ParseException {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
             return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        if (userService.update(user) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/user/remove",method = RequestMethod.GET,apiTitle = "删除用户信息")
    @ApiOperation("删除用户信息")
    R remove(@ApiParam(value = "用户主键",required = true) @RequestParam(value="id",defaultValue = "") Long id) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
             return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        if (userService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @EvApiByToken(value = "/apis/user/batchRemove",method = RequestMethod.GET,apiTitle = "批量删除用户信息")
    @ApiOperation("批量删除用户信息")
    R batchRemove(@ApiParam(value = "用户主键数组",required = true, example = "[1,2,3,4]") @RequestParam("ids") Long[] userIds) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
             return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        int r = userService.batchremove(userIds);
        if (r > 0) {
            return R.ok();
        }
        return R.error();
    }
    
    @EvApiByToken(value = "/apis/user/resetPwd",method = RequestMethod.POST,apiTitle = "重置用户密码")
    @ApiOperation("重置用户密码")
    public R resetPwd(UserVO userVO) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			 return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
		}
		try{
			userService.resetPwd(userVO,getUser());
			return R.ok();
		}catch (Exception e){
			return R.error(1,e.getMessage());
		}
	}
    @EvApiByToken(value = "/apis/user/adminResetPwd",method = RequestMethod.POST,apiTitle = "admin重置用户密码")
    @ApiOperation("admin重置用户密码")
    public R adminResetPwd(UserVO userVO) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			 return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
		}
		try{
			userService.adminResetPwd(userVO);
			return R.ok();
		}catch (Exception e){
			return R.error(1,e.getMessage());
		}

	}
    
    @EvApiByToken(value = "/apis/user/isEnabled",method = RequestMethod.POST,apiTitle = "添加用户信息")
    @ApiOperation("是否启用(1)/禁用(0)")
    public R isEnabled(@ApiParam(value = "用户ID",required = true) @RequestParam(value = "userId",defaultValue = "")  Long id,
            		@ApiParam(value = "是否启用(1)/禁用(0)",required = true) @RequestParam(value = "isEnabled",defaultValue = "") Integer status) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
             return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        UserDO user = new UserDO();
        user.setUserId(id);
        if (!(status==0||status==1)) {
            return R.error(messageSourceHandler.getMessage("basicInfo.correct.param",null));
		}
        user.setStatus(status);
        if (userService.edit(user) > 0) {
            return R.ok();
        }
        return R.error();
    }

}
