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

import com.ev.custom.domain.ReportItemDO;
import com.ev.custom.service.ReportItemService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 报告明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-19 13:48:45
 */
 
@Controller
@RequestMapping("/custom/reportItem")
public class ReportItemController {
	@Autowired
	private ReportItemService reportItemService;
	
	@GetMapping()
	@RequiresPermissions("custom:reportItem:reportItem")
	String ReportItem(){
	    return "custom/reportItem/reportItem";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:reportItem:reportItem")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ReportItemDO> reportItemList = reportItemService.list(query);
		int total = reportItemService.count(query);
		PageUtils pageUtils = new PageUtils(reportItemList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:reportItem:add")
	String add(){
	    return "custom/reportItem/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:reportItem:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ReportItemDO reportItem = reportItemService.get(id);
		model.addAttribute("reportItem", reportItem);
	    return "custom/reportItem/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:reportItem:add")
	public R save( ReportItemDO reportItem){
		if(reportItemService.save(reportItem)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:reportItem:edit")
	public R update( ReportItemDO reportItem){
		reportItemService.update(reportItem);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:reportItem:remove")
	public R remove( Long id){
		if(reportItemService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:reportItem:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		reportItemService.batchRemove(ids);
		return R.ok();
	}
	
}
