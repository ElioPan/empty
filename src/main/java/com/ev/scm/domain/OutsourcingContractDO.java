package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 委外合同
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:38
 */
@Data
@ApiModel(value = "委外合同")
public class OutsourcingContractDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//供应商id
    @ApiModelProperty(value = "供应商id")
	private Long supplierId;
	//采购员
    @ApiModelProperty(value = "采购员")
	private Long purchasePerson;
	//合同编号
    @ApiModelProperty(value = "合同编号")
	private String contractCode;
	//合同类型
    @ApiModelProperty(value = "合同类型")
	private Long contractType;
	//合同日期
    @ApiModelProperty(value = "合同日期")
	private Date contractDate;
	//合同有效期
    @ApiModelProperty(value = "合同有效期")
	private Date contractExpireDate;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//已开票金额
    @ApiModelProperty(value = "已开票金额")
	private BigDecimal invoicedAmount;
	//未开票金额
    @ApiModelProperty(value = "未开票金额")
	private BigDecimal uninvoicedAmount;
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
	//关闭状态(0未关闭/1关闭)
    @ApiModelProperty(value = "关闭状态(0未关闭/1关闭)")
	private Integer closeStatus;

}
