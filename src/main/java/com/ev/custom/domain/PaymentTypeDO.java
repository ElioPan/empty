package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 收支类型
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 01:45:14
 */
@Data
@ApiModel(value = "收支类型")
public class PaymentTypeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//编码
    @ApiModelProperty(value = "编码")
	private String code;
	//名称
    @ApiModelProperty(value = "名称")
	private String name;
	//
    @ApiModelProperty(value = "")
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
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;

}
