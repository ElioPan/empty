package com.ev.custom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ev.custom.domain.SupplierDO;
import com.ev.custom.service.SupplierService;
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

import com.ev.custom.domain.SupplierLinkmanDO;
import com.ev.custom.service.SupplierLinkmanService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 供应商联系人表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:03
 */
 
@Controller
@RequestMapping("/custom/supplierLinkman")
public class SupplierLinkmanController {
	@Autowired
	private SupplierLinkmanService supplierLinkmanService;
	@Autowired
	private SupplierService supplierService;
	
	@GetMapping()
	@RequiresPermissions("custom:supplierLinkman:supplierLinkman")
	String SupplierLinkman(){
	    return "custom/supplierLinkman/supplierLinkman";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:supplierLinkman:supplierLinkman")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<SupplierLinkmanDO> supplierLinkmanList = supplierLinkmanService.list(query);
		int total = supplierLinkmanService.count(query);
		PageUtils pageUtils = new PageUtils(supplierLinkmanList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:supplierLinkman:add")
	String add(){
	    return "custom/supplierLinkman/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:supplierLinkman:edit")
	String edit(@PathVariable("id") Long id,Model model){
		SupplierLinkmanDO supplierLinkman = supplierLinkmanService.get(id);
		model.addAttribute("supplierLinkman", supplierLinkman);
		List<SupplierDO> suppliers = this.supplierService.list(new HashMap<>(16));
		model.addAttribute("suppliers", suppliers);
	    return "custom/supplierLinkman/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:supplierLinkman:add")
	public R save( SupplierLinkmanDO supplierLinkman){
		if(supplierLinkmanService.save(supplierLinkman)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:supplierLinkman:edit")
	public R update( SupplierLinkmanDO supplierLinkman){
		supplierLinkmanService.update(supplierLinkman);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:supplierLinkman:remove")
	public R remove( Long id){
		if(supplierLinkmanService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:supplierLinkman:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		supplierLinkmanService.batchRemove(ids);
		return R.ok();
	}
	
}
