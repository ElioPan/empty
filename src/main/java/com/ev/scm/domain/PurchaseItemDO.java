package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购申请明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 13:12:11
 */
@Data
@ApiModel(value = "采购申请明细表")
public class PurchaseItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "明细主键")
	private Long id;
	//采购主表id
    @ApiModelProperty(value = "采购主表id")
	private Long purchaseId;
	//商品/产品id
    @ApiModelProperty(value = "商品/产品id")
	private Long materielId;
	//请购数量
    @ApiModelProperty(value = "请购数量")
	private BigDecimal count;
	//采购单价
    @ApiModelProperty(value = "采购单价")
	private BigDecimal unitPrice;
	//采购金额
    @ApiModelProperty(value = "采购金额")
	private BigDecimal amount;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceCode;
	//
    @ApiModelProperty(value = "来源单明细主键")
	private Long sourceId;
	//制单人
    @ApiModelProperty(value = "制单人",hidden = false)
	private Long createBy;
	//制单时间
    @ApiModelProperty(value = "制单时间",hidden = false)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = false)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = false)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = false)
	private Integer delFlag;

}
