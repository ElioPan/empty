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
import com.ev.custom.domain.PaymentformDO;
import com.ev.custom.service.PaymentformService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-06 13:23:31
 */
 
@Controller
@RequestMapping("/custom/paymentform")
public class PaymentformController {
	@Autowired
	private PaymentformService paymentformService;
	
	@GetMapping()
	@RequiresPermissions("custom:paymentform:paymentform")
	String Paymentform(){
	    return "custom/paymentform/paymentform";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:paymentform:paymentform")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PaymentformDO> paymentformList = paymentformService.list(query);
		int total = paymentformService.count(query);
		PageUtils pageUtils = new PageUtils(paymentformList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:paymentform:add")
	String add(){
	    return "custom/paymentform/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:paymentform:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PaymentformDO paymentform = paymentformService.get(id);
		model.addAttribute("paymentform", paymentform);
	    return "custom/paymentform/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:paymentform:add")
	public R save( PaymentformDO paymentform){
		if(paymentformService.save(paymentform)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:paymentform:edit")
	public R update( PaymentformDO paymentform){
		paymentformService.update(paymentform);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:paymentform:remove")
	public R remove( Long id){
		if(paymentformService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:paymentform:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		paymentformService.batchRemove(ids);
		return R.ok();
	}
	
}
