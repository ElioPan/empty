package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 采购费用主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-10 14:30:32
 */
@Data
@ApiModel(value = "采购费用主表")
public class PurchaseExpenseDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//费用单号
    @ApiModelProperty(value = "费用单号")
	private String expenseCode;
	//采购入库单主键
    @ApiModelProperty(value = "采购入库单主键")
	private Long purchaseId;
	//公司/供应商
    @ApiModelProperty(value = "公司/供应商")
	private Long supplierId;
	//开票日期
    @ApiModelProperty(value = "开票日期")
	private Date invoiceDate;
	//收票人
    @ApiModelProperty(value = "收票人")
	private Long drawer;
	//发票号码
    @ApiModelProperty(value = "发票号码")
	private String invoiceNum;
	//票据类型
    @ApiModelProperty(value = "票据类型")
	private Long billType;
	//审核状态
    @ApiModelProperty(value = "审核状态")
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
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

}
