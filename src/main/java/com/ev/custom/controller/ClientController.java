package com.ev.custom.controller;

import java.util.List;
import java.util.Map;

import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
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

import com.ev.custom.domain.ClientDO;
import com.ev.custom.service.ClientService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 客户表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-04 13:56:21
 */
 
@Controller
@RequestMapping("/custom/client")
public class ClientController {
	@Autowired
	private ClientService clientService;
	@Autowired
	private DictionaryService dictionaryService;

	@GetMapping()
	@RequiresPermissions("custom:client:client")
	String Client(){
	    return "custom/client/client";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:client:client")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<Map<String,Object>> clientList = clientService.listForMap(query);
		int total = clientService.count(query);
		PageUtils pageUtils = new PageUtils(clientList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:client:add")
	String add(Model model){
		List<DictionaryDO> typeList = dictionaryService.listByType("client_type");
		model.addAttribute("typeList", typeList);
		return "custom/client/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:client:edit")
	String edit(@PathVariable("id") Long id,Model model){
		ClientDO client = clientService.get(id);
		model.addAttribute("client", client);
		List<DictionaryDO> typeList = dictionaryService.listByType("client_type");
		model.addAttribute("typeList", typeList);
	    return "custom/client/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:client:add")
	public R save( ClientDO client){
		if(clientService.save(client)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:client:edit")
	public R update( ClientDO client){
		clientService.update(client);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:client:remove")
	public R remove( Long id){
		if(clientService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:client:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		clientService.batchRemove(ids);
		return R.ok();
	}

	@GetMapping("/objectList")
	@ResponseBody
	public List<ClientDO> objectList() {
		return clientService.objectList();
	}

}
