package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 产品入库明细表子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:36:27
 */
@Data
@ApiModel(value = "产品入库明细表子表")
public class StockInItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表主键
    @ApiModelProperty(value = "主表主键")
	private Long inheadId;
	//商品/产品id
    @ApiModelProperty(value = "商品/产品id")
	private Long materielId;
	//批次
    @ApiModelProperty(value = "批次")
	private String batch;
	//数量
    @ApiModelProperty(value = "数量")
	private BigDecimal count;
	//单价
    @ApiModelProperty(value = "单价")
	private BigDecimal unitPrice;
	//金额
    @ApiModelProperty(value = "金额")
	private BigDecimal amount;
	//仓库
    @ApiModelProperty(value = "仓库")
	private Long warehouse;
	//库位
    @ApiModelProperty(value = "库位")
	private Long warehLocation;
	//退料原因
    @ApiModelProperty(value = "退料原因")
	private String returnReason;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceCode;
	//来源单据明细id
    @ApiModelProperty(value = "来源单据明细id")
	private Long sourceId;
	//费用
    @ApiModelProperty(value = "费用")
	private BigDecimal expense;
	@ApiModelProperty(value = "成本")
	private BigDecimal cost;
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

}
