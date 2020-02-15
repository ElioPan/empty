package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 收款/付款明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:18:17
 */
@Data
@ApiModel(value = "收款/付款明细")
public class PaymentReceivedItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long paymentReceivedId;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//源单编号
    @ApiModelProperty(value = "源单编号")
	private String sourceCode;
	//源单主键
    @ApiModelProperty(value = "源单主键")
	private Long sourceId;
	//源单明细主键
    @ApiModelProperty(value = "源单明细主键")
	private Long sourcePayItemId;
	//应收/应付日期
    @ApiModelProperty(value = "应收/应付日期")
	private Date accrualDate;
	//应收/应付金额
    @ApiModelProperty(value = "应收/应付金额")
	private BigDecimal 
receivablePayablesAmount;
	//已收/已付金额
    @ApiModelProperty(value = "已收/已付金额")
	private BigDecimal paidReceivedAmount;
	//本次收款/付款金额
    @ApiModelProperty(value = "本次收款/付款金额")
	private BigDecimal thisAmount;
	//未收/未金额
    @ApiModelProperty(value = "未收/未金额")
	private String noReceiptPaymentAmount;
	//收款/付款账号
    @ApiModelProperty(value = "收款/付款账号")
	private Long accountNumber;
	//结算方式
    @ApiModelProperty(value = "结算方式")
	private Long settlementType;
	//结算号
    @ApiModelProperty(value = "结算号")
	private String statementNumber;
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

}
