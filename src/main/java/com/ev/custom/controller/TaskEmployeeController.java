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

import com.ev.custom.domain.TaskEmployeeDO;
import com.ev.custom.service.TaskEmployeeService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 任务关联人
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-09 16:39:45
 */
 
@Controller
@RequestMapping("/custom/taskEmployee")
public class TaskEmployeeController {
	@Autowired
	private TaskEmployeeService taskEmployeeService;
	
	@GetMapping()
	@RequiresPermissions("custom:taskEmployee:taskEmployee")
	String TaskEmployee(){
	    return "custom/taskEmployee/taskEmployee";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:taskEmployee:taskEmployee")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TaskEmployeeDO> taskEmployeeList = taskEmployeeService.list(query);
		int total = taskEmployeeService.count(query);
		PageUtils pageUtils = new PageUtils(taskEmployeeList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:taskEmployee:add")
	String add(){
	    return "custom/taskEmployee/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:taskEmployee:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		TaskEmployeeDO taskEmployee = taskEmployeeService.get(id);
		model.addAttribute("taskEmployee", taskEmployee);
	    return "custom/taskEmployee/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:taskEmployee:add")
	public R save( TaskEmployeeDO taskEmployee){
		if(taskEmployeeService.save(taskEmployee)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:taskEmployee:edit")
	public R update( TaskEmployeeDO taskEmployee){
		taskEmployeeService.update(taskEmployee);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:taskEmployee:remove")
	public R remove( Integer id){
		if(taskEmployeeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:taskEmployee:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		taskEmployeeService.batchRemove(ids);
		return R.ok();
	}
	
}
