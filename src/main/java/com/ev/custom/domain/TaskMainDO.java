package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 任务
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 13:26:30
 */
@ApiModel(value = "任务")
public class TaskMainDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键" )
	private Long id;
	//主题
    @ApiModelProperty(value = "主题")
	private String title;
	//任务类型
    @ApiModelProperty(value = "任务类型")
	private Integer taskType;
	//任务等级
    @ApiModelProperty(value = "任务等级")
	private Integer taskLevel;
	//要求完成日期
    @ApiModelProperty(value = "要求完成日期")
	private Date expireDate;
	//详细描述
    @ApiModelProperty(value = "详细描述")
	private String detail;
	//任务编号
    @ApiModelProperty(value = "任务编号",hidden = true)
	private String taskNo;
	//处理状态
    @ApiModelProperty(value = "处理状态",hidden = true)
	private Integer status;
	//关联单号
    @ApiModelProperty(value = "关联单号")
	private String linkOrderId;
    //关联单号
    @ApiModelProperty(value = "关联单号类型")
	private Integer linkOrderType;
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
    @ApiModelProperty(value = "",hidden = true)
	private Integer providerId;

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
	 * 设置：主题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：主题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：任务类型
	 */
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}
	/**
	 * 获取：任务类型
	 */
	public Integer getTaskType() {
		return taskType;
	}
	/**
	 * 设置：任务等级
	 */
	public void setTaskLevel(Integer taskLevel) {
		this.taskLevel = taskLevel;
	}
	/**
	 * 获取：任务等级
	 */
	public Integer getTaskLevel() {
		return taskLevel;
	}
	/**
	 * 设置：要求完成日期
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	/**
	 * 获取：要求完成日期
	 */
	public Date getExpireDate() {
		return expireDate;
	}
	/**
	 * 设置：详细描述
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}
	/**
	 * 获取：详细描述
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * 设置：任务编号
	 */
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	/**
	 * 获取：任务编号
	 */
	public String getTaskNo() {
		return taskNo;
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
	 * 设置：
	 */
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	/**
	 * 获取：
	 */
	public Integer getProviderId() {
		return providerId;
	}
}
