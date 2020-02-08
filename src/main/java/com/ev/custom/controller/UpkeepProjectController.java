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

import com.ev.custom.domain.UpkeepProjectDO;
import com.ev.custom.service.UpkeepProjectService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 保养项目表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-12 16:53:16
 */
 
@Controller
@RequestMapping("/custom/upkeepProject")
public class UpkeepProjectController {
	@Autowired
	private UpkeepProjectService upkeepProjectService;
	
	@GetMapping()
	@RequiresPermissions("custom:upkeepProject:upkeepProject")
	String UpkeepProject(){
	    return "custom/upkeepProject/upkeepProject";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:upkeepProject:upkeepProject")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<UpkeepProjectDO> upkeepProjectList = upkeepProjectService.list(query);
		int total = upkeepProjectService.count(query);
		PageUtils pageUtils = new PageUtils(upkeepProjectList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:upkeepProject:add")
	String add(){
	    return "custom/upkeepProject/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:upkeepProject:edit")
	String edit(@PathVariable("id") Long id,Model model){
		UpkeepProjectDO upkeepProject = upkeepProjectService.get(id);
		model.addAttribute("upkeepProject", upkeepProject);
	    return "custom/upkeepProject/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:upkeepProject:add")
	public R save( UpkeepProjectDO upkeepProject){
		if(upkeepProjectService.save(upkeepProject)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:upkeepProject:edit")
	public R update( UpkeepProjectDO upkeepProject){
		upkeepProjectService.update(upkeepProject);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepProject:remove")
	public R remove( Long id){
		if(upkeepProjectService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:upkeepProject:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		upkeepProjectService.batchRemove(ids);
		return R.ok();
	}

	@GetMapping("/objectList")
	@ResponseBody
	public List<UpkeepProjectDO> objectList() {
		return upkeepProjectService.objectList();
	}
}
