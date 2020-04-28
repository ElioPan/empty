package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 保养记录表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-31 14:47:08
 */
@Data
@ApiModel(value = "保养记录表")
public class UpkeepRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty()
	private Long id;
	//工单号
	@ApiModelProperty(value = "工单号")
	private String workOrderno;
	//记录名称
	@ApiModelProperty(value = "记录名称")
	private String name;
	//保养开始时间
	@ApiModelProperty(value = "保养开始时间")
	private Date startTime;
	//保养结束时间
	@ApiModelProperty(value = "保养结束时间")
	private Date endTime;
	//实际开始时间
	@ApiModelProperty(value = "实际开始时间")
	private Date actualStartTime;
	//实际结束时间
	@ApiModelProperty(value = "实际结束时间")
	private Date actualEndTime;
	//保养级别
	@ApiModelProperty(value = "保养级别")
	private Long upkeepLevel;
	//工时
	@ApiModelProperty(value = "工时")
	private Double manHour;
	//责任人
	@ApiModelProperty(value = "责任人")
	private Long engineerId;
	//停机时长
	@ApiModelProperty(value = "停机时长")
	private Double downHour;
	//成本
	@ApiModelProperty(value = "成本")
	private BigDecimal cost;
	@ApiModelProperty(value = "保养费用")
	private BigDecimal upkeepCost;
	//使用状态
	@ApiModelProperty(value = "使用状态")
	private Long status;
	//任务结果/状态
	@ApiModelProperty(value = "任务结果/状态")
	private Long result;
	//关闭原因
	@ApiModelProperty(value = "关闭原因")
	private String closureReason;
	//内容
	@ApiModelProperty(value = "内容")
	private String content;
	//计划ID
	@ApiModelProperty(value = "计划ID")
	private Long planId;
	//设备ID
	@ApiModelProperty(value = "设备ID")
	private Long deviceId;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//保养类型
	@ApiModelProperty(value = "保养类型")
	private Long type;


	//总工时费用
	@ApiModelProperty(value = "总工时费用")
	private BigDecimal manHourCost;

	//消息id(保养定时提醒)
	@ApiModelProperty(value = "消息id(保养定时提醒)")
	private Long messageId;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除标志(1删除)
	@ApiModelProperty(value = "删除标志(1删除)")
	private Integer delFlag;


}
