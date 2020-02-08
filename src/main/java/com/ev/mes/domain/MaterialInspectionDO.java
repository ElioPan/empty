package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料检验
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 12:54:56
 */
@ApiModel(value = "物料检验")
public class MaterialInspectionDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	// 检验单类型
	@ApiModelProperty(value = "检验单类型(216、来料检验、217产品检验、218、发货检验)", required = true)
	private Integer inspectionType;
	// 来源单号ID
	@ApiModelProperty(value = "来源单号ID")
	private Long sourceId;
	// 源单类型
	@ApiModelProperty(value = "源单类型")
	private Integer sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNo;
	// 供应商ID
	@ApiModelProperty(value = "供应商ID(来料检验选择该字段)")
	private Long supplierId;
	// 客户ID
	@ApiModelProperty(value = "客户ID(发货检验选择该字段)")
	private Long clientId;
	// 部门
	@ApiModelProperty(value = "部门", required = true)
	private Long deptId;
	// 检验员
	@ApiModelProperty(value = "检验员", required = true)
	private Long inspector;
	// 物料ID
	@ApiModelProperty(value = "物料ID")
	private Integer materielId;
	// 批号
	@ApiModelProperty(value = "批号")
	private String batchNo;
	// 检验方案
	@ApiModelProperty(value = "检验方案")
	private Long inspectionScheme;
	// 送检数量
	@ApiModelProperty(value = "送检数量", required = true)
	private BigDecimal sendCount;
	// 检验数量
	@ApiModelProperty(value = "检验数量")
	private BigDecimal inspectionCount;
	// 合格数量
	@ApiModelProperty(value = "合格数量")
	private BigDecimal qualifiedCount;
	// 不合格数量
	@ApiModelProperty(value = "不合格数量")
	private BigDecimal unqualifiedCount;
	// 工单号
	@ApiModelProperty(value = "工单号", hidden = true)
	private String inspectionNo;
	// 工废数量
	@ApiModelProperty(value = "工废数量(产品检验选择该字段)")
	private BigDecimal industrialWasteCount;
	// 料废数量
	@ApiModelProperty(value = "料废数量(产品检验选择该字段)")
	private BigDecimal scrapWasteCount;
	// 审核人员
	@ApiModelProperty(value = "审核人员")
	private Long auditor;
	// 单据状态
	@ApiModelProperty(value = "单据状态", hidden = true)
	private Integer status;
	// 备注
	@ApiModelProperty(value = "备注", hidden = true)
	private String remarks;
	// 创建人
	@ApiModelProperty(value = "创建人", hidden = true)
	private Long createBy;
	// 创建时间
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Date createTime;
	// 修改人
	@ApiModelProperty(value = "修改人", hidden = true)
	private Long updateBy;
	// 修改时间
	@ApiModelProperty(value = "修改时间", hidden = true)
	private Date updateTime;
	// 删除状态
	@ApiModelProperty(value = "删除状态", hidden = true)
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
	 * 设置：检验单类型
	 */
	public void setInspectionType(Integer inspectionType) {
		this.inspectionType = inspectionType;
	}

	/**
	 * 获取：检验单类型
	 */
	public Integer getInspectionType() {
		return inspectionType;
	}

	/**
	 * 设置：来源单号ID
	 */
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * 获取：来源单号ID
	 */
	public Long getSourceId() {
		return sourceId;
	}

	/**
	 * 设置：源单类型
	 */
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * 获取：源单类型
	 */
	public Integer getSourceType() {
		return sourceType;
	}

	/**
	 * 设置：供应商ID
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * 获取：供应商ID
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * 设置：客户ID
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	/**
	 * 获取：客户ID
	 */
	public Long getClientId() {
		return clientId;
	}

	/**
	 * 设置：部门
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取：部门
	 */
	public Long getDeptId() {
		return deptId;
	}

	/**
	 * 设置：检验员
	 */
	public void setInspector(Long inspector) {
		this.inspector = inspector;
	}

	/**
	 * 获取：检验员
	 */
	public Long getInspector() {
		return inspector;
	}

	/**
	 * 设置：物料ID
	 */
	public void setMaterielId(Integer materielId) {
		this.materielId = materielId;
	}

	/**
	 * 获取：物料ID
	 */
	public Integer getMaterielId() {
		return materielId;
	}

	/**
	 * 设置：批号
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 获取：批号
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * 设置：检验方案
	 */
	public void setInspectionScheme(Long inspectionScheme) {
		this.inspectionScheme = inspectionScheme;
	}

	/**
	 * 获取：检验方案
	 */
	public Long getInspectionScheme() {
		return inspectionScheme;
	}

	/**
	 * 设置：送检数量
	 */
	public void setSendCount(BigDecimal sendCount) {
		this.sendCount = sendCount;
	}

	/**
	 * 获取：送检数量
	 */
	public BigDecimal getSendCount() {
		return sendCount;
	}

	/**
	 * 设置：检验数量
	 */
	public void setInspectionCount(BigDecimal inspectionCount) {
		this.inspectionCount = inspectionCount;
	}

	/**
	 * 获取：检验数量
	 */
	public BigDecimal getInspectionCount() {
		return inspectionCount;
	}

	/**
	 * 设置：合格数量
	 */
	public void setQualifiedCount(BigDecimal qualifiedCount) {
		this.qualifiedCount = qualifiedCount;
	}

	/**
	 * 获取：合格数量
	 */
	public BigDecimal getQualifiedCount() {
		return qualifiedCount;
	}

	/**
	 * 设置：不合格数量
	 */
	public void setUnqualifiedCount(BigDecimal unqualifiedCount) {
		this.unqualifiedCount = unqualifiedCount;
	}

	/**
	 * 获取：不合格数量
	 */
	public BigDecimal getUnqualifiedCount() {
		return unqualifiedCount;
	}

	/**
	 * 设置：工单号
	 */
	public void setInspectionNo(String inspectionNo) {
		this.inspectionNo = inspectionNo;
	}

	/**
	 * 获取：工单号
	 */
	public String getInspectionNo() {
		return inspectionNo;
	}

	/**
	 * 设置：工废数量
	 */
	public void setIndustrialWasteCount(BigDecimal industrialWasteCount) {
		this.industrialWasteCount = industrialWasteCount;
	}

	/**
	 * 获取：工废数量
	 */
	public BigDecimal getIndustrialWasteCount() {
		return industrialWasteCount;
	}

	/**
	 * 设置：料废数量
	 */
	public void setScrapWasteCount(BigDecimal scrapWasteCount) {
		this.scrapWasteCount = scrapWasteCount;
	}

	/**
	 * 获取：料废数量
	 */
	public BigDecimal getScrapWasteCount() {
		return scrapWasteCount;
	}

	/**
	 * 设置：审核人员
	 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	/**
	 * 获取：审核人员
	 */
	public Long getAuditor() {
		return auditor;
	}

	/**
	 * 设置：单据状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：单据状态
	 */
	public Integer getStatus() {
		return status;
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
	/**
	 * 设置：来源单号
	 */
	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
	/**
	 * 获取：来源单号
	 */
	public String getSourceNo() {
		return sourceNo;
	}
}
