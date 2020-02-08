package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 付款申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:36
 */
@ApiModel(value = "付款申请")
public class PayApplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//付款总额
    @ApiModelProperty(value = "付款总额")
	private Double totalNumber;
	//付款方式
    @ApiModelProperty(value = "付款方式")
	private Long payMethod;
	//支付日期
    @ApiModelProperty(value = "支付日期")
	private Date payDate;
	//支付对象
    @ApiModelProperty(value = "支付对象")
	private String payUser;
	//开户行
    @ApiModelProperty(value = "开户行")
	private String openingBlank;
	//银行账号
    @ApiModelProperty(value = "银行账号")
	private String blankAccount;
	//付款事由
    @ApiModelProperty(value = "付款事由")
	private String payReason;
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
	//状态
    @ApiModelProperty(value = "状态")
	private Integer status;
	//流程实例ID
    @ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;
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
	 * 设置：付款总额
	 */
	public void setTotalNumber(Double totalNumber) {
		this.totalNumber = totalNumber;
	}
	/**
	 * 获取：付款总额
	 */
	public Double getTotalNumber() {
		return totalNumber;
	}
	/**
	 * 设置：付款方式
	 */
	public void setPayMethod(Long payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * 获取：付款方式
	 */
	public Long getPayMethod() {
		return payMethod;
	}
	/**
	 * 设置：支付日期
	 */
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	/**
	 * 获取：支付日期
	 */
	public Date getPayDate() {
		return payDate;
	}
	/**
	 * 设置：支付对象
	 */
	public void setPayUser(String payUser) {
		this.payUser = payUser;
	}
	/**
	 * 获取：支付对象
	 */
	public String getPayUser() {
		return payUser;
	}
	/**
	 * 设置：开户行
	 */
	public void setOpeningBlank(String openingBlank) {
		this.openingBlank = openingBlank;
	}
	/**
	 * 获取：开户行
	 */
	public String getOpeningBlank() {
		return openingBlank;
	}
	/**
	 * 设置：银行账号
	 */
	public void setBlankAccount(String blankAccount) {
		this.blankAccount = blankAccount;
	}
	/**
	 * 获取：银行账号
	 */
	public String getBlankAccount() {
		return blankAccount;
	}
	/**
	 * 设置：付款事由
	 */
	public void setPayReason(String payReason) {
		this.payReason = payReason;
	}
	/**
	 * 获取：付款事由
	 */
	public String getPayReason() {
		return payReason;
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
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：流程实例ID
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	/**
	 * 获取：流程实例ID
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
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
