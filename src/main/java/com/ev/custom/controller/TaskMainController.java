package com.ev.custom.controller;

import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import com.ev.system.domain.UserDO;
import com.ev.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-08 13:03:04
 */

@Transactional
@Controller
@RequestMapping("/custom/taskMain")
public class TaskMainController {
	@Autowired
	private TaskMainService taskMainService;

	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskEmployeeService taskEmployeeService;

	@GetMapping()
	@RequiresPermissions("custom:taskMain:taskMain")
	String TaskMain(){
	    return "custom/taskMain/taskMain";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:taskMain:taskMain")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TaskMainDO> taskMainList = taskMainService.list(query);
		int total = taskMainService.count(query);
		PageUtils pageUtils = new PageUtils(taskMainList, total);
		return pageUtils;
	}

	@ResponseBody
	@GetMapping("/listForMap")
	@RequiresPermissions("custom:taskMain:taskMain")
	public PageUtils listForMap(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String,Object>> taskMainList = taskMainService.listForMap(query);
		int total = taskMainService.countForMap(query);
		PageUtils pageUtils = new PageUtils(taskMainList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:taskMain:add")
	String add(Model model){
		List<DictionaryDO> taskTypeList = dictionaryService.listByType("task_type");
		model.addAttribute("taskTypeList",taskTypeList);
		List<DictionaryDO> taskLevelList = dictionaryService.listByType("task_level");
		model.addAttribute("taskLevelList",taskLevelList);
		List<UserDO> userList = userService.list(null);
		model.addAttribute("userList",userList);
	    return "custom/taskMain/add";
	}

	/**
	 * 任务单详情
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/detail/{id}")
	@RequiresPermissions("custom:taskMain:detail")
	String detail(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(taskMainService.detail(id));
		return "custom/taskMain/taskDetail";
	}

	/**
	 * 编辑任务单
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:taskMain:edit")
	String edit(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(taskMainService.detail(id));
	    return "custom/taskMain/edit";
	}

	/**
	 * 处理任务单
	 */
	@GetMapping("/deal/{id}")
	String deal(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(taskMainService.detail(id));
		return "custom/taskMain/deal";
	}

	/**
	 * 验收任务单
	 */
	@GetMapping("/check/{id}")
	String check(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(taskMainService.detail(id));
		return "custom/taskMain/check";
	}

	/**
	 * 保存任务单
	 */
/*	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:taskMain:add")
	public R save(TaskMainDO taskMain, Long[] ccList,Long heldPerson,String[] taglocationappearanceImage,String linkOrderNo,Integer linkOrderType,Integer linkStageType){
		try {
			taskMainService.add(taskMain,ccList,heldPerson,taglocationappearanceImage,linkOrderNo,linkOrderType,linkStageType);
			return R.ok();
		}catch (Exception ex){
			return R.error(ex.getMessage());
		}
	}*/
	/**
	 * 修改任务单
	 */
/*	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:taskMain:edit")
	public R update( TaskMainDO taskMain, Long[] ccList,Long heldPerson,String[] taglocationappearanceImage,String[] deletetag_appearanceImage){
		taskMainService.edit(taskMain,ccList,heldPerson,taglocationappearanceImage,deletetag_appearanceImage);
		return R.ok();
	}*/

	/**
	 * 处理任务单
	 */
	@ResponseBody
	@PostMapping("/dealSave")
	@RequiresPermissions("custom:taskMain:deal")
	public R dealSave(TaskReplyDO taskReplyDO){
		try {
			taskMainService.dealSave(taskReplyDO);
			return R.ok();
		}catch (Exception ex){
			return R.error(ex.getMessage());
		}
	}

	/**
	 * 验收任务单
	 */
	@ResponseBody
	@PostMapping("/checkSave")
	@RequiresPermissions("custom:taskMain:check")
	public R checkSave(TaskReplyDO taskReplyDO/* , Long[] ccList */){
		try {
			taskMainService.checkSave(taskReplyDO/* ,ccList */);
			return R.ok();
		}catch (Exception ex){
			return R.error(ex.getMessage());
		}
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:taskMain:remove")
	public R remove( Long id){
		if(taskMainService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:taskMain:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		taskMainService.batchRemove(ids);
		return R.ok();
	}
}
