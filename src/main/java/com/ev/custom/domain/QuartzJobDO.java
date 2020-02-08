package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-15 10:47:59
 */
@ApiModel(value = "")
public class QuartzJobDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//任务名称
    @ApiModelProperty(value = "任务名称")
	private String name;
	//
    @ApiModelProperty(value = "")
	private String cronExpression;
	//
    @ApiModelProperty(value = "")
	private String description;
	//
    @ApiModelProperty(value = "")
	private String jobClassName;
	//
    @ApiModelProperty(value = "")
	private String parameter;
	//
    @ApiModelProperty(value = "")
	private Integer status;
	//
    @ApiModelProperty(value = "")
	private Long createBy;
	//
    @ApiModelProperty(value = "")
	private Date createTime;
	//
    @ApiModelProperty(value = "")
	private Long updateBy;
	//
    @ApiModelProperty(value = "")
	private Date updateTime;
	//
    @ApiModelProperty(value = "")
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
	 * 设置：任务名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：任务名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	/**
	 * 获取：
	 */
	public String getCronExpression() {
		return cronExpression;
	}
	/**
	 * 设置：
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：
	 */
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}
	/**
	 * 获取：
	 */
	public String getJobClassName() {
		return jobClassName;
	}
	/**
	 * 设置：
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	/**
	 * 获取：
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * 设置：
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
