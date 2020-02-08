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
import com.ev.custom.domain.PaymentformItemDO;
import com.ev.custom.service.PaymentformItemService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-06 13:23:32
 */
 
@Controller
@RequestMapping("/custom/paymentformItem")
public class PaymentformItemController {
	@Autowired
	private PaymentformItemService paymentformItemService;
	
	@GetMapping()
	@RequiresPermissions("custom:paymentformItem:paymentformItem")
	String PaymentformItem(){
	    return "custom/paymentformItem/paymentformItem";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:paymentformItem:paymentformItem")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PaymentformItemDO> paymentformItemList = paymentformItemService.list(query);
		int total = paymentformItemService.count(query);
		PageUtils pageUtils = new PageUtils(paymentformItemList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:paymentformItem:add")
	String add(){
	    return "custom/paymentformItem/add";
	}

	@GetMapping("/edit/{pid}")
	@RequiresPermissions("custom:paymentformItem:edit")
	String edit(@PathVariable("pid") Long pid,Model model){
		PaymentformItemDO paymentformItem = paymentformItemService.get(pid);
		model.addAttribute("paymentformItem", paymentformItem);
	    return "custom/paymentformItem/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:paymentformItem:add")
	public R save( PaymentformItemDO paymentformItem){
		if(paymentformItemService.save(paymentformItem)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:paymentformItem:edit")
	public R update( PaymentformItemDO paymentformItem){
		paymentformItemService.update(paymentformItem);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:paymentformItem:remove")
	public R remove( Long pid){
		if(paymentformItemService.remove(pid)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:paymentformItem:batchRemove")
	public R remove(@RequestParam("ids") Long[] pids){
		paymentformItemService.batchRemove(pids);
		return R.ok();
	}
	
}
