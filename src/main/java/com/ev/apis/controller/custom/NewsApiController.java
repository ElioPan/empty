package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSONArray;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.custom.domain.NewsDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.NewsService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/",tags = "新闻公告管理API")
@RestController
public class NewsApiController {
    @Autowired
    NewsService newsService;
    @Autowired
    private ContentAssocService contentAssocService;
    @Autowired
    private MessageSourceHandler messageSourceHandler;

    @EvApiByToken(value = "/apis/news/list", method = RequestMethod.GET, apiTitle = "获取新闻公告列表信息")
    @ApiOperation("获取列表信息")
    public R list(@ApiParam(value = "当前第几页", required = true) @RequestParam(value = "pageno", defaultValue = "1") int pageno,
                  @ApiParam(value = "一页多少条", required = true) @RequestParam(value = "pagesize", defaultValue = "20") int pagesize,
                  @ApiParam(value = "标题") @RequestParam(value = "title", defaultValue = "", required = false) String title,
                  @ApiParam(value = "发布人") @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                  @ApiParam(value = "类型") @RequestParam(value = "type", defaultValue = "", required = false) Integer type) {
    	Map<String, Object> params = Maps.newHashMapWithExpectedSize(5);
    	params.put("title", title);
    	params.put("userName", userName);
    	params.put("type", type);
    	params.put("offset", (pageno - 1) * pagesize);
    	params.put("limit", pagesize);
        Map<String, Object> results = Maps.newHashMap();
        List<Map<String, Object>> data = this.newsService.listForMap(params);
        int total = this.newsService.countForMap(params);
        if (data != null && data.size() > 0) {
            DsResultResponse dsRet = new DsResultResponse(); 
            dsRet.setDatas(data);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((int) (total + pagesize - 1) / pagesize);
			results.put("data", dsRet);
        }
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/news/detail",method = RequestMethod.GET,apiTitle = "获取新闻公告详细信息")
    @ApiOperation("获取新闻公告详细信息")
    public R detail(@ApiParam(value = "主键ID",required = true) @RequestParam(value = "id",defaultValue = "",required = false)  Long id) {
        Map<String,Object> results = new HashMap<>();
//        results.put("data",newsService.get(id));
        R r = newsService.dealDetails(id);
        return r;
    }



//    @EvApiByToken(value = "/apis/news/save",method = RequestMethod.POST,apiTitle = "保存新闻公告信息")
//    @ApiOperation("保存新闻公告信息")
//    public R save(@ApiParam(value = "新闻公告信息",required = true) NewsDO newsDO){
//        try {
//            newsService.save(newsDO);
//            return R.ok();
//        }catch (Exception ex){
//            return R.error(ex.getMessage());
//        }
//    }

    //增加文档上传工能
    @EvApiByToken(value = "/apis/news/save",method = RequestMethod.POST,apiTitle = "保存新闻公告信息")
    @ApiOperation("保存新闻公告信息")
    @Transactional(rollbackFor = Exception.class)
    public R save(@ApiParam(value = "新闻公告信息",required = true) NewsDO newsDO,
                  @ApiParam(value = "上传文档信息[{\"fileName\":\"文件名\",\"filePath\":\"文件路径\"},{\"fileName\":\"文件名\",\"filePath\":\"文件路径\"}]", required = false) @RequestParam(value = "fileOFNameAndPath", defaultValue = "", required = false) String fileOFNameAndPath){

            int rows = newsService.save(newsDO);
            if(rows>0){
                JSONArray nameAndPath=new JSONArray();
                if(!"".equals(fileOFNameAndPath)&&null!=fileOFNameAndPath){
                   nameAndPath = JSONArray.parseArray(fileOFNameAndPath);

                }
                contentAssocService.saveList(newsDO.getId(),nameAndPath,Constant.NEWS_PRESS_RELEASE);
                return R.ok();
            }
            return R.error(messageSourceHandler.getMessage("common.dailyReport.save",null));
    }


    @EvApiByToken(value = "/apis/news/update", method = RequestMethod.POST, apiTitle = "修改新闻公告信息")
    @ApiOperation("修改新闻公告信息")
    @Transactional(rollbackFor = Exception.class)
    public R update(@ApiParam(value = "新闻公告信息", required = true) NewsDO newsDO,
                    @ApiParam(value = "上传文档信息[{\"fileName\":\"文件名\",\"filePath\":\"文件路径\"},{\"fileName\":\"文件名\",\"filePath\":\"文件路径\"}]", required = false) @RequestParam(value = "fileOFNameAndPath", defaultValue = "", required = false) String fileOFNameAndPath) {

                R r = newsService.saveChangOfUpdate(newsDO, fileOFNameAndPath);
                return r;
    }


    @EvApiByToken(value = "/apis/news/batchRemove", method = RequestMethod.POST, apiTitle = "删除/批量删除新闻公告信息")
    @ApiOperation("删除/批量删除新闻公告信息")
    @Transactional(rollbackFor = Exception.class)
    public R remove(@ApiParam(value = "新闻公告主键数组", required = true, example = "[1,2,3,4]") @RequestParam(value = "ids", defaultValue = "") Long[] ids) {

            //删除要验证创建人是否是当前登录人
            R r = newsService.listOfCanDelet(ids);
            return r;

    }




}

