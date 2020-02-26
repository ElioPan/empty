package com.ev.custom.vo;

import lombok.Data;

@Data
public class WxBaseMessageEntity {
    private String touser;

    private String toparty;

    private String totag;

    private String msgtype;

    private Integer agentid;

    private Integer safe;

    private Integer enable_id_trans;

    private Integer enable_duplicate_check;
}
