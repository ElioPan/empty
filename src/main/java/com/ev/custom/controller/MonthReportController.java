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

import com.ev.custom.domain.MonthReportDO;
import com.ev.custom.service.MonthReportService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 月报
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
 
@Controller
@RequestMapping("/custom/monthReport")
public class MonthReportController {
	@Autowired
	private MonthReportService monthReportService;
	
	@GetMapping()
	@RequiresPermissions("custom:monthReport:monthReport")
	String MonthReport(){
	    return "custom/monthReport/monthReport";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:monthReport:monthReport")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<MonthReportDO> monthReportList = monthReportService.list(query);
		int total = monthReportService.count(query);
		PageUtils pageUtils = new PageUtils(monthReportList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:monthReport:add")
	String add(){
	    return "custom/monthReport/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:monthReport:edit")
	String edit(@PathVariable("id") Long id,Model model){
		MonthReportDO monthReport = monthReportService.get(id);
		model.addAttribute("monthReport", monthReport);
	    return "custom/monthReport/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:monthReport:add")
	public R save( MonthReportDO monthReport){
		if(monthReportService.save(monthReport)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:monthReport:edit")
	public R update( MonthReportDO monthReport){
		monthReportService.update(monthReport);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:monthReport:remove")
	public R remove( Long id){
		if(monthReportService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:monthReport:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		monthReportService.batchRemove(ids);
		return R.ok();
	}
	
}
