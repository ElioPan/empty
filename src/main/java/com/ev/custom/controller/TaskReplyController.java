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

import com.ev.custom.domain.TaskReplyDO;
import com.ev.custom.service.TaskReplyService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 任务处理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-12 12:36:25
 */
 
@Controller
@RequestMapping("/custom/taskReply")
public class TaskReplyController {
	@Autowired
	private TaskReplyService taskReplyService;
	
	@GetMapping()
	@RequiresPermissions("custom:taskReply:taskReply")
	String TaskReply(){
	    return "custom/taskReply/taskReply";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:taskReply:taskReply")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TaskReplyDO> taskReplyList = taskReplyService.list(query);
		int total = taskReplyService.count(query);
		PageUtils pageUtils = new PageUtils(taskReplyList, total);
		return pageUtils;
	}

	@ResponseBody
	@GetMapping("/listForMap")
	@RequiresPermissions("custom:taskReply:taskReply")
	public List<Map<String,Object>> listForMap(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String,Object>> taskReplyList = taskReplyService.listForMap(params);
		return taskReplyList;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:taskReply:add")
	String add(){
	    return "custom/taskReply/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:taskReply:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		TaskReplyDO taskReply = taskReplyService.get(id);
		model.addAttribute("taskReply", taskReply);
	    return "custom/taskReply/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:taskReply:add")
	public R save( TaskReplyDO taskReply){
		if(taskReplyService.save(taskReply)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:taskReply:edit")
	public R update( TaskReplyDO taskReply){
		taskReplyService.update(taskReply);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:taskReply:remove")
	public R remove( Integer id){
		if(taskReplyService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:taskReply:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		taskReplyService.batchRemove(ids);
		return R.ok();
	}
	
}
