package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;



/**
 * 返工返修中间表（工序计划 与 报工、报检中间表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-19 11:32:31
 */
@Data
@ApiModel(value = "返工返修中间表（工序计划 与 报工、报检中间表）")
public class ReworkRepairMiddleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//工序计划id
    @ApiModelProperty(value = "工序计划id")
	private Long planId;
	//返工返修类型 (report ++ check)
    @ApiModelProperty(value = "返工返修类型 (report ++ check)")
	private String type;
	//返工返修源id
    @ApiModelProperty(value = "返工返修源id")
	private Long sourceId;

}
