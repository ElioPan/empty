package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 资金初始子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-16 09:55:59
 */
@Data
@ApiModel(value = "资金初始子表")
public class FundInitializationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//期间
    @ApiModelProperty(value = "期间")
	private Date period;
	//开户行
    @ApiModelProperty(value = "开户行")
	private Long bank;
	//银行账号
    @ApiModelProperty(value = "银行账号")
	private String accountNumber;
	//期初金额
    @ApiModelProperty(value = "期初金额")
	private BigDecimal initialAmount;
	//启用0，禁用1
    @ApiModelProperty(value = "启用0，禁用1")
	private Integer usingStart;
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
