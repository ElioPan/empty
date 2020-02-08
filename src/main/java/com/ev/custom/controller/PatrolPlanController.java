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

import com.ev.custom.domain.PatrolPlanDO;
import com.ev.custom.service.PatrolPlanService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 保养计划表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
 
@Controller
@RequestMapping("/custom/patrolPlan")
public class PatrolPlanController {
	@Autowired
	private PatrolPlanService patrolPlanService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolPlan:patrolPlan")
	String PatrolPlan(){
	    return "custom/patrolPlan/patrolPlan";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolPlan:patrolPlan")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolPlanDO> patrolPlanList = patrolPlanService.list(query);
		int total = patrolPlanService.count(query);
		PageUtils pageUtils = new PageUtils(patrolPlanList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolPlan:add")
	String add(){
	    return "custom/patrolPlan/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolPlan:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolPlanDO patrolPlan = patrolPlanService.get(id);
		model.addAttribute("patrolPlan", patrolPlan);
	    return "custom/patrolPlan/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolPlan:add")
	public R save( PatrolPlanDO patrolPlan){
		if(patrolPlanService.save(patrolPlan)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolPlan:edit")
	public R update( PatrolPlanDO patrolPlan){
		patrolPlanService.update(patrolPlan);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolPlan:remove")
	public R remove( Long id){
		if(patrolPlanService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolPlan:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		patrolPlanService.batchRemove(ids);
		return R.ok();
	}
	
}
