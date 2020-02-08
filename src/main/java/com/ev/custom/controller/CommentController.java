package com.ev.custom.controller;

import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;
import com.ev.custom.domain.CommentDO;
import com.ev.custom.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 回复信息表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-22 08:55:56
 */
 
@Controller
@RequestMapping("/custom/comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@GetMapping()
	@RequiresPermissions("custom:comment:comment")
	String Comment(){
	    return "custom/comment/comment";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:comment:comment")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<CommentDO> commentList = commentService.list(query);
		int total = commentService.count(query);
		PageUtils pageUtils = new PageUtils(commentList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:comment:add")
	String add(){
	    return "custom/comment/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:comment:edit")
	String edit(@PathVariable("id") Long id,Model model){
		CommentDO comment = commentService.get(id);
		model.addAttribute("comment", comment);
	    return "custom/comment/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:comment:add")
	public R save( CommentDO comment){
		if(commentService.save(comment)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:comment:edit")
	public R update( CommentDO comment){
		commentService.update(comment);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:comment:remove")
	public R remove( Long id){
		if(commentService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:comment:batchRemove")
	public R remove(@RequestParam("ids") Long[] ids){
		commentService.batchRemove(ids);
		return R.ok();
	}
	
}
