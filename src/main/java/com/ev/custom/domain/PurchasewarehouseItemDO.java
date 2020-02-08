package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购入库明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-21 13:35:16
 */
@ApiModel(value = "采购入库明细表")
public class PurchasewarehouseItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//采购入库明细表id
    @ApiModelProperty(value = "采购入库明细表自增主键id")
	private Long id;
	//主表主键
    @ApiModelProperty(value = "主表主键")
	private Long inheadId;
	//产品名称
    @ApiModelProperty(value = "产品名称")
	private String proName;
	//产品编号
    @ApiModelProperty(value = "产品编号")
	private String proCode;
	//规格型号
    @ApiModelProperty(value = "规格型号")
	private String proType;
	//批次
    @ApiModelProperty(value = "批次")
	private String proBatch;
	//单位
    @ApiModelProperty(value = "单位")
	private Long proUnit;
	//采购数量
    @ApiModelProperty(value = "采购数量")
	private BigDecimal proCount;
	//采购单价
    @ApiModelProperty(value = "采购单价")
	private BigDecimal proPrice;
	//采购金额
    @ApiModelProperty(value = "采购金额")
	private BigDecimal proAmount;
	//仓库
    @ApiModelProperty(value = "仓库")
	private Long warehouse;
	//库位
    @ApiModelProperty(value = "库位")
	private Long warehLocation;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceSingletype;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceSinglecode;
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
	//删除状态（）
    @ApiModelProperty(value = "删除状态（）")
	private Integer delFlag;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：主表主键
	 */
	public void setInheadId(Long inheadId) {
		this.inheadId = inheadId;
	}
	/**
	 * 获取：主表主键
	 */
	public Long getInheadId() {
		return inheadId;
	}
	/**
	 * 设置：产品名称
	 */
	public void setProName(String proName) {
		this.proName = proName;
	}
	/**
	 * 获取：产品名称
	 */
	public String getProName() {
		return proName;
	}
	/**
	 * 设置：产品编号
	 */
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}
	/**
	 * 获取：产品编号
	 */
	public String getProCode() {
		return proCode;
	}
	/**
	 * 设置：规格型号
	 */
	public void setProType(String proType) {
		this.proType = proType;
	}
	/**
	 * 获取：规格型号
	 */
	public String getProType() {
		return proType;
	}
	/**
	 * 设置：批次
	 */
	public void setProBatch(String proBatch) {
		this.proBatch = proBatch;
	}
	/**
	 * 获取：批次
	 */
	public String getProBatch() {
		return proBatch;
	}
	/**
	 * 设置：单位
	 */
	public void setProUnit(Long proUnit) {
		this.proUnit = proUnit;
	}
	/**
	 * 获取：单位
	 */
	public Long getProUnit() {
		return proUnit;
	}
	/**
	 * 设置：采购数量
	 */
	public void setProCount(BigDecimal proCount) {
		this.proCount = proCount;
	}
	/**
	 * 获取：采购数量
	 */
	public BigDecimal getProCount() {
		return proCount;
	}
	/**
	 * 设置：采购单价
	 */
	public void setProPrice(BigDecimal proPrice) {
		this.proPrice = proPrice;
	}
	/**
	 * 获取：采购单价
	 */
	public BigDecimal getProPrice() {
		return proPrice;
	}
	/**
	 * 设置：采购金额
	 */
	public void setProAmount(BigDecimal proAmount) {
		this.proAmount = proAmount;
	}
	/**
	 * 获取：采购金额
	 */
	public BigDecimal getProAmount() {
		return proAmount;
	}
	/**
	 * 设置：仓库
	 */
	public void setWarehouse(Long warehouse) {
		this.warehouse = warehouse;
	}
	/**
	 * 获取：仓库
	 */
	public Long getWarehouse() {
		return warehouse;
	}
	/**
	 * 设置：库位
	 */
	public void setWarehLocation(Long warehLocation) {
		this.warehLocation = warehLocation;
	}
	/**
	 * 获取：库位
	 */
	public Long getWarehLocation() {
		return warehLocation;
	}
	/**
	 * 设置：源单类型
	 */
	public void setSourceSingletype(Long sourceSingletype) {
		this.sourceSingletype = sourceSingletype;
	}
	/**
	 * 获取：源单类型
	 */
	public Long getSourceSingletype() {
		return sourceSingletype;
	}
	/**
	 * 设置：来源单号
	 */
	public void setSourceSinglecode(String sourceSinglecode) {
		this.sourceSinglecode = sourceSinglecode;
	}
	/**
	 * 获取：来源单号
	 */
	public String getSourceSinglecode() {
		return sourceSinglecode;
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
	 * 设置：删除状态（）
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态（）
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
