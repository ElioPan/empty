package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养计划表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 12:58:23
 */
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
	private Integer type;
	//工时
	@ApiModelProperty(value = "工时")
	private Double manHour;
	//任务状态：启用；停用
	@ApiModelProperty(value = "任务状态：暂存，启用；停用")
	private Integer status;
	//任务结果
	@ApiModelProperty(value = "任务结果")
	private Integer result;
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
	 * 设置：计划名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：计划名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：关联设备
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：关联设备
	 */
	public Long getDeviceId() {
		return deviceId;
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
	 * 设置：责任人电话
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	/**
	 * 获取：责任人电话
	 */
	public String getCellphone() {
		return cellphone;
	}
	/**
	 * 设置：保养级别
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：保养级别
	 */
	public Integer getType() {
		return type;
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
	 * 设置：任务状态：启用；停用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：任务状态：启用；停用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：任务结果
	 */
	public void setResult(Integer result) {
		this.result = result;
	}
	/**
	 * 获取：任务结果
	 */
	public Integer getResult() {
		return result;
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
	 * 设置：间隔时间
	 */
	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}
	/**
	 * 获取：间隔时间
	 */
	public Integer getIntervalTime() {
		return intervalTime;
	}
	/**
	 * 设置：到期提醒
	 */
	public void setExpireday(Integer expireday) {
		this.expireday = expireday;
	}
	/**
	 * 获取：到期提醒
	 */
	public Integer getExpireday() {
		return expireday;
	}
	/**
	 * 设置：计划开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：计划开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：计划结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：计划结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateTime() {
		return createTime;
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
	 * 设置：保养部门id
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：保养部门id
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：删除状态：1删除
	 */
	public void setDeletStatus(Long deletStatus) {
		this.deletStatus = deletStatus;
	}
	/**
	 * 获取：删除状态：1删除
	 */
	public Long getDeletStatus() {
		return deletStatus;
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
	 * 设置：删除状态：1删除
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态：1删除
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
