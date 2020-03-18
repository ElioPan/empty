package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 往来初始化
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-18 10:48:36
 */
@Data
@ApiModel(value = "往来初始化")
public class CommutationInitializationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//期间
    @ApiModelProperty(value = "期间")
	private Date period;
	//客户
    @ApiModelProperty(value = "客户")
	private Long clientId;
	//供应商
    @ApiModelProperty(value = "供应商")
	private Long supplierId;
	//期初金额
    @ApiModelProperty(value = "期初金额")
	private BigDecimal initialAmount;
	//启用0，禁用1
    @ApiModelProperty(value = "启用0，禁用1",hidden = true)
	private Integer usingStart;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;

}
