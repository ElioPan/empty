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

import com.ev.custom.domain.QualityReportDO;
import com.ev.custom.service.QualityReportService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-16 13:15:40
 */
 
@Controller
@RequestMapping("/custom/qualityReport")
public class QualityReportController {
	@Autowired
	private QualityReportService qualityReportService;
	
	@GetMapping()
	@RequiresPermissions("custom:qualityReport:qualityReport")
	String QualityReport(){
	    return "custom/qualityReport/qualityReport";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:qualityReport:qualityReport")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<QualityReportDO> qualityReportList = qualityReportService.list(query);
		int total = qualityReportService.count(query);
		PageUtils pageUtils = new PageUtils(qualityReportList, total);
		return pageUtils;
	}
	
	
	@ResponseBody
	@GetMapping("/listForMap")
	@RequiresPermissions("custom:qualityReport:qualityReport")
	public PageUtils listForMap(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String,Object>> qualityReportList = qualityReportService.listForMap(query);
		int total = qualityReportService.countForMap(query);
		PageUtils pageUtils = new PageUtils(qualityReportList, total);
		return pageUtils;
	}
	
	/**
	 * 任务单详情
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/detail/{id}")
	@RequiresPermissions("custom:qualityReport:detail")
	String detail(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(qualityReportService.detail(id));
		return "custom/qualityReport/qualityDetail";
	}
	
	
	@GetMapping("/add")
	@RequiresPermissions("custom:qualityReport:add")
	String add(){
	    return "custom/qualityReport/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:qualityReport:edit")
	String edit(@PathVariable("id") Long id,Model model){
		QualityReportDO qualityReport = qualityReportService.get(id);
		model.addAttribute("qualityReport", qualityReport);
	    return "custom/qualityReport/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:qualityReport:add")
	public R save( QualityReportDO qualityReport){
		if(qualityReportService.save(qualityReport)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:qualityReport:edit")
	public R update( QualityReportDO qualityReport){
		qualityReportService.update(qualityReport);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:qualityReport:remove")
	public R remove( Long id){
		if(qualityReportService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:qualityReport:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		qualityReportService.batchRemove(ids);
		return R.ok();
	}
	
}
