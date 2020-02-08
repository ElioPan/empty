package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;

/**
 * 求购单主表单类
 * @author ABC
 *
 */
public class PurchaseFirstDO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
    private Long id;
    //求购单编号
    @ApiModelProperty(value = "求购单编号")
    private String purchaseCode;
    //公司供应商
    @ApiModelProperty(value = "公司供应商")
    private Long company;
    //申请人
    @ApiModelProperty(value = "申请人")
    private Long applicant;
    //申请时间
    @ApiModelProperty(value = "申请时间")
    private Date applicantTime;
    //预交日期
    @ApiModelProperty(value = "预交日期")
    private Date advanceDate;
    //优惠折扣率
    @ApiModelProperty(value = "优惠折扣率")
    private BigDecimal discountRate;
    //优惠金额
    @ApiModelProperty(value = "优惠金额")
	private BigDecimal preferentialAmount;
    //优惠后金额
    @ApiModelProperty(value = "优惠后金额")
    private BigDecimal payAmount;
    //审核状态
    @ApiModelProperty(value = "审核状态")
    private Long auditStatus;
    //备注
    @ApiModelProperty(value = "备注")
    private String remarks;
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
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
    //删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
    private Integer delFlag; 
 
	public String getPurchaseCode() {
		return purchaseCode;
	}
	
	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
	
	public Long getCompany() {
		return company;
	}
	
	public void setCompany(Long company) {
		this.company = company;
	}
	
	public Long getApplicant() {
		return applicant;
	}
	
	public void setApplicant(Long applicant) {
		this.applicant = applicant;
	}
	
	public Date getApplicantTime() {
		return applicantTime;
	}
	
	public void setApplicantTime(Date applicantTime) {
		this.applicantTime = applicantTime;
	}
	
	public Date getAdvanceDate() {
		return advanceDate;
	}
	
	public void setAdvanceDate(Date advanceDate) {
		this.advanceDate = advanceDate;
	}
	
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
	
	public BigDecimal getPreferentialAmount() {
		return preferentialAmount;
	}
	
	public void setPreferentialAmount(BigDecimal preferentialAmount) {
		this.preferentialAmount = preferentialAmount;
	}
	
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Long getAuditor() {
		return auditor;
	}
	
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}
	
	public Date getAuditorTime() {
		return auditorTime;
	}
	
	public void setAuditorTime(Date auditorTime) {
		this.auditorTime = auditorTime;
	}
	
	public Long getCreateBy() {
		return createBy;
	}
	
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Long getUpdateBy() {
		return updateBy;
	}
	
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getDelFlag() {
		return delFlag;
	}
	
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Long auditStatus) {
		this.auditStatus = auditStatus;
	}
}
