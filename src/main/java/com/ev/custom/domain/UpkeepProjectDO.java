package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养项目表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 11:01:56
 */
@ApiModel(value = "保养项目表")
public class UpkeepProjectDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//编号
	@ApiModelProperty(value = "编号")
	private String code;
	//保养类型
	@ApiModelProperty(value = "保养类型")
	private Integer type;
	//保养项目名称
	@ApiModelProperty(value = "保养项目名称")
	private String name;
	//保养方法
	@ApiModelProperty(value = "保养方法")
	private String function;
	//设备类型
	@ApiModelProperty(value = "设备类型")
	private Integer deviceTepy;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
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
	 * 设置：保养类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：保养类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：保养项目名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：保养项目名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：保养方法
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	/**
	 * 获取：保养方法
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * 设置：设备类型
	 */
	public void setDeviceTepy(Integer deviceTepy) {
		this.deviceTepy = deviceTepy;
	}
	/**
	 * 获取：设备类型
	 */
	public Integer getDeviceTepy() {
		return deviceTepy;
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
}
