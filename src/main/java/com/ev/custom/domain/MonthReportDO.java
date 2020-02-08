package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 月报
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-07 09:54:47
 */
@ApiModel(value = "月报")
public class MonthReportDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//本月完成工作
	@ApiModelProperty(value = "本月完成工作")
	private String comContent;
	//未完成工作
	@ApiModelProperty(value = "未完成工作")
	private String uncomContent;
	//需协调工作
	@ApiModelProperty(value = "需协调工作")
	private String needConcertContent;
	//待定事项
	@ApiModelProperty(value = "待定事项")
	private String waitContet;
	//必做事项
	@ApiModelProperty(value = "必做事项")
	private String mustContet;
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
	//删除状态
	@ApiModelProperty(value = "删除状态")
	private Integer delFlag;
	//状态
	@ApiModelProperty(value = "状态")
	private Integer status;

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
	 * 设置：本月完成工作
	 */
	public void setComContent(String comContent) {
		this.comContent = comContent;
	}
	/**
	 * 获取：本月完成工作
	 */
	public String getComContent() {
		return comContent;
	}
	/**
	 * 设置：未完成工作
	 */
	public void setUncomContent(String uncomContent) {
		this.uncomContent = uncomContent;
	}
	/**
	 * 获取：未完成工作
	 */
	public String getUncomContent() {
		return uncomContent;
	}
	/**
	 * 设置：需协调工作
	 */
	public void setNeedConcertContent(String needConcertContent) {
		this.needConcertContent = needConcertContent;
	}
	/**
	 * 获取：需协调工作
	 */
	public String getNeedConcertContent() {
		return needConcertContent;
	}
	/**
	 * 设置：待定事项
	 */
	public void setWaitContet(String waitContet) {
		this.waitContet = waitContet;
	}
	/**
	 * 获取：待定事项
	 */
	public String getWaitContet() {
		return waitContet;
	}
	/**
	 * 设置：必做事项
	 */
	public void setMustContet(String mustContet) {
		this.mustContet = mustContet;
	}
	/**
	 * 获取：必做事项
	 */
	public String getMustContet() {
		return mustContet;
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
}
