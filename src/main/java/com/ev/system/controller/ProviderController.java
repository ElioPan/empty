package com.ev.system.controller;

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

import com.ev.system.domain.ProviderDO;
import com.ev.system.service.ProviderService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 租户表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-04 13:55:46
 */
 
@Controller
@RequestMapping("/system/provider")
public class ProviderController {
	@Autowired
	private ProviderService providerService;
	
	@GetMapping()
	@RequiresPermissions("system:provider:provider")
	String Provider(){
	    return "system/provider/provider";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:provider:provider")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ProviderDO> providerList = providerService.list(query);
		int total = providerService.count(query);
		PageUtils pageUtils = new PageUtils(providerList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("system:provider:add")
	String add(){
	    return "system/provider/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:provider:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ProviderDO provider = providerService.get(id);
		model.addAttribute("provider", provider);
	    return "system/provider/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:provider:add")
	public R save( ProviderDO provider){
		if(providerService.save(provider)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:provider:edit")
	public R update( ProviderDO provider){
		providerService.update(provider);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:provider:remove")
	public R remove( Long id){
		if(providerService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:provider:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		providerService.batchRemove(ids);
		return R.ok();
	}
	
}
