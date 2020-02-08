package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 任务处理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 13:26:30
 */
@ApiModel(value = "任务处理")
public class TaskReplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//任务单号
    @ApiModelProperty(value = "任务单号")
	private Long taskid;
	//原因分析
    @ApiModelProperty(value = "原因分析")
	private String reason;
	//解决方案
    @ApiModelProperty(value = "解决方案/验收说明")
	private String solution;
    //处理状态
    @ApiModelProperty(value = "处理状态115 通过 116 不通过")
	private Integer status;
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
	//
    @ApiModelProperty(value = "处理记录ID")
	private Integer dealId;
	//
    @ApiModelProperty(value = "",hidden = true)
	private Integer replyType;
	//
	@ApiModelProperty(value = "完成效率")
	private Integer completeRate;
	//
    @ApiModelProperty(value = "完成质量")
	private Integer completeQuality;
	//
    @ApiModelProperty(value = "工作态度")
	private Integer workState;

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
	 * 设置：任务单号
	 */
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	/**
	 * 获取：任务单号
	 */
	public Long getTaskid() {
		return taskid;
	}
	/**
	 * 设置：原因分析
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 获取：原因分析
	 */
	public String getReason() {
		return reason;
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
	 * 设置：处理记录ID
	 */
	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}
	/**
	 * 获取：处理记录ID
	 */
	public Integer getDealId() {
		return dealId;
	}
	/**
	 * 设置：
	 */
	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}
	/**
	 * 获取：
	 */
	public Integer getReplyType() {
		return replyType;
	}
	/**
	 * 设置：
	 */
	public void setCompleteRate(Integer completeRate) {
		this.completeRate = completeRate;
	}
	/**
	 * 获取：
	 */
	public Integer getCompleteRate() {
		return completeRate;
	}
	/**
	 * 设置：
	 */
	public void setCompleteQuality(Integer completeQuality) {
		this.completeQuality = completeQuality;
	}
	/**
	 * 获取：
	 */
	public Integer getCompleteQuality() {
		return completeQuality;
	}
	/**
	 * 设置：
	 */
	public void setWorkState(Integer workState) {
		this.workState = workState;
	}
	/**
	 * 获取：
	 */
	public Integer getWorkState() {
		return workState;
	}
	/**
	 * 设置：处理状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：处理状态
	 */
	public Integer getStatus() {
		return status;
	}
	
}
