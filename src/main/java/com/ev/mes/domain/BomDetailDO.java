package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BOM子物料表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-19 08:45:34
 */
@ApiModel(value = "BOM子物料表")
public class BomDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	// 子物料ID
	@ApiModelProperty(value = "子物料ID")
	private Integer materielId;
	// BOMID
	@ApiModelProperty(value = "BOMID")
	private Long bomId;
	// 标准数量
	@ApiModelProperty(value = "标准数量")
	private BigDecimal standardCount;
	// 损耗率(单位%)
	@ApiModelProperty(value = "损耗率(单位%)")
	private BigDecimal wasteRate;
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
	 * 设置：子物料ID
	 */
	public void setMaterielId(Integer materielId) {
		this.materielId = materielId;
	}

	/**
	 * 获取：子物料ID
	 */
	public Integer getMaterielId() {
		return materielId;
	}

	/**
	 * 设置：标准数量
	 */
	public void setStandardCount(BigDecimal standardCount) {
		this.standardCount = standardCount;
	}

	/**
	 * 获取：标准数量
	 */
	public BigDecimal getStandardCount() {
		return standardCount;
	}

	/**
	 * 设置：损耗率(单位%)
	 */
	public void setWasteRate(BigDecimal wasteRate) {
		this.wasteRate = wasteRate;
	}

	/**
	 * 获取：损耗率(单位%)
	 */
	public BigDecimal getWasteRate() {
		return wasteRate;
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
	 * 设置：BOMID
	 */
	public Long getBomId() {
		return bomId;
	}

	/**
	 * 获取：BOMID
	 */
	public void setBomId(Long bomId) {
		this.bomId = bomId;
	}
}
