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
 * @date 2019-11-01 14:40:37
 */
@ApiModel(value = "收款单明细类")
public class ReceiptItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long rid;
	//单据日期
    @ApiModelProperty(value = "单据日期")
	private Date documentDate;
	//应收日期
    @ApiModelProperty(value = "应收日期")
	private Date receivableDate;
	//应收金额
    @ApiModelProperty(value = "应收金额")
	private BigDecimal receivableMoney;
	//已收金额
    @ApiModelProperty(value = "已收金额")
	private BigDecimal receivedMoney;
	//本次收款
    @ApiModelProperty(value = "本次收款")
	private BigDecimal thisReceivable;
	//未收金额
    @ApiModelProperty(value = "未收金额")
	private BigDecimal unpayMoney;
	//收款帐号
    @ApiModelProperty(value = "收款帐号")
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
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNum;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long receiptId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

	/**
	 * 设置：id
	 */
	public void setRid(Long rid) {
		this.rid = rid;
	}
	/**
	 * 获取：id
	 */
	public Long getRid() {
		return rid;
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
	 * 设置：应收日期
	 */
	public void setReceivableDate(Date receivableDate) {
		this.receivableDate = receivableDate;
	}
	/**
	 * 获取：应收日期
	 */
	public Date getReceivableDate() {
		return receivableDate;
	}
	/**
	 * 设置：应收金额
	 */
	public void setReceivableMoney(BigDecimal receivableMoney) {
		this.receivableMoney = receivableMoney;
	}
	/**
	 * 获取：应收金额
	 */
	public BigDecimal getReceivableMoney() {
		return receivableMoney;
	}
	/**
	 * 设置：已收金额
	 */
	public void setReceivedMoney(BigDecimal receivedMoney) {
		this.receivedMoney = receivedMoney;
	}
	/**
	 * 获取：已收金额
	 */
	public BigDecimal getReceivedMoney() {
		return receivedMoney;
	}
	/**
	 * 设置：本次收款
	 */
	public void setThisReceivable(BigDecimal thisReceivable) {
		this.thisReceivable = thisReceivable;
	}
	/**
	 * 获取：本次收款
	 */
	public BigDecimal getThisReceivable() {
		return thisReceivable;
	}
	/**
	 * 设置：未收金额
	 */
	public void setUnpayMoney(BigDecimal unpayMoney) {
		this.unpayMoney = unpayMoney;
	}
	/**
	 * 获取：未收金额
	 */
	public BigDecimal getUnpayMoney() {
		return unpayMoney;
	}
	/**
	 * 设置：收款帐号
	 */
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	/**
	 * 获取：收款帐号
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
	 * 设置：源单类型
	 */
	public void setSourceType(Long sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * 获取：源单类型
	 */
	public Long getSourceType() {
		return sourceType;
	}
	/**
	 * 设置：来源单号
	 */
	public void setSourceNum(String sourceNum) {
		this.sourceNum = sourceNum;
	}
	/**
	 * 获取：来源单号
	 */
	public String getSourceNum() {
		return sourceNum;
	}
	/**
	 * 设置：主表id
	 */
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}
	/**
	 * 获取：主表id
	 */
	public Long getReceiptId() {
		return receiptId;
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
