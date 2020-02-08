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

import com.ev.custom.domain.SupplierDO;
import com.ev.custom.service.SupplierService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 供应商表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:03
 */
 
@Controller
@RequestMapping("/custom/supplier")
public class SupplierController {
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private DictionaryService dictionaryService;
	
	@GetMapping()
	@RequiresPermissions("custom:supplier:supplier")
	String Supplier(){
	    return "custom/supplier/supplier";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:supplier:supplier")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> supplierList = supplierService.listForMap(query);
		int total = supplierService.count(query);
		PageUtils pageUtils = new PageUtils(supplierList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:supplier:add")
	String add(Model model){
		List<DictionaryDO> typeList = dictionaryService.listByType("supplier_type");
		model.addAttribute("typeList", typeList);
		return "custom/supplier/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:supplier:edit")
	String edit(@PathVariable("id") Long id,Model model){
		SupplierDO supplier = supplierService.get(id);
		model.addAttribute("supplier", supplier);
		List<DictionaryDO> typeList = dictionaryService.listByType("supplier_type");
		model.addAttribute("typeList", typeList);
	    return "custom/supplier/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:supplier:add")
	public R save( SupplierDO supplier){
		if(supplierService.save(supplier)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:supplier:edit")
	public R update( SupplierDO supplier){
		supplierService.update(supplier);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:supplier:remove")
	public R remove( Long id){
		if(supplierService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:supplier:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		supplierService.batchRemove(ids);
		return R.ok();
	}

	@GetMapping("/objectList")
	@ResponseBody
	public List<SupplierDO> objectList() {
		return supplierService.objectList();
	}

}
