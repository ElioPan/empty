package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 用料报废明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:35:39
 */
@ApiModel(value = "用料报废明细")
public class MaterialsScrapItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//用料报废单主键
    @ApiModelProperty(value = "用料报废单主键")
	private Long scrapId;
	//物料id
    @ApiModelProperty(value = "物料id")
	private Long materialId;
	//批号
    @ApiModelProperty(value = "批号")
	private String batch;
	//报废原因
    @ApiModelProperty(value = "报废原因")
	private String scrapReason;
	//报废数量
    @ApiModelProperty(value = "报废数量")
	private BigDecimal scrapCount;
	//备注
    @ApiModelProperty(value = "备注")
	private String remark;
	//创建人 
    @ApiModelProperty(value = "创建人 ")
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
	 * 设置：用料报废单主键
	 */
	public void setScrapId(Long scrapId) {
		this.scrapId = scrapId;
	}
	/**
	 * 获取：用料报废单主键
	 */
	public Long getScrapId() {
		return scrapId;
	}
	/**
	 * 设置：物料id
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	/**
	 * 获取：物料id
	 */
	public Long getMaterialId() {
		return materialId;
	}
	/**
	 * 设置：批号
	 */
	public void setBatch(String batch) {
		this.batch = batch;
	}
	/**
	 * 获取：批号
	 */
	public String getBatch() {
		return batch;
	}
	/**
	 * 设置：报废原因
	 */
	public void setScrapReason(String scrapReason) {
		this.scrapReason = scrapReason;
	}
	/**
	 * 获取：报废原因
	 */
	public String getScrapReason() {
		return scrapReason;
	}
	/**
	 * 设置：报废数量
	 */
	public void setScrapCount(BigDecimal scrapCount) {
		this.scrapCount = scrapCount;
	}
	/**
	 * 获取：报废数量
	 */
	public BigDecimal getScrapCount() {
		return scrapCount;
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
