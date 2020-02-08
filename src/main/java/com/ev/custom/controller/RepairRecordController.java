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

import com.ev.custom.domain.RepairRecordDO;
import com.ev.custom.service.RepairRecordService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 维修记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
 
@Controller
@RequestMapping("/custom/repairRecord")
public class RepairRecordController {
	@Autowired
	private RepairRecordService repairRecordService;
	
	@GetMapping()
	@RequiresPermissions("custom:repairRecord:repairRecord")
	String RepairRecord(){
	    return "custom/repairRecord/repairRecord";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:repairRecord:repairRecord")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<RepairRecordDO> repairRecordList = repairRecordService.list(query);
		int total = repairRecordService.count(query);
		PageUtils pageUtils = new PageUtils(repairRecordList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:repairRecord:add")
	String add(){
	    return "custom/repairRecord/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:repairRecord:edit")
	String edit(@PathVariable("id") Long id,Model model){
		RepairRecordDO repairRecord = repairRecordService.get(id);
		model.addAttribute("repairRecord", repairRecord);
	    return "custom/repairRecord/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:repairRecord:add")
	public R save( RepairRecordDO repairRecord){
		if(repairRecordService.save(repairRecord)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:repairRecord:edit")
	public R update( RepairRecordDO repairRecord){
		repairRecordService.update(repairRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:repairRecord:remove")
	public R remove( Long id){
		if(repairRecordService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:repairRecord:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		repairRecordService.batchRemove(ids);
		return R.ok();
	}
	
}
