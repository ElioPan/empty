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

import com.ev.custom.domain.NewsDO;
import com.ev.custom.service.NewsService;
import com.ev.framework.utils.PageUtils;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

/**
 * 新闻公告
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 16:53:40
 */
 
@Controller
@RequestMapping("/custom/news")
public class NewsController {
	@Autowired
	private NewsService newsService;
	
	@GetMapping()
	@RequiresPermissions("custom:news:news")
	String News(){
	    return "custom/news/news";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("custom:news:news")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<NewsDO> newsList = newsService.list(query);
		int total = newsService.count(query);
		PageUtils pageUtils = new PageUtils(newsList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("custom:news:add")
	String add(){
	    return "custom/news/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("custom:news:edit")
	String edit(@PathVariable("id") Long id,Model model){
		NewsDO news = newsService.get(id);
		model.addAttribute("news", news);
	    return "custom/news/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("custom:news:add")
	public R save( NewsDO news){
		if(newsService.save(news)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("custom:news:edit")
	public R update( NewsDO news){
		newsService.update(news);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("custom:news:remove")
	public R remove( Long id){
		if(newsService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("custom:news:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		newsService.batchRemove(ids);
		return R.ok();
	}
	
}
