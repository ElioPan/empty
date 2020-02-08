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

import com.ev.custom.domain.QualityGroupDO;
import com.ev.custom.service.QualityGroupService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-16 13:59:08
 */
 
@Controller
@RequestMapping("/custom/qualityGroup")
public class QualityGroupController {
	@Autowired
	private QualityGroupService qualityGroupService;
	
	@GetMapping()
	@RequiresPermissions("custom:qualityGroup:qualityGroup")
	String QualityGroup(){
	    return "custom/qualityGroup/qualityGroup";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:qualityGroup:qualityGroup")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<QualityGroupDO> qualityGroupList = qualityGroupService.list(query);
		int total = qualityGroupService.count(query);
		PageUtils pageUtils = new PageUtils(qualityGroupList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:qualityGroup:add")
	String add(){
	    return "custom/qualityGroup/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:qualityGroup:edit")
	String edit(@PathVariable("id") Long id,Model model){
		QualityGroupDO qualityGroup = qualityGroupService.get(id);
		model.addAttribute("qualityGroup", qualityGroup);
	    return "custom/qualityGroup/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:qualityGroup:add")
	public R save( QualityGroupDO qualityGroup){
		if(qualityGroupService.save(qualityGroup)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:qualityGroup:edit")
	public R update( QualityGroupDO qualityGroup){
		qualityGroupService.update(qualityGroup);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:qualityGroup:remove")
	public R remove( Long id){
		if(qualityGroupService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:qualityGroup:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		qualityGroupService.batchRemove(ids);
		return R.ok();
	}
	
}
