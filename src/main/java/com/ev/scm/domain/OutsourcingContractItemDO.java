package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 委外合同明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:38
 */
@Data
@ApiModel(value = "委外合同明细")
public class OutsourcingContractItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long contractId;
	//物料ID
    @ApiModelProperty(value = "物料ID")
	private Long materielId;
	//0检验1 不检验
    @ApiModelProperty(value = "0检验1 不检验")
	private Integer isCheck;
	//BOMid
    @ApiModelProperty(value = "BOMid")
	private Long bomId;
	//是否限额
    @ApiModelProperty(value = "是否限额")
	private Integer isQuota;
	//加工数量
    @ApiModelProperty(value = "加工数量")
	private BigDecimal count;
	//含税单价
    @ApiModelProperty(value = "含税单价")
	private BigDecimal taxUnitPrice;
	//税率%
    @ApiModelProperty(value = "税率%")
	private BigDecimal taxRate;
	//销售金额
    @ApiModelProperty(value = "销售金额")
	private BigDecimal taxAmount;
	//不含税单价
    @ApiModelProperty(value = "不含税单价")
	private BigDecimal unitPrice;
	//不含税金额
    @ApiModelProperty(value = "不含税金额")
	private BigDecimal amount;
	//税额
    @ApiModelProperty(value = "税额")
	private BigDecimal taxes;
	//交货日期
    @ApiModelProperty(value = "交货日期")
	private Date deliveryDate;
	//源单ID（子表ID）
    @ApiModelProperty(value = "源单ID（子表ID）")
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

	@ApiModelProperty(value = "关闭状态")
	private Integer closeStatus;
	@ApiModelProperty(value = "关闭时间")
	private Date closeTime;
	@ApiModelProperty(value = "关闭原因")
	private String closeReason;

}
