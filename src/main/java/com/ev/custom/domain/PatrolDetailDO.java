package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 巡检明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:37
 */
@ApiModel(value = "巡检明细表")
public class PatrolDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "",hidden = true)
	private Long id;
	//关联设备
    @ApiModelProperty(value = "关联设备")
	private Long deviceId;
	//关联项目
    @ApiModelProperty(value = "关联项目")
	private Long projectId;
	//结果
    @ApiModelProperty(value = "结果(1为正常,2为异常)")
	private Integer result;
	//任务状态
    @ApiModelProperty(value = "任务状态")
	private Integer status;
    //停机时间
    @ApiModelProperty(value = "停机时间")
	private Date offTime;
	//内容
    @ApiModelProperty(value = "内容")
	private String content;
	//巡检记录ID
    @ApiModelProperty(value = "巡检记录ID")
	private Long recordId;
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
	 * 设置：关联设备
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：关联设备
	 */
	public Long getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：关联项目
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：关联项目
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：结果
	 */
	public void setResult(Integer result) {
		this.result = result;
	}
	/**
	 * 获取：结果
	 */
	public Integer getResult() {
		return result;
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
	 * 设置：巡检记录ID
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：巡检记录ID
	 */
	public Long getRecordId() {
		return recordId;
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
	 * 设置：停机时间
	 */
	public Date getOffTime() {
		return offTime;
	}
	/**
	 * 获取：停机时间
	 */
	public void setOffTime(Date offTime) {
		this.offTime = offTime;
	}
}
