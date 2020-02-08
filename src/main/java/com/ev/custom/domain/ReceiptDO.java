package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:36
 */
@ApiModel(value = "收款单类")
public class ReceiptDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//客户名称
    @ApiModelProperty(value = "客户名称")
	private String username;
	//应收总额
    @ApiModelProperty(value = "应收总额")
	private BigDecimal totalReceivables;
	//收款日期
    @ApiModelProperty(value = "收款日期")
	private Date receivablesDate;
	//收款人
    @ApiModelProperty(value = "收款人")
	private String payee;
	//收款类型
    @ApiModelProperty(value = "收款类型")
	private Long collectionType;
	//收款单号
    @ApiModelProperty(value = "收款单号")
	private String receiptNum;
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
	 * 设置：客户名称
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：客户名称
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：应收总额
	 */
	public void setTotalReceivables(BigDecimal totalReceivables) {
		this.totalReceivables = totalReceivables;
	}
	/**
	 * 获取：应收总额
	 */
	public BigDecimal getTotalReceivables() {
		return totalReceivables;
	}
	/**
	 * 设置：收款日期
	 */
	public void setReceivablesDate(Date receivablesDate) {
		this.receivablesDate = receivablesDate;
	}
	/**
	 * 获取：收款日期
	 */
	public Date getReceivablesDate() {
		return receivablesDate;
	}
	/**
	 * 设置：收款人
	 */
	public void setPayee(String payee) {
		this.payee = payee;
	}
	/**
	 * 获取：收款人
	 */
	public String getPayee() {
		return payee;
	}
	/**
	 * 设置：收款类型
	 */
	public void setCollectionType(Long collectionType) {
		this.collectionType = collectionType;
	}
	/**
	 * 获取：收款类型
	 */
	public Long getCollectionType() {
		return collectionType;
	}
	/**
	 * 设置：收款单号
	 */
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	/**
	 * 获取：收款单号
	 */
	public String getReceiptNum() {
		return receiptNum;
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
