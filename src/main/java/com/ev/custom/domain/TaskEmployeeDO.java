package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务关联人
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 13:26:30
 */
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
	private Integer assocType;
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

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：任务ID
	 */
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：任务ID
	 */
	public Long getTaskId() {
		return taskId;
	}
	/**
	 * 设置：员工ID
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * 获取：员工ID
	 */
	public Long getEmployeeId() {
		return employeeId;
	}
	/**
	 * 设置：关联类型
	 */
	public void setAssocType(Integer assocType) {
		this.assocType = assocType;
	}
	/**
	 * 获取：关联类型
	 */
	public Integer getAssocType() {
		return assocType;
	}
	/**
	 * 设置：回复ID
	 */
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
	/**
	 * 获取：回复ID
	 *
	 *
	 *
	 */
	public Long getReplyId() {
		return replyId;
	}


	/**
	 * 设置：创建人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：修改人
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：删除标志
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDelFlag() {
		return delFlag;
	}

	public TaskEmployeeDO(Long taskId, Long employeeId, Integer assocType) {
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.assocType = assocType;
	}

	public TaskEmployeeDO(Long taskId, Long employeeId, Integer assocType, Long replyId) {
		this.taskId = taskId;
		this.employeeId = employeeId;
		this.assocType = assocType;
		this.replyId = replyId;
	}

	public TaskEmployeeDO() {
	}
}
