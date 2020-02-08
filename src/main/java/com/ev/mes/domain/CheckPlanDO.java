package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 检验方案
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:58
 */
@ApiModel(value = "检验方案")
public class CheckPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//检验方案编号
    @ApiModelProperty(value = "检验方案编号")
	private String code;
	//检验方案名称
    @ApiModelProperty(value = "检验方案名称")
	private String name;
	//
    @ApiModelProperty(value = "")
	private Long auditId;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Integer auditSign;
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
	 * 设置：检验方案编号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：检验方案编号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：检验方案名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：检验方案名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	/**
	 * 获取：
	 */
	public Long getAuditId() {
		return auditId;
	}
	/**
	 * 设置：审核状态
	 */
	public void setAuditSign(Integer auditSign) {
		this.auditSign = auditSign;
	}
	/**
	 * 获取：审核状态
	 */
	public Integer getAuditSign() {
		return auditSign;
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
