package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务关联人
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 13:26:30
 */
@Data
@ApiModel(value = "任务关联人")
public class TaskEmployeeDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//任务ID
    @ApiModelProperty(value = "任务ID")
	private Long taskId;
	//员工ID
    @ApiModelProperty(value = "员工ID")
	private Long employeeId;
	//关联类型
    @ApiModelProperty(value = "关联类型")
	private Long assocType;
	//回复ID
    @ApiModelProperty(value = "回复ID")
	private Long replyId;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;


	public TaskEmployeeDO(Long taskId, Long employeeId, Long assocType) {
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.assocType = assocType;
	}

	public TaskEmployeeDO(Long taskId, Long employeeId, Long assocType, Long replyId) {
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.assocType = assocType;
		this.replyId = replyId;
	}

	public TaskEmployeeDO() {
	}
}
