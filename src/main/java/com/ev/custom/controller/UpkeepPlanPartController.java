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

import com.ev.custom.domain.UpkeepPlanPartDO;
import com.ev.custom.service.UpkeepPlanPartService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 保养计划与备件中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
 
@Controller
@RequestMapping("/custom/upkeepPlanPart")
public class UpkeepPlanPartController {
	@Autowired
	private UpkeepPlanPartService upkeepPlanPartService;
	
	@GetMapping()
	@RequiresPermissions("custom:upkeepPlanPart:upkeepPlanPart")
	String UpkeepPlanPart(){
	    return "custom/upkeepPlanPart/upkeepPlanPart";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:upkeepPlanPart:upkeepPlanPart")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UpkeepPlanPartDO> upkeepPlanPartList = upkeepPlanPartService.list(query);
		int total = upkeepPlanPartService.count(query);
		PageUtils pageUtils = new PageUtils(upkeepPlanPartList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:upkeepPlanPart:add")
	String add(){
	    return "custom/upkeepPlanPart/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:upkeepPlanPart:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UpkeepPlanPartDO upkeepPlanPart = upkeepPlanPartService.get(id);
		model.addAttribute("upkeepPlanPart", upkeepPlanPart);
	    return "custom/upkeepPlanPart/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:upkeepPlanPart:add")
	public R save( UpkeepPlanPartDO upkeepPlanPart){
		if(upkeepPlanPartService.save(upkeepPlanPart)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:upkeepPlanPart:edit")
	public R update( UpkeepPlanPartDO upkeepPlanPart){
		upkeepPlanPartService.update(upkeepPlanPart);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepPlanPart:remove")
	public R remove( Long id){
		if(upkeepPlanPartService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepPlanPart:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		upkeepPlanPartService.batchRemove(ids);
		return R.ok();
	}
	
}
