package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-16 13:59:08
 */
@ApiModel(value = "8D报告改善小组表")
public class QualityGroupDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键",hidden = true)
	private Long id;
	//8D报告编号
    @ApiModelProperty(value = "8D报告编号",hidden = true)
	private Long reportId;
	//责任部门
    @ApiModelProperty(value = "责任部门")
	private Integer deptId;
	//责任人
    @ApiModelProperty(value = "责任人")
	private Long userId;
	//负责事项
    @ApiModelProperty(value = "负责事项")
	private String responsibleContent;
	//改善小组创建时间
    @ApiModelProperty(value = "改善小组创建时间",hidden = true)
	private Date createTime;
	//创建者
    @ApiModelProperty(value = "创建者",hidden = true)
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
	 * 设置：8D报告编号
	 */
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	/**
	 * 获取：8D报告编号
	 */
	public Long getReportId() {
		return reportId;
	}
	/**
	 * 设置：责任部门
	 */
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：责任部门
	 */
	public Integer getDeptId() {
		return deptId;
	}
	/**
	 * 设置：责任人
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：责任人
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：负责事项
	 */
	public void setResponsibleContent(String responsibleContent) {
		this.responsibleContent = responsibleContent;
	}
	/**
	 * 获取：负责事项
	 */
	public String getResponsibleContent() {
		return responsibleContent;
	}
	/**
	 * 设置：改善小组创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：改善小组创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建者
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
	public QualityGroupDO() {
	}
	
}
