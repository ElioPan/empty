package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 存货报表分析
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-15 09:25:43
 */
@Data
@ApiModel(value = "存货报表分析")
public class StockAnalysisDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//期间
    @ApiModelProperty(value = "期间")
	private Date period;
	//物料ID
    @ApiModelProperty(value = "物料ID")
	private Integer materielId;
	//批号
    @ApiModelProperty(value = "批号")
	private String batch;
	//期初数量
    @ApiModelProperty(value = "期初数量")
	private BigDecimal initialCount;
	//期初金额
    @ApiModelProperty(value = "期初金额")
	private BigDecimal initialAmount;
	//本月入库数量
    @ApiModelProperty(value = "本月入库数量")
	private BigDecimal inCount;
	//本月入库金额
    @ApiModelProperty(value = "本月入库金额")
	private BigDecimal inAmount;
	//本月发出数量
    @ApiModelProperty(value = "本月发出数量")
	private BigDecimal outCount;
	//本月发出金额
    @ApiModelProperty(value = "本月发出金额")
	private BigDecimal outAmount;
	//期末结存数量
    @ApiModelProperty(value = "期末结存数量")
	private BigDecimal finalCount;
	//期末结存金额
    @ApiModelProperty(value = "期末结存金额")
	private BigDecimal finalAmount;
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
