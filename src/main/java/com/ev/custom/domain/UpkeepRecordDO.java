package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@ApiModel(value = "保养记录表")
public class UpkeepRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
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
	private Integer upkeepLevel;
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
	//使用状态
	@ApiModelProperty(value = "使用状态")
	private Integer status;
	//任务结果/状态
	@ApiModelProperty(value = "任务结果/状态")
	private Integer result;
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
	private Integer type;


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

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：工单号
	 */
	public void setWorkOrderno(String workOrderno) {
		this.workOrderno = workOrderno;
	}
	/**
	 * 获取：工单号
	 */
	public String getWorkOrderno() {
		return workOrderno;
	}
	/**
	 * 设置：记录名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：记录名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：保养开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：保养开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：保养结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：保养结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：实际开始时间
	 */
	public void setActualStartTime(Date actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	/**
	 * 获取：实际开始时间
	 */
	public Date getActualStartTime() {
		return actualStartTime;
	}
	/**
	 * 设置：实际结束时间
	 */
	public void setActualEndTime(Date actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	/**
	 * 获取：实际结束时间
	 */
	public Date getActualEndTime() {
		return actualEndTime;
	}
	/**
	 * 设置：保养级别
	 */
	public void setUpkeepLevel(Integer upkeepLevel) {
		this.upkeepLevel = upkeepLevel;
	}
	/**
	 * 获取：保养级别
	 */
	public Integer getUpkeepLevel() {
		return upkeepLevel;
	}
	/**
	 * 设置：工时
	 */
	public void setManHour(Double manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：工时
	 */
	public Double getManHour() {
		return manHour;
	}
	/**
	 * 设置：责任人
	 */
	public void setEngineerId(Long engineerId) {
		this.engineerId = engineerId;
	}
	/**
	 * 获取：责任人
	 */
	public Long getEngineerId() {
		return engineerId;
	}
	/**
	 * 设置：停机时长
	 */
	public void setDownHour(Double downHour) {
		this.downHour = downHour;
	}
	/**
	 * 获取：停机时长
	 */
	public Double getDownHour() {
		return downHour;
	}
	/**
	 * 设置：成本
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	/**
	 * 获取：成本
	 */
	public BigDecimal getCost() {
		return cost;
	}
	/**
	 * 设置：使用状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：使用状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：任务结果/状态
	 */
	public void setResult(Integer result) {
		this.result = result;
	}
	/**
	 * 获取：任务结果/状态
	 */
	public Integer getResult() {
		return result;
	}
	/**
	 * 设置：关闭原因
	 */
	public void setClosureReason(String closureReason) {
		this.closureReason = closureReason;
	}
	/**
	 * 获取：关闭原因
	 */
	public String getClosureReason() {
		return closureReason;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：计划ID
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * 获取：计划ID
	 */
	public Long getPlanId() {
		return planId;
	}
	/**
	 * 设置：设备ID
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：设备ID
	 */
	public Long getDeviceId() {
		return deviceId;
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
	 * 设置：保养类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：保养类型
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置：总工时费用
	 */
	public void setManHourCost(BigDecimal manHourCost) {
		this.manHourCost = manHourCost;
	}
	/**
	 * 获取：总工时费用
	 */
	public BigDecimal getManHourCost() {
		return manHourCost;
	}
	/**
	 * 设置：消息id(保养定时提醒)
	 */
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	/**
	 * 获取：消息id(保养定时提醒)
	 */
	public Long getMessageId() {
		return messageId;
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
	 * 设置：删除标志(1删除)
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志(1删除)
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
