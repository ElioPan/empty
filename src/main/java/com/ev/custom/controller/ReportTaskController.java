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

import com.ev.custom.domain.ReportTaskDO;
import com.ev.custom.service.ReportTaskService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 关联单据与关联任务联系表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-21 13:01:44
 */
 
@Controller
@RequestMapping("/custom/reportTask")
public class ReportTaskController {
	@Autowired
	private ReportTaskService reportTaskService;
	
	@GetMapping()
	@RequiresPermissions("custom:reportTask:reportTask")
	String ReportTask(){
	    return "custom/reportTask/reportTask";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:reportTask:reportTask")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ReportTaskDO> reportTaskList = reportTaskService.list(query);
		int total = reportTaskService.count(query);
		PageUtils pageUtils = new PageUtils(reportTaskList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:reportTask:add")
	String add(){
	    return "custom/reportTask/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:reportTask:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		ReportTaskDO reportTask = reportTaskService.get(id);
		model.addAttribute("reportTask", reportTask);
	    return "custom/reportTask/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:reportTask:add")
	public R save( ReportTaskDO reportTask){
		if(reportTaskService.save(reportTask)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:reportTask:edit")
	public R update( ReportTaskDO reportTask){
		reportTaskService.update(reportTask);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:reportTask:remove")
	public R remove( Integer id){
		if(reportTaskService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:reportTask:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		reportTaskService.batchRemove(ids);
		return R.ok();
	}
	
}
