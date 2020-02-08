package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 银行转账单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:17:37
 */
@Data
@ApiModel(value = "银行转账单主表")
public class BankTransferItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表主键
    @ApiModelProperty(value = "主表主键")
	private Long transferId;
	//转出账号
    @ApiModelProperty(value = "转出账号")
	private Long transferOutAcc;
	//转入账号
    @ApiModelProperty(value = "转入账号")
	private Long transferInAcc;
	//转账金额
    @ApiModelProperty(value = "转账金额")
	private BigDecimal transferAmount;
	//结算方式
    @ApiModelProperty(value = "结算方式")
	private Long settlementType;
	//结算号
    @ApiModelProperty(value = "结算号")
	private String statementNumber;
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
