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

import com.ev.custom.domain.DeviceArgDO;
import com.ev.custom.service.DeviceArgService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 设备参数表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-15 12:54:05
 */
 
@Controller
@RequestMapping("/custom/deviceArg")
public class DeviceArgController {
	@Autowired
	private DeviceArgService deviceArgService;
	
	@GetMapping()
	@RequiresPermissions("custom:deviceArg:deviceArg")
	String DeviceArg(){
	    return "custom/deviceArg/deviceArg";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:deviceArg:deviceArg")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DeviceArgDO> deviceArgList = deviceArgService.list(query);
		int total = deviceArgService.count(query);
		PageUtils pageUtils = new PageUtils(deviceArgList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:deviceArg:add")
	String add(){
	    return "custom/deviceArg/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:deviceArg:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DeviceArgDO deviceArg = deviceArgService.get(id);
		model.addAttribute("deviceArg", deviceArg);
	    return "custom/deviceArg/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:deviceArg:add")
	public R save( DeviceArgDO deviceArg){
		if(deviceArgService.save(deviceArg)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:deviceArg:edit")
	public R update( DeviceArgDO deviceArg){
		deviceArgService.update(deviceArg);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:deviceArg:remove")
	public R remove( Long id){
		if(deviceArgService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:deviceArg:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		deviceArgService.batchRemove(ids);
		return R.ok();
	}
	
}
