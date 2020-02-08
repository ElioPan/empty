package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购票据明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-16 10:30:18
 */
@Data
@ApiModel(value = "采购票据明细表")
public class PurchaseInvoiceItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long purchasebillId;
	//商品/产品id
    @ApiModelProperty(value = "商品/产品id")
	private Long materielId;
	//数量
    @ApiModelProperty(value = "数量")
	private BigDecimal count;
	//不含税单价
    @ApiModelProperty(value = "不含税单价")
	private BigDecimal unitPrice;
	//含税单价
    @ApiModelProperty(value = "含税单价")
	private BigDecimal taxUnitPrice;
	//不含税金额
    @ApiModelProperty(value = "不含税金额")
	private BigDecimal amount;
	//税率
    @ApiModelProperty(value = "税率")
	private BigDecimal taxRate;
	//税额
    @ApiModelProperty(value = "税额")
	private BigDecimal taxes;
	//含税金额（价格合计）
    @ApiModelProperty(value = "含税金额（价格合计）")
	private BigDecimal taxAmount;
	//来源单id
    @ApiModelProperty(value = "来源单id")
	private Long sourceId;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceCode;
	//制单人
    @ApiModelProperty(value = "制单人")
	private Long createBy;
	//制单时间
    @ApiModelProperty(value = "制单时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden=true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden=true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden=true)
	private Integer delFlag;

}
