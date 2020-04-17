package com.ev.custom.vo;

import lombok.Data;

/**
 * @Author xy
 * @Date 2020/4/15 16:00
 * @Description
 */
@Data
public class WeChatSettingEntity {
    private String corpId;

    private String appId;

    private String appSecret;

    private String mobileSecret;

    private String appUrl;

    private Integer userOrNot;
}
