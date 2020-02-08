package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 开工/挂起记录
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 20:21:50
 */
@ApiModel(value = "开工/挂起记录")
public class DispatchWorkingHungDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工单明细主键
    @ApiModelProperty(value = "工单明细主键")
	private Long dispatchItemId;
	//开始时间
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	//挂起时间
    @ApiModelProperty(value = "挂起时间")
	private Date hangTime;
	//时长
    @ApiModelProperty(value = "时长")
	private BigDecimal manHour;
	//开工/挂起 人
    @ApiModelProperty(value = "开工/挂起 人")
	private Long startHangId;
	//标记（1开工记录 0挂起记录）
    @ApiModelProperty(value = "标记（1开工记录 0挂起记录）")
	private Integer sign;
	//创建人(派单人)
    @ApiModelProperty(value = "创建人(派单人)")
	private Long createBy;
	//派单时间
    @ApiModelProperty(value = "派单时间")
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
	 * 设置：工单明细主键
	 */
	public void setDispatchItemId(Long dispatchItemId) {
		this.dispatchItemId = dispatchItemId;
	}
	/**
	 * 获取：工单明细主键
	 */
	public Long getDispatchItemId() {
		return dispatchItemId;
	}
	/**
	 * 设置：开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：挂起时间
	 */
	public void setHangTime(Date hangTime) {
		this.hangTime = hangTime;
	}
	/**
	 * 获取：挂起时间
	 */
	public Date getHangTime() {
		return hangTime;
	}
	/**
	 * 设置：时长
	 */
	public void setManHour(BigDecimal manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：时长
	 */
	public BigDecimal getManHour() {
		return manHour;
	}
	/**
	 * 设置：开工/挂起 人
	 */
	public void setStartHangId(Long startHangId) {
		this.startHangId = startHangId;
	}
	/**
	 * 获取：开工/挂起 人
	 */
	public Long getStartHangId() {
		return startHangId;
	}
	/**
	 * 设置：标记（1开工记录 0挂起记录）
	 */
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	/**
	 * 获取：标记（1开工记录 0挂起记录）
	 */
	public Integer getSign() {
		return sign;
	}
	/**
	 * 设置：创建人(派单人)
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人(派单人)
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：派单时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：派单时间
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
