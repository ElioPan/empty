package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 出库子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 16:55:49
 */
@Data
@ApiModel(value = "出库子表")
public class StockOutItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//主表主键
    @ApiModelProperty(value = "主表主键")
	private Long outId;
	//所选商品库存id集
    @ApiModelProperty(value = "所选商品库存id集")
	private String stockId;
	//物料ID
    @ApiModelProperty(value = "物料ID")
	private Integer materielId;
	//批号
    @ApiModelProperty(value = "批号")
	private String batch;
	//数量/退货/出库数量
    @ApiModelProperty(value = "数量/退货/出库数量")
	private BigDecimal count;
	//单价/成本单价
    @ApiModelProperty(value = "单价/成本单价")
	private BigDecimal unitPrice;
	//销售单价
    @ApiModelProperty(value = "销售单价")
	private BigDecimal sellUnitPrice;
	//成本金额/出库金额
    @ApiModelProperty(value = "成本金额/出库金额")
	private BigDecimal amount;
	//销售金额
    @ApiModelProperty(value = "销售金额")
	private BigDecimal sellAmount;
    //核销数量
    @ApiModelProperty(value = "核销数量")
    private BigDecimal chargeOffCount;
	//出库用途/退货原因
    @ApiModelProperty(value = "出库用途/退货原因")
	private Long purpose;
	//出库状态
    @ApiModelProperty(value = "出库状态")
	private Long outStatus;
	//源单ID（子表ID）
    @ApiModelProperty(value = "源单ID（子表ID）")
	private Long sourceId;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceCode;
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

}
