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

import com.ev.custom.domain.DeviceInsuranceDO;
import com.ev.custom.service.DeviceInsuranceService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 设备保险管理
 * 
 * @author ev-wang
 * @email 286600136@qq.com
 * @date 2019-08-12 08:57:12
 */
 
@Controller
@RequestMapping("/custom/deviceInsurance")
public class DeviceInsuranceController {
	@Autowired
	private DeviceInsuranceService deviceInsuranceService;
	
	@GetMapping()
	@RequiresPermissions("custom:deviceInsurance:deviceInsurance")
	String DeviceInsurance(){
	    return "custom/deviceInsurance/deviceInsurance";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:deviceInsurance:deviceInsurance")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DeviceInsuranceDO> deviceInsuranceList = deviceInsuranceService.list(query);
		int total = deviceInsuranceService.count(query);
		PageUtils pageUtils = new PageUtils(deviceInsuranceList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:deviceInsurance:add")
	String add(){
	    return "custom/deviceInsurance/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:deviceInsurance:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DeviceInsuranceDO deviceInsurance = deviceInsuranceService.get(id);
		model.addAttribute("deviceInsurance", deviceInsurance);
	    return "custom/deviceInsurance/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:deviceInsurance:add")
	public R save( DeviceInsuranceDO deviceInsurance){
		if(deviceInsuranceService.save(deviceInsurance)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:deviceInsurance:edit")
	public R update( DeviceInsuranceDO deviceInsurance){
		deviceInsuranceService.update(deviceInsurance);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:deviceInsurance:remove")
	public R remove( Long id){
		if(deviceInsuranceService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:deviceInsurance:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		deviceInsuranceService.batchRemove(ids);
		return R.ok();
	}
	
}
