package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 维修事件表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
@Data
@ApiModel(value = "维修事件表")
public class RepairEventDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "事件主键")
	private Long id;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderno;
	//故障类型
    @ApiModelProperty(value = "故障类型(设备故障：103 电路故障：104 油路故障：105)",hidden = true)
	private Long type;
	//故障等级
    @ApiModelProperty(value = "故障等级(普通任务：50 紧急任务：49)",required = true)
	private Long level;
	//使用情况
    @ApiModelProperty(value = "使用情况(正常运行:106 带病运行:107 停机待修:108 停用报废:109)",hidden = true)
	private Long usage;
	//维修责任人
    @ApiModelProperty(value = "维修责任人",required = true)
	private Long engineerId;
	//要求完成时间
    @ApiModelProperty(value = "要求完成时间 (格式：2019-08-01 10:00:00)")
	private Date planTime;
	//工时
    @ApiModelProperty(value = "工时",hidden = true)
	private Double manHour;
	//成本
    @ApiModelProperty(value = "成本",hidden = true)
	private BigDecimal cost;
	//维修状态
    @ApiModelProperty(value = "维修状态",hidden = true)
	private Long status;
	//内容
    @ApiModelProperty(value = "详细描述",required = true)
	private String content;
	//关联设备
    @ApiModelProperty(value = "关联设备",required = true)
	private Long deviceId;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//报修时间
    @ApiModelProperty(value = "报修时间",hidden = true)
	private Date createTime;
    //关联单据ID
    @ApiModelProperty(value = "关联单据ID")
    private Long parentId;
    //关联单据类型
    @ApiModelProperty(value = "关联单据类型")
    private Long parentType;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;


}
