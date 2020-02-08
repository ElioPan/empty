package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备-备件中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 09:27:24
 */
@ApiModel(value = "设备-备件中间表")
public class DeviceSpareDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//主设备主键
	@ApiModelProperty(value = "主设备主键")
	private Long deviceId;
	//备件主键
	@ApiModelProperty(value = "备件主键")
	private Long spareId;
	//与主设备绑定数量
	@ApiModelProperty(value = "与主设备绑定数量")
	private Integer amount;
	//备件所在库位id
	@ApiModelProperty(value = "备件所在库位id")
	private Integer spareLocationid;
	//自定义1
	@ApiModelProperty(value = "自定义1")
	private String def1;
	//自定义2
	@ApiModelProperty(value = "自定义2")
	private String def2;
	//自定义3
	@ApiModelProperty(value = "自定义3")
	private String def3;
	//自定义4
	@ApiModelProperty(value = "自定义4")
	private String def4;
	//自定义5
	@ApiModelProperty(value = "自定义5")
	private String def5;
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
	 * 设置：主设备主键
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：主设备主键
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：备件主键
	 */
	public void setSpareId(Long spareId) {
		this.spareId = spareId;
	}
	/**
	 * 获取：备件主键
	 */
	public Long getSpareId() {
		return spareId;
	}
	/**
	 * 设置：与主设备绑定数量
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * 获取：与主设备绑定数量
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * 设置：备件所在库位id
	 */
	public void setSpareLocationid(Integer spareLocationid) {
		this.spareLocationid = spareLocationid;
	}
	/**
	 * 获取：备件所在库位id
	 */
	public Integer getSpareLocationid() {
		return spareLocationid;
	}
	/**
	 * 设置：自定义1
	 */
	public void setDef1(String def1) {
		this.def1 = def1;
	}
	/**
	 * 获取：自定义1
	 */
	public String getDef1() {
		return def1;
	}
	/**
	 * 设置：自定义2
	 */
	public void setDef2(String def2) {
		this.def2 = def2;
	}
	/**
	 * 获取：自定义2
	 */
	public String getDef2() {
		return def2;
	}
	/**
	 * 设置：自定义3
	 */
	public void setDef3(String def3) {
		this.def3 = def3;
	}
	/**
	 * 获取：自定义3
	 */
	public String getDef3() {
		return def3;
	}
	/**
	 * 设置：自定义4
	 */
	public void setDef4(String def4) {
		this.def4 = def4;
	}
	/**
	 * 获取：自定义4
	 */
	public String getDef4() {
		return def4;
	}
	/**
	 * 设置：自定义5
	 */
	public void setDef5(String def5) {
		this.def5 = def5;
	}
	/**
	 * 获取：自定义5
	 */
	public String getDef5() {
		return def5;
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
