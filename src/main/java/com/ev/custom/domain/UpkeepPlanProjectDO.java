package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养计划与项目中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 09:53:13
 */
@ApiModel(value = "保养计划与项目中间表")
public class UpkeepPlanProjectDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//保养计划ID
	@ApiModelProperty(value = "保养计划ID")
	private Long planId;
	//养保项目ID
	@ApiModelProperty(value = "养保项目ID")
	private Long projectId;
	//工时
	@ApiModelProperty(value = "工时")
	private Integer manhour;
	//结果
	@ApiModelProperty(value = "结果")
	private Integer result;
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
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
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
	 * 设置：保养计划ID
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * 获取：保养计划ID
	 */
	public Long getPlanId() {
		return planId;
	}
	/**
	 * 设置：养保项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取：养保项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * 设置：工时
	 */
	public void setManhour(Integer manhour) {
		this.manhour = manhour;
	}
	/**
	 * 获取：工时
	 */
	public Integer getManhour() {
		return manhour;
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
	 * 设置：创建时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：删除标志
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
