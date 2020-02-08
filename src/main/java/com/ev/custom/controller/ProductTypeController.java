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
import com.ev.custom.domain.ProductTypeDO;
import com.ev.custom.service.ProductTypeService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 商品类型表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 08:48:26
 */
 
@Controller
@RequestMapping("/custom/productType")
public class ProductTypeController {
	@Autowired
	private ProductTypeService productTypeService;
	
	@GetMapping()
	@RequiresPermissions("custom:productType:productType")
	String ProductType(){
	    return "custom/productType/productType";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:productType:productType")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ProductTypeDO> productTypeList = productTypeService.list(query);
		int total = productTypeService.count(query);
		PageUtils pageUtils = new PageUtils(productTypeList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:productType:add")
	String add(){
	    return "custom/productType/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:productType:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ProductTypeDO productType = productTypeService.get(id);
		model.addAttribute("productType", productType);
	    return "custom/productType/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:productType:add")
	public R save( ProductTypeDO productType){
		if(productTypeService.save(productType)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:productType:edit")
	public R update( ProductTypeDO productType){
		productTypeService.update(productType);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:productType:remove")
	public R remove( Long id){
		if(productTypeService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:productType:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		productTypeService.batchRemove(ids);
		return R.ok();
	}
	
}
