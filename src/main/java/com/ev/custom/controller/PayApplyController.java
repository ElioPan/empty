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

import com.ev.custom.domain.PayApplyDO;
import com.ev.custom.service.PayApplyService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 付款申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:36
 */
 
@Controller
@RequestMapping("/custom/payApply")
public class PayApplyController {
	@Autowired
	private PayApplyService payApplyService;
	
	@GetMapping()
	@RequiresPermissions("custom:payApply:payApply")
	String PayApply(){
	    return "custom/payApply/payApply";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:payApply:payApply")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PayApplyDO> payApplyList = payApplyService.list(query);
		int total = payApplyService.count(query);
		PageUtils pageUtils = new PageUtils(payApplyList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:payApply:add")
	String add(){
	    return "custom/payApply/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:payApply:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PayApplyDO payApply = payApplyService.get(id);
		model.addAttribute("payApply", payApply);
	    return "custom/payApply/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:payApply:add")
	public R save( PayApplyDO payApply){
		if(payApplyService.save(payApply)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:payApply:edit")
	public R update( PayApplyDO payApply){
		payApplyService.update(payApply);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:payApply:remove")
	public R remove( Long id){
		if(payApplyService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:payApply:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		payApplyService.batchRemove(ids);
		return R.ok();
	}
	
}
