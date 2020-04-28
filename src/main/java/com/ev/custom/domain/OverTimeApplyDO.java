package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 加班申请管理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 08:55:04
 */
@Data
@ApiModel(value = "加班申请管理")
public class OverTimeApplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//加班人
    @ApiModelProperty(value = "加班人")
	private Long overTimeUser;
	//开始时间
    @ApiModelProperty(value = "开始时间")
	private Date beginTime;
	//结束时间
    @ApiModelProperty(value = "结束时间")
	private Date endTime;
	//时长
    @ApiModelProperty(value = "时长")
	private Double timeArea;
	//原因
    @ApiModelProperty(value = "原因")
	private String reason;
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
	//状态
    @ApiModelProperty(value = "状态")
	private Long status;
	//流程实例ID
    @ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;


}
