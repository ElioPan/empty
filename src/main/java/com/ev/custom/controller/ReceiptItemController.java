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
import com.ev.custom.domain.ReceiptItemDO;
import com.ev.custom.service.ReceiptItemService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:37
 */
 
@Controller
@RequestMapping("/custom/receiptItem")
public class ReceiptItemController {
	@Autowired
	private ReceiptItemService receiptItemService;
	
	@GetMapping()
	@RequiresPermissions("custom:receiptItem:receiptItem")
	String ReceiptItem(){
	    return "custom/receiptItem/receiptItem";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:receiptItem:receiptItem")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ReceiptItemDO> receiptItemList = receiptItemService.list(query);
		int total = receiptItemService.count(query);
		PageUtils pageUtils = new PageUtils(receiptItemList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:receiptItem:add")
	String add(){
	    return "custom/receiptItem/add";
	}

	@GetMapping("/edit/{rid}")
	@RequiresPermissions("custom:receiptItem:edit")
	String edit(@PathVariable("rid") Long rid,Model model){
		ReceiptItemDO receiptItem = receiptItemService.get(rid);
		model.addAttribute("receiptItem", receiptItem);
	    return "custom/receiptItem/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:receiptItem:add")
	public R save( ReceiptItemDO receiptItem){
		if(receiptItemService.save(receiptItem)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:receiptItem:edit")
	public R update( ReceiptItemDO receiptItem){
		receiptItemService.update(receiptItem);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:receiptItem:remove")
	public R remove( Long rid){
		if(receiptItemService.remove(rid)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:receiptItem:batchRemove")
	public R remove(@RequestParam("ids") Long[] rids){
		receiptItemService.batchRemove(rids);
		return R.ok();
	}
	
}
