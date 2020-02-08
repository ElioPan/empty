package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 公司信息表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-26 16:02:10
 */
@ApiModel(value = "公司信息表")
public class CompanyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
    @ApiModelProperty(value = "主键ID",hidden = true)
	private Long id;
	//公司名称
    @ApiModelProperty(value = "公司名称",required = true)
	private String name;
	//省
    @ApiModelProperty(value = "省")
	private Integer province;
	//市
    @ApiModelProperty(value = "市")
	private Integer city;
	//区
    @ApiModelProperty(value = "区")
	private Integer district;
	//详细地址
    @ApiModelProperty(value = "详细地址",required = true)
	private String address;
	//联系电话
    @ApiModelProperty(value = "联系电话")
	private String telephone;
	//联系人
    @ApiModelProperty(value = "联系人",required = true)
	private String linkman;
	//手机
    @ApiModelProperty(value = "手机")
	private String mobile;
	//Email
    @ApiModelProperty(value = "Email")
	private String email;
	//传真
    @ApiModelProperty(value = "传真")
	private String zipCode;
	//传真
    @ApiModelProperty(value = "传真")
	private String fax;
	//职务
    @ApiModelProperty(value = "职务")
	private String job;
	//QQ
    @ApiModelProperty(value = "QQ")
	private String qq;
	//公司网址
    @ApiModelProperty(value = "公司网址")
	private String url;
	//公司介绍
    @ApiModelProperty(value = "公司介绍")
	private String companyIntroduction;
    //服务码
    @ApiModelProperty(value = "服务码",hidden = true)
	private String serviceCode;
    //服务码
    @ApiModelProperty(value = "服务结束时间",hidden = true)
	private String serviceEndTime;
    //服务码
    @ApiModelProperty(value = "服务开始时间",hidden = true)
	private String serviceStartTime;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//更改时间
    @ApiModelProperty(value = "更改时间",hidden = true)
	private Date updateTime;
	//删除标志
    @ApiModelProperty(value = "删除标志",hidden = true)
	private Integer delFlag;

	/**
	 * 设置：主键ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：公司名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：公司名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：详细地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：详细地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：联系电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：联系人
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * 获取：联系人
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * 设置：手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：Email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：Email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：传真
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * 获取：传真
	 */
	public String getZipCode() {
		return zipCode;
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
	 * 设置：职务
	 */
	public void setJob(String job) {
		this.job = job;
	}
	/**
	 * 获取：职务
	 */
	public String getJob() {
		return job;
	}
	/**
	 * 设置：QQ
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}
	/**
	 * 获取：QQ
	 */
	public String getQq() {
		return qq;
	}
	/**
	 * 设置：公司网址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：公司网址
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：公司介绍
	 */
	public void setCompanyIntroduction(String companyIntroduction) {
		this.companyIntroduction = companyIntroduction;
	}
	/**
	 * 获取：公司介绍
	 */
	public String getCompanyIntroduction() {
		return companyIntroduction;
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
	 * 设置：更改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更改时间
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
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceEndTime() {
		return serviceEndTime;
	}
	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}
	public String getServiceStartTime() {
		return serviceStartTime;
	}
	public void setServiceStartTime(String serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}
	
}
