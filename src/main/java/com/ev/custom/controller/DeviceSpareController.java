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

import com.ev.custom.domain.DeviceSpareDO;
import com.ev.custom.service.DeviceSpareService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 设备-备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-15 08:55:07
 */
 
@Controller
@RequestMapping("/custom/deviceSpare")
public class DeviceSpareController {
	@Autowired
	private DeviceSpareService deviceSpareService;
	
	@GetMapping()
	@RequiresPermissions("custom:deviceSpare:deviceSpare")
	String DeviceSpare(){
	    return "custom/deviceSpare/deviceSpare";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:deviceSpare:deviceSpare")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DeviceSpareDO> deviceSpareList = deviceSpareService.list(query);
		int total = deviceSpareService.count(query);
		PageUtils pageUtils = new PageUtils(deviceSpareList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:deviceSpare:add")
	String add(){
	    return "custom/deviceSpare/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:deviceSpare:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DeviceSpareDO deviceSpare = deviceSpareService.get(id);
		model.addAttribute("deviceSpare", deviceSpare);
	    return "custom/deviceSpare/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:deviceSpare:add")
	public R save( DeviceSpareDO deviceSpare){
		if(deviceSpareService.save(deviceSpare)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:deviceSpare:edit")
	public R update( DeviceSpareDO deviceSpare){
		deviceSpareService.update(deviceSpare);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:deviceSpare:remove")
	public R remove( Long id){
		if(deviceSpareService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:deviceSpare:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		deviceSpareService.batchRemove(ids);
		return R.ok();
	}
	
}
