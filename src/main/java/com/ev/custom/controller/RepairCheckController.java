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

import com.ev.custom.domain.RepairCheckDO;
import com.ev.custom.service.RepairCheckService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 维修验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:36
 */
 
@Controller
@RequestMapping("/custom/repairCheck")
public class RepairCheckController {
	@Autowired
	private RepairCheckService repairCheckService;
	
	@GetMapping()
	@RequiresPermissions("custom:repairCheck:repairCheck")
	String RepairCheck(){
	    return "custom/repairCheck/repairCheck";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:repairCheck:repairCheck")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<RepairCheckDO> repairCheckList = repairCheckService.list(query);
		int total = repairCheckService.count(query);
		PageUtils pageUtils = new PageUtils(repairCheckList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:repairCheck:add")
	String add(){
	    return "custom/repairCheck/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:repairCheck:edit")
	String edit(@PathVariable("id") Long id,Model model){
		RepairCheckDO repairCheck = repairCheckService.get(id);
		model.addAttribute("repairCheck", repairCheck);
	    return "custom/repairCheck/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:repairCheck:add")
	public R save( RepairCheckDO repairCheck){
		if(repairCheckService.save(repairCheck)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:repairCheck:edit")
	public R update( RepairCheckDO repairCheck){
		repairCheckService.update(repairCheck);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:repairCheck:remove")
	public R remove( Long id){
		if(repairCheckService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:repairCheck:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		repairCheckService.batchRemove(ids);
		return R.ok();
	}
	
}
