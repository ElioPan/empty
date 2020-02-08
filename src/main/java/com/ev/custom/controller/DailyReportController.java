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

import com.ev.custom.domain.DailyReportDO;
import com.ev.custom.service.DailyReportService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 日报
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
 
@Controller
@RequestMapping("/custom/dailyReport")
public class DailyReportController {
	@Autowired
	private DailyReportService dailyReportService;
	
	@GetMapping()
	@RequiresPermissions("custom:dailyReport:dailyReport")
	String DailyReport(){
	    return "custom/dailyReport/dailyReport";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:dailyReport:dailyReport")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DailyReportDO> dailyReportList = dailyReportService.list(query);
		int total = dailyReportService.count(query);
		PageUtils pageUtils = new PageUtils(dailyReportList, total);
		return pageUtils;
	}

	@ResponseBody
	@GetMapping("/listForMap")
	@RequiresPermissions("custom:taskMain:taskMain")
	public PageUtils listForMap(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String,Object>> taskMainList = dailyReportService.listForMap(query);
		int total = dailyReportService.countForMap(query);
		PageUtils pageUtils = new PageUtils(taskMainList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:dailyReport:add")
	String add(){
	    return "custom/dailyReport/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:dailyReport:edit")
	String edit(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(dailyReportService.detail(id));
	    return "custom/dailyReport/edit";
	}

	/**
	 * 日志详情
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/detail/{id}")
	@RequiresPermissions("custom:dailyReport:detail")
	String detail(@PathVariable("id") Long id,Model model){
		model.addAllAttributes(dailyReportService.detail(id));
		return "custom/dailyReport/detail";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:dailyReport:add")
	public R save( DailyReportDO dailyReport, Long[] targetList, String[] taglocationappearanceImage){
		try {
			dailyReportService.add(dailyReport,targetList,taglocationappearanceImage);
			return R.ok();
		}catch (Exception ex){
			return R.error(ex.getMessage());
		}
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:dailyReport:edit")
	public R update( DailyReportDO dailyReport ,Long[] targetList, String[] taglocationappearanceImage,String[] deletetag_appearanceImage){
		dailyReportService.edit(dailyReport,targetList,taglocationappearanceImage,deletetag_appearanceImage);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:dailyReport:remove")
	public R remove( Long id){
		if(dailyReportService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:dailyReport:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		dailyReportService.batchRemove(ids);
		return R.ok();
	}
	
}
