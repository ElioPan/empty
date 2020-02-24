package com.ev.custom.vo;

import lombok.Data;

import java.util.List;

@Data
public class WxUserEntity {
    private String userid;

    private String name;

    private String mobile;

    private List<Long> department;

    private String email;

    private Integer enable;
}
