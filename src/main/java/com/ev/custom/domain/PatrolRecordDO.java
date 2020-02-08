package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 巡检记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@ApiModel(value = "巡检记录表")
public class PatrolRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderno;
	//计划名称
    @ApiModelProperty(value = "巡检名称")
	private String name;
	//责任人
    @ApiModelProperty(value = "责任人")
	private Long engineerId;
	//联系电话
    @ApiModelProperty(value = "联系电话")
	private String cellphone;
	//巡检开始时间
    @ApiModelProperty(value = "巡检开始时间")
	private Date startTime;
	//巡检结束时间
    @ApiModelProperty(value = "巡检结束时间")
	private Date endTime;
	//任务状态
    @ApiModelProperty(value = "任务状态",hidden = true)
	private Integer status ;//56待处理；57待验收；58已完成
	//内容
    @ApiModelProperty(value = "内容",hidden = true)
	private String content;
	//任务结果
    @ApiModelProperty(value = "任务结果",hidden = true)
	private Integer result;
	//计划ID
    @ApiModelProperty(value = "计划ID",hidden = true)
	private Long planId;
    //计划ID
    @ApiModelProperty(value = "通知单ID",hidden = true)
	private Long informId;
	//完成时间
    @ApiModelProperty(value = "完成时间")
	private Date completeTime;
    //关闭原因
    @ApiModelProperty(value = "关闭原因")
	private String closeReason;
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
	 * 设置：联系电话
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getCellphone() {
		return cellphone;
	}
	/**
	 * 设置：巡检开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：巡检开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：巡检结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：巡检结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：任务状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：任务状态
	 */
	public Integer getStatus() {
		return status;
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
	 * 设置：任务结果
	 */
	public void setResult(Integer result) {
		this.result = result;
	}
	/**
	 * 获取：任务结果
	 */
	public Integer getResult() {
		return result;
	}
	/**
	 * 设置：计划ID
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * 获取：计划ID
	 */
	public Long getPlanId() {
		return planId;
	}
	/**
	 * 设置：通知单ID
	 */
	public void setInformId(Long informId) {
		this.informId = informId;
	}
	/**
	 * 获取：通知单ID
	 */
	public Long getInformId() {
		return informId;
	}
	/**
	 * 设置：完成时间
	 */
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	/**
	 * 获取：完成时间
	 */
	public Date getCompleteTime() {
		return completeTime;
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
	 * 获取：关闭原因
	 */
	public String getCloseReason() {
		return closeReason;
	}
	/**
	 * 设置：关闭原因
	 */
	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}
}
