package com.ev.scm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 销售合同明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-09 11:18:14
 */
@Data
@ApiModel(value = "销售合同明细表")
public class SalescontractItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long salescontractId;
	//产品ID
    @ApiModelProperty(value = "产品ID")
	private Integer materielId;
    //0检验1 不检验
    @ApiModelProperty(value = "发货是否检验 0是/1否")
    private Integer isCheck;
	//销售数量
    @ApiModelProperty(value = "销售数量")
	private BigDecimal count;
	//销售金额
    @ApiModelProperty(value = "销售金额")
	private BigDecimal taxAmount;
	//税率%
    @ApiModelProperty(value = "税率%")
	private BigDecimal taxRate;
	//不含税单价
    @ApiModelProperty(value = "不含税单价")
	private BigDecimal unitPrice;
	//含税单价
    @ApiModelProperty(value = "含税单价")
	private BigDecimal taxUnitPrice;
	//不含税金额
    @ApiModelProperty(value = "不含税金额")
	private BigDecimal amount;
	//交货日期
    @ApiModelProperty(value = "交货日期")
	private Date deliveryDate;
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
