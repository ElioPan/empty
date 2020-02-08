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

import com.ev.custom.domain.SparePartDO;
import com.ev.custom.service.SparePartService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 备品备件分类表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 15:43:45
 */
 
@Controller
@RequestMapping("/custom/sparePart")
public class SparePartController {
	@Autowired
	private SparePartService sparePartService;
	
	@GetMapping()
	@RequiresPermissions("custom:sparePart:sparePart")
	String SparePart(){
	    return "custom/sparePart/sparePart";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:sparePart:sparePart")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<SparePartDO> sparePartList = sparePartService.list(query);
		int total = sparePartService.count(query);
		PageUtils pageUtils = new PageUtils(sparePartList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:sparePart:add")
	String add(){
	    return "custom/sparePart/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:sparePart:edit")
	String edit(@PathVariable("id") Long id,Model model){
		SparePartDO sparePart = sparePartService.get(id);
		model.addAttribute("sparePart", sparePart);
	    return "custom/sparePart/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:sparePart:add")
	public R save( SparePartDO sparePart){
		if(sparePartService.save(sparePart)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:sparePart:edit")
	public R update( SparePartDO sparePart){
		sparePartService.update(sparePart);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:sparePart:remove")
	public R remove( Long id){
		if(sparePartService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:sparePart:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		sparePartService.batchRemove(ids);
		return R.ok();
	}
	
}
