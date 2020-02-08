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

import com.ev.custom.domain.DeviceDO;
import com.ev.custom.service.DeviceService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 设备表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-09 14:35:29
 */
 
@Controller
@RequestMapping("/custom/device")
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	
	@GetMapping()
	@RequiresPermissions("custom:device:device")
	String Device(){
	    return "custom/device/device";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:device:device")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DeviceDO> deviceList = deviceService.list(query);
		int total = deviceService.count(query);
		PageUtils pageUtils = new PageUtils(deviceList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:device:add")
	String add(){
	    return "custom/device/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:device:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DeviceDO device = deviceService.get(id);
		model.addAttribute("device", device);
	    return "custom/device/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:device:add")
	public R save( DeviceDO device){
		if(deviceService.save(device)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:device:edit")
	public R update( DeviceDO device){
		deviceService.update(device);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:device:remove")
	public R remove( Long id){
		if(deviceService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:device:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		deviceService.batchRemove(ids);
		return R.ok();
	}
	
}
