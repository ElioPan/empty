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

import com.ev.custom.domain.PatrolRecordDO;
import com.ev.custom.service.PatrolRecordService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 巡检记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
 
@Controller
@RequestMapping("/custom/patrolRecord")
public class PatrolRecordController {
	@Autowired
	private PatrolRecordService patrolRecordService;
	
	@GetMapping()
	@RequiresPermissions("custom:patrolRecord:patrolRecord")
	String PatrolRecord(){
	    return "custom/patrolRecord/patrolRecord";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:patrolRecord:patrolRecord")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PatrolRecordDO> patrolRecordList = patrolRecordService.list(query);
		int total = patrolRecordService.count(query);
		PageUtils pageUtils = new PageUtils(patrolRecordList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:patrolRecord:add")
	String add(){
	    return "custom/patrolRecord/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:patrolRecord:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PatrolRecordDO patrolRecord = patrolRecordService.get(id);
		model.addAttribute("patrolRecord", patrolRecord);
	    return "custom/patrolRecord/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:patrolRecord:add")
	public R save( PatrolRecordDO patrolRecord){
		if(patrolRecordService.save(patrolRecord)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:patrolRecord:edit")
	public R update( PatrolRecordDO patrolRecord){
		patrolRecordService.update(patrolRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:patrolRecord:remove")
	public R remove( Long id){
		if(patrolRecordService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:patrolRecord:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		patrolRecordService.batchRemove(ids);
		return R.ok();
	}
	
}
