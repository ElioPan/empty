package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序报工单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:14:53
 */
@ApiModel(value = "工序报工单")
public class ProcessReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//报工单号
    @ApiModelProperty(value = "报工单号")
	private String code;
	//工单明细主键
    @ApiModelProperty(value = "工单明细主键")
	private Long dispatchItemId;
	//供应商
    @ApiModelProperty(value = "供应商")
	private Long supplierId;
	//完工数量
    @ApiModelProperty(value = "完工数量")
	private BigDecimal completionCount;
	//合格数量
    @ApiModelProperty(value = "合格数量")
	private BigDecimal conformityCount;
	//返工数量
    @ApiModelProperty(value = "返工数量")
	private BigDecimal reworkCount;
	//报废数量
    @ApiModelProperty(value = "报废数量")
	private BigDecimal scrapCount;
	//状态
    @ApiModelProperty(value = "状态")
	private Integer status;
	//创建人 （汇报人)
    @ApiModelProperty(value = "创建人 （汇报人)")
	private Long createBy;
	//派单时间 (汇报时间)
    @ApiModelProperty(value = "派单时间 (汇报时间)")
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
	 * 设置：报工单号
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：报工单号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：工单明细主键
	 */
	public void setDispatchItemId(Long dispatchItemId) {
		this.dispatchItemId = dispatchItemId;
	}
	/**
	 * 获取：工单明细主键
	 */
	public Long getDispatchItemId() {
		return dispatchItemId;
	}
	/**
	 * 设置：供应商
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/**
	 * 获取：供应商
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**
	 * 设置：完工数量
	 */
	public void setCompletionCount(BigDecimal completionCount) {
		this.completionCount = completionCount;
	}
	/**
	 * 获取：完工数量
	 */
	public BigDecimal getCompletionCount() {
		return completionCount;
	}
	/**
	 * 设置：合格数量
	 */
	public void setConformityCount(BigDecimal conformityCount) {
		this.conformityCount = conformityCount;
	}
	/**
	 * 获取：合格数量
	 */
	public BigDecimal getConformityCount() {
		return conformityCount;
	}
	/**
	 * 设置：返工数量
	 */
	public void setReworkCount(BigDecimal reworkCount) {
		this.reworkCount = reworkCount;
	}
	/**
	 * 获取：返工数量
	 */
	public BigDecimal getReworkCount() {
		return reworkCount;
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
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：创建人 （汇报人)
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人 （汇报人)
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：派单时间 (汇报时间)
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：派单时间 (汇报时间)
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
