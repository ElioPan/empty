package com.ev.custom.controller;

import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;
import com.ev.custom.domain.FacilityDO;
import com.ev.custom.domain.FacilityLocationDO;
import com.ev.custom.service.FacilityLocationService;
import com.ev.custom.service.FacilityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
 
@Controller
@RequestMapping("/custom/facilityLocation")
public class FacilityLocationController {
	@Autowired
	private FacilityLocationService facilityLocationService;

	@Autowired
	private FacilityService facilityService;
	
	@GetMapping()
	@RequiresPermissions("custom:facilityLocation:facilityLocation")
	String FacilityLocation(){
	    return "custom/facilityLocation/facilityLocation";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:facilityLocation:facilityLocation")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> facilityLocationList = facilityLocationService.listForMap(query);
		int total = facilityLocationService.count(query);
		PageUtils pageUtils = new PageUtils(facilityLocationList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:facilityLocation:add")
	String add(Model model){
		List<FacilityDO> facilityList = facilityService.list(null);
		model.addAttribute("facilityList",facilityList);
	    return "custom/facilityLocation/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:facilityLocation:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		FacilityLocationDO facilityLocation = facilityLocationService.get(id);
		model.addAttribute("facilityLocation", facilityLocation);
		List<FacilityDO> facilityList = facilityService.list(null);
		model.addAttribute("facilityList",facilityList);
	    return "custom/facilityLocation/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:facilityLocation:add")
	public R save( FacilityLocationDO facilityLocation){
		if(facilityLocationService.save(facilityLocation)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:facilityLocation:edit")
	public R update( FacilityLocationDO facilityLocation){
		facilityLocationService.update(facilityLocation);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:facilityLocation:remove")
	public R remove( Integer id){
		if(facilityLocationService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:facilityLocation:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		facilityLocationService.batchRemove(ids);
		return R.ok();
	}
	
}
