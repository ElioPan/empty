package com.ev.custom.vo;

import lombok.Data;

@Data
public class WxDeptEntity {
    private String name;

    private String name_en;

    private Long parentid;

    private Integer order;

    private Long id;
}
