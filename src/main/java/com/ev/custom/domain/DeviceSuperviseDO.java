package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 设备安监管理
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 09:33:13
 */
@ApiModel(value = "设备安监管理")
public class DeviceSuperviseDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//id
	@ApiModelProperty(value = "id")
	private Long id;
	//
	@ApiModelProperty(value = "")
	private Long deviceid;
	//检查项目
	@ApiModelProperty(value = "检查项目")
	private String checkitem;
	//检查项目id
	@ApiModelProperty(value = "检查项目id")
	private Long checkitemid;
	//监管机构（部门）
	@ApiModelProperty(value = "监管机构（部门）")
	private String regulatorname;
	//监管机构(部门)id
	@ApiModelProperty(value = "监管机构(部门)id")
	private Long regulatorid;
	//检查时间
	@ApiModelProperty(value = "检查时间")
	private Date checktime;
	//检查周期（天）
	@ApiModelProperty(value = "检查周期（天）")
	private String period;
	//检查人
	@ApiModelProperty(value = "检查人")
	private String rummager;
	//检查人id
	@ApiModelProperty(value = "检查人id")
	private String rummagerid;
	//下次检查时间
	@ApiModelProperty(value = "下次检查时间")
	private Date nextchecktime;
	//预警天数（天）
	@ApiModelProperty(value = "预警天数（天）")
	private String warningdays;
	//备注
	@ApiModelProperty(value = "备注")
	private String remark;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
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
	 * 设置：
	 */
	public void setDeviceid(Long deviceid) {
		this.deviceid = deviceid;
	}
	/**
	 * 获取：
	 */
	public Long getDeviceid() {
		return deviceid;
	}
	/**
	 * 设置：检查项目
	 */
	public void setCheckitem(String checkitem) {
		this.checkitem = checkitem;
	}
	/**
	 * 获取：检查项目
	 */
	public String getCheckitem() {
		return checkitem;
	}
	/**
	 * 设置：检查项目id
	 */
	public void setCheckitemid(Long checkitemid) {
		this.checkitemid = checkitemid;
	}
	/**
	 * 获取：检查项目id
	 */
	public Long getCheckitemid() {
		return checkitemid;
	}
	/**
	 * 设置：监管机构（部门）
	 */
	public void setRegulatorname(String regulatorname) {
		this.regulatorname = regulatorname;
	}
	/**
	 * 获取：监管机构（部门）
	 */
	public String getRegulatorname() {
		return regulatorname;
	}
	/**
	 * 设置：监管机构(部门)id
	 */
	public void setRegulatorid(Long regulatorid) {
		this.regulatorid = regulatorid;
	}
	/**
	 * 获取：监管机构(部门)id
	 */
	public Long getRegulatorid() {
		return regulatorid;
	}
	/**
	 * 设置：检查时间
	 */
	public void setChecktime(Date checktime) {
		this.checktime = checktime;
	}
	/**
	 * 获取：检查时间
	 */
	public Date getChecktime() {
		return checktime;
	}
	/**
	 * 设置：检查周期（天）
	 */
	public void setPeriod(String period) {
		this.period = period;
	}
	/**
	 * 获取：检查周期（天）
	 */
	public String getPeriod() {
		return period;
	}
	/**
	 * 设置：检查人
	 */
	public void setRummager(String rummager) {
		this.rummager = rummager;
	}
	/**
	 * 获取：检查人
	 */
	public String getRummager() {
		return rummager;
	}
	/**
	 * 设置：检查人id
	 */
	public void setRummagerid(String rummagerid) {
		this.rummagerid = rummagerid;
	}
	/**
	 * 获取：检查人id
	 */
	public String getRummagerid() {
		return rummagerid;
	}
	/**
	 * 设置：下次检查时间
	 */
	public void setNextchecktime(Date nextchecktime) {
		this.nextchecktime = nextchecktime;
	}
	/**
	 * 获取：下次检查时间
	 */
	public Date getNextchecktime() {
		return nextchecktime;
	}
	/**
	 * 设置：预警天数（天）
	 */
	public void setWarningdays(String warningdays) {
		this.warningdays = warningdays;
	}
	/**
	 * 获取：预警天数（天）
	 */
	public String getWarningdays() {
		return warningdays;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
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
	 * 设置：创建人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：创建人
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
