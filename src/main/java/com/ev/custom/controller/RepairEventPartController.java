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

import com.ev.custom.domain.RepairEventPartDO;
import com.ev.custom.service.RepairEventPartService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 修维事件与备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
 
@Controller
@RequestMapping("/custom/repairEventPart")
public class RepairEventPartController {
	@Autowired
	private RepairEventPartService repairEventPartService;
	
	@GetMapping()
	@RequiresPermissions("custom:repairEventPart:repairEventPart")
	String RepairEventPart(){
	    return "custom/repairEventPart/repairEventPart";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:repairEventPart:repairEventPart")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<RepairEventPartDO> repairEventPartList = repairEventPartService.list(query);
		int total = repairEventPartService.count(query);
		PageUtils pageUtils = new PageUtils(repairEventPartList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:repairEventPart:add")
	String add(){
	    return "custom/repairEventPart/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:repairEventPart:edit")
	String edit(@PathVariable("id") Long id,Model model){
		RepairEventPartDO repairEventPart = repairEventPartService.get(id);
		model.addAttribute("repairEventPart", repairEventPart);
	    return "custom/repairEventPart/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:repairEventPart:add")
	public R save( RepairEventPartDO repairEventPart){
		if(repairEventPartService.save(repairEventPart)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:repairEventPart:edit")
	public R update( RepairEventPartDO repairEventPart){
		repairEventPartService.update(repairEventPart);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:repairEventPart:remove")
	public R remove( Long id){
		if(repairEventPartService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:repairEventPart:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		repairEventPartService.batchRemove(ids);
		return R.ok();
	}
	
}
