package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 加班申请管理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 08:55:04
 */
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
	private Integer status;
	//流程实例ID
    @ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;

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
	 * 设置：加班人
	 */
	public void setOverTimeUser(Long overTimeUser) {
		this.overTimeUser = overTimeUser;
	}
	/**
	 * 获取：加班人
	 */
	public Long getOverTimeUser() {
		return overTimeUser;
	}
	/**
	 * 设置：开始时间
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getBeginTime() {
		return beginTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：时长
	 */
	public void setTimeArea(Double timeArea) {
		this.timeArea = timeArea;
	}
	/**
	 * 获取：时长
	 */
	public Double getTimeArea() {
		return timeArea;
	}
	/**
	 * 设置：原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 获取：原因
	 */
	public String getReason() {
		return reason;
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
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：删除状态
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：流程实例ID
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	/**
	 * 获取：流程实例ID
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
	}
}
