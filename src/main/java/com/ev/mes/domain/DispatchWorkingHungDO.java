package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 开工/挂起记录
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-27 20:21:50
 */

@Data
@ApiModel(value = "开工/挂起记录")
public class DispatchWorkingHungDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工单明细主键
    @ApiModelProperty(value = "工单明细主键")
	private Long dispatchItemId;
	//开始时间
    @ApiModelProperty(value = "开始时间")
	private Date startTime;
	//挂起时间
    @ApiModelProperty(value = "挂起时间")
	private Date hangTime;
	//时长
    @ApiModelProperty(value = "时长")
	private BigDecimal manHour;
	//开工/挂起 人
    @ApiModelProperty(value = "开工/挂起 人")
	private Long startHangId;
	//标记（1开工记录 0挂起记录）
    @ApiModelProperty(value = "标记（1开工记录 0挂起记录）")
	private Integer sign;
	//创建人(派单人)
    @ApiModelProperty(value = "创建人(派单人)")
	private Long createBy;
	//派单时间
    @ApiModelProperty(value = "派单时间")
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

	@ApiModelProperty(value = "挂起原因")
	private String  pendingReason;




}
