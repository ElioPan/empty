package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BOM主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-19 08:45:05
 */
@ApiModel(value = "BOM主表")
public class BomDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	// BOM编号
	@ApiModelProperty(value = "BOM编号", required = true)
	private String serialno;
	// BOM名称
	@ApiModelProperty(value = "BOM名称", required = true)
	private String name;
	// BOM类别
	@ApiModelProperty(value = "BOM类别（211正常生产/212返工返修）")
	private Integer type;
	// BOM版本号
	@ApiModelProperty(value = "BOM版本号")
	private String version;
	// 产品ID
	@ApiModelProperty(value = "产品ID", required = true)
	private Integer materielId;
	// 数量
	@ApiModelProperty(value = "数量")
	private BigDecimal count;
	// 审核人员
	@ApiModelProperty(value = "审核人员")
	private Long auditor;
	// 图号
	@ApiModelProperty(value = "图号")
	private String imageNo;
	// 备注
	@ApiModelProperty(value = "备注")
	private String remarks;
	// 审核标志：（待审核/已审核）
	@ApiModelProperty(value = "审核标志：（待审核/已审核）", hidden = true)
	private Integer auditSign;
	// 启用状态
	@ApiModelProperty(value = "启用状态(是否启用 1启用，0禁用)")
	private Integer useStatus;
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
	 * 设置：BOM编号
	 */
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	/**
	 * 获取：BOM编号
	 */
	public String getSerialno() {
		return serialno;
	}

	/**
	 * 设置：BOM名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：BOM名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：BOM类别
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取：BOM类别
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置：BOM版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取：BOM版本号
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置：产品ID
	 */
	public void setMaterielId(Integer materielId) {
		this.materielId = materielId;
	}

	/**
	 * 获取：产品ID
	 */
	public Integer getMaterielId() {
		return materielId;
	}

	/**
	 * 设置：数量
	 */
	public void setCount(BigDecimal count) {
		this.count = count;
	}

	/**
	 * 获取：数量
	 */
	public BigDecimal getCount() {
		return count;
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
	 * 设置：图号
	 */
	public void setImageNo(String imageNo) {
		this.imageNo = imageNo;
	}

	/**
	 * 获取：图号
	 */
	public String getImageNo() {
		return imageNo;
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
	 * 设置：审核标志：（待审核/已审核）
	 */
	public void setAuditSign(Integer auditSign) {
		this.auditSign = auditSign;
	}

	/**
	 * 获取：审核标志：（待审核/已审核）
	 */
	public Integer getAuditSign() {
		return auditSign;
	}

	/**
	 * 设置：启用状态
	 */
	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}

	/**
	 * 获取：启用状态
	 */
	public Integer getUseStatus() {
		return useStatus;
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
