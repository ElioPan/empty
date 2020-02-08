package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料检验详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 09:35:57
 */
@ApiModel(value = "物料检验详情")
public class MaterialInspectionDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	// 主表ID
	@ApiModelProperty(value = "主表ID")
	private Long headId;
	// 检验项目ID
	@ApiModelProperty(value = "检验项目ID")
	private Long projectId;
	// 检验项目代码
	@ApiModelProperty(value = "检验项目代码")
	private String projectCode;
	// 检验项目名称
	@ApiModelProperty(value = "检验项目名称")
	private String projectName;
	// 检验结果（0异常/1正常）
	@ApiModelProperty(value = "检验结果（0异常/1正常）")
	private Integer checkResult;
	// 检验方法
	@ApiModelProperty(value = "检验方法")
	private String projectMethod;
	// 单位
	@ApiModelProperty(value = "单位")
	private Integer unit;
	// 目标值
	@ApiModelProperty(value = "目标值")
	private BigDecimal targetValue;
	// 检验值
	@ApiModelProperty(value = "检验值")
	private BigDecimal checkValue;
	// 是否必检项(0非必检/1必检）
	@ApiModelProperty(value = "是否必检项(0非必检/1必检）")
	private Integer isMustCheck;
	// 不良数量
	@ApiModelProperty(value = "不良数量")
	private BigDecimal unqualifiedCount;
	// 不良原因ID
	@ApiModelProperty(value = "不良原因ID")
	private Long reasonId;
	// 备注
	@ApiModelProperty(value = "备注")
	private String remarks;
	// 创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	// 创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	// 修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	// 修改时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	// 删除状态
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
	 * 设置：主表ID
	 */
	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	/**
	 * 获取：主表ID
	 */
	public Long getHeadId() {
		return headId;
	}

	/**
	 * 设置：检验项目ID
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取：检验项目ID
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * 设置：检验项目代码
	 */
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	/**
	 * 获取：检验项目代码
	 */
	public String getProjectCode() {
		return projectCode;
	}

	/**
	 * 设置：检验项目名称
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * 获取：检验项目名称
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * 设置：检验结果（0异常/1正常）
	 */
	public void setCheckResult(Integer checkResult) {
		this.checkResult = checkResult;
	}

	/**
	 * 获取：检验结果（0异常/1正常）
	 */
	public Integer getCheckResult() {
		return checkResult;
	}

	/**
	 * 设置：检验方法
	 */
	public void setProjectMethod(String projectMethod) {
		this.projectMethod = projectMethod;
	}

	/**
	 * 获取：检验方法
	 */
	public String getProjectMethod() {
		return projectMethod;
	}

	/**
	 * 设置：单位
	 */
	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	/**
	 * 获取：单位
	 */
	public Integer getUnit() {
		return unit;
	}

	/**
	 * 设置：目标值
	 */
	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}

	/**
	 * 获取：目标值
	 */
	public BigDecimal getTargetValue() {
		return targetValue;
	}

	/**
	 * 设置：检验值
	 */
	public void setCheckValue(BigDecimal checkValue) {
		this.checkValue = checkValue;
	}

	/**
	 * 获取：检验值
	 */
	public BigDecimal getCheckValue() {
		return checkValue;
	}

	/**
	 * 设置：是否必检项(0非必检/1必检）
	 */
	public void setIsMustCheck(Integer isMustCheck) {
		this.isMustCheck = isMustCheck;
	}

	/**
	 * 获取：是否必检项(0非必检/1必检）
	 */
	public Integer getIsMustCheck() {
		return isMustCheck;
	}

	/**
	 * 设置：不良数量
	 */
	public void setUnqualifiedCount(BigDecimal unqualifiedCount) {
		this.unqualifiedCount = unqualifiedCount;
	}

	/**
	 * 获取：不良数量
	 */
	public BigDecimal getUnqualifiedCount() {
		return unqualifiedCount;
	}

	/**
	 * 设置：不良原因ID
	 */
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	/**
	 * 获取：不良原因ID
	 */
	public Long getReasonId() {
		return reasonId;
	}

	/**
	 * 设置：备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 获取：备注
	 */
	public String getRemarks() {
		return remarks;
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
