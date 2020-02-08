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

import com.ev.custom.domain.PatrolProjectDO;
import com.ev.custom.service.PatrolProjectService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 巡检标准表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
 
@Controller
@RequestMapping("/custom/patrolProject")
public class PatrolProjectController {
	@Autowired
	private PatrolProjectService patrolProjectService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolProject:patrolProject")
	String PatrolProject(){
	    return "custom/patrolProject/patrolProject";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolProject:patrolProject")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolProjectDO> patrolProjectList = patrolProjectService.list(query);
		int total = patrolProjectService.count(query);
		PageUtils pageUtils = new PageUtils(patrolProjectList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolProject:add")
	String add(){
	    return "custom/patrolProject/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolProject:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolProjectDO patrolProject = patrolProjectService.get(id);
		model.addAttribute("patrolProject", patrolProject);
	    return "custom/patrolProject/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolProject:add")
	public R save( PatrolProjectDO patrolProject){
		if(patrolProjectService.save(patrolProject)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolProject:edit")
	public R update( PatrolProjectDO patrolProject){
		patrolProjectService.update(patrolProject);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolProject:remove")
	public R remove( Long id){
		if(patrolProjectService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolProject:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		patrolProjectService.batchRemove(ids);
		return R.ok();
	}
	
}
