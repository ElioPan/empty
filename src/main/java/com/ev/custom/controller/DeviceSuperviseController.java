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

import com.ev.custom.domain.DeviceSuperviseDO;
import com.ev.custom.service.DeviceSuperviseService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 设备安监管理
 *
 * @author ev-wang
 * @email 286600136@qq.com
 * @date 2019-08-12 17:05:50
 */

@Controller
@RequestMapping("/custom/deviceSupervise")
public class DeviceSuperviseController {
	@Autowired
	private DeviceSuperviseService deviceSuperviseService;

	@GetMapping()
	@RequiresPermissions("custom:deviceSupervise:deviceSupervise")
	String DeviceSupervise(){
		return "custom/deviceSupervise/deviceSupervise";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:deviceSupervise:deviceSupervise")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<DeviceSuperviseDO> deviceSuperviseList = deviceSuperviseService.list(query);
		int total = deviceSuperviseService.count(query);
		PageUtils pageUtils = new PageUtils(deviceSuperviseList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	@RequiresPermissions("custom:deviceSupervise:add")
	String add(){
		return "custom/deviceSupervise/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:deviceSupervise:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DeviceSuperviseDO deviceSupervise = deviceSuperviseService.get(id);
		model.addAttribute("deviceSupervise", deviceSupervise);
		return "custom/deviceSupervise/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:deviceSupervise:add")
	public R save( DeviceSuperviseDO deviceSupervise){
		if(deviceSuperviseService.save(deviceSupervise)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:deviceSupervise:edit")
	public R update( DeviceSuperviseDO deviceSupervise){
		deviceSuperviseService.update(deviceSupervise);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:deviceSupervise:remove")
	public R remove( Long id){
		if(deviceSuperviseService.remove(id)>0){
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:deviceSupervise:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		deviceSuperviseService.batchRemove(ids);
		return R.ok();
	}

}
