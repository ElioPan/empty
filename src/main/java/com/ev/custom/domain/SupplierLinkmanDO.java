package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 供应商联系人表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-16 13:06:33
 */
@ApiModel(value = "供应商联系人表")
public class SupplierLinkmanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//姓名
    @ApiModelProperty(value = "姓名")
	private String name;
	//供应商ID
    @ApiModelProperty(value = "供应商ID")
	private Long supplierId;
	//电话
    @ApiModelProperty(value = "电话")
	private String phone;
	//邮箱
    @ApiModelProperty(value = "邮箱")
	private String email;
	//性别
    @ApiModelProperty(value = "性别")
	private Integer gender;
	//职位
    @ApiModelProperty(value = "职位")
	private String post;
	//备注
    @ApiModelProperty(value = "备注")
	private String desc;

	//
    @ApiModelProperty(value = "")
	private Long providerId;
    //创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
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
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：供应商ID
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/**
	 * 获取：供应商ID
	 */
	public Long getSupplierId() {
		return supplierId;
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
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：性别
	 */
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	/**
	 * 获取：性别
	 */
	public Integer getGender() {
		return gender;
	}
	/**
	 * 设置：职位
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/**
	 * 获取：职位
	 */
	public String getPost() {
		return post;
	}
	/**
	 * 设置：备注
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 获取：备注
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
