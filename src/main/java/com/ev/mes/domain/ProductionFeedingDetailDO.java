package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 生产投料单（详情列表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 10:10:27
 */
@ApiModel(value = "生产投料单（详情列表）")
public class ProductionFeedingDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
    // 主表ID
    @ApiModelProperty(value = "")
	private Long headId;
	//产品ID
    @ApiModelProperty(value = "产品ID")
	private Integer materielId;
	//批号
    @ApiModelProperty(value = "批号")
	private String batchNo;
	//计划投料数量
    @ApiModelProperty(value = "计划投料数量")
	private BigDecimal planFeeding;
	//已出数量
    @ApiModelProperty(value = "已出数量")
	private BigDecimal outCount;
	//报废数量
    @ApiModelProperty(value = "报废数量")
	private BigDecimal scrapCount;
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
	 * 设置：计划投料数量
	 */
	public void setPlanFeeding(BigDecimal planFeeding) {
		this.planFeeding = planFeeding;
	}
	/**
	 * 获取：计划投料数量
	 */
	public BigDecimal getPlanFeeding() {
		return planFeeding;
	}
	/**
	 * 设置：已出数量
	 */
	public void setOutCount(BigDecimal outCount) {
		this.outCount = outCount;
	}
	/**
	 * 获取：已出数量
	 */
	public BigDecimal getOutCount() {
		return outCount;
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
	 * 设置：主表ID
	 */
	public Long getHeadId() {
		return headId;
	}
	/**
	 * 获取：主表ID
	 */
	public void setHeadId(Long headId) {
		this.headId = headId;
	}
	
}
