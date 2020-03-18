package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 巡检记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:38
 */
@Data
@ApiModel(value = "巡检记录表")
public class PatrolRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderno;
	//计划名称
    @ApiModelProperty(value = "巡检名称")
	private String name;
	//责任人
    @ApiModelProperty(value = "责任人")
	private Long engineerId;
	//联系电话
    @ApiModelProperty(value = "联系电话")
	private String cellphone;
	//巡检开始时间
    @ApiModelProperty(value = "巡检开始时间")
	private Date startTime;
	//巡检结束时间
    @ApiModelProperty(value = "巡检结束时间")
	private Date endTime;
	//任务状态
    @ApiModelProperty(value = "任务状态",hidden = true)
	private Integer status ;//56待处理；57待验收；58已完成
	//内容
    @ApiModelProperty(value = "内容",hidden = true)
	private String content;
	//任务结果
    @ApiModelProperty(value = "任务结果",hidden = true)
	private Integer result;
	//计划ID
    @ApiModelProperty(value = "计划ID",hidden = true)
	private Long planId;
    //计划ID
    @ApiModelProperty(value = "通知单ID",hidden = true)
	private Long informId;
	//完成时间
    @ApiModelProperty(value = "完成时间")
	private Date completeTime;
    //关闭原因
    @ApiModelProperty(value = "关闭原因")
	private String closeReason;
    //创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;
	//设备ID
	@ApiModelProperty(value = "设备ID",hidden = true)
	private Long deviceId;

}
