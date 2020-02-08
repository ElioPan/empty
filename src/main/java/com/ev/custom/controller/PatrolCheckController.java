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

import com.ev.custom.domain.PatrolCheckDO;
import com.ev.custom.service.PatrolCheckService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 巡检验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-30 16:39:24
 */
 
@Controller
@RequestMapping("/custom/patrolCheck")
public class PatrolCheckController {
	@Autowired
	private PatrolCheckService patrolCheckService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolCheck:patrolCheck")
	String PatrolCheck(){
	    return "custom/patrolCheck/patrolCheck";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolCheck:patrolCheck")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolCheckDO> patrolCheckList = patrolCheckService.list(query);
		int total = patrolCheckService.count(query);
		PageUtils pageUtils = new PageUtils(patrolCheckList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolCheck:add")
	String add(){
	    return "custom/patrolCheck/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolCheck:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolCheckDO patrolCheck = patrolCheckService.get(id);
		model.addAttribute("patrolCheck", patrolCheck);
	    return "custom/patrolCheck/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolCheck:add")
	public R save( PatrolCheckDO patrolCheck){
		if(patrolCheckService.save(patrolCheck)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolCheck:edit")
	public R update( PatrolCheckDO patrolCheck){
		patrolCheckService.update(patrolCheck);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolCheck:remove")
	public R remove( Long id){
		if(patrolCheckService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolCheck:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		patrolCheckService.batchRemove(ids);
		return R.ok();
	}
	
}
