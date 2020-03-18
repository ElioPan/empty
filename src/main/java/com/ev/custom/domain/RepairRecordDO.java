package com.ev.custom.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 维修记录表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:37
 */
@Data
@ApiModel(value = "维修记录表")
public class RepairRecordDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "记录主键")
	private Long id;
	//故障类型
    @ApiModelProperty(value = "故障类型(设备故障：103 电路故障：104 油路故障：105)",required = true)
	private Integer type;
	//故障等级
    @ApiModelProperty(value = "故障等级",hidden = true)
	private Integer level;
	//使用情况
    @ApiModelProperty(value = "使用情况(正常运行:106 带病运行:107 停机待修:108 停用报废:109)",required = true)
	private Integer usage;
	//记录状态
    @ApiModelProperty(value = "维修状态",hidden = true)
	private Integer status;
	//维修人
    @ApiModelProperty(value = "维修人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//维修开始时间
    @ApiModelProperty(value = "维修开始时间(格式：2019-08-01 10:00:00)",required = true)
	private Date startTime;
	//维修结束时间
    @ApiModelProperty(value = "维修结束时间(格式：2019-08-01 10:00:00)",required = true)
	private Date endTime;
	//维修结束时间
	@ApiModelProperty(value = "停机时间(格式：2019-08-01 10:00:00)")
	private Date offTime;
	//停机时长
    @ApiModelProperty(value = "停机时长")
	private Double offHour;
	//工时
    @ApiModelProperty(value = "工时")
	private Double manHour;
	//成本
    @ApiModelProperty(value = "成本")
	private BigDecimal cost;
	//内容
    @ApiModelProperty(value = "内容",hidden = true)
	private String content;
	//故障原因
    @ApiModelProperty(value = "故障原因",required = true)
	private String cause;
	//解决方案
    @ApiModelProperty(value = "解决方案")
	private String solution;
	//事件ID
    @ApiModelProperty(value = "事件ID",hidden = true)
	private Long eventId;
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
