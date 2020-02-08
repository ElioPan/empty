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
import com.ev.custom.domain.BanktransferslipItemDO;
import com.ev.custom.service.BanktransferslipItemService;
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
@RequestMapping("/custom/banktransferslipItem")
public class BanktransferslipItemController {
	@Autowired
	private BanktransferslipItemService banktransferslipItemService;
	
	@GetMapping()
	@RequiresPermissions("custom:banktransferslipItem:banktransferslipItem")
	String BanktransferslipItem(){
	    return "custom/banktransferslipItem/banktransferslipItem";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:banktransferslipItem:banktransferslipItem")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<BanktransferslipItemDO> banktransferslipItemList = banktransferslipItemService.list(query);
		int total = banktransferslipItemService.count(query);
		PageUtils pageUtils = new PageUtils(banktransferslipItemList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:banktransferslipItem:add")
	String add(){
	    return "custom/banktransferslipItem/add";
	}

	@GetMapping("/edit/{tid}")
	@RequiresPermissions("custom:banktransferslipItem:edit")
	String edit(@PathVariable("tid") Long tid,Model model){
		BanktransferslipItemDO banktransferslipItem = banktransferslipItemService.get(tid);
		model.addAttribute("banktransferslipItem", banktransferslipItem);
	    return "custom/banktransferslipItem/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:banktransferslipItem:add")
	public R save( BanktransferslipItemDO banktransferslipItem){
		if(banktransferslipItemService.save(banktransferslipItem)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:banktransferslipItem:edit")
	public R update( BanktransferslipItemDO banktransferslipItem){
		banktransferslipItemService.update(banktransferslipItem);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:banktransferslipItem:remove")
	public R remove( Long tid){
		if(banktransferslipItemService.remove(tid)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:banktransferslipItem:batchRemove")
	public R remove(@RequestParam("ids") Long[] tids){
		banktransferslipItemService.batchRemove(tids);
		return R.ok();
	}
	
}
