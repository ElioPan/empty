package com.ev.custom.controller;


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

import com.ev.custom.domain.MaterielTypeDO;
import com.ev.custom.service.MaterielTypeService;
import com.ev.framework.utils.R;

/**
 * 物料类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-08 14:33:15
 */
 
@Controller
@RequestMapping("/custom/materielType")
public class MaterielTypeController {
	@Autowired
	private MaterielTypeService materielTypeService;
	
	@GetMapping()
	@RequiresPermissions("custom:materielType:materielType")
	String MaterielType(){
	    return "custom/materielType/materielType";
	}
	
	
	@GetMapping("/add")
	@RequiresPermissions("custom:materielType:add")
	String add(){
	    return "custom/materielType/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:materielType:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		MaterielTypeDO materielType = materielTypeService.get(id);
		model.addAttribute("materielType", materielType);
	    return "custom/materielType/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:materielType:add")
	public R save( MaterielTypeDO materielType){
		if(materielTypeService.save(materielType)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:materielType:edit")
	public R update( MaterielTypeDO materielType){
		materielTypeService.update(materielType);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:materielType:remove")
	public R remove( Integer id){
		if(materielTypeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:materielType:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		materielTypeService.batchRemove(ids);
		return R.ok();
	}
	
}
