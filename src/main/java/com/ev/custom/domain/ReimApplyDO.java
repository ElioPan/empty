package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 报销申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:37
 */
@ApiModel(value = "报销申请")
public class ReimApplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//收款账号
    @ApiModelProperty(value = "收款账号")
	private String accountReceivable;
	//备注
    @ApiModelProperty(value = "备注")
	private String description;
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
	//状态
    @ApiModelProperty(value = "状态")
	private Integer status;
	//流程实例ID
    @ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
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
	 * 设置：收款账号
	 */
	public void setAccountReceivable(String accountReceivable) {
		this.accountReceivable = accountReceivable;
	}
	/**
	 * 获取：收款账号
	 */
	public String getAccountReceivable() {
		return accountReceivable;
	}
	/**
	 * 设置：备注
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：备注
	 */
	public String getDescription() {
		return description;
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
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：流程实例ID
	 */
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	/**
	 * 获取：流程实例ID
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
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
