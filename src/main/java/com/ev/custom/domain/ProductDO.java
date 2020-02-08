package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 产品表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-10 08:49:45
 */
@ApiModel(value = "产品表")
public class ProductDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//产品编号
    @ApiModelProperty(value = "产品编号",required = true)
	private String serialno;
	//产品名称
    @ApiModelProperty(value = "产品名称",required = true)
	private String name;
	//规格型号
    @ApiModelProperty(value = "规格型号")
	private String model;
	//产品条码
    @ApiModelProperty(value = "产品条码")
	private String barCode;
	//产品类型
    @ApiModelProperty(value = "产品类型",required = true)
	private Long type;
    //库位
    @ApiModelProperty(value = "仓库")
	private Long facilityId;
	//库位
    @ApiModelProperty(value = "库位")
	private Long facilityLocationId;
	//单位
    @ApiModelProperty(value = "单位")
	private Long unit;
	//采购价
    @ApiModelProperty(value = "采购价")
	private BigDecimal purchasePrice;
	//零售价
    @ApiModelProperty(value = "零售价")
	private BigDecimal retailPrice;
	//批发价
    @ApiModelProperty(value = "批发价")
	private BigDecimal tradePrice;
	//会员价
    @ApiModelProperty(value = "会员价")
	private BigDecimal memberPrice;
	//销售折扣1
    @ApiModelProperty(value = "销售折扣1")
	private BigDecimal discountOne;
	//销售折扣2
    @ApiModelProperty(value = "销售折扣2")
	private BigDecimal discountTwo;
	//最低库存
    @ApiModelProperty(value = "最低库存")
	private Long minStock;
	//最高库存
    @ApiModelProperty(value = "最高库存")
	private Long maxStock;
	//使用率
    @ApiModelProperty(value = "使用率")
	private Integer usage=0;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除标志
    @ApiModelProperty(value = "删除标志",hidden = true)
	private Integer delFlag;

	/**
	 * 设置：自增主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：自增主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：产品编号
	 */
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	/**
	 * 获取：产品编号
	 */
	public String getSerialno() {
		return serialno;
	}
	/**
	 * 设置：产品名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：产品名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：规格型号
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * 获取：规格型号
	 */
	public String getModel() {
		return model;
	}
	/**
	 * 设置：产品条码
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	/**
	 * 获取：产品条码
	 */
	public String getBarCode() {
		return barCode;
	}
	/**
	 * 设置：产品类型
	 */
	public void setType(Long type) {
		this.type = type;
	}
	/**
	 * 获取：产品类型
	 */
	public Long getType() {
		return type;
	}
	/**
	 * 设置：仓库
	 */
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	/**
	 * 获取：仓库
	 */
	public Long getFacilityId() {
		return facilityId;
	}
	/**
	 * 设置：库位
	 */
	public void setFacilityLocationId(Long facilityLocationId) {
		this.facilityLocationId = facilityLocationId;
	}
	/**
	 * 获取：库位
	 */
	public Long getFacilityLocationId() {
		return facilityLocationId;
	}
	/**
	 * 设置：单位
	 */
	public void setUnit(Long unit) {
		this.unit = unit;
	}
	/**
	 * 获取：单位
	 */
	public Long getUnit() {
		return unit;
	}
	/**
	 * 设置：采购价
	 */
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	/**
	 * 获取：采购价
	 */
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	/**
	 * 设置：零售价
	 */
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}
	/**
	 * 获取：零售价
	 */
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	/**
	 * 设置：批发价
	 */
	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}
	/**
	 * 获取：批发价
	 */
	public BigDecimal getTradePrice() {
		return tradePrice;
	}
	/**
	 * 设置：会员价
	 */
	public void setMemberPrice(BigDecimal memberPrice) {
		this.memberPrice = memberPrice;
	}
	/**
	 * 获取：会员价
	 */
	public BigDecimal getMemberPrice() {
		return memberPrice;
	}
	/**
	 * 设置：销售折扣1
	 */
	public void setDiscountOne(BigDecimal discountOne) {
		this.discountOne = discountOne;
	}
	/**
	 * 获取：销售折扣1
	 */
	public BigDecimal getDiscountOne() {
		return discountOne;
	}
	/**
	 * 设置：销售折扣2
	 */
	public void setDiscountTwo(BigDecimal discountTwo) {
		this.discountTwo = discountTwo;
	}
	/**
	 * 获取：销售折扣2
	 */
	public BigDecimal getDiscountTwo() {
		return discountTwo;
	}
	/**
	 * 设置：最低库存
	 */
	public void setMinStock(Long minStock) {
		this.minStock = minStock;
	}
	/**
	 * 获取：最低库存
	 */
	public Long getMinStock() {
		return minStock;
	}
	/**
	 * 设置：最高库存
	 */
	public void setMaxStock(Long maxStock) {
		this.maxStock = maxStock;
	}
	/**
	 * 获取：最高库存
	 */
	public Long getMaxStock() {
		return maxStock;
	}
	/**
	 * 设置：使用率
	 */
	public void setUsage(Integer usage) {
		this.usage = usage;
	}
	/**
	 * 获取：使用率
	 */
	public Integer getUsage() {
		return usage;
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
	 * 设置：删除标志
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
