package com.ev.apis.controller.test;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ev.base.GyhlBaseController;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.utils.R;
import com.ev.system.domain.UserDO;
import com.ev.test.TestService;
import com.ev.test.vo.UserParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xy
 * @Date 2020/4/23 13:13
 * @Description
 */

@Api(value = "/", tags = "测试接口管理")
@RestController
public class TestApiController extends GyhlBaseController<UserDO> {

    @Autowired
    TestService testService;


    @Override
    public IService<UserDO> getService() {
        return testService;
    }

    @EvApiByToken(value = "/apis/test/userList", method = RequestMethod.GET, apiTitle = "查询用户列表数据")
    @ApiOperation("查询用户列表数据")
    public R userList(
            @ApiParam("用户查询参数") @RequestBody UserParam userParam) {
        Map<String,Object> result = new HashMap<>();
        result.put("result",testService.pageList(userParam));
        return R.ok(result);
    }
}
