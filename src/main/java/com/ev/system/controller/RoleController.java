package com.ev.system.controller;

import com.ev.framework.annotation.Log;
import com.ev.framework.config.Constant;
import com.ev.common.controller.BaseController;
import com.ev.framework.utils.R;
import com.ev.system.domain.RoleDO;
import com.ev.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {
	String prefix = "system/role";
	@Autowired
	RoleService roleService;

	@RequiresPermissions("sys:role:role")
	@GetMapping()
	String role() {
		return prefix + "/role";
	}

	@RequiresPermissions("sys:role:role")
	@GetMapping("/list")
	@ResponseBody()
	List<RoleDO> list() {
		List<RoleDO> roles = roleService.list();
		return roles;
	}

	@RequiresPermissions("sys:role:add")
	@GetMapping("/add")
	String add() {
		return prefix + "/add";
	}

	@RequiresPermissions("sys:role:edit")
	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		RoleDO roleDO = roleService.get(id);
		model.addAttribute("role", roleDO);
		return prefix + "/edit";
	}

	@RequiresPermissions("sys:role:add")
	@PostMapping("/save")
	@ResponseBody()
	R save(RoleDO role) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (roleService.save(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	@RequiresPermissions("sys:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	R update(RoleDO role) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (roleService.update(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	@RequiresPermissions("sys:role:remove")
	@PostMapping("/remove")
	@ResponseBody()
	R save(Long id) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (roleService.remove(id) > 0) {
			return R.ok();
		} else {
			return R.error(1, "删除失败");
		}
	}
	
	@RequiresPermissions("sys:role:batchRemove")
	@PostMapping("/batchRemove")
	@ResponseBody
	R batchRemove(@RequestParam("ids") Long[] ids) {
		if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		int r = roleService.batchremove(ids);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}
}
