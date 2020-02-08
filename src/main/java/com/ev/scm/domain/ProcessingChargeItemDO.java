package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 加工费用明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
@Data
@ApiModel(value = "加工费用明细表")
public class ProcessingChargeItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceCode;
	//
    @ApiModelProperty(value = "")
	private Long sourceId;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long chargeId;
	//产品ID
    @ApiModelProperty(value = "产品ID")
	private Integer materielId;
	//数量
    @ApiModelProperty(value = "数量")
	private BigDecimal count;
	//不含税单价
    @ApiModelProperty(value = "不含税单价")
	private BigDecimal unitPrice;
	//含税单价
    @ApiModelProperty(value = "含税单价")
	private BigDecimal taxUnitPrice;
	//税率%
    @ApiModelProperty(value = "税率%")
	private BigDecimal taxRate;
	//不含税金额
    @ApiModelProperty(value = "不含税金额")
	private BigDecimal amount;
	//税额
    @ApiModelProperty(value = "税额")
	private BigDecimal taxes;
	//价税合计
    @ApiModelProperty(value = "价税合计")
	private BigDecimal taxAmount;
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
