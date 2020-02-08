package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 保养工单与实际保养项目中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 10:19:51
 */
@ApiModel(value = "保养工单与实际保养项目中间表")
public class UpkeepRecordProjectDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//保养计划ID
	@ApiModelProperty(value = "保养计划ID")
	private Long planId;
	//保养工单ID
	@ApiModelProperty(value = "保养工单ID")
	private Long recordId;
	//养保项目ID
	@ApiModelProperty(value = "养保项目ID")
	private Long projectId;
	//工时
	@ApiModelProperty(value = "工时")
	private Integer manhour;
	//工时费用
	@ApiModelProperty(value = "工时费用")
	private BigDecimal manHourCost;
	/**
	 * 设置：工时费用
	 */
	public void setManHourCost(BigDecimal manHourCost) {
		this.manHourCost = manHourCost;
	}
	/**
	 * 获取：工时费用
	 */
	public BigDecimal getManHourCost() {
		return manHourCost;
	}
	//结果:1为正常，0为异常
	@ApiModelProperty(value = "结果:1为正常，0为异常")
	private Integer result;
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
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
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
	 * 设置：保养计划ID
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * 获取：保养计划ID
	 */
	public Long getPlanId() {
		return planId;
	}
	/**
	 * 设置：保养工单ID
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：保养工单ID
	 */
	public Long getRecordId() {
		return recordId;
	}
	/**
	 * 设置：养保项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：养保项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：工时
	 */
	public void setManhour(Integer manhour) {
		this.manhour = manhour;
	}
	/**
	 * 获取：工时
	 */
	public Integer getManhour() {
		return manhour;
	}
	/**
	 * 设置：结果:1为正常，0为异常
	 */
	public void setResult(Integer result) {
		this.result = result;
	}
	/**
	 * 获取：结果:1为正常，0为异常
	 */
	public Integer getResult() {
		return result;
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
}
