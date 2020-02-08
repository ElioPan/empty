package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购申请表主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 13:11:44
 */
@Data
@ApiModel(value = "采购申请表主表")
public class PurchaseDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//单据类型
    @ApiModelProperty(value = "单据类型")
	private Long purchaseType;
	//单据号
    @ApiModelProperty(value = "单据号")
	private String purchaseCode;
	//公司/供应商
    @ApiModelProperty(value = "公司/供应商")
	private Long supplierId;
	//申请人
    @ApiModelProperty(value = "申请人")
	private Long applicant;
	//申请时间
    @ApiModelProperty(value = "申请时间")
	private Date applicantTime;
	//预交日期
    @ApiModelProperty(value = "预交日期")
	private Date advanceDate;
	//优惠折扣率
    @ApiModelProperty(value = "优惠折扣率",hidden = true)
	private BigDecimal discountRate;
	//优惠金额
    @ApiModelProperty(value = "优惠金额",hidden = true)
	private BigDecimal preferentialAmount;
	//优惠后金额
    @ApiModelProperty(value = "优惠后金额",hidden = true)
	private BigDecimal payAmount;
	//审核状态
    @ApiModelProperty(value = "审核状态",hidden = true)
	private Long auditSign;
	//审核人
    @ApiModelProperty(value = "审核人")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
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
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;

}
