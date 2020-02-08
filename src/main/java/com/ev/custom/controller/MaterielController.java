package com.ev.custom.controller;

import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;
import com.ev.custom.domain.*;
import com.ev.custom.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 物料
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
 
@Controller
@RequestMapping("/custom/materiel")
public class MaterielController {
	@Autowired
	private MaterielService materielService;
	@Autowired
	private DictionaryService dictionaryService;

	@Autowired
	private FacilityService facilityService;

	@Autowired
	private FacilityLocationService facilityLocationService;

	@Autowired
	private UomService uomService;

	
	@GetMapping()
	@RequiresPermissions("custom:materiel:materiel")
	String Materiel(){
	    return "custom/materiel/materiel";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:materiel:materiel")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> materielList = materielService.listForMap(query);
		int total = materielService.count(query);
		PageUtils pageUtils = new PageUtils(materielList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:materiel:add")
	String add(Model model){
		List<FacilityLocationDO> defaultLocationList = facilityLocationService.list(null);
		List<FacilityDO> defaultFacilityList = facilityService.list(null);
		List<UomDO> unitUomList = uomService.list(null);
		List<DictionaryDO> materialTypeList = dictionaryService.listByType("material_type");
		model.addAttribute("defaultLocationList",defaultLocationList);
		model.addAttribute("defaultFacilityList",defaultFacilityList);
		model.addAttribute("unitUomList",unitUomList);
		model.addAttribute("materialTypeList",materialTypeList);
	    return "custom/materiel/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:materiel:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		MaterielDO materiel = materielService.get(id);
		model.addAttribute("materiel", materiel);
		List<FacilityLocationDO> defaultLocationList = facilityLocationService.list(null);
		List<FacilityDO> defaultFacilityList = facilityService.list(null);
		List<UomDO> unitUomList = uomService.list(null);
		List<DictionaryDO> materialTypeList = dictionaryService.listByType("material_type");
		model.addAttribute("defaultLocationList",defaultLocationList);
		model.addAttribute("defaultFacilityList",defaultFacilityList);
		model.addAttribute("unitUomList",unitUomList);
		model.addAttribute("materialTypeList",materialTypeList);
	    return "custom/materiel/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:materiel:add")
	public R save( MaterielDO materiel){
		if(materielService.save(materiel)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:materiel:edit")
	public R update( MaterielDO materiel){
		materielService.update(materiel);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:materiel:remove")
	public R remove( Integer id){
		if(materielService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:materiel:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		materielService.batchRemove(ids);
		return R.ok();
	}
	
}
