package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.service.TaskReplyService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/",tags = "任务回复管理API")
public class TaskReplyApiController {

    @Autowired
    private TaskReplyService taskReplyService;

    @EvApiByToken(value = "/apis/taskReply/list",method = RequestMethod.POST,apiTitle = "获取任务回复列表信息")
    @ApiOperation("获取任务回复列表信息")
    public R listForMap(@ApiParam(value = "当前第几页",required = true) @RequestParam(value = "pageno",defaultValue = "1",required = true) int pageno,
                                               @ApiParam(value = "一页多少条",required = true) @RequestParam(value = "pagesize",defaultValue = "20",required = true) int pagesize,
                                               @ApiParam(value = "任务ID",required = false) @RequestParam(value = "taskId",defaultValue = "",required = false)  Long taskId){
        //查询列表数据
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("pageno",pageno);
        params.put("pagesize",pagesize);
        params.put("taskId",taskId);
        Map<String,Object> results = Maps.newHashMap();
        List<Map<String,Object>> data = taskReplyService.listForMap(params);
        int total = this.taskReplyService.countForMap(params);
        if(data!=null && data.size()>0){
            DsResultResponse dsRet = new DsResultResponse();
            dsRet.setDatas(data);
            dsRet.setPageno(pageno);
            dsRet.setPagesize(pagesize);
            dsRet.setTotalRows(total);
            dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
            results.put("data",dsRet);
        }
        return R.ok(results);
    }
}
