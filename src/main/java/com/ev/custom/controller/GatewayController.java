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

import com.ev.custom.domain.GatewayDO;
import com.ev.custom.service.GatewayService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 网关信息
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:23:38
 */
 
@Controller
@RequestMapping("/custom/gateway")
public class GatewayController {
	@Autowired
	private GatewayService gatewayService;
	
	@GetMapping()
	@RequiresPermissions("custom:gateway:gateway")
	String Gateway(){
	    return "custom/gateway/gateway";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:gateway:gateway")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<GatewayDO> gatewayList = gatewayService.list(query);
		int total = gatewayService.count(query);
		PageUtils pageUtils = new PageUtils(gatewayList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:gateway:add")
	String add(){
	    return "custom/gateway/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:gateway:edit")
	String edit(@PathVariable("id") Long id,Model model){
		GatewayDO gateway = gatewayService.get(id);
		model.addAttribute("gateway", gateway);
	    return "custom/gateway/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:gateway:add")
	public R save( GatewayDO gateway){
		if(gatewayService.save(gateway)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:gateway:edit")
	public R update( GatewayDO gateway){
		gatewayService.update(gateway);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:gateway:remove")
	public R remove( Long id){
		if(gatewayService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:gateway:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		gatewayService.batchRemove(ids);
		return R.ok();
	}
	
}
