package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 其他应收应付单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 16:27:52
 */
@Data
@ApiModel(value = "其他应收应付单主表")
public class OtherReceivablesDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//其他应收应付标记（应收YS，应付YF）
    @ApiModelProperty(value = "其他应收应付标记（应收YS，应付YF）")
	private String sign;
	//客户/供应商
    @ApiModelProperty(value = "客户/供应商")
	private Long supplierClientId;
	//收付款单号
    @ApiModelProperty(value = "收付款单号")
	private String code;
	//金额
    @ApiModelProperty(value = "金额")
	private BigDecimal amount;
	//摘要
    @ApiModelProperty(value = "摘要")
	private String digest;
	//部门
    @ApiModelProperty(value = "部门")
	private Long deptId;
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
