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
 * @date 2019-10-17 13:06:48
 */
@ApiModel(value = "采购票据类")
public class PurchasebillDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//采购票据类id
    @ApiModelProperty(value = "自增主键id")
	private Long id;
	//公司/供应商
    @ApiModelProperty(value = "公司/供应商")
	private Long company;
	//开票日期
    @ApiModelProperty(value = "开票日期")
	private Date invoiceDate;
	//收票人
    @ApiModelProperty(value = "收票人")
	private String ticketCollector;
	//票据单号
    @ApiModelProperty(value = "票据单号")
	private String billNumber;
	//发票号码
    @ApiModelProperty(value = "发票号码")
	private Long invoiceNumber;
    //发票金额
    @ApiModelProperty(value = "发票金额")
	private BigDecimal invoiceMoney;
	//票据类型
    @ApiModelProperty(value = "票据类型")
	private Long billType;
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
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

	public BigDecimal getInvoiceMoney() {
		return invoiceMoney;
	}
	
	public void setInvoiceMoney(BigDecimal invoiceMoney) {
		this.invoiceMoney = invoiceMoney;
	}
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
	 * 设置：公司/供应商
	 */
	public void setCompany(Long company) {
		this.company = company;
	}
	/**
	 * 获取：公司/供应商
	 */
	public Long getCompany() {
		return company;
	}
	/**
	 * 设置：开票日期
	 */
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	/**
	 * 获取：开票日期
	 */
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	/**
	 * 设置：收票人
	 */
	public void setTicketCollector(String ticketCollector) {
		this.ticketCollector = ticketCollector;
	}
	/**
	 * 获取：收票人
	 */
	public String getTicketCollector() {
		return ticketCollector;
	}
	/**
	 * 设置：票据单号
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	/**
	 * 获取：票据单号
	 */
	public String getBillNumber() {
		return billNumber;
	}
	/**
	 * 设置：发票号码
	 */
	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	/**
	 * 获取：发票号码
	 */
	public Long getInvoiceNumber() {
		return invoiceNumber;
	}
	/**
	 * 设置：票据类型
	 */
	public void setBillType(Long billType) {
		this.billType = billType;
	}
	/**
	 * 获取：票据类型
	 */
	public Long getBillType() {
		return billType;
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
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
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
