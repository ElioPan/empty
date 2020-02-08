package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApi;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.UserResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.MD5Utils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.system.service.DeptService;
import com.ev.system.service.MenuService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yunian
 * @date 2018/6/17
 */
@Api(value = "/",tags = "App登录API")
@RestController
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DeptService deptService;


    @EvApi(value = "/apis/user/checkToken", method = RequestMethod.POST, apiTitle = "Token校验")
    public R checkOfToken(@ApiParam(value = "userToken", required = true) @RequestParam(value = "token", required = true) String token,
                          @ApiParam(value = "用户名", required = true) @RequestParam(value = "username", required = true) String username,
                          @ApiParam(value = "密码", required = true) @RequestParam(value = "password", required = true) String password) {

        if (StringUtils.isNotEmpty(token)) {
            //校验令牌是否有效
            Object obj = redisTemplate.opsForValue().get(Constant.REDIS_USER_TOKEN_PREFIX + token);

            Map<String,Object> results = Maps.newHashMap();
            if (obj == null) {
                //令牌过期重新生成返回
                password = MD5Utils.encrypt(username, password);
                UsernamePasswordToken tokens = new UsernamePasswordToken(username, password);
                Subject subject = SecurityUtils.getSubject();
                try{
                    subject.login(tokens);
                    String userToken = ShiroUtils.getSubjct().getSession().getId().toString();
                    UserResponse userResponse = new UserResponse();
                    UserDO userDO = userService.findByUsername(username);
                    BeanUtils.copyProperties(userDO,userResponse);
                    userResponse.setToken(userToken);

                    results.put("userInfo",userResponse);
                    results.put("msg",messageSourceHandler.getMessage("login.token.isExpired",null));
                    results.put("isLate","true");
                    return R.ok(results);
                }catch(AuthenticationException e){
                    return R.error(messageSourceHandler.getMessage("login.userOrPassword.inCorrect",null));
                }
            }else{
                //令牌有效，返回验证信息
                results.put("isLate","false");
                return R.ok(results);
            }
        } else {
            //令牌为空！
            return R.error(messageSourceHandler.getMessage("login.token.isEmpty",null));
        }
    }


    @EvApi(value = "/apis/user/login",method = RequestMethod.POST,apiTitle = "登录")
    public R login(@ApiParam(value = "用户名",required = true)@RequestParam(value = "username",required = true)String username,
                   @ApiParam(value = "密码",required = true)@RequestParam(value = "password",required = true)String password){
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            String userToken = ShiroUtils.getSubjct().getSession().getId().toString();
            //session缓存一个月。
            //redisTemplate.opsForValue().set(Constant.REDIS_USER_TOKEN_PREFIX+userToken,username,3600*24*30, TimeUnit.SECONDS);
            UserResponse userResponse = new UserResponse();
            UserDO userDO = userService.findByUsername(username);
            BeanUtils.copyProperties(userDO,userResponse);
            userResponse.setToken(userToken);
            DeptDO dept = deptService.get(userDO.getDeptId());
            userResponse.setDeptName(dept == null?null:dept.getName());
            //获取权限列表
            Set<String> permSet = menuService.listPerms(userDO.getUserId());
            Map<String,Object> results = Maps.newHashMap();
            results.put("userInfo",userResponse);
            results.put("permSet",permSet);
            return R.ok(results);
        } catch (AuthenticationException e) {
            return R.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/user/logout",method = RequestMethod.POST,apiTitle = "注销")
    public R logout(HttpServletRequest request){
        String userToken = request.getHeader("Authorization");
        //redisTemplate.delete(Constant.REDIS_USER_TOKEN_PREFIX+userToken);
        ShiroUtils.logout();
        return R.ok(messageSourceHandler.getMessage("login.success",null));
    }

    @Transactional(rollbackFor = Exception.class)
    @EvApiByToken(value = "/apis/user/appResetPasswd",method = RequestMethod.POST,apiTitle = "修改个人密码")
    public R appResetPasswd(HttpServletRequest request,
                          @ApiParam(value = "旧密码",required = true)@RequestParam(value = "oldpasswd")String oldpasswd,
                          @ApiParam(value = "新密码",required = true)@RequestParam(value = "newpasswd")String newpasswd,
                          @ApiParam(value = "新密码确认",required = true)@RequestParam(value = "newpasswd")String newpasswordConfirm){
        String userToken = request.getHeader("Authorization");
        ///根据key获取缓存中的用户名
        //String username = redisTemplate.opsForValue().get(Constant.REDIS_USER_TOKEN_PREFIX+userToken);
        UserDO userDO = ShiroUtils.getUser();
        try {
            int code = userService.appResetPasswd(userDO,oldpasswd,newpasswd);
            if(code == 500){
                return R.error(messageSourceHandler.getMessage("login.oldPassword.error",null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(messageSourceHandler.getMessage("login.update.success",null));
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        List<String> excludes = new ArrayList<>();

        if (excludes == null || excludes.isEmpty()) {
            return false;
        }

        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }

}
