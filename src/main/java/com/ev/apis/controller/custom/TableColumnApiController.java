package com.ev.apis.controller.custom;

import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张栋
 * @date 2019/12/31
 */
@Api(value = "/",tags = "前端列表显示字段管理")
@RestController
public class TableColumnApiController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @EvApiByToken(value = "/apis/tableColumn/get",method = RequestMethod.GET,apiTitle = "获取列表显示字段")
    @ApiOperation("获取列表显示字段")
    public R list(@ApiParam(value = "列表标识",required = true) @RequestParam(value = "tableNo",defaultValue = "")  String tableNo){
        Map<String,Object> result = new HashMap<>();
        Object obj = redisTemplate.opsForValue().get(Constant.TABLE_COLUMN + ":"+ ShiroUtils.getUserId()+":" + tableNo);
        if(obj != null){
            result.put("results",obj);
        }
        return R.ok(result);
    }

    @EvApiByToken(value = "/apis/tableColumn/save",method = RequestMethod.POST,apiTitle = "保存用户自定义列表字段")
    @ApiOperation("保存用户自定义列表字段")
    public R save(@ApiParam(value = "列表标识",required = true) @RequestParam(value = "tableNo",defaultValue = "")  String tableNo,
        @ApiParam(value = "列表字段",required = true) @RequestParam(value = "tableColumns",defaultValue = "")  String tableColumns){
        redisTemplate.opsForValue().set(Constant.TABLE_COLUMN + ":"+ ShiroUtils.getUserId()+":"+ tableNo,tableColumns);
        return R.ok();
    }

}
