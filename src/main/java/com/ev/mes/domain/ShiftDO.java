package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;



/**
 * 班制
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-26 19:58:39
 */
@ApiModel(value = "班制")
public class ShiftDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//班制代码
    @ApiModelProperty(value = "班制代码")
	private String code;
	//班制名字
    @ApiModelProperty(value = "班制名字")
	private String name;
	//开始时间
    @ApiModelProperty(value = "开始时间")
	private Time startTime;
	//结束时间
    @ApiModelProperty(value = "结束时间")
	private Time endTime;
	//工作时长
    @ApiModelProperty(value = "工作时长")
	private BigDecimal manHour;
	//备注
    @ApiModelProperty(value = "备注")
	private String remark;
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
	 * 设置：班制代码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：班制代码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：班制名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：班制名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：开始时间
	 */
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：工作时长
	 */
	public void setManHour(BigDecimal manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：工作时长
	 */
	public BigDecimal getManHour() {
		return manHour;
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
