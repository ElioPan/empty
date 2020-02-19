package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 产品入库明细表主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-21 14:36:20
 */
@Data
@ApiModel(value = "产品入库明细表主表")
public class StockInDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//入库单据号
    @ApiModelProperty(value = "入库单据号")
	private String inheadCode;
	//入库类型
    @ApiModelProperty(value = "入库类型")
	private Long storageType;
	//来源公司/供应商
    @ApiModelProperty(value = "来源公司/供应商")
	private Long sourceCompany;
	//客戶
    @ApiModelProperty(value = "客戶")
	private Long clientId;
	//銷售部門
    @ApiModelProperty(value = "銷售部門")
	private Long deptId;
	//进出仓日期
    @ApiModelProperty(value = "进出仓日期")
	private Date inOutTime;
	//操作员/銷售員
    @ApiModelProperty(value = "操作员/銷售員")
	private Long operator;
	//采购方式/销售方式
    @ApiModelProperty(value = "采购方式/销售方式")
	private Integer purchaseType;
	//付款帐号
    @ApiModelProperty(value = "付款帐号")
	private String payAccount;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//审核标志：（审核/反审）
    @ApiModelProperty(value = "审核标志：（审核/反审）")
	private Long auditSign;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
	//入库时间
    @ApiModelProperty(value = "入库时间")
	private Date inTime;
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
	//删除状态（）
    @ApiModelProperty(value = "删除状态（）")
	private Integer delFlag;
	@ApiModelProperty(value = "核算1，分配0（默认0）")
	private Integer sign;

}
