package com.ev.custom.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ev.custom.domain.UserAssocDO;
import com.ev.custom.service.UserAssocService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 用户关联表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 14:58:02
 */
 
@Controller
@RequestMapping("/custom/userAssoc")
public class UserAssocController {
	@Autowired
	private UserAssocService userAssocService;
	
	@GetMapping()
	@RequiresPermissions("custom:userAssoc:userAssoc")
	String UserAssoc(){
	    return "custom/userAssoc/userAssoc";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:userAssoc:userAssoc")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> userAssocList = userAssocService.list(query);
		int total = userAssocService.count(query);
		PageUtils pageUtils = new PageUtils(userAssocList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:userAssoc:add")
	String add(){
	    return "custom/userAssoc/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:userAssoc:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UserAssocDO userAssoc = userAssocService.get(id);
		model.addAttribute("userAssoc", userAssoc);
	    return "custom/userAssoc/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:userAssoc:add")
	public R save( UserAssocDO userAssoc){
		if(userAssocService.save(userAssoc)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:userAssoc:edit")
	public R update( UserAssocDO userAssoc){
		userAssocService.update(userAssoc);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:userAssoc:remove")
	public R remove( Long id){
		if(userAssocService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:userAssoc:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		userAssocService.batchRemove(ids);
		return R.ok();
	}
	
}
