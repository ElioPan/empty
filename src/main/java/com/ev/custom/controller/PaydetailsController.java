package com.ev.custom.controller;

import java.util.List;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ev.custom.domain.PaydetailsDO;
import com.ev.custom.service.PaydetailsService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-29 16:36:57
 */
 
@Controller
@RequestMapping("/custom/paydetails")
public class PaydetailsController {
	@Autowired
	private PaydetailsService paydetailsService;
	
	@GetMapping()
	@RequiresPermissions("custom:paydetails:paydetails")
	String Paydetails(){
	    return "custom/paydetails/paydetails";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:paydetails:paydetails")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PaydetailsDO> paydetailsList = paydetailsService.list(query);
		int total = paydetailsService.count(query);
		PageUtils pageUtils = new PageUtils(paydetailsList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:paydetails:add")
	String add(){
	    return "custom/paydetails/add";
	}

//	@GetMapping("/edit/{id}")
//	@RequiresPermissions("custom:paydetails:edit")
//	String edit(@PathVariable("id") Long id,Model model){
//		PaydetailsDO paydetails = paydetailsService.get(id);
//		model.addAttribute("paydetails", paydetails);
//	    return "custom/paydetails/edit";
//	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:paydetails:add")
	public R save( PaydetailsDO paydetails){
		if(paydetailsService.save(paydetails)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:paydetails:edit")
	public R update( PaydetailsDO paydetails){
		paydetailsService.update(paydetails);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:paydetails:remove")
	public R remove( Long id){
		if(paydetailsService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:paydetails:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		paydetailsService.batchRemove(ids);
		return R.ok();
	}
	
}
