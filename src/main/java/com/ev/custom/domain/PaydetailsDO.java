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
 * @date 2019-10-29 16:36:57
 */
@ApiModel(value = "付款详情")
public class PaydetailsDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//客户名称
    @ApiModelProperty(value = "客户名称")
	private String username;
	//付款单号
    @ApiModelProperty(value = "付款单号")
	private String payNumber;
	//付款日期
    @ApiModelProperty(value = "付款日期")
	private Date payDate;
	//收款人
    @ApiModelProperty(value = "收款人")
	private String payee;
	//退款金额
    @ApiModelProperty(value = "退款金额")
	private BigDecimal refundAmount;
	//付款人
    @ApiModelProperty(value = "付款人")
	private String drawee;
	//付款帐号
    @ApiModelProperty(value = "付款帐号")
	private String payAccount;
	//结算方式
    @ApiModelProperty(value = "结算方式")
	private String settlementMethod;
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
	 * 设置：付款单号
	 */
	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
	}
	/**
	 * 获取：付款单号
	 */
	public String getPayNumber() {
		return payNumber;
	}
	/**
	 * 设置：付款日期
	 */
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	/**
	 * 获取：付款日期
	 */
	public Date getPayDate() {
		return payDate;
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
	 * 设置：退款金额
	 */
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	/**
	 * 获取：退款金额
	 */
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	/**
	 * 设置：付款人
	 */
	public void setDrawee(String drawee) {
		this.drawee = drawee;
	}
	/**
	 * 获取：付款人
	 */
	public String getDrawee() {
		return drawee;
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
	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	/**
	 * 获取：结算方式
	 */
	public String getSettlementMethod() {
		return settlementMethod;
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
