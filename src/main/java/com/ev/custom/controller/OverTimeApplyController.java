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

import com.ev.custom.domain.OverTimeApplyDO;
import com.ev.custom.service.OverTimeApplyService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 加班申请管理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 08:55:04
 */
 
@Controller
@RequestMapping("/custom/overTimeApply")
public class OverTimeApplyController {
	@Autowired
	private OverTimeApplyService overTimeApplyService;
	
	@GetMapping()
	@RequiresPermissions("custom:overTimeApply:overTimeApply")
	String OverTimeApply(){
	    return "custom/overTimeApply/overTimeApply";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:overTimeApply:overTimeApply")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<OverTimeApplyDO> overTimeApplyList = overTimeApplyService.list(query);
		int total = overTimeApplyService.count(query);
		PageUtils pageUtils = new PageUtils(overTimeApplyList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:overTimeApply:add")
	String add(){
	    return "custom/overTimeApply/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:overTimeApply:edit")
	String edit(@PathVariable("id") Long id,Model model){
		OverTimeApplyDO overTimeApply = overTimeApplyService.get(id);
		model.addAttribute("overTimeApply", overTimeApply);
	    return "custom/overTimeApply/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:overTimeApply:add")
	public R save( OverTimeApplyDO overTimeApply){
		if(overTimeApplyService.save(overTimeApply)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:overTimeApply:edit")
	public R update( OverTimeApplyDO overTimeApply){
		overTimeApplyService.update(overTimeApply);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:overTimeApply:remove")
	public R remove( Long id){
		if(overTimeApplyService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:overTimeApply:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		overTimeApplyService.batchRemove(ids);
		return R.ok();
	}
	
}
