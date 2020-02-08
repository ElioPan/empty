package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.custom.service.DingdingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "/", tags = "审批流程管理")
public class DingdingApiController {
    @Autowired
    private DingdingService dingdingService;

    @EvApiByToken(value = "/apis/dingding/getHistoryByProcessId",method = RequestMethod.POST,apiTitle = "根据流程ID获取流程历史信息")
    @ApiOperation("根据流程ID获取流程历史信息")
    public R getHistoryByProcessId(@ApiParam(value = "流程实例ID") @RequestParam(value = "processId",defaultValue = "",required = false)  String processId){
        List<Map<String,Object>> activitiList = dingdingService.getHistoryByProcessId(processId);
        Map<String,Object> result = new HashMap<>();
        result.put("activitiList",activitiList);
        return  R.ok(result);
    }
}
