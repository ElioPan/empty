package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-08 13:11:30
 */
@Data
@ApiModel(value = "银行转账单明细类")
public class BanktransferslipItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long tid;
	//转出帐号
    @ApiModelProperty(value = "转出帐号")
	private String transferredAccount;
	//转入帐号
    @ApiModelProperty(value = "转入帐号")
	private String transferAccount;
	//金额
    @ApiModelProperty(value = "金额")
	private BigDecimal money;
	//结算方式
    @ApiModelProperty(value = "结算方式")
	private Long settlementMethod;
	//结算号
    @ApiModelProperty(value = "结算号")
	private String settlementNumber;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long banktransferslipId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;
}
