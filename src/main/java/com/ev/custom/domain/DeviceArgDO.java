package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备参数表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 12:45:34
 */
@ApiModel(value = "设备参数表")
public class DeviceArgDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//编号
	@ApiModelProperty(value = "编号")
	private String code;
	//参数名称
	@ApiModelProperty(value = "参数名称")
	private String name;
	//数值
	@ApiModelProperty(value = "数值")
	private String value;
	//备注
	@ApiModelProperty(value = "备注")
	private String desc;
	//设备ID
	@ApiModelProperty(value = "设备ID")
	private Long deviceId;
	//录入人
	@ApiModelProperty(value = "录入人")
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
	 * 设置：编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：参数名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：参数名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：数值
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：数值
	 */
	public String getValue() {
		return value;
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
	 * 设置：设备ID
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：设备ID
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：录入人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：录入人
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
}
