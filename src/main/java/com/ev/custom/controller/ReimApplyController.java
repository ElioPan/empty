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

import com.ev.custom.domain.ReimApplyDO;
import com.ev.custom.service.ReimApplyService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 报销申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:37
 */
 
@Controller
@RequestMapping("/custom/reimApply")
public class ReimApplyController {
	@Autowired
	private ReimApplyService reimApplyService;
	
	@GetMapping()
	@RequiresPermissions("custom:reimApply:reimApply")
	String ReimApply(){
	    return "custom/reimApply/reimApply";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:reimApply:reimApply")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ReimApplyDO> reimApplyList = reimApplyService.list(query);
		int total = reimApplyService.count(query);
		PageUtils pageUtils = new PageUtils(reimApplyList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:reimApply:add")
	String add(){
	    return "custom/reimApply/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:reimApply:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ReimApplyDO reimApply = reimApplyService.get(id);
		model.addAttribute("reimApply", reimApply);
	    return "custom/reimApply/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:reimApply:add")
	public R save( ReimApplyDO reimApply){
		if(reimApplyService.save(reimApply)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:reimApply:edit")
	public R update( ReimApplyDO reimApply){
		reimApplyService.update(reimApply);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:reimApply:remove")
	public R remove( Long id){
		if(reimApplyService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:reimApply:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		reimApplyService.batchRemove(ids);
		return R.ok();
	}
	
}
