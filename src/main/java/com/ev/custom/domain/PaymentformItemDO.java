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
 * @date 2019-11-06 13:23:32
 */
@ApiModel(value = "付款单明细类")
public class PaymentformItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long pid;
	//单据日期
    @ApiModelProperty(value = "单据日期")
	private Date documentDate;
	//应付日期
    @ApiModelProperty(value = "应付日期")
	private Date dueDate;
	//应付金额
    @ApiModelProperty(value = "应付金额")
	private BigDecimal payMoney;
	//已付金额
    @ApiModelProperty(value = "已付金额")
	private BigDecimal amountPaid;
	//本次付款
    @ApiModelProperty(value = "本次付款")
	private BigDecimal thisPay;
	//未付金额
    @ApiModelProperty(value = "未付金额")
	private BigDecimal unpayMoney;
	//付款帐号
    @ApiModelProperty(value = "付款帐号")
	private String payAccount;
	//结算方式
    @ApiModelProperty(value = "结算方式")
	private Long settlementMethod;
	//结算单号
    @ApiModelProperty(value = "结算单号")
	private String settlementNum;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//源单编号
    @ApiModelProperty(value = "源单编号")
	private String sourceNum;
	//业务类别
    @ApiModelProperty(value = "业务类别")
	private Long sourceType;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long paymentformId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

	/**
	 * 设置：id
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：id
	 */
	public Long getPid() {
		return pid;
	}
	/**
	 * 设置：单据日期
	 */
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	/**
	 * 获取：单据日期
	 */
	public Date getDocumentDate() {
		return documentDate;
	}
	/**
	 * 设置：应付日期
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * 获取：应付日期
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * 设置：应付金额
	 */
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	/**
	 * 获取：应付金额
	 */
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	/**
	 * 设置：已付金额
	 */
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}
	/**
	 * 获取：已付金额
	 */
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}
	/**
	 * 设置：本次付款
	 */
	public void setThisPay(BigDecimal thisPay) {
		this.thisPay = thisPay;
	}
	/**
	 * 获取：本次付款
	 */
	public BigDecimal getThisPay() {
		return thisPay;
	}
	/**
	 * 设置：未付金额
	 */
	public void setUnpayMoney(BigDecimal unpayMoney) {
		this.unpayMoney = unpayMoney;
	}
	/**
	 * 获取：未付金额
	 */
	public BigDecimal getUnpayMoney() {
		return unpayMoney;
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
	 * 设置：结算方式
	 */
	public void setSettlementMethod(Long settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	/**
	 * 获取：结算方式
	 */
	public Long getSettlementMethod() {
		return settlementMethod;
	}
	/**
	 * 设置：结算单号
	 */
	public void setSettlementNum(String settlementNum) {
		this.settlementNum = settlementNum;
	}
	/**
	 * 获取：结算单号
	 */
	public String getSettlementNum() {
		return settlementNum;
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
	 * 设置：源单编号
	 */
	public void setSourceNum(String sourceNum) {
		this.sourceNum = sourceNum;
	}
	/**
	 * 获取：源单编号
	 */
	public String getSourceNum() {
		return sourceNum;
	}
	/**
	 * 设置：业务类别
	 */
	public void setSourceType(Long sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * 获取：业务类别
	 */
	public Long getSourceType() {
		return sourceType;
	}
	/**
	 * 设置：主表id
	 */
	public void setPaymentformId(Long paymentformId) {
		this.paymentformId = paymentformId;
	}
	/**
	 * 获取：主表id
	 */
	public Long getPaymentformId() {
		return paymentformId;
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
