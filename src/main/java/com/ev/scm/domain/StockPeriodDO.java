package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 期间记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
@Data
@ApiModel(value = "期间记录表")
public class StockPeriodDO implements Serializable {
	private static final long serialVersionUID = 1L;
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//主表id
    @ApiModelProperty(value = "期间")
	private Date period;

}
