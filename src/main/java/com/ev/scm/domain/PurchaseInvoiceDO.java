package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购发票主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-16 10:13:42
 */
@Data
@ApiModel(value = "采购发票主表")
public class PurchaseInvoiceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//公司/供应商
    @ApiModelProperty(value = "公司/供应商")
	private Long supplierId;
	//开票日期
    @ApiModelProperty(value = "开票日期")
	private Date invoiceDate;
	//收票人
    @ApiModelProperty(value = "收票人")
	private Long drawer;
	//票据单号
    @ApiModelProperty(value = "票据单号")
	private String billCode;
	//发票号码
    @ApiModelProperty(value = "发票号码")
	private String invoiceNum;
	//发票金额
    @ApiModelProperty(value = "发票金额")
	private BigDecimal invoiceAmount;
	//票据类型
    @ApiModelProperty(value = "票据类型")
	private Long billType;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
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
