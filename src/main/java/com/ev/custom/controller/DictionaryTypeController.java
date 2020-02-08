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

import com.ev.custom.domain.DictionaryTypeDO;
import com.ev.custom.service.DictionaryTypeService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 字典类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 08:57:07
 */
 
@Controller
@RequestMapping("/custom/dictionaryType")
public class DictionaryTypeController {
	@Autowired
	private DictionaryTypeService dictionaryTypeService;
	
	@GetMapping()
	@RequiresPermissions("custom:dictionaryType:dictionaryType")
	String DictionaryType(){
	    return "custom/dictionaryType/dictionaryType";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:dictionaryType:dictionaryType")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DictionaryTypeDO> dictionaryTypeList = dictionaryTypeService.list(query);
		int total = dictionaryTypeService.count(query);
		PageUtils pageUtils = new PageUtils(dictionaryTypeList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:dictionaryType:add")
	String add(){
	    return "custom/dictionaryType/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:dictionaryType:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DictionaryTypeDO dictionaryType = dictionaryTypeService.get(id);
		model.addAttribute("dictionaryType", dictionaryType);
	    return "custom/dictionaryType/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:dictionaryType:add")
	public R save( DictionaryTypeDO dictionaryType){
		if(dictionaryTypeService.save(dictionaryType)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:dictionaryType:edit")
	public R update( DictionaryTypeDO dictionaryType){
		dictionaryTypeService.update(dictionaryType);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:dictionaryType:remove")
	public R remove( Long id){
		if(dictionaryTypeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:dictionaryType:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		dictionaryTypeService.batchRemove(ids);
		return R.ok();
	}
	
}
