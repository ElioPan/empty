package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 生产投料单（详情列表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 10:10:27
 */
@Data
@ApiModel(value = "生产投料单（详情列表）")
public class ProductionFeedingDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
    // 主表ID
    @ApiModelProperty()
	private Long headId;
	//产品ID
    @ApiModelProperty(value = "产品ID")
	private Integer materielId;
	//批号
    @ApiModelProperty(value = "批号")
	private String batchNo;
	//计划投料数量
    @ApiModelProperty(value = "计划投料数量")
	private BigDecimal planFeeding;
	//已出数量
    @ApiModelProperty(value = "已出数量")
	private BigDecimal outCount;
	//报废数量
    @ApiModelProperty(value = "报废数量")
	private BigDecimal scrapCount;
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
