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

import com.ev.custom.domain.PatrolDetailDO;
import com.ev.custom.service.PatrolDetailService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 巡检明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:37
 */
 
@Controller
@RequestMapping("/custom/patrolDetail")
public class PatrolDetailController {
	@Autowired
	private PatrolDetailService patrolDetailService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolDetail:patrolDetail")
	String PatrolDetail(){
	    return "custom/patrolDetail/patrolDetail";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolDetail:patrolDetail")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolDetailDO> patrolDetailList = patrolDetailService.list(query);
		int total = patrolDetailService.count(query);
		PageUtils pageUtils = new PageUtils(patrolDetailList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolDetail:add")
	String add(){
	    return "custom/patrolDetail/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolDetail:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolDetailDO patrolDetail = patrolDetailService.get(id);
		model.addAttribute("patrolDetail", patrolDetail);
	    return "custom/patrolDetail/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolDetail:add")
	public R save( PatrolDetailDO patrolDetail){
		if(patrolDetailService.save(patrolDetail)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolDetail:edit")
	public R update( PatrolDetailDO patrolDetail){
		patrolDetailService.update(patrolDetail);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolDetail:remove")
	public R remove( Long id){
		if(patrolDetailService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolDetail:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		patrolDetailService.batchRemove(ids);
		return R.ok();
	}
	
}
