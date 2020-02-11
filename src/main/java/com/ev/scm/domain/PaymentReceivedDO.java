package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 收款付款单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:18:04
 */
@Data
@ApiModel(value = "收款付款单主表")
public class PaymentReceivedDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//收付款单据标记（收款SK，付款FK）
    @ApiModelProperty(value = "收付款单据标记（收款SK，付款FK）")
	private String sign;
	//收款/付款类型
    @ApiModelProperty(value = "收款/付款类型")
	private Long prType;
	//收付款单号
    @ApiModelProperty(value = "收付款单号")
	private String prCode;
	//客户/供应商
    @ApiModelProperty(value = "客户/供应商")
	private Long supplierClientId;
	//申请人
    @ApiModelProperty(value = "申请人")
	private BigDecimal prAmount;
	//收款/付款日期
    @ApiModelProperty(value = "收款/付款日期")
	private Date prDate;
	//收款人
    @ApiModelProperty(value = "收款人")
	private String payee;
	//付款人id
    @ApiModelProperty(value = "付款人id")
	private Long payer;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//审核人
    @ApiModelProperty(value = "审核人")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
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
