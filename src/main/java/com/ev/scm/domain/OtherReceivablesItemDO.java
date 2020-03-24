package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 其他应收应付明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 16:27:57
 */
@Data
@ApiModel(value = "其他应收应付明细")
public class OtherReceivablesItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long receivedId;
	//应收/应付日期
    @ApiModelProperty(value = "应收/应付日期")
	private Date accrualDate;
	//应收/应付金额
    @ApiModelProperty(value = "应收/应付金额")
	private BigDecimal receivablePayablesAmount;
	//已收/已付金额
    @ApiModelProperty(value = "已收/已付金额")
	private BigDecimal paidReceivedAmount;
	//未收/未付金额
    @ApiModelProperty(value = "未收/未付金额")
	private BigDecimal noReceiptPaymentAmount;
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
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

}
