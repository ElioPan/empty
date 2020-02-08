package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 巡检计划表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@ApiModel(value = "巡检计划表")
public class PatrolPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "主键")
	private Long id;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderno;
	//计划名称
    @ApiModelProperty(value = "计划名称")
	private String name;
	//责任人
    @ApiModelProperty(value = "责任人")
	private Long engineerId;
	//责任人电话
    @ApiModelProperty(value = "责任人电话")
	private String cellphone;
	//内容
    @ApiModelProperty(value = "内容")
	private String content;
	//计划状态
    @ApiModelProperty(value = "计划状态(129:启用;131:完成)",hidden = true)
	private Integer status;
	//频次间隔
    @ApiModelProperty(value = "频次间隔")
	private Integer frequency;
	//到期提醒
    @ApiModelProperty(value = "到期提醒")
	private Integer expireday;
	//计划开始时间
    @ApiModelProperty(value = "计划开始时间")
	private Date startTime;
	//计划结束时间
    @ApiModelProperty(value = "计划结束时间")
	private Date endTime;
	//创建日期
    @ApiModelProperty(value = "创建日期",hidden = true)
	private Date createTime;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
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
	 * 设置：工单号
	 */
	public void setWorkOrderno(String workOrderno) {
		this.workOrderno = workOrderno;
	}
	/**
	 * 获取：工单号
	 */
	public String getWorkOrderno() {
		return workOrderno;
	}
	/**
	 * 设置：计划名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：计划名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：责任人
	 */
	public void setEngineerId(Long engineerId) {
		this.engineerId = engineerId;
	}
	/**
	 * 获取：责任人
	 */
	public Long getEngineerId() {
		return engineerId;
	}
	/**
	 * 设置：责任人电话
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	/**
	 * 获取：责任人电话
	 */
	public String getCellphone() {
		return cellphone;
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
	 * 设置：计划状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：计划状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：频次间隔
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	/**
	 * 获取：频次间隔
	 */
	public Integer getFrequency() {
		return frequency;
	}
	/**
	 * 设置：到期提醒
	 */
	public void setExpireday(Integer expireday) {
		this.expireday = expireday;
	}
	/**
	 * 获取：到期提醒
	 */
	public Integer getExpireday() {
		return expireday;
	}
	/**
	 * 设置：计划开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：计划开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：计划结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：计划结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateTime() {
		return createTime;
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
