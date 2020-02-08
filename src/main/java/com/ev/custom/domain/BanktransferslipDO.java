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
 * @date 2019-11-08 13:11:30
 */
@ApiModel(value = "银行转账单类")
public class BanktransferslipDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//转帐日期
    @ApiModelProperty(value = "转帐日期")
	private Date transferDate;
	//经办人
    @ApiModelProperty(value = "经办人")
	private Long handlePeople;
	//转帐单号
    @ApiModelProperty(value = "转帐单号")
	private String transferNum;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditStatus;
	//审核人
    @ApiModelProperty(value = "审核人")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditorTime;
	//制单人
    @ApiModelProperty(value = "制单人")
	private Long createBy;
	//制单时间
    @ApiModelProperty(value = "制单时间")
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
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：转帐日期
	 */
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	/**
	 * 获取：转帐日期
	 */
	public Date getTransferDate() {
		return transferDate;
	}
	/**
	 * 设置：经办人
	 */
	public void setHandlePeople(Long handlePeople) {
		this.handlePeople = handlePeople;
	}
	/**
	 * 获取：经办人
	 */
	public Long getHandlePeople() {
		return handlePeople;
	}
	/**
	 * 设置：转帐单号
	 */
	public void setTransferNum(String transferNum) {
		this.transferNum = transferNum;
	}
	/**
	 * 获取：转帐单号
	 */
	public String getTransferNum() {
		return transferNum;
	}
	/**
	 * 设置：审核状态
	 */
	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * 获取：审核状态
	 */
	public Long getAuditStatus() {
		return auditStatus;
	}
	/**
	 * 设置：审核人
	 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}
	/**
	 * 获取：审核人
	 */
	public Long getAuditor() {
		return auditor;
	}
	/**
	 * 设置：审核时间
	 */
	public void setAuditorTime(Date auditorTime) {
		this.auditorTime = auditorTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getAuditorTime() {
		return auditorTime;
	}
	/**
	 * 设置：制单人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：制单人
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：制单时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：制单时间
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
