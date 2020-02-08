package com.ev.scm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 销售合同主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 11:18:13
 */
@Data
@ApiModel(value = "销售合同主表")
public class SalescontractDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//客户id
    @ApiModelProperty(value = "客户id")
	private Long clientId;
	//销售员
    @ApiModelProperty(value = "销售员")
	private Long salesPerson;
	//销售部门
    @ApiModelProperty(value = "销售部门")
	private Long salesPersonDept;
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
	//优惠折扣率
    @ApiModelProperty(value = "优惠折扣率")
	private BigDecimal discountRate;
	//优惠金额
    @ApiModelProperty(value = "优惠金额")
	private BigDecimal discountAmount;
	//优惠后金额
    @ApiModelProperty(value = "优惠后金额")
	private BigDecimal payAmount;
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
