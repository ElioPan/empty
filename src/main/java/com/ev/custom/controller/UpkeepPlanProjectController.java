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

import com.ev.custom.domain.UpkeepPlanProjectDO;
import com.ev.custom.service.UpkeepPlanProjectService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 保养计划与项目中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
 
@Controller
@RequestMapping("/custom/upkeepPlanProject")
public class UpkeepPlanProjectController {
	@Autowired
	private UpkeepPlanProjectService upkeepPlanProjectService;
	
	@GetMapping()
	@RequiresPermissions("custom:upkeepPlanProject:upkeepPlanProject")
	String UpkeepPlanProject(){
	    return "custom/upkeepPlanProject/upkeepPlanProject";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:upkeepPlanProject:upkeepPlanProject")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UpkeepPlanProjectDO> upkeepPlanProjectList = upkeepPlanProjectService.list(query);
		int total = upkeepPlanProjectService.count(query);
		PageUtils pageUtils = new PageUtils(upkeepPlanProjectList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:upkeepPlanProject:add")
	String add(){
	    return "custom/upkeepPlanProject/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:upkeepPlanProject:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UpkeepPlanProjectDO upkeepPlanProject = upkeepPlanProjectService.get(id);
		model.addAttribute("upkeepPlanProject", upkeepPlanProject);
	    return "custom/upkeepPlanProject/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:upkeepPlanProject:add")
	public R save( UpkeepPlanProjectDO upkeepPlanProject){
		if(upkeepPlanProjectService.save(upkeepPlanProject)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:upkeepPlanProject:edit")
	public R update( UpkeepPlanProjectDO upkeepPlanProject){
		upkeepPlanProjectService.update(upkeepPlanProject);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepPlanProject:remove")
	public R remove( Long id){
		if(upkeepPlanProjectService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepPlanProject:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		upkeepPlanProjectService.batchRemove(ids);
		return R.ok();
	}
	
}
