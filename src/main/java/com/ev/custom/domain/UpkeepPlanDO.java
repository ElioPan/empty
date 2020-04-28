package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养计划表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 12:58:23
 */
@Data
@ApiModel(value = "保养计划表")
public class UpkeepPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//工单号
	@ApiModelProperty(value = "计划单号")
	private String workOrderno;
	//计划名称
	@ApiModelProperty(value = "计划名称")
	private String name;
	//关联设备
	@ApiModelProperty(value = "关联设备")
	private Long deviceId;
	//责任人
	@ApiModelProperty(value = "责任人")
	private Long engineerId;
	//责任人电话
	@ApiModelProperty(value = "责任人电话")
	private String cellphone;
	//计划类型
	@ApiModelProperty(value = "保养级别")
	private Long type;
	//工时
	@ApiModelProperty(value = "工时")
	private Double manHour;
	//任务状态：启用；停用
	@ApiModelProperty(value = "任务状态：暂存，启用；停用")
	private Long status;
	//任务结果
	@ApiModelProperty(value = "任务结果")
	private Long result;
	//内容
	@ApiModelProperty(value = "内容")
	private String content;
	//间隔时间
	@ApiModelProperty(value = "间隔时间")
	private Integer intervalTime;
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
	@ApiModelProperty(value = "创建日期")
	private Date createTime;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//保养部门id
	@ApiModelProperty(value = "保养部门id")
	private Long deptId;
	//删除状态：1删除
	@ApiModelProperty(value = "删除状态：1删除")
	private Long deletStatus;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态：1删除
	@ApiModelProperty(value = "删除状态：1删除")
	private Integer delFlag;

}
