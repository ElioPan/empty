package com.ev.apis.controller.custom;

import com.alibaba.fastjson.JSON;
import com.ev.custom.vo.WeChatSettingEntity;
import com.ev.framework.annotation.EvApiByToken;
import com.ev.framework.config.ConstantForSYS;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.system.domain.SettingDO;
import com.ev.system.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.coyote.http2.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xy
 * @Date 2020/4/15 15:40
 * @Description
 */
@Api(value = "",tags = "系统配置接口")
@RestController
@RequestMapping("/apis/setting")
public class SettingApiController {


    @Autowired
    private SettingService settingService;

    @EvApiByToken(value = "/weChat/detail", method = RequestMethod.GET, apiTitle = "查看企业微信配置信息")
    @ApiOperation(value = "查看企业微信配置信息")
    public R getWeChatSetting() {
        SettingDO setting = settingService.get(ConstantForSYS.QIYE_WECHAT_SETTING);
        if(setting==null|| StringUtils.isBlank(setting.getValue())){
            return R.error("配置不存在");
        }
        WeChatSettingEntity weChatSetting = JSON.toJavaObject(JSON.parseObject(setting.getValue()),WeChatSettingEntity.class);
        Map<String,Object> result = new HashMap<>();
        result.put("data", weChatSetting);
        return R.ok(result);

    }

    @EvApiByToken(value = "/weChat/set", method = RequestMethod.POST, apiTitle = "企业微信配置")
    @ApiOperation(value = "企业微信配置")
    public R weChatSet(@ModelAttribute WeChatSettingEntity weChatSetting) {
        SettingDO setting = settingService.get(ConstantForSYS.QIYE_WECHAT_SETTING);
        if(setting==null){
            setting = new SettingDO();
            setting.setKey(ConstantForSYS.QIYE_WECHAT_SETTING);
        }
        setting.setValue(JSON.toJSONString(weChatSetting));
        settingService.saveOrUpdate(setting);
        return R.ok();
    }
}
