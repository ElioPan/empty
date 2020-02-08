package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序生产设备
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:04
 */
@ApiModel(value = "工序生产设备")
public class ProcessDeviceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工序主键
    @ApiModelProperty(value = "工序主键")
	private Long processId;
	//代码
    @ApiModelProperty(value = "代码")
	private String code;
	//生产设备id
    @ApiModelProperty(value = "生产设备id")
	private Long deviceId;
	//作业时长
    @ApiModelProperty(value = "作业时长")
	private BigDecimal operationTime;
	//机台工时（分钟）
    @ApiModelProperty(value = "机台工时（分钟）")
	private Integer manHour;
	//备注
    @ApiModelProperty(value = "备注")
	private String remark;
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
	 * 设置：工序主键
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	/**
	 * 获取：工序主键
	 */
	public Long getProcessId() {
		return processId;
	}
	/**
	 * 设置：代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：生产设备id
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：生产设备id
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：作业时长
	 */
	public void setOperationTime(BigDecimal operationTime) {
		this.operationTime = operationTime;
	}
	/**
	 * 获取：作业时长
	 */
	public BigDecimal getOperationTime() {
		return operationTime;
	}
	/**
	 * 设置：机台工时（分钟）
	 */
	public void setManHour(Integer manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：机台工时（分钟）
	 */
	public Integer getManHour() {
		return manHour;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
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
}
