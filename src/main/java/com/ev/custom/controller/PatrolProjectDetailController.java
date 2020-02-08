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

import com.ev.custom.domain.PatrolProjectDetailDO;
import com.ev.custom.service.PatrolProjectDetailService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 巡检标准明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 15:22:51
 */
 
@Controller
@RequestMapping("/custom/patrolProjectDetail")
public class PatrolProjectDetailController {
	@Autowired
	private PatrolProjectDetailService patrolProjectDetailService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolProjectDetail:patrolProjectDetail")
	String PatrolProjectDetail(){
	    return "custom/patrolProjectDetail/patrolProjectDetail";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolProjectDetail:patrolProjectDetail")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolProjectDetailDO> patrolProjectDetailList = patrolProjectDetailService.list(query);
		int total = patrolProjectDetailService.count(query);
		PageUtils pageUtils = new PageUtils(patrolProjectDetailList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolProjectDetail:add")
	String add(){
	    return "custom/patrolProjectDetail/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolProjectDetail:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolProjectDetailDO patrolProjectDetail = patrolProjectDetailService.get(id);
		model.addAttribute("patrolProjectDetail", patrolProjectDetail);
	    return "custom/patrolProjectDetail/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolProjectDetail:add")
	public R save( PatrolProjectDetailDO patrolProjectDetail){
		if(patrolProjectDetailService.save(patrolProjectDetail)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolProjectDetail:edit")
	public R update( PatrolProjectDetailDO patrolProjectDetail){
		patrolProjectDetailService.update(patrolProjectDetail);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolProjectDetail:remove")
	public R remove( Long id){
		if(patrolProjectDetailService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolProjectDetail:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		patrolProjectDetailService.batchRemove(ids);
		return R.ok();
	}
	
}
