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

import com.ev.custom.domain.WeekReportDO;
import com.ev.custom.service.WeekReportService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 周报
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
 
@Controller
@RequestMapping("/custom/weekReport")
public class WeekReportController {
	@Autowired
	private WeekReportService weekReportService;
	
	@GetMapping()
	@RequiresPermissions("custom:weekReport:weekReport")
	String WeekReport(){
	    return "custom/weekReport/weekReport";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:weekReport:weekReport")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<WeekReportDO> weekReportList = weekReportService.list(query);
		int total = weekReportService.count(query);
		PageUtils pageUtils = new PageUtils(weekReportList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:weekReport:add")
	String add(){
	    return "custom/weekReport/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:weekReport:edit")
	String edit(@PathVariable("id") Long id,Model model){
		WeekReportDO weekReport = weekReportService.get(id);
		model.addAttribute("weekReport", weekReport);
	    return "custom/weekReport/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:weekReport:add")
	public R save( WeekReportDO weekReport){
		if(weekReportService.save(weekReport)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:weekReport:edit")
	public R update( WeekReportDO weekReport){
		weekReportService.update(weekReport);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:weekReport:remove")
	public R remove( Long id){
		if(weekReportService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:weekReport:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		weekReportService.batchRemove(ids);
		return R.ok();
	}
	
}
