package com.ev.system.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ev.framework.config.ApplicationContextRegister;
import com.ev.framework.context.ApiContext;
import com.ev.system.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.ev.framework.utils.ShiroUtils;
import com.ev.system.dao.UserDao;
import com.ev.system.domain.UserDO;
import com.ev.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
/*	@Autowired
	UserDao userMapper;
	@Autowired
	MenuService menuService;*/
	@Autowired
	private ApiContext apiContext;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		Long userId = ShiroUtils.getUserId();
		MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
		Set<String> perms = menuService.listPerms(userId);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perms);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		Map<String, Object> map = new HashMap<>(16);
		map.put("username", username);
		String password = new String((char[]) token.getCredentials());

		UserDao userMapper = ApplicationContextRegister.getBean(UserDao.class);
        UserService userService = ApplicationContextRegister.getBean(UserService.class);
		// 查询用户信息
		List<UserDO> userList = userMapper.list(map);
		UserDO user = userList.size()>0?userList.get(0):null;
		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		// 密码错误
		if (!password.equals(user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		// 账号锁定
		if (user.getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
        // 将数据权限的信息查出
        UserDO userDO = userService.get(user.getUserId());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDO, password, getName());
		return info;
	}

}
