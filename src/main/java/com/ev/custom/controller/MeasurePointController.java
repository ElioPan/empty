package com.ev.custom.controller;

import java.util.List;
import java.util.Map;

import com.ev.custom.domain.MeasurePointDO;
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

import com.ev.custom.service.MeasurePointService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:20:41
 */
 
@Controller
@RequestMapping("/custom/measurePoint")
public class MeasurePointController {
	@Autowired
	private MeasurePointService measurePointService;
	
	@GetMapping()
	@RequiresPermissions("custom:measurePoint:measurePoint")
	String MeasurePoint(){
	    return "custom/measurePoint/measurePoint";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:measurePoint:measurePoint")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<MeasurePointDO> measurePointList = measurePointService.list(query);
		int total = measurePointService.count(query);
		PageUtils pageUtils = new PageUtils(measurePointList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:measurePoint:add")
	String add(){
	    return "custom/measurePoint/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:measurePoint:edit")
	String edit(@PathVariable("id") Long id,Model model){
		MeasurePointDO measurePoint = measurePointService.get(id);
		model.addAttribute("measurePoint", measurePoint);
	    return "custom/measurePoint/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:measurePoint:add")
	public R save( MeasurePointDO measurePoint){
		if(measurePointService.save(measurePoint)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:measurePoint:edit")
	public R update( MeasurePointDO measurePoint){
		measurePointService.update(measurePoint);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:measurePoint:remove")
	public R remove( Long id){
		if(measurePointService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:measurePoint:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		measurePointService.batchRemove(ids);
		return R.ok();
	}
	
}
