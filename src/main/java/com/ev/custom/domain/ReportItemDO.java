package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 报告明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 14:50:08
 */
@ApiModel(value = "报告明细")
public class ReportItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//
    @ApiModelProperty(value = "")
	private Long weekReportId;
	//日期
    @ApiModelProperty(value = "日期")
	private Date reportDate;
	//星期
    @ApiModelProperty(value = "星期")
	private String week;
	//必做事项
    @ApiModelProperty(value = "必做事项")
	private String mustContent;
	//待做事项
    @ApiModelProperty(value = "待做事项")
	private String waitContet;
	//需求资源
    @ApiModelProperty(value = "需求资源")
	private String needSource;
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
	 * 设置：
	 */
	public void setWeekReportId(Long weekReportId) {
		this.weekReportId = weekReportId;
	}
	/**
	 * 获取：
	 */
	public Long getWeekReportId() {
		return weekReportId;
	}
	/**
	 * 设置：日期
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * 获取：日期
	 */
	public Date getReportDate() {
		return reportDate;
	}
	/**
	 * 设置：星期
	 */
	public void setWeek(String week) {
		this.week = week;
	}
	/**
	 * 获取：星期
	 */
	public String getWeek() {
		return week;
	}
	/**
	 * 设置：必做事项
	 */
	public void setMustContent(String mustContent) {
		this.mustContent = mustContent;
	}
	/**
	 * 获取：必做事项
	 */
	public String getMustContent() {
		return mustContent;
	}
	/**
	 * 设置：待做事项
	 */
	public void setWaitContet(String waitContet) {
		this.waitContet = waitContet;
	}
	/**
	 * 获取：待做事项
	 */
	public String getWaitContet() {
		return waitContet;
	}
	/**
	 * 设置：需求资源
	 */
	public void setNeedSource(String needSource) {
		this.needSource = needSource;
	}
	/**
	 * 获取：需求资源
	 */
	public String getNeedSource() {
		return needSource;
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
