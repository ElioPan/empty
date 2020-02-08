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

import com.ev.custom.domain.UomDO;
import com.ev.custom.service.UomService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 单位
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
 
@Controller
@RequestMapping("/custom/uom")
public class UomController {
	@Autowired
	private UomService uomService;

	@Autowired
	private DictionaryService dictionaryService;
	
	@GetMapping()
	@RequiresPermissions("custom:uom:uom")
	String Uom(){
	    return "custom/uom/uom";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:uom:uom")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> uomList = uomService.listForMap(query);
		int total = uomService.count(query);
		PageUtils pageUtils = new PageUtils(uomList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:uom:add")
	String add(Model model){
		List<DictionaryDO> facTypeList = dictionaryService.listByType("uom_type");
		model.addAttribute("uomTypeList", facTypeList);
	    return "custom/uom/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:uom:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		UomDO uom = uomService.get(id);
		model.addAttribute("uom", uom);
		List<DictionaryDO> facTypeList = dictionaryService.listByType("uom_type");
		model.addAttribute("uomTypeList", facTypeList);
	    return "custom/uom/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:uom:add")
	public R save( UomDO uom){
		if(uomService.save(uom)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:uom:edit")
	public R update( UomDO uom){
		uomService.update(uom);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:uom:remove")
	public R remove( Integer id){
		if(uomService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:uom:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		uomService.batchRemove(ids);
		return R.ok();
	}
	
}
