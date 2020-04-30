package com.ev.apis.controller.custom;

import com.ev.common.controller.BaseController;
import com.ev.custom.service.WeChatService;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
@Api(value = "/", tags = "微信相关接口")
public class WeChatApiController extends BaseController {
    @Autowired
    private WeChatService weChatService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @EvApiByToken(value = "/apis/weChat/getAccessToken", method = RequestMethod.POST, apiTitle = "获取调用接口凭证")
    @ApiOperation("获取调用接口凭证")
    R getAccessTOken() throws IOException, ParseException {
        Map<String, Object> results = new HashMap<>();
        results = weChatService.getAccessToken(new Date());
        return R.ok(results);
    }

    @EvApiByToken(value = "/apis/weChat/getSignature", method = RequestMethod.POST, apiTitle = "获取签名信息")
    @ApiOperation("获取签名信息")
    R getSignature(
            @ApiParam(value = "地址url", required = true) @RequestParam(value = "url", defaultValue = "") String url) throws IOException, ParseException {
        Map<String, Object> results = new HashMap<>();
        results = weChatService.getSignature(url, new Date());
        return R.ok(results);
    }
}
