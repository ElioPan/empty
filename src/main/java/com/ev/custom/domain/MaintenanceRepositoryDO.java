package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 维保知识库
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-10 10:25:28
 */
@Data
@ApiModel(value = "维保知识库")
public class MaintenanceRepositoryDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//故障代码
    @ApiModelProperty(value = "故障代码")
	private String faultCode;
	//故障类型
    @ApiModelProperty(value = "故障类型")
	private Long faultType;
	//设备类别
    @ApiModelProperty(value = "设备类别")
	private Long deviceType;
	//设备ID
    @ApiModelProperty(value = "设备ID")
	private Long deviceId;
	//解决方案
    @ApiModelProperty(value = "解决方案")
	private String solution;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

}
