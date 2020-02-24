package com.ev.custom.vo;

import lombok.Data;

@Data
public class WxUserEntity {
    private String userid;

    private String name;

    private String mobile;

    private Long deptId;

    private String email;

    private Integer enable;
}
