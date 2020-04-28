package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 盘点盈亏单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-24 14:50:47
 */
@Data
@ApiModel(value = "盘点盈亏单")
public class InventoryPlanFitlossDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//盘点主表id
    @ApiModelProperty(value = "盘点主表id")
	private Long headId;
	//盘点子表id
    @ApiModelProperty(value = "盘点子表id")
	private Long bodyId;
	//盈亏编码（作为来源单号）
    @ApiModelProperty(value = "盈亏编码（作为来源单号）")
	private String code;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long documentType;


}
