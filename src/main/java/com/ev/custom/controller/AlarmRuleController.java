package com.ev.custom.controller;

import com.ev.custom.domain.AlarmRuleDO;
import com.ev.custom.service.AlarmRuleService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 报警规则
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:22:16
 */
 
@Controller
@RequestMapping("/custom/alarmRule")
public class AlarmRuleController {
	@Autowired
	private AlarmRuleService alarmRuleService;
	
	@GetMapping()
	@RequiresPermissions("custom:alarmRule:alarmRule")
	String AlarmRule(){
	    return "custom/alarmRule/alarmRule";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:alarmRule:alarmRule")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<AlarmRuleDO> alarmRuleList = alarmRuleService.list(query);
		int total = alarmRuleService.count(query);
		PageUtils pageUtils = new PageUtils(alarmRuleList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:alarmRule:add")
	String add(){
	    return "custom/alarmRule/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:alarmRule:edit")
	String edit(@PathVariable("id") Long id,Model model){
		AlarmRuleDO alarmRule = alarmRuleService.get(id);
		model.addAttribute("alarmRule", alarmRule);
	    return "custom/alarmRule/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:alarmRule:add")
	public R save( AlarmRuleDO alarmRule){
		if(alarmRuleService.save(alarmRule)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:alarmRule:edit")
	public R update( AlarmRuleDO alarmRule){
		alarmRuleService.update(alarmRule);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:alarmRule:remove")
	public R remove( Long id){
		if(alarmRuleService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:alarmRule:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		alarmRuleService.batchRemove(ids);
		return R.ok();
	}
	
}
