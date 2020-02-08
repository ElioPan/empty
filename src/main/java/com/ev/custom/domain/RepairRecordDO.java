package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 维修记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
@ApiModel(value = "维修记录表")
public class RepairRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "记录主键")
	private Long id;
	//故障类型
    @ApiModelProperty(value = "故障类型(设备故障：103 电路故障：104 油路故障：105)",required = true)
	private Integer type;
	//故障等级
    @ApiModelProperty(value = "故障等级",hidden = true)
	private Integer level;
	//使用情况
    @ApiModelProperty(value = "使用情况(正常运行:106 带病运行:107 停机待修:108 停用报废:109)",required = true)
	private Integer usage;
	//记录状态
    @ApiModelProperty(value = "维修状态",hidden = true)
	private Integer status;
	//维修人
    @ApiModelProperty(value = "维修人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//维修开始时间
    @ApiModelProperty(value = "维修开始时间(格式：2019-08-01 10:00:00)",required = true)
	private Date startTime;
	//维修结束时间
    @ApiModelProperty(value = "维修结束时间(格式：2019-08-01 10:00:00)",required = true)
	private Date endTime;
	//维修结束时间
	@ApiModelProperty(value = "停机时间(格式：2019-08-01 10:00:00)")
	private Date offTime;
	//停机时长
    @ApiModelProperty(value = "停机时长")
	private Double offHour;
	//工时
    @ApiModelProperty(value = "工时")
	private Double manHour;
	//成本
    @ApiModelProperty(value = "成本")
	private BigDecimal cost;
	//内容
    @ApiModelProperty(value = "内容",hidden = true)
	private String content;
	//故障原因
    @ApiModelProperty(value = "故障原因",required = true)
	private String cause;
	//解决方案
    @ApiModelProperty(value = "解决方案")
	private String solution;
	//事件ID
    @ApiModelProperty(value = "事件ID",hidden = true)
	private Long eventId;
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
	 * 设置：故障类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：故障类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：故障等级
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：故障等级
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：使用情况
	 */
	public void setUsage(Integer usage) {
		this.usage = usage;
	}
	/**
	 * 获取：使用情况
	 */
	public Integer getUsage() {
		return usage;
	}
	/**
	 * 设置：维修人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：维修人
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
	 * 设置：维修开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：维修开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：维修结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：维修结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：工时
	 */
	public void setManHour(Double manHour) {
		this.manHour = manHour;
	}
	/**
	 * 获取：工时
	 */
	public Double getManHour() {
		return manHour;
	}
	/**
	 * 设置：成本
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	/**
	 * 获取：成本
	 */
	public BigDecimal getCost() {
		return cost;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：故障原因
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}
	/**
	 * 获取：故障原因
	 */
	public String getCause() {
		return cause;
	}
	/**
	 * 设置：解决方案
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}
	/**
	 * 获取：解决方案
	 */
	public String getSolution() {
		return solution;
	}
	/**
	 * 设置：事件ID
	 */
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	/**
	 * 获取：事件ID
	 */
	public Long getEventId() {
		return eventId;
	}

	public Date getOffTime() {
		return offTime;
	}

	public void setOffTime(Date offTime) {
		this.offTime = offTime;
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
	 * 设置：记录状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：记录状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 获取: 停机时长
	 */
	public Double getOffHour() {
		return offHour;
	}
	/**
	 * 设置: 停机时长
	 */
	public void setOffHour(Double offHour) {
		this.offHour = offHour;
	}
	
}
