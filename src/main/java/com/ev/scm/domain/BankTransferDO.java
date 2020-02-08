package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 银行转账单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 16:17:25
 */
@Data
@ApiModel(value = "银行转账单主表")
public class BankTransferDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//转账日期
    @ApiModelProperty(value = "转账日期")
	private Date transferDate;
	//经办人
    @ApiModelProperty(value = "经办人")
	private Long 
transactorId;
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
