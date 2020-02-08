package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购合同明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:54:59
 */
@Data
@ApiModel(value = "采购合同明细表")
public class PurchasecontractItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long purchaseContractId;
	//商品/产品id
    @ApiModelProperty(value = "商品/产品id")
	private Long materielId;
	//采购数量
    @ApiModelProperty(value = "采购数量")
	private BigDecimal count;
	//含税单价
    @ApiModelProperty(value = "含税单价")
	private BigDecimal taxUnitPrice;
	//不含税单价
    @ApiModelProperty(value = "不含税单价")
	private BigDecimal unitPrice;
	//税率
    @ApiModelProperty(value = "税率")
	private BigDecimal taxRate;
	//含税金额
    @ApiModelProperty(value = "含税金额")
	private BigDecimal taxAmount;
	//不含税金额
    @ApiModelProperty(value = "不含税金额")
	private BigDecimal amount;
	//c
    @ApiModelProperty(value = "c")
	private BigDecimal taxes;
	//是否检验（1是0否）
    @ApiModelProperty(value = "是否检验（1是0否）")
	private Integer isCheck;
	//是否允许超收（1是0否）
    @ApiModelProperty(value = "是否允许超收（1是0否）")
	private Integer isOvercharge;
	//超收比例
    @ApiModelProperty(value = "超收比例")
	private BigDecimal proportion;
	//交货日期
    @ApiModelProperty(value = "交货日期")
	private Date deliveryDate;
	//原单主键
    @ApiModelProperty(value = "原单主键")
	private Long sourceId;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceCode;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//制单人
    @ApiModelProperty(value = "制单人")
	private Long createBy;
	//制单时间
    @ApiModelProperty(value = "制单时间")
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

}
