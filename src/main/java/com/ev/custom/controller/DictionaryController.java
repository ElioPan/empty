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

import com.ev.custom.domain.DictionaryDO;
import com.ev.custom.service.DictionaryService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 数据字典
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-03 09:40:08
 */
 
@Controller
@RequestMapping("/custom/dictionary")
public class DictionaryController {
	@Autowired
	private DictionaryService dictionaryService;
	
	@GetMapping()
	@RequiresPermissions("custom:dictionary:dictionary")
	String Dictionary(){
	    return "custom/dictionary/dictionary";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:dictionary:dictionary")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<DictionaryDO> dictionaryList = dictionaryService.list(query);
		int total = dictionaryService.count(query);
		PageUtils pageUtils = new PageUtils(dictionaryList, total);
		return pageUtils;
	}

	@ResponseBody
	@GetMapping("/listForMap")
	@RequiresPermissions("custom:dictionary:dictionary")
	public PageUtils listForMap(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String,Object>> dictionaryList = dictionaryService.listForMap(query);
		int total = dictionaryService.countForMap(query);
		PageUtils pageUtils = new PageUtils(dictionaryList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:dictionary:add")
	String add(){
	    return "custom/dictionary/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:dictionary:edit")
	String edit(@PathVariable("id") Long id,Model model){
		DictionaryDO dictionary = dictionaryService.get(id);
		model.addAttribute("dictionary", dictionary);
	    return "custom/dictionary/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:dictionary:add")
	public R save( DictionaryDO dictionary){
		if(dictionaryService.save(dictionary)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:dictionary:edit")
	public R update( DictionaryDO dictionary){
		dictionaryService.update(dictionary);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:dictionary:remove")
	public R remove( Long id){
		if(dictionaryService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:dictionary:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		dictionaryService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 根据前台类型值，获取字典表数据集合
	 */
	@GetMapping("/objectList")
	@ResponseBody
	public List<DictionaryDO> objectList(String typeValue) {
		return dictionaryService.listByType(typeValue);
	}

}
