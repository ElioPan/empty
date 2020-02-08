package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 库存变化明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-22 09:10:36
 */
@Data
@ApiModel(value = "库存变化明细表")
public class StockItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//产品库存主键
    @ApiModelProperty(value = "产品库存主键")
	private Long stockId;
	//单据主键(主表主键)
    @ApiModelProperty(value = "单据主键(主表主键)")
	private Long inheadId;
	//单据主键（子表主键)
    @ApiModelProperty(value = "单据主键（子表主键)")
	private Long inbodyId;
	//单价
    @ApiModelProperty(value = "单价")
	private BigDecimal unitPrice;
	//数量
    @ApiModelProperty(value = "数量")
	private BigDecimal count;
	//出入库类型
    @ApiModelProperty(value = "出入库类型")
	private Long inOutType;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//生效状态(0未生效/1生效)
    @ApiModelProperty(value = "生效状态(0未生效/1生效)")
	private Long handleSign;
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
