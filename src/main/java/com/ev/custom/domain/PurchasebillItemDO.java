package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-17 13:06:48
 */
@ApiModel(value = "采购票据明细表类")
public class PurchasebillItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//采购票据明细表类bid
    @ApiModelProperty(value = "自增主键bid")
	private Long bid;
	//产品名称
    @ApiModelProperty(value = "产品名称")
	private String productName;
    //产品id
    @ApiModelProperty(value = "产品id")
    private Long productId;
	//采购数量
    @ApiModelProperty(value = "采购数量")
	private BigDecimal num;
	//采购单价
    @ApiModelProperty(value = "采购单价")
	private BigDecimal price;
	//采购金额
    @ApiModelProperty(value = "采购金额")
	private BigDecimal money;
	//税率
    @ApiModelProperty(value = "税率")
	private BigDecimal taxRate;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long purchaseType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNum;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long purchasebillId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;
    //标志状态
    @ApiModelProperty(value = "标志状态")
	private Integer state;
    
	public Integer getState() {
		return state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	/**
	 * 设置：bid
	 */
	public void setBid(Long bid) {
		this.bid = bid;
	}
	/**
	 * 获取：bid
	 */
	public Long getBid() {
		return bid;
	}
	/**
	 * 设置：产品名称
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 获取：产品名称
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * 设置：采购数量
	 */
	public void setNum(BigDecimal num) {
		this.num = num;
	}
	/**
	 * 获取：采购数量
	 */
	public BigDecimal getNum() {
		return num;
	}
	/**
	 * 设置：采购单价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：采购单价
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：采购金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：采购金额
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * 设置：税率
	 */
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	/**
	 * 获取：税率
	 */
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	/**
	 * 设置：源单类型
	 */
	public void setPurchaseType(Long purchaseType) {
		this.purchaseType = purchaseType;
	}
	/**
	 * 获取：源单类型
	 */
	public Long getPurchaseType() {
		return purchaseType;
	}
	/**
	 * 设置：来源单号
	 */
	public void setSourceNum(String sourceNum) {
		this.sourceNum = sourceNum;
	}
	/**
	 * 获取：来源单号
	 */
	public String getSourceNum() {
		return sourceNum;
	}
	/**
	 * 设置：主表id
	 */
	public void setPurchasebillId(Long purchasebillId) {
		this.purchasebillId = purchasebillId;
	}
	/**
	 * 获取：主表id
	 */
	public Long getPurchasebillId() {
		return purchasebillId;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
