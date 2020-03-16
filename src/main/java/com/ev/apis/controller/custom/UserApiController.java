package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.common.controller.BaseController;
import com.ev.framework.config.Constant;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.MD5Utils;
import com.ev.framework.utils.R;
import com.ev.system.domain.UserDO;
import com.ev.system.service.RoleService;
import com.ev.system.service.UserService;
import com.ev.system.vo.UserVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.identity.User;
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

    @EvApiByToken(value = "/apis/user/list",method = RequestMethod.GET,apiTitle = "获取用户列表信息")
    @ApiOperation("获取用户列表信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20") int pagesize,
                  @ApiParam(value = "用户ID") @RequestParam(value = "userId",defaultValue = "",required = false)  String userId,
                  @ApiParam(value = "姓名") @RequestParam(value = "userName",defaultValue = "",required = false)  String userName,
                  @ApiParam(value = "姓名&&手机号码") @RequestParam(value = "query",defaultValue = "",required = false)  String query,
                  @ApiParam(value = "手机号码") @RequestParam(value = "mobile",defaultValue = "",required = false)  String mobile,
                  @ApiParam(value = "是否显示禁用") @RequestParam(value = "status",defaultValue = "",required = false)  Integer status,
                  @ApiParam(value = "部门ID") @RequestParam(value = "deptId",defaultValue = "",required = false)  String deptId){
        Map<String, Object> params = Maps.newHashMap();
        params.put("createBy",userId);
        params.put("status",status);
        params.put("query",query);
        params.put("username",userName);
        params.put("mobile",mobile);
        params.put("deptId",deptId);
        params.put("offset",(pageno-1)*pagesize);
        params.put("limit",pagesize);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data= this.userService.listForMap(params);
        int total = this.userService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return  R.ok(results);
    }

    @EvApiByToken(value = "/apis/user/add",method = RequestMethod.POST,apiTitle = "添加用户信息")
    @ApiOperation("添加用户信息")
    public R save(UserDO user) throws IOException, ParseException {
        //校验用户名是否重复
        Map<String,Object> userNameParams = new HashMap<String,Object>();
        userNameParams.put("username",user.getUsername());
        if(userService.exit(userNameParams)){
            String[] args = {user.getUsername()};
            return R.error(messageSourceHandler.getMessage("basicInfo.user.isPresence",args));
        }

        //校验手机号码是否重复
        Map<String,Object> mobileParams = new HashMap<String,Object>();
        mobileParams.put("mobile",user.getMobile());
        if(userService.exit(mobileParams)){
            String[] args = {user.getMobile()};
            return R.error(messageSourceHandler.getMessage("basicInfo.mobile.isPresence",args));
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
        UserDO userDO = userService.get(user.getUserId());
        //校验用户名是否重复
        if(!userDO.getUsername().equals(user.getUsername())){
            Map<String,Object> userNameParams = new HashMap<String,Object>();
            userNameParams.put("username",user.getUsername());
            if(userService.exit(userNameParams)){
                String[] args = {user.getUsername()};
                return R.error(messageSourceHandler.getMessage("basicInfo.user.isPresence",args));
            }
        }
        //校验手机号码是否重复
        if(!userDO.getMobile().equals(user.getMobile())){
            Map<String,Object> mobileParams = new HashMap<String,Object>();
            mobileParams.put("mobile",user.getMobile());
            if(userService.exit(mobileParams)){
                String[] args = {user.getMobile()};
                return R.error(messageSourceHandler.getMessage("basicInfo.mobile.isPresence",args));
            }
        }
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
            		@ApiParam(value = "是否启用(1)/禁用(0)",required = true) @RequestParam(value = "isEnabled",defaultValue = "") Integer status) throws IOException, ParseException {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
             return R.error(messageSourceHandler.getMessage("basicInfo.showProject.update",null));
        }
        UserDO user = userService.get(id);
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
