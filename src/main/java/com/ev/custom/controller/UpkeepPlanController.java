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

import com.ev.custom.domain.UpkeepPlanDO;
import com.ev.custom.service.UpkeepPlanService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 保养计划表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:53
 */
 
@Controller
@RequestMapping("/custom/upkeepPlan")
public class UpkeepPlanController {
	@Autowired
	private UpkeepPlanService upkeepPlanService;
	
	@GetMapping()
	@RequiresPermissions("custom:upkeepPlan:upkeepPlan")
	String UpkeepPlan(){
	    return "custom/upkeepPlan/upkeepPlan";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:upkeepPlan:upkeepPlan")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UpkeepPlanDO> upkeepPlanList = upkeepPlanService.list(query);
		int total = upkeepPlanService.count(query);
		PageUtils pageUtils = new PageUtils(upkeepPlanList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:upkeepPlan:add")
	String add(){
	    return "custom/upkeepPlan/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:upkeepPlan:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UpkeepPlanDO upkeepPlan = upkeepPlanService.get(id);
		model.addAttribute("upkeepPlan", upkeepPlan);
	    return "custom/upkeepPlan/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:upkeepPlan:add")
	public R save( UpkeepPlanDO upkeepPlan){
		if(upkeepPlanService.save(upkeepPlan)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:upkeepPlan:edit")
	public R update( UpkeepPlanDO upkeepPlan){
		upkeepPlanService.update(upkeepPlan);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepPlan:remove")
	public R remove( Long id){
		if(upkeepPlanService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepPlan:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		upkeepPlanService.batchRemove(ids);
		return R.ok();
	}
	
}
