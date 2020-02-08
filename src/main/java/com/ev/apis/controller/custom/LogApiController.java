package com.ev.apis.controller.custom;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Maps;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.common.domain.LogDO;
import com.ev.common.domain.PageDO;
import com.ev.common.service.LogService;
import com.ev.framework.utils.Query;
import com.ev.framework.utils.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/", tags = "操作日志API")
public class LogApiController {
	@Autowired
	LogService logService;

    @EvApiByToken(value = "/apis/log/list",method = RequestMethod.GET,apiTitle = "获取操作日志信息")
    @ApiOperation("获取操作日志信息")
    public R list(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
            @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
            @ApiParam(value = "操作",required = false) @RequestParam(value = "operation",defaultValue = "",required = false)  String operation,
            @ApiParam(value = "用户",required = false) @RequestParam(value = "username",defaultValue = "",required = false)  String username) {
    	Map<String, Object>params=Maps.newHashMap();
		params.put("operation", operation);
		params.put("username", username);
		params.put("offset", (pageno - 1) * pagesize);
		params.put("limit", pagesize);
		Query query = new Query(params);
		PageDO<LogDO> page = logService.queryList(query);
		Map<String, Object> results = Maps.newHashMap();
		results.put("datas", page);
		
		return R.ok(results);
	}

    @EvApiByToken(value = "/apis/log/remove",method = RequestMethod.POST,apiTitle = "删除操作日志")
    @ApiOperation("删除操作日志")
    public R remove(@ApiParam(value = "删除日志ID",required = true) @RequestParam(value = "id",defaultValue = "",required = true)Long id) {
		if (logService.remove(id)>0) {
			return R.ok();
		}
		return R.error();
	}
    @EvApiByToken(value = "/apis/log/batchRemove",method = RequestMethod.DELETE,apiTitle = "批量删除操作日志")
    @ApiOperation("批量删除操作日志")
    public R batchRemove(@ApiParam(value = "批量删除日志ID数组",required = true)@RequestParam(value = "ids",defaultValue = "",required = true)Long[] ids) {
		int r = logService.batchRemove(ids);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}
}
