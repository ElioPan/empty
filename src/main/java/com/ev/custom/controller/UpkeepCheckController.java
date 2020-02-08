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

import com.ev.custom.domain.UpkeepCheckDO;
import com.ev.custom.service.UpkeepCheckService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 养保验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 17:06:42
 */
 
@Controller
@RequestMapping("/custom/upkeepCheck")
public class UpkeepCheckController {
	@Autowired
	private UpkeepCheckService upkeepCheckService;
	
	@GetMapping()
	@RequiresPermissions("custom:upkeepCheck:upkeepCheck")
	String UpkeepCheck(){
	    return "custom/upkeepCheck/upkeepCheck";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:upkeepCheck:upkeepCheck")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UpkeepCheckDO> upkeepCheckList = upkeepCheckService.list(query);
		int total = upkeepCheckService.count(query);
		PageUtils pageUtils = new PageUtils(upkeepCheckList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:upkeepCheck:add")
	String add(){
	    return "custom/upkeepCheck/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:upkeepCheck:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UpkeepCheckDO upkeepCheck = upkeepCheckService.get(id);
		model.addAttribute("upkeepCheck", upkeepCheck);
	    return "custom/upkeepCheck/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:upkeepCheck:add")
	public R save( UpkeepCheckDO upkeepCheck){
		if(upkeepCheckService.save(upkeepCheck)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:upkeepCheck:edit")
	public R update( UpkeepCheckDO upkeepCheck){
		upkeepCheckService.update(upkeepCheck);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepCheck:remove")
	public R remove( Long id){
		if(upkeepCheckService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepCheck:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		upkeepCheckService.batchRemove(ids);
		return R.ok();
	}
	
}
