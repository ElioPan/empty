package com.ev.apis.controller.custom;


import com.alibaba.fastjson.JSONArray;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.custom.service.InfluxService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

/**
    * 时序数据接口
    *
    * @author zhangdong
    * @email 911435330@qq.com
    * @date 2019-11-30
    */

@Api(value = "/", tags = "时序数据接口")
@RestController
public class InfluxApiController {
    @Autowired
    private InfluxService influxService;

    @EvApiByToken(value = "/apis/influx/list", method = RequestMethod.POST, apiTitle = "获取时序数据信息")
    @ApiOperation("获取时序数据信息")
    public R list(@ApiParam(value = "开始时间(yyyy-MM-dd)") @RequestParam(value = "startTime", required = false) String startTime,
              @ApiParam(value = "结束时间(yyyy-MM-dd)") @RequestParam(value = "endTime", required = false) String endTime,
              @ApiParam(value = "时间间隔(S)") @RequestParam(value = "timeInterval", required = false,defaultValue = "1") String timeInterval,
              @ApiParam(value = "聚合方式(0：首值；1：末值；2：平均值；3：最大值；4：最小值)") @RequestParam(value = "convergenceMode", required = false,defaultValue = "0") String convergenceMode,
              @ApiParam(value = "测点ID") @RequestParam(value = "pointIds", required = false, defaultValue = "") Long[] pointIds) throws ParseException {
        Map<String,Object> results = Maps.newHashMap();
        JSONArray result = influxService.list(startTime,endTime,timeInterval,convergenceMode,pointIds);
        results.put("result",result);
        return R.ok(results);
    }
}
