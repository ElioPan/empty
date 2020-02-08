package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养计划与备件中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-28 10:47:44
 */
@ApiModel(value = "保养计划与备件中间表")
public class UpkeepPlanPartDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//保养计划ID
	@ApiModelProperty(value = "保养计划ID")
	private Long planId;
	//保养备件ID
	@ApiModelProperty(value = "保养备件ID")
	private Long partId;
	//数量
	@ApiModelProperty(value = "数量")
	private Integer amount;


	//备件单价
	@ApiModelProperty(value = "备件单价")
	private Double spartPrice;
	//备件单位名
	@ApiModelProperty(value = "备件单位名")
	private String spartUnit;
	//总金额
	@ApiModelProperty(value = "总金额")
	private Double spartSum;

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
	//备注
	@ApiModelProperty(value = "备注")
	private String remark;

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
	 * 设置：保养备件ID
	 */
	public void setPartId(Long partId) {
		this.partId = partId;
	}
	/**
	 * 获取：保养备件ID
	 */
	public Long getPartId() {
		return partId;
	}
	/**
	 * 设置：数量
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * 获取：数量
	 */
	public Integer getAmount() {
		return amount;
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
}
