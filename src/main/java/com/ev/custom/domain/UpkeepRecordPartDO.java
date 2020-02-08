package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养工单与实际使用备件中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 10:37:18
 */
@ApiModel(value = "保养工单与实际使用备件中间表")
public class UpkeepRecordPartDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Integer id;
	//保养计划ID
	@ApiModelProperty(value = "保养计划ID")
	private Long planId;
	//保养工单ID
	@ApiModelProperty(value = "保养工单ID")
	private Long recordId;
	//备件id
	@ApiModelProperty(value = "备件id")
	private Long partId;
	//使用备件数量
	@ApiModelProperty(value = "使用备件数量")
	private String spartAmount;
	//备件单价
	@ApiModelProperty(value = "备件单价")
	private Double spartPrice;
	//备件单位名
	@ApiModelProperty(value = "备件单位名")
	private String spartUnit;
	//总金额
	@ApiModelProperty(value = "总金额")
	private Double spartSum;
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
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
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
	 * 设置：备件id
	 */
	public void setPartId(Long partId) {
		this.partId = partId;
	}
	/**
	 * 获取：备件id
	 */
	public Long getPartId() {
		return partId;
	}
	/**
	 * 设置：使用备件数量
	 */
	public void setSpartAmount(String spartAmount) {
		this.spartAmount = spartAmount;
	}
	/**
	 * 获取：使用备件数量
	 */
	public String getSpartAmount() {
		return spartAmount;
	}
	/**
	 * 设置：备件单价
	 */
	public void setSpartPrice(Double spartPrice) {
		this.spartPrice = spartPrice;
	}
	/**
	 * 获取：备件单价
	 */
	public Double getSpartPrice() {
		return spartPrice;
	}
	/**
	 * 设置：备件单位名
	 */
	public void setSpartUnit(String spartUnit) {
		this.spartUnit = spartUnit;
	}
	/**
	 * 获取：备件单位名
	 */
	public String getSpartUnit() {
		return spartUnit;
	}
	/**
	 * 设置：总金额
	 */
	public void setSpartSum(Double spartSum) {
		this.spartSum = spartSum;
	}
	/**
	 * 获取：总金额
	 */
	public Double getSpartSum() {
		return spartSum;
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
