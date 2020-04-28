package com.ev.custom.controller;

import java.util.List;
import java.util.Map;

import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
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

import com.ev.custom.domain.FacilityDO;
import com.ev.custom.service.FacilityService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 仓库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
 
@Controller
@RequestMapping("/custom/facility")
public class FacilityController {
	@Autowired
	private FacilityService facilityService;

	@Autowired
	private DictionaryService dictionaryService;
	
	@GetMapping()
	@RequiresPermissions("custom:facility:facility")
	String Facility(){
	    return "custom/facility/facility";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:facility:facility")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> facilityList = facilityService.listForMap(query);
		int total = facilityService.count(query);
		PageUtils pageUtils = new PageUtils(facilityList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:facility:add")
	String add(Model model){
		List<DictionaryDO> facTypeList = dictionaryService.listByType("fac_type");
		model.addAttribute("fac_type", facTypeList);
	    return "custom/facility/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:facility:edit")
	String edit(@PathVariable("id") Long id,Model model){
		FacilityDO facility = facilityService.get(id);
		model.addAttribute("facility", facility);
		List<DictionaryDO> facTypeList = dictionaryService.listByType("fac_type");
		model.addAttribute("fac_type", facTypeList);
	    return "custom/facility/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:facility:add")
	public R save( FacilityDO facility){
		if(facilityService.save(facility)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:facility:edit")
	public R update( FacilityDO facility){
		facilityService.update(facility);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:facility:remove")
	public R remove( Long id){
		if(facilityService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:facility:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		facilityService.batchRemove(ids);
		return R.ok();
	}
	
}
