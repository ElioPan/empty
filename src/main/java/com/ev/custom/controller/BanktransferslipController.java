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
import com.ev.custom.domain.BanktransferslipDO;
import com.ev.custom.service.BanktransferslipService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-08 13:11:30
 */
 
@Controller
@RequestMapping("/custom/banktransferslip")
public class BanktransferslipController {
	@Autowired
	private BanktransferslipService banktransferslipService;
	
	@GetMapping()
	@RequiresPermissions("custom:banktransferslip:banktransferslip")
	String Banktransferslip(){
	    return "custom/banktransferslip/banktransferslip";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:banktransferslip:banktransferslip")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<BanktransferslipDO> banktransferslipList = banktransferslipService.list(query);
		int total = banktransferslipService.count(query);
		PageUtils pageUtils = new PageUtils(banktransferslipList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:banktransferslip:add")
	String add(){
	    return "custom/banktransferslip/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:banktransferslip:edit")
	String edit(@PathVariable("id") Long id,Model model){
		BanktransferslipDO banktransferslip = banktransferslipService.get(id);
		model.addAttribute("banktransferslip", banktransferslip);
	    return "custom/banktransferslip/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:banktransferslip:add")
	public R save( BanktransferslipDO banktransferslip){
		if(banktransferslipService.save(banktransferslip)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:banktransferslip:edit")
	public R update( BanktransferslipDO banktransferslip){
		banktransferslipService.update(banktransferslip);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:banktransferslip:remove")
	public R remove( Long id){
		if(banktransferslipService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:banktransferslip:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		banktransferslipService.batchRemove(ids);
		return R.ok();
	}
	
}
