package com.ev.custom.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ev.custom.domain.LeaveApplyDO;
import com.ev.custom.service.LeaveApplyService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 请假管理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 15:18:26
 */
 
@Controller
@RequestMapping("/custom/leaveApply")
public class LeaveApplyController {
	@Autowired
	private LeaveApplyService leaveApplyService;
	
	@GetMapping()
	@RequiresPermissions("custom:leaveApply:leaveApply")
	String LeaveApply(){
	    return "custom/leaveApply/leaveApply";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:leaveApply:leaveApply")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<LeaveApplyDO> leaveApplyList = leaveApplyService.list(query);
		int total = leaveApplyService.count(query);
		PageUtils pageUtils = new PageUtils(leaveApplyList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:leaveApply:add")
	String add(){
	    return "custom/leaveApply/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:leaveApply:edit")
	String edit(@PathVariable("id") Long id,Model model){
		LeaveApplyDO leaveApply = leaveApplyService.get(id);
		model.addAttribute("leaveApply", leaveApply);
	    return "custom/leaveApply/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:leaveApply:add")
	public R save( LeaveApplyDO leaveApply){
		if(leaveApplyService.save(leaveApply)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:leaveApply:edit")
	public R update( LeaveApplyDO leaveApply){
		leaveApplyService.update(leaveApply);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:leaveApply:remove")
	public R remove( Long id){
		if(leaveApplyService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:leaveApply:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		leaveApplyService.batchRemove(ids);
		return R.ok();
	}
	
}
