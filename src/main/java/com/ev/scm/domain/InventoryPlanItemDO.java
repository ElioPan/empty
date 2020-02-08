package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 盘点子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-22 14:33:34
 */
@Data
@ApiModel(value = "盘点子表")
public class InventoryPlanItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//主表主键
    @ApiModelProperty(value = "主表主键")
	private Long headId;
	//商品/产品id 
    @ApiModelProperty(value = "商品/产品id ")
	private Long materielId;
	//库存id 
    @ApiModelProperty(value = "库存id ")
	private Long stockId;
	//所在仓库id
    @ApiModelProperty(value = "所在仓库id")
	private Long warehouse;
	//所在仓位
    @ApiModelProperty(value = "所在仓位")
	private Long warehLocation;
	//批次
    @ApiModelProperty(value = "批次")
	private String batch;
	//系统数量
    @ApiModelProperty(value = "系统数量")
	private BigDecimal systemCount;
	//盘点数量
    @ApiModelProperty(value = "盘点数量")
	private BigDecimal checkCount;
	//盈亏数量 （赢+亏-）
    @ApiModelProperty(value = "盈亏数量 （赢+亏-）")
	private BigDecimal profitLoss;
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
