package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 报销申请明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-12 15:52:33
 */
@ApiModel(value = "报销申请明细")
public class ReimApplyItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//
    @ApiModelProperty(value = "")
	private Long reimApplyId;
	//报销类型
    @ApiModelProperty(value = "报销类型")
	private Long type;
	//
    @ApiModelProperty(value = "")
	private Date reiDate;
	//报销金额
    @ApiModelProperty(value = "报销金额")
	private Double reiCount;
	//报销明细
    @ApiModelProperty(value = "报销明细")
	private String reiItemDescription;
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
	 * 设置：
	 */
	public void setReimApplyId(Long reimApplyId) {
		this.reimApplyId = reimApplyId;
	}
	/**
	 * 获取：
	 */
	public Long getReimApplyId() {
		return reimApplyId;
	}
	/**
	 * 设置：报销类型
	 */
	public void setType(Long type) {
		this.type = type;
	}
	/**
	 * 获取：报销类型
	 */
	public Long getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setReiDate(Date reiDate) {
		this.reiDate = reiDate;
	}
	/**
	 * 获取：
	 */
	public Date getReiDate() {
		return reiDate;
	}
	/**
	 * 设置：报销金额
	 */
	public void setReiCount(Double reiCount) {
		this.reiCount = reiCount;
	}
	/**
	 * 获取：报销金额
	 */
	public Double getReiCount() {
		return reiCount;
	}
	/**
	 * 设置：报销明细
	 */
	public void setReiItemDescription(String reiItemDescription) {
		this.reiItemDescription = reiItemDescription;
	}
	/**
	 * 获取：报销明细
	 */
	public String getReiItemDescription() {
		return reiItemDescription;
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
