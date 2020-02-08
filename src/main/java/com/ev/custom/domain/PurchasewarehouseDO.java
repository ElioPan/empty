package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购入库主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 13:35:16
 */
@ApiModel(value = "采购入库主表")
public class PurchasewarehouseDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//采购入库主表
    @ApiModelProperty(value = "采购入库主表自增主键")
	private Long id;
	//来源公司/供应商
    @ApiModelProperty(value = "来源公司/供应商")
	private Long sourceCompany;
	//入库时间
    @ApiModelProperty(value = "入库时间")
	private Date inTime;
	//出/入库操作员
    @ApiModelProperty(value = "出/入库操作员")
	private Long operator;
	//入库单据号
    @ApiModelProperty(value = "入库单据号")
	private String inheadCode;
	//采购方式
    @ApiModelProperty(value = "采购方式")
	private Long purchaseType;
	//付款帐号
    @ApiModelProperty(value = "付款帐号")
	private String payAccount;
	//审核标志：（审核/反审）
    @ApiModelProperty(value = "审核标志：（审核/反审）")
	private Long auditSign;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
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
	//删除状态（）
    @ApiModelProperty(value = "删除状态（）")
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
	 * 设置：来源公司/供应商
	 */
	public void setSourceCompany(Long sourceCompany) {
		this.sourceCompany = sourceCompany;
	}
	/**
	 * 获取：来源公司/供应商
	 */
	public Long getSourceCompany() {
		return sourceCompany;
	}
	/**
	 * 设置：入库时间
	 */
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}
	/**
	 * 获取：入库时间
	 */
	public Date getInTime() {
		return inTime;
	}
	/**
	 * 设置：出/入库操作员
	 */
	public void setOperator(Long operator) {
		this.operator = operator;
	}
	/**
	 * 获取：出/入库操作员
	 */
	public Long getOperator() {
		return operator;
	}
	/**
	 * 设置：入库单据号
	 */
	public void setInheadCode(String inheadCode) {
		this.inheadCode = inheadCode;
	}
	/**
	 * 获取：入库单据号
	 */
	public String getInheadCode() {
		return inheadCode;
	}
	/**
	 * 设置：采购方式
	 */
	public void setPurchaseType(Long purchaseType) {
		this.purchaseType = purchaseType;
	}
	/**
	 * 获取：采购方式
	 */
	public Long getPurchaseType() {
		return purchaseType;
	}
	/**
	 * 设置：付款帐号
	 */
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	/**
	 * 获取：付款帐号
	 */
	public String getPayAccount() {
		return payAccount;
	}
	/**
	 * 设置：审核标志：（审核/反审）
	 */
	public void setAuditSign(Long auditSign) {
		this.auditSign = auditSign;
	}
	/**
	 * 获取：审核标志：（审核/反审）
	 */
	public Long getAuditSign() {
		return auditSign;
	}
	/**
	 * 设置：审核人员
	 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}
	/**
	 * 获取：审核人员
	 */
	public Long getAuditor() {
		return auditor;
	}
	/**
	 * 设置：审核时间
	 */
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getAuditTime() {
		return auditTime;
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
	 * 设置：删除状态（）
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态（）
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
