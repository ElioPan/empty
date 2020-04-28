package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 巡检计划表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@Data
@ApiModel(value = "巡检计划表")
public class PatrolPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "主键")
	private Long id;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderno;
	//计划名称
    @ApiModelProperty(value = "计划名称")
	private String name;
	//责任人
    @ApiModelProperty(value = "责任人")
	private Long engineerId;
	//责任人电话
    @ApiModelProperty(value = "责任人电话")
	private String cellphone;
	//内容
    @ApiModelProperty(value = "内容")
	private String content;
	//计划状态
    @ApiModelProperty(value = "计划状态(129:启用;131:完成)",hidden = true)
	private Long status;
	//频次间隔
    @ApiModelProperty(value = "频次间隔")
	private Integer frequency;
	//到期提醒
    @ApiModelProperty(value = "到期提醒")
	private Integer expireday;
	//计划开始时间
    @ApiModelProperty(value = "计划开始时间")
	private Date startTime;
	//计划结束时间
    @ApiModelProperty(value = "计划结束时间")
	private Date endTime;
	//创建日期
    @ApiModelProperty(value = "创建日期",hidden = true)
	private Date createTime;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
    //修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;


}
