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

import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 附件关联表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-10 14:51:45
 */
 
@Controller
@RequestMapping("/custom/contentAssoc")
public class ContentAssocController {
	@Autowired
	private ContentAssocService contentAssocService;
	
	@GetMapping()
	@RequiresPermissions("custom:contentAssoc:contentAssoc")
	String ContentAssoc(){
	    return "custom/contentAssoc/contentAssoc";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:contentAssoc:contentAssoc")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ContentAssocDO> contentAssocList = contentAssocService.list(query);
		int total = contentAssocService.count(query);
		PageUtils pageUtils = new PageUtils(contentAssocList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:contentAssoc:add")
	String add(){
	    return "custom/contentAssoc/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:contentAssoc:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		ContentAssocDO contentAssoc = contentAssocService.get(id);
		model.addAttribute("contentAssoc", contentAssoc);
	    return "custom/contentAssoc/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:contentAssoc:add")
	public R save( ContentAssocDO contentAssoc){
		if(contentAssocService.save(contentAssoc)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:contentAssoc:edit")
	public R update( ContentAssocDO contentAssoc){
		contentAssocService.update(contentAssoc);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:contentAssoc:remove")
	public R remove( Integer id){
		if(contentAssocService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:contentAssoc:batchRemove")
	public R remove(@RequestParam("ids") Integer[] ids){
		contentAssocService.batchRemove(ids);
		return R.ok();
	}
	
}
