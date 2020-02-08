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
import com.ev.custom.domain.ReceiptDO;
import com.ev.custom.service.ReceiptService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:36
 */
 
@Controller
@RequestMapping("/custom/receipt")
public class ReceiptController {
	@Autowired
	private ReceiptService receiptService;
	
	@GetMapping()
	@RequiresPermissions("custom:receipt:receipt")
	String Receipt(){
	    return "custom/receipt/receipt";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:receipt:receipt")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ReceiptDO> receiptList = receiptService.list(query);
		int total = receiptService.count(query);
		PageUtils pageUtils = new PageUtils(receiptList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:receipt:add")
	String add(){
	    return "custom/receipt/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:receipt:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ReceiptDO receipt = receiptService.get(id);
		model.addAttribute("receipt", receipt);
	    return "custom/receipt/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:receipt:add")
	public R save( ReceiptDO receipt){
		if(receiptService.save(receipt)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:receipt:edit")
	public R update( ReceiptDO receipt){
		receiptService.update(receipt);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:receipt:remove")
	public R remove( Long id){
		if(receiptService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:receipt:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		receiptService.batchRemove(ids);
		return R.ok();
	}
	
}
