package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 报警规则
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-22 14:06:08
 */
@Data
@ApiModel(value = "报警规则")
public class AlarmRuleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
    //告警规则ID
    @ApiModelProperty(value = "告警规则ID",hidden = true)
	private Long groupId;
	//告警名称
    @ApiModelProperty(value = "告警名称",required = true)
	private String name;
	//告警类型
    @ApiModelProperty(value = "告警类型(157,158)",required = true)
	private Long alarmType;
	//告警等级
    @ApiModelProperty(value = "告警等级(165,166)",required = true)
	private Long alarmLevel;
	//触发方式
    @ApiModelProperty(value = "触发方式(0:大于 1:小于 2:等于)",required = true)
	private Long triggerMode;
	//触发时长
    @ApiModelProperty(value = "触发时长",required = true)
	private Integer triggerTime;
	//持续时长
    @ApiModelProperty(value = "持续时长",required = true)
	private Integer continueTime;
	//告警方式
    @ApiModelProperty(value = "告警方式(0:APP推送 1:短信推送 2:电话推送)格式如: 0,1,2")
	private String alarmWay;
	//排序号
    @ApiModelProperty(value = "排序号",hidden = true)
	private Long sortNo;
	//测点ID
    @ApiModelProperty(value = "测点ID",hidden = true)
	private Long pointId;
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

}
