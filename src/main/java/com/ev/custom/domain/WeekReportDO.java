package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 周报
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-13 16:22:59
 */
@ApiModel(value = "周报")
public class WeekReportDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//本周完成工作
	@ApiModelProperty(value = "本周完成工作")
	private String comContent;
	//本周工作总结
	@ApiModelProperty(value = "本周工作总结")
	private String contentSum;
	//下周工作计划
	@ApiModelProperty(value = "下周工作计划")
	private String contentPlan;
	//备注
	@ApiModelProperty(value = "备注")
	private String description;
	//本周开始时间
	@ApiModelProperty(value = "本周开始时间")
	private Date startTime;
	//本周结束时间
	@ApiModelProperty(value = "本周结束时间")
	private Date endTime;
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
	//周报状态
	@ApiModelProperty(value = "周报状态")
	private Integer status;

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
	 * 设置：本周完成工作
	 */
	public void setComContent(String comContent) {
		this.comContent = comContent;
	}
	/**
	 * 获取：本周完成工作
	 */
	public String getComContent() {
		return comContent;
	}
	/**
	 * 设置：本周工作总结
	 */
	public void setContentSum(String contentSum) {
		this.contentSum = contentSum;
	}
	/**
	 * 获取：本周工作总结
	 */
	public String getContentSum() {
		return contentSum;
	}
	/**
	 * 设置：下周工作计划
	 */
	public void setContentPlan(String contentPlan) {
		this.contentPlan = contentPlan;
	}
	/**
	 * 获取：下周工作计划
	 */
	public String getContentPlan() {
		return contentPlan;
	}
	/**
	 * 设置：备注
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：备注
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：本周开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：本周开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：本周结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：本周结束时间
	 */
	public Date getEndTime() {
		return endTime;
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
	 * 设置：周报状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：周报状态
	 */
	public Integer getStatus() {
		return status;
	}
}
