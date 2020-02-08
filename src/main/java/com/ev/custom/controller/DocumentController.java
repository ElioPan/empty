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

import com.ev.custom.domain.DocumentDO;
import com.ev.custom.service.DocumentService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 文档
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-01 12:59:46
 */
 
@Controller
@RequestMapping("/custom/document")
public class DocumentController {
	@Autowired
	private DocumentService documentService;
	
	@GetMapping()
	@RequiresPermissions("custom:document:document")
	String Document(){
	    return "custom/document/document";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:document:document")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DocumentDO> documentList = documentService.list(query);
		int total = documentService.count(query);
		PageUtils pageUtils = new PageUtils(documentList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:document:add")
	String add(){
	    return "custom/document/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:document:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DocumentDO document = documentService.get(id);
		model.addAttribute("document", document);
	    return "custom/document/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:document:add")
	public R save( DocumentDO document){
		if(documentService.save(document)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:document:edit")
	public R update( DocumentDO document){
		documentService.update(document);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:document:remove")
	public R remove( Long id){
		if(documentService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:document:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		documentService.batchRemove(ids);
		return R.ok();
	}
	
}
