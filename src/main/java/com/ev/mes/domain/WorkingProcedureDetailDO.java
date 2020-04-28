package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 工序计划单的工序详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-25 11:26:07
 */
@Data
@ApiModel(value = "工序计划单的工序详情")
public class WorkingProcedureDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//工序计划ID
    @ApiModelProperty(value = "工序计划ID")
	private Long planId;

	@ApiModelProperty(value = "序号")
	private Integer serialNumber;
	//工序ID
    @ApiModelProperty(value = "工序ID")
	private Long processId;
    //工序类型
    @ApiModelProperty(value = "工序类型")
    private Long processType;
	//工艺要求
    @ApiModelProperty(value = "工艺要求")
	private String demand;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long deptId;
	//操作工
    @ApiModelProperty(value = "操作工")
	private Long operator;
	//是否检验   1是 0否
    @ApiModelProperty(value = "是否检验   1是 0否")
	private Integer isExamine;
	//是否委外(1是0否) 
    @ApiModelProperty(value = "是否委外(1是0否) ")
	private Integer isOutsource;
	//是否联网采集（1是/0不是）
    @ApiModelProperty(value = "是否联网采集（1是/0不是）")
	private Integer isCollect;
	//使用设备id
    @ApiModelProperty(value = "使用设备id")
	private Long deviceId;
	//设备名称
    @ApiModelProperty(value = "设备名称")
	private String deviceName;
	//是否自动派工(0非自动/1自动)
    @ApiModelProperty(value = "是否自动派工(0非自动/1自动)")
	private Integer isAuto;
	//是否已被派工(工序计划状态值）
    @ApiModelProperty(value = "是否已被派工(工序计划状态值）")
	private Long isDispatching;
	//计划生产数量
    @ApiModelProperty(value = "计划生产数量")
	private BigDecimal planCount;
	//计划开始时间
    @ApiModelProperty(value = "计划开始时间")
	private Date planStartTime;
	//计划完工时间
    @ApiModelProperty(value = "计划完工时间")
	private Date planEndTime;
	//基准良率%
    @ApiModelProperty(value = "基准良率%")
	private BigDecimal standard;
	//机台工作时长
    @ApiModelProperty(value = "机台工作时长")
	private BigDecimal totalHour;
	//单件工时
    @ApiModelProperty(value = "单件工时")
	private BigDecimal manHour;
	//单件工价
    @ApiModelProperty(value = "单件工价")
	private BigDecimal labourPrice;
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
  //已派工数量
    @ApiModelProperty(value = "已派工数量")
	private BigDecimal alreadyCount;
	//完工数量
    @ApiModelProperty(value = "完工数量")
	private BigDecimal completionCount;
    // 实际开工时间
    @ApiModelProperty(value = "实际开工时间",hidden = true)
	private Date realityEndTime;
    // 实际完工时间
    @ApiModelProperty(value = "实际完工时间",hidden = true)
	private Date realityStartTime;
    // 拆分前的工序ID
    @ApiModelProperty(value = "拆分前的工序ID")
	private Long beforeSplitId;


	
}
