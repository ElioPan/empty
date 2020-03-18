package com.ev.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "设备管理参数")
public class DeviceVO extends CommonVO implements Serializable{
    //设备名称（编号名称模糊查询）
    @ApiModelProperty(value = "设备名称（编号名称模糊查询）")
    private String nameAndCode;
    // 使用状况
    @ApiModelProperty(value = "使用状况")
    private Long usingStatus;
    // 设备类型
    @ApiModelProperty(value = "设备类型")
    private Long type;
}
