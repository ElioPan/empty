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

import com.ev.custom.domain.UpkeepRecordDO;
import com.ev.custom.service.UpkeepRecordService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 保养记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-24 17:26:54
 */
 
@Controller
@RequestMapping("/custom/upkeepRecord")
public class UpkeepRecordController {
	@Autowired
	private UpkeepRecordService upkeepRecordService;
	
	@GetMapping()
	@RequiresPermissions("custom:upkeepRecord:upkeepRecord")
	String UpkeepRecord(){
	    return "custom/upkeepRecord/upkeepRecord";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:upkeepRecord:upkeepRecord")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UpkeepRecordDO> upkeepRecordList = upkeepRecordService.list(query);
		int total = upkeepRecordService.count(query);
		PageUtils pageUtils = new PageUtils(upkeepRecordList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:upkeepRecord:add")
	String add(){
	    return "custom/upkeepRecord/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:upkeepRecord:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UpkeepRecordDO upkeepRecord = upkeepRecordService.get(id);
		model.addAttribute("upkeepRecord", upkeepRecord);
	    return "custom/upkeepRecord/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:upkeepRecord:add")
	public R save( UpkeepRecordDO upkeepRecord){
		if(upkeepRecordService.save(upkeepRecord)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:upkeepRecord:edit")
	public R update( UpkeepRecordDO upkeepRecord){
		upkeepRecordService.update(upkeepRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepRecord:remove")
	public R remove( Long id){
		if(upkeepRecordService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepRecord:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		upkeepRecordService.batchRemove(ids);
		return R.ok();
	}
	
}
