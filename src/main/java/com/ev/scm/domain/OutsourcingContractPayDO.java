package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 委外合同付款条件
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
@Data
@ApiModel(value = "委外合同付款条件")
public class OutsourcingContractPayDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long contractId;
	//应付日期
    @ApiModelProperty(value = "应付日期")
	private Date payableDate;
	//应付金额
    @ApiModelProperty(value = "应付金额")
	private BigDecimal payableAmount;
	//已付金额
    @ApiModelProperty(value = "已付金额")
	private BigDecimal paidAmount;
	//未收金额
    @ApiModelProperty(value = "未收金额")
	private BigDecimal unpaidAmount;
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
