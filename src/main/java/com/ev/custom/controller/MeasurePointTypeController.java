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

import com.ev.custom.domain.MeasurePointTypeDO;
import com.ev.custom.service.MeasurePointTypeService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 17:21:32
 */
 
@Controller
@RequestMapping("/custom/measurePointType")
public class MeasurePointTypeController {
	@Autowired
	private MeasurePointTypeService measurePointTypeService;
	
	@GetMapping()
	@RequiresPermissions("custom:measurePointType:measurePointType")
	String MeasurePointType(){
	    return "custom/measurePointType/measurePointType";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:measurePointType:measurePointType")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<MeasurePointTypeDO> measurePointTypeList = measurePointTypeService.list(query);
		int total = measurePointTypeService.count(query);
		PageUtils pageUtils = new PageUtils(measurePointTypeList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:measurePointType:add")
	String add(){
	    return "custom/measurePointType/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:measurePointType:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		MeasurePointTypeDO measurePointType = measurePointTypeService.get(id);
		model.addAttribute("measurePointType", measurePointType);
	    return "custom/measurePointType/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:measurePointType:add")
	public R save( MeasurePointTypeDO measurePointType){
		if(measurePointTypeService.save(measurePointType)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:measurePointType:edit")
	public R update( MeasurePointTypeDO measurePointType){
		measurePointTypeService.update(measurePointType);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:measurePointType:remove")
	public R remove( Integer id){
		if(measurePointTypeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:measurePointType:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		measurePointTypeService.batchRemove(ids);
		return R.ok();
	}
	
}
