package com.ev.custom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ev.custom.domain.ClientDO;
import com.ev.custom.service.ClientService;
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

import com.ev.custom.domain.ClientLinkmanDO;
import com.ev.custom.service.ClientLinkmanService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 客户联系人表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:02
 */
 
@Controller
@RequestMapping("/custom/clientLinkman")
public class ClientLinkmanController {
	@Autowired
	private ClientLinkmanService clientLinkmanService;
	@Autowired
	private ClientService clientService;
	
	@GetMapping()
	@RequiresPermissions("custom:clientLinkman:clientLinkman")
	String ClientLinkman(){
	    return "custom/clientLinkman/clientLinkman";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:clientLinkman:clientLinkman")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ClientLinkmanDO> clientLinkmanList = clientLinkmanService.list(query);
		int total = clientLinkmanService.count(query);
		PageUtils pageUtils = new PageUtils(clientLinkmanList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:clientLinkman:add")
	String add(){
	    return "custom/clientLinkman/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:clientLinkman:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ClientLinkmanDO clientLinkman = clientLinkmanService.get(id);
		model.addAttribute("clientLinkman", clientLinkman);
		List<ClientDO> clients = this.clientService.list(new HashMap<>(16));
		model.addAttribute("clients", clients);
	    return "custom/clientLinkman/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:clientLinkman:add")
	public R save( ClientLinkmanDO clientLinkman){
		if(clientLinkmanService.save(clientLinkman)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:clientLinkman:edit")
	public R update( ClientLinkmanDO clientLinkman){
		clientLinkmanService.update(clientLinkman);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:clientLinkman:remove")
	public R remove( Long id){
		if(clientLinkmanService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:clientLinkman:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		clientLinkmanService.batchRemove(ids);
		return R.ok();
	}
	
}
