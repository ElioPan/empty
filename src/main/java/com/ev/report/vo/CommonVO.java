package com.ev.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "共通参数")
public class CommonVO implements Serializable {
    //当前第几页
    @ApiModelProperty(value = "当前第几页")
    private int pageno = 1;
    //一页多少条
    @ApiModelProperty(value = "一页多少条")
    private int pagesize = 20;
    //开始时间
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    //结束时间
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    //部门
    @ApiModelProperty(value = "部门")
    private Long deptId;
    //职员
    @ApiModelProperty(value = "员工")
    private Long userId;



}
