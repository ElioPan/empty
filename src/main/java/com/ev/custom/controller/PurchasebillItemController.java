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
import com.ev.custom.domain.PurchasebillItemDO;
import com.ev.custom.service.PurchasebillItemService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-17 13:06:48
 */
 
@Controller
@RequestMapping("/custom/purchasebillItem")
public class PurchasebillItemController {
	@Autowired
	private PurchasebillItemService purchasebillItemService;
	
	@GetMapping()
	@RequiresPermissions("custom:purchasebillItem:purchasebillItem")
	String PurchasebillItem(){
	    return "custom/purchasebillItem/purchasebillItem";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:purchasebillItem:purchasebillItem")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PurchasebillItemDO> purchasebillItemList = purchasebillItemService.list(query);
		int total = purchasebillItemService.count(query);
		PageUtils pageUtils = new PageUtils(purchasebillItemList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:purchasebillItem:add")
	String add(){
	    return "custom/purchasebillItem/add";
	}

	@GetMapping("/edit/{bid}")
	@RequiresPermissions("custom:purchasebillItem:edit")
	String edit(@PathVariable("bid") Long bid,Model model){
		PurchasebillItemDO purchasebillItem = purchasebillItemService.get(bid);
		model.addAttribute("purchasebillItem", purchasebillItem);
	    return "custom/purchasebillItem/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:purchasebillItem:add")
	public R save( PurchasebillItemDO purchasebillItem){
		if(purchasebillItemService.save(purchasebillItem)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:purchasebillItem:edit")
	public R update( PurchasebillItemDO purchasebillItem){
		purchasebillItemService.update(purchasebillItem);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:purchasebillItem:remove")
	public R remove( Long bid){
		if(purchasebillItemService.remove(bid)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:purchasebillItem:batchRemove")
	public R remove(@RequestParam("ids") Long[] bids){
		purchasebillItemService.batchRemove(bids);
		return R.ok();
	}
	
}
