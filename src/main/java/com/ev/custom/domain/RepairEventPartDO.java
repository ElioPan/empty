package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 修维事件与备件中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 09:42:12
 */
@ApiModel(value = "修维事件与备件中间表")
public class RepairEventPartDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//备件ID
	@ApiModelProperty(value = "备件ID")
	private Long partId;
	//数量
	@ApiModelProperty(value = "数量")
	private Integer amount;
	//总额
	@ApiModelProperty(value = "总额")
	private Double total;
	//备注
	@ApiModelProperty(value = "备注")
	private String remark;
	//事件ID
	@ApiModelProperty(value = "事件ID")
	private Long eventId;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;
	//单价
	@ApiModelProperty(value = "单价")
	private Double price;

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
	 * 设置：备件ID
	 */
	public void setPartId(Long partId) {
		this.partId = partId;
	}
	/**
	 * 获取：备件ID
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
	 * 设置：总额
	 */
	public void setTotal(Double total) {
		this.total = total;
	}
	/**
	 * 获取：总额
	 */
	public Double getTotal() {
		return total;
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
	 * 设置：事件ID
	 */
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	/**
	 * 获取：事件ID
	 */
	public Long getEventId() {
		return eventId;
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
	 * 设置：创建人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：创建人
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
	 * 设置：单价
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Double getPrice() {
		return price;
	}
}
