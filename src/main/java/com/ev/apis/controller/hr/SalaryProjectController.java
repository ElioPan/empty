package com.ev.apis.controller.hr;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ev.hr.domain.SalaryProjectDO;
import com.ev.hr.service.SalaryProjectService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 薪资项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 13:10:37
 */
 
@Controller
@RequestMapping("/hr/salaryProject")
public class SalaryProjectController {
	@Autowired
	private SalaryProjectService salaryProjectService;
	
	@GetMapping()
	@RequiresPermissions("hr:salaryProject:salaryProject")
	String SalaryProject(){
	    return "hr/salaryProject/salaryProject";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("hr:salaryProject:salaryProject")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<SalaryProjectDO> salaryProjectList = salaryProjectService.list(query);
		int total = salaryProjectService.count(query);
		PageUtils pageUtils = new PageUtils(salaryProjectList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("hr:salaryProject:add")
	String add(){
	    return "hr/salaryProject/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("hr:salaryProject:edit")
	String edit(@PathVariable("id") Long id,Model model){
		SalaryProjectDO salaryProject = salaryProjectService.get(id);
		model.addAttribute("salaryProject", salaryProject);
	    return "hr/salaryProject/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("hr:salaryProject:add")
	public R save( SalaryProjectDO salaryProject){
		if(salaryProjectService.save(salaryProject)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("hr:salaryProject:edit")
	public R update( SalaryProjectDO salaryProject){
		salaryProjectService.update(salaryProject);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("hr:salaryProject:remove")
	public R remove( Long id){
		if(salaryProjectService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("hr:salaryProject:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		salaryProjectService.batchRemove(ids);
		return R.ok();
	}
	
}
