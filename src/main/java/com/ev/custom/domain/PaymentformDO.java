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
 * @date 2019-11-06 13:23:31
 */
@ApiModel(value = "付款单类")
public class PaymentformDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//供货商
    @ApiModelProperty(value = "供货商")
	private Long company;
	//应收总额
    @ApiModelProperty(value = "应收总额")
	private BigDecimal totalReceivables;
	//付款日期
    @ApiModelProperty(value = "付款日期")
	private Date payDate;
	//付款人
    @ApiModelProperty(value = "付款人")
	private String drawee;
	//付款单号
    @ApiModelProperty(value = "付款单号")
	private String payNum;
	//付款类型
    @ApiModelProperty(value = "付款类型")
	private Long payType;
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
	 * 设置：供货商
	 */
	public void setCompany(Long company) {
		this.company = company;
	}
	/**
	 * 获取：供货商
	 */
	public Long getCompany() {
		return company;
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
	 * 设置：付款单号
	 */
	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}
	/**
	 * 获取：付款单号
	 */
	public String getPayNum() {
		return payNum;
	}
	/**
	 * 设置：付款类型
	 */
	public void setPayType(Long payType) {
		this.payType = payType;
	}
	/**
	 * 获取：付款类型
	 */
	public Long getPayType() {
		return payType;
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
