package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购合同付款条件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:55:10
 */
@Data
@ApiModel(value = "采购合同付款条件表")
public class PurchasecontractPayDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//合同主表id
    @ApiModelProperty(value = "合同主表id")
	private Long purchaseContractId;
	//应付日期
    @ApiModelProperty(value = "应付日期")
	private Date dueDate;
	//应付金额
    @ApiModelProperty(value = "应付金额")
	private BigDecimal payAmount;
	//已付金额
    @ApiModelProperty(value = "已付金额")
	private BigDecimal amountPaid;
	//未付金额
    @ApiModelProperty(value = "未付金额")
	private BigDecimal unpayAmount;
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
