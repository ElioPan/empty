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

import com.ev.custom.domain.RepairEventDO;
import com.ev.custom.service.RepairEventService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 维修事件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
 
@Controller
@RequestMapping("/custom/repairEvent")
public class RepairEventController {
	@Autowired
	private RepairEventService repairEventService;
	
	@GetMapping()
	@RequiresPermissions("custom:repairEvent:repairEvent")
	String RepairEvent(){
	    return "custom/repairEvent/repairEvent";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:repairEvent:repairEvent")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<RepairEventDO> repairEventList = repairEventService.list(query);
		int total = repairEventService.count(query);
		PageUtils pageUtils = new PageUtils(repairEventList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:repairEvent:add")
	String add(){
	    return "custom/repairEvent/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:repairEvent:edit")
	String edit(@PathVariable("id") Long id,Model model){
		RepairEventDO repairEvent = repairEventService.get(id);
		model.addAttribute("repairEvent", repairEvent);
	    return "custom/repairEvent/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:repairEvent:add")
	public R save( RepairEventDO repairEvent){
		if(repairEventService.save(repairEvent)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:repairEvent:edit")
	public R update( RepairEventDO repairEvent){
		repairEventService.update(repairEvent);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:repairEvent:remove")
	public R remove( Long id){
		if(repairEventService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:repairEvent:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		repairEventService.batchRemove(ids);
		return R.ok();
	}
	
}
