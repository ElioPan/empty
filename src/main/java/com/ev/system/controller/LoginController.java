package com.ev.system.controller;

import com.ev.framework.annotation.Log;
import com.ev.common.controller.BaseController;
import com.ev.common.domain.FileDO;
import com.ev.common.domain.MenuTree;
import com.ev.common.service.FileService;
import com.ev.framework.utils.MD5Utils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.MenuDO;
import com.ev.system.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LoginController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MenuService menuService;
	@Autowired
	FileService fileService;
	@GetMapping({ "/", "" })
	String welcome(Model model) {

		return "redirect:/index";
	}

	@Log("请求访问主页")
	@GetMapping({ "/index" })
	String index(Model model) {
		List<MenuTree<MenuDO>> menus = menuService.listMenuTree(getUserId(),"0");
		model.addAttribute("menus", menus);
		model.addAttribute("name", getUser().getName());
		FileDO fileDO = fileService.get(getUser().getPicId());
		if(fileDO!=null&&fileDO.getUrl()!=null){
			if(fileService.isExist(fileDO.getUrl())){
				model.addAttribute("picUrl",fileDO.getUrl());
			}else {
				model.addAttribute("picUrl","/img/photo_s.jpg");
			}
		}else {
			model.addAttribute("picUrl","/img/photo_s.jpg");
		}
		model.addAttribute("username", getUser().getUsername());
		return "index_v1";
	}

	@GetMapping("/login")
	String login() {
		return "login";
	}

	@Log("登录")
	@PostMapping("/login")
	@ResponseBody
	R ajaxLogin(String username, String password) {

		password = MD5Utils.encrypt(username, password);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			System.out.println(ShiroUtils.getSubjct().getSession().getId());
			return R.ok();
		} catch (AuthenticationException e) {
			return R.error(e.getMessage());
		}
	}

	@GetMapping("/logout")
	String logout() {
		ShiroUtils.logout();
		return "redirect:/login";
	}

	@GetMapping("/main")
	String main() {
		return "main";
	}

}
