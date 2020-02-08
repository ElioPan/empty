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
import com.ev.custom.domain.PurchasebillDO;
import com.ev.custom.service.PurchasebillService;
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
@RequestMapping("/custom/purchasebill")
public class PurchasebillController {
	@Autowired
	private PurchasebillService purchasebillService;
	
	@GetMapping()
	@RequiresPermissions("custom:purchasebill:purchasebill")
	String Purchasebill(){
	    return "custom/purchasebill/purchasebill";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:purchasebill:purchasebill")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<PurchasebillDO> purchasebillList = purchasebillService.list(query);
		int total = purchasebillService.count(query);
		PageUtils pageUtils = new PageUtils(purchasebillList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:purchasebill:add")
	String add(){
	    return "custom/purchasebill/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:purchasebill:edit")
	String edit(@PathVariable("id") Long id,Model model){
		PurchasebillDO purchasebill = purchasebillService.get(id);
		model.addAttribute("purchasebill", purchasebill);
	    return "custom/purchasebill/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:purchasebill:add")
	public R save( PurchasebillDO purchasebill){
		if(purchasebillService.save(purchasebill)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:purchasebill:edit")
	public R update( PurchasebillDO purchasebill){
		purchasebillService.update(purchasebill);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:purchasebill:remove")
	public R remove( Long id){
		if(purchasebillService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:purchasebill:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		purchasebillService.batchRemove(ids);
		return R.ok();
	}
	
}
