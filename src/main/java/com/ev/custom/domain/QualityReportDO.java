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
 * @date 2019-08-16 13:15:40
 */
@ApiModel(value = "8D报告表")
public class QualityReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//报告名称
    @ApiModelProperty(value = "报告名称",required = true)
	private String reportName;
	//编制人
    @ApiModelProperty(value = "编制人",required = true)
	private Long userId;
	//严重性
    @ApiModelProperty(value = "严重性(轻微的：82，致命的：83，严重的：84)")
	private Integer ponderance;
	//8D报告编号
    @ApiModelProperty(value = "8D报告编号",hidden = true)
	private String reportNo;
	//报告类型
    @ApiModelProperty(value = "报告类型(生产：85，质量：86，营销：87，客户：88，内部：89，供应商：90)")
	private Integer reportType;
	//报告状态
    @ApiModelProperty(value = "报告状态(编制中：91，已完成：92)",hidden = true)
	private Integer status;
	//详细描述
    @ApiModelProperty(value = "详细描述")
	private String detailContent;
	//分析主要原因
    @ApiModelProperty(value = "分析主要原因",hidden = true)
	private String analyzeReason;
	//效果分析
    @ApiModelProperty(value = "效果分析",hidden = true)
	private String resultContent;
	//
    @ApiModelProperty(value = "验收人",hidden = true)
	private Long checkId;
	//验收状态
    @ApiModelProperty(value = "验收状态",hidden = true)
	private Integer checkStatus;
	//编制时间
    @ApiModelProperty(value = "编制时间",hidden = true)
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
	 * 设置：报告名称
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	/**
	 * 获取：报告名称
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * 设置：编制人
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：编制人
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：严重性
	 */
	public void setPonderance(Integer ponderance) {
		this.ponderance = ponderance;
	}
	/**
	 * 获取：严重性
	 */
	public Integer getPonderance() {
		return ponderance;
	}
	/**
	 * 设置：8D报告编号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	/**
	 * 获取：8D报告编号
	 */
	public String getReportNo() {
		return reportNo;
	}
	/**
	 * 设置：报告类型
	 */
	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}
	/**
	 * 获取：报告类型
	 */
	public Integer getReportType() {
		return reportType;
	}
	/**
	 * 设置：报告状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：报告状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：详细描述
	 */
	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}
	/**
	 * 获取：详细描述
	 */
	public String getDetailContent() {
		return detailContent;
	}
	/**
	 * 设置：分析主要原因
	 */
	public void setAnalyzeReason(String analyzeReason) {
		this.analyzeReason = analyzeReason;
	}
	/**
	 * 获取：分析主要原因
	 */
	public String getAnalyzeReason() {
		return analyzeReason;
	}
	/**
	 * 设置：效果分析
	 */
	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}
	/**
	 * 获取：效果分析
	 */
	public String getResultContent() {
		return resultContent;
	}
	/**
	 * 设置：
	 */
	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}
	/**
	 * 获取：
	 */
	public Long getCheckId() {
		return checkId;
	}
	/**
	 * 设置：验收状态
	 */
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * 获取：验收状态
	 */
	public Integer getCheckStatus() {
		return checkStatus;
	}
	/**
	 * 设置：编制时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：编制时间
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
	
	
}
