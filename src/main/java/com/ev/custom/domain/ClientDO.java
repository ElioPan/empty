package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 客户表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 11:11:37
 */
@ApiModel(value = "客户表")
public class ClientDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//客户编号
	@ApiModelProperty(value = "客户编号")
	private String code;
	//客户名称
	@ApiModelProperty(value = "客户名称")
	private String name;
	//客户类型
	@ApiModelProperty(value = "客户类型")
	private Integer type;
	//电话
	@ApiModelProperty(value = "电话")
	private String phone;
	//传真
	@ApiModelProperty(value = "传真")
	private String fax;
	//地址
	@ApiModelProperty(value = "地址")
	private String address;
	//法人代表
	@ApiModelProperty(value = "法人代表")
	private String legalperson;
	//公司介绍
	@ApiModelProperty(value = "公司介绍")
	private String desc;
	//
	@ApiModelProperty(value = "")
	private Long providerId;
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
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;
	//开户行
	@ApiModelProperty(value = "开户行")
	private Integer bank;
	//账号
	@ApiModelProperty(value = "账号")
	private String account;
	//状态
	@ApiModelProperty(value = "状态")
	private Integer status;
	//审核人
	@ApiModelProperty(value = "审核人")
	private Long auditId;
	//使用状态(1是0否)
	@ApiModelProperty(value = "使用状态(1是0否)")
	private Integer useStatus;
	//税号
	@ApiModelProperty(value = "税号")
	private String taxNumber;

	//所属部门
	@ApiModelProperty(value = "所属部门")
	private Long belongDept;
	//所属业务员
	@ApiModelProperty(value = "所属业务员")
	private Long belongSalesman;


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
	 * 设置：客户编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：客户编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：客户名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：客户名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：客户类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：客户类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：传真
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * 获取：传真
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * 设置：地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：法人代表
	 */
	public void setLegalperson(String legalperson) {
		this.legalperson = legalperson;
	}
	/**
	 * 获取：法人代表
	 */
	public String getLegalperson() {
		return legalperson;
	}
	/**
	 * 设置：公司介绍
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：公司介绍
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * 设置：
	 */
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	/**
	 * 获取：
	 */
	public Long getProviderId() {
		return providerId;
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
	 * 设置：删除标志
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setBank(Integer bank) {
		this.bank= bank;
	}
	public Integer getBank() {
		return bank;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getStatus() {
		return status;
	}
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	public Long getAuditId() {
		return auditId;
	}
	public void setUseStatus(Integer useStatus) {
		this.useStatus= useStatus;
	}
	public Integer getUseStatus() {
		return useStatus;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getTaxNumber() {return taxNumber;}

	public void setBelongDept(Long belongDept) {
		this.belongDept = belongDept;
	}
	public Long getBelongDept() {
		return belongDept;
	}

	public void setBelongSalesman(Long belongSalesman) {
		this.belongSalesman = belongSalesman;
	}
	public Long getBelongSalesman() {
		return belongSalesman;
	}


}
