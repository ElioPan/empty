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

import com.ev.custom.domain.PatrolPlanDetailDO;
import com.ev.custom.service.PatrolPlanDetailService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 巡检计划与设备及子项目关联表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-23 15:55:42
 */
 
@Controller
@RequestMapping("/custom/patrolPlanDetail")
public class PatrolPlanDetailController {
	@Autowired
	private PatrolPlanDetailService patrolPlanDetailService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolPlanDetail:patrolPlanDetail")
	String PatrolPlanDetail(){
	    return "custom/patrolPlanDetail/patrolPlanDetail";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolPlanDetail:patrolPlanDetail")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolPlanDetailDO> patrolPlanDetailList = patrolPlanDetailService.list(query);
		int total = patrolPlanDetailService.count(query);
		PageUtils pageUtils = new PageUtils(patrolPlanDetailList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolPlanDetail:add")
	String add(){
	    return "custom/patrolPlanDetail/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolPlanDetail:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolPlanDetailDO patrolPlanDetail = patrolPlanDetailService.get(id);
		model.addAttribute("patrolPlanDetail", patrolPlanDetail);
	    return "custom/patrolPlanDetail/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolPlanDetail:add")
	public R save( PatrolPlanDetailDO patrolPlanDetail){
		if(patrolPlanDetailService.save(patrolPlanDetail)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolPlanDetail:edit")
	public R update( PatrolPlanDetailDO patrolPlanDetail){
		patrolPlanDetailService.update(patrolPlanDetail);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolPlanDetail:remove")
	public R remove( Long id){
		if(patrolPlanDetailService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolPlanDetail:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		patrolPlanDetailService.batchRemove(ids);
		return R.ok();
	}
	
}
