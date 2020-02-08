package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-08 13:11:30
 */
@ApiModel(value = "银行转账单明细类")
public class BanktransferslipItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long tid;
	//转出帐号
    @ApiModelProperty(value = "转出帐号")
	private String transferredAccount;
	//转入帐号
    @ApiModelProperty(value = "转入帐号")
	private String transferAccount;
	//金额
    @ApiModelProperty(value = "金额")
	private BigDecimal money;
	//结算方式
    @ApiModelProperty(value = "结算方式")
	private Long settlementMethod;
	//结算号
    @ApiModelProperty(value = "结算号")
	private String settlementNumber;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long banktransferslipId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

	/**
	 * 设置：id
	 */
	public void setTid(Long tid) {
		this.tid = tid;
	}
	/**
	 * 获取：id
	 */
	public Long getTid() {
		return tid;
	}
	/**
	 * 设置：转出帐号
	 */
	public void setTransferredAccount(String transferredAccount) {
		this.transferredAccount = transferredAccount;
	}
	/**
	 * 获取：转出帐号
	 */
	public String getTransferredAccount() {
		return transferredAccount;
	}
	/**
	 * 设置：转入帐号
	 */
	public void setTransferAccount(String transferAccount) {
		this.transferAccount = transferAccount;
	}
	/**
	 * 获取：转入帐号
	 */
	public String getTransferAccount() {
		return transferAccount;
	}
	/**
	 * 设置：金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：金额
	 */
	public BigDecimal getMoney() {
		return money;
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
	 * 设置：结算号
	 */
	public void setSettlementNumber(String settlementNumber) {
		this.settlementNumber = settlementNumber;
	}
	/**
	 * 获取：结算号
	 */
	public String getSettlementNumber() {
		return settlementNumber;
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
	 * 设置：主表id
	 */
	public void setBanktransferslipId(Long banktransferslipId) {
		this.banktransferslipId = banktransferslipId;
	}
	/**
	 * 获取：主表id
	 */
	public Long getBanktransferslipId() {
		return banktransferslipId;
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
