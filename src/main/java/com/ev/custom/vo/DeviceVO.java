package com.ev.custom.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "设备管理参数")
public class DeviceVO implements Serializable {
    //当前第几页
    @ApiModelProperty(value = "当前第几页", required = true)
    private int pageno = 1;
    //一页多少条
    @ApiModelProperty(value = "一页多少条", required = true)
    private int pagesize = 20;
    //设备名称（编号名称模糊查询）
    @ApiModelProperty(value = "设备名称（编号名称模糊查询）")
    private String nameAndCode;
    //部门
    @ApiModelProperty(value = "部门")
    private Long deptId;
    // 使用状况
    @ApiModelProperty(value = "使用状况")
    private Long usingStatus;
    // 设备类型
    @ApiModelProperty(value = "设备类型")
    private Long type;
}
