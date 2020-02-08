package com.ev.custom.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 求购单商品详细类
 * @author ABC
 *
 */
public class PurchaseProductDO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 //商品详情自增主键
    @ApiModelProperty(value = "商品详情自增主键")
    private Long pid;
	//产品名称
    @ApiModelProperty(value = "产品名称")
    private String productName;
    //产品id
    @ApiModelProperty(value = "产品id")
    private Long productId;
	//产品编号
    @ApiModelProperty(value = "产品编号")
    private String productCode;
    //规格型号
    @ApiModelProperty(value = "规格型号")
    private String model;
    //单位
    @ApiModelProperty(value = "单位")
    private String unit;
    //请购数量
    @ApiModelProperty(value = "请购数量")
    private BigDecimal num;
    //采购单价
    @ApiModelProperty(value = "采购单价")
    private BigDecimal price;
    //采购金额
    @ApiModelProperty(value = "采购金额")
    private BigDecimal money;
    //源单类型
    @ApiModelProperty(value = "源单类型")
    private Long purchaseType;
    //来源单号
    @ApiModelProperty(value = "来源单号")
    private String sourceNum;
    //商品详情删除状态
    @ApiModelProperty(value = "商品详情删除状态")
    private Integer delFlag;
    //主表ID
    @ApiModelProperty(value = "主表ID")
    private Long purchaseId;
    
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductCode() {
		return productCode;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Long getPurchaseType() {
		return purchaseType;
	}
	
	public void setPurchaseType(Long purchaseType) {
		this.purchaseType = purchaseType;
	}
	
	public String getSourceNum() {
		return sourceNum;
	}
	
	public void setSourceNum(String sourceNum) {
		this.sourceNum = sourceNum;
	}
	
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
