package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工艺路线 明细
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:17
 */
@Data
@ApiModel(value = "工艺路线 明细")
public class CraftItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	@ApiModelProperty(value = "序号")
	private Integer serialNumber;
	//工艺路线主键
    @ApiModelProperty(value = "工艺路线主键")
	private Long craftId;
	//工序主键
    @ApiModelProperty(value = "工序主键")
	private Long processId;
	//是否委外(1是0否) 
    @ApiModelProperty(value = "是否委外(1是0否) ")
	private Integer whetherOutsource;
	//是否自动采集   1是 0否
    @ApiModelProperty(value = "是否自动采集   1是 0否")
	private Integer whetherCollect;
	//是否自动派工(1是0否)
    @ApiModelProperty(value = "是否自动派工(1是0否)")
	private Integer autoDispatch;
	//是否检验   1是 0否
    @ApiModelProperty(value = "是否检验   1是 0否")
	private Integer whetherExamine;
	//工艺要求
    @ApiModelProperty(value = "工艺要求")
	private String demand;
	//工作时长
    @ApiModelProperty(value = "工作时长")
	private BigDecimal totalHour;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long deptId;
	//基准良率%
    @ApiModelProperty(value = "基准良率%")
	private BigDecimal standard;
	//使用设备id
    @ApiModelProperty(value = "使用设备id")
	private Long deviceId;
	//工序类型
    @ApiModelProperty(value = "工序类型")
	private Long type;
	//单件工时
    @ApiModelProperty(value = "单件工时")
	private BigDecimal manHour;
	//单件工价
    @ApiModelProperty(value = "单件工价")
	private BigDecimal labourPrice;
	//操作工
    @ApiModelProperty(value = "操作工")
	private Long operator;
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
