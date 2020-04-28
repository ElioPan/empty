package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 任务处理
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 13:26:30
 */
@Data
@ApiModel(value = "任务处理")
public class TaskReplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//任务单号
    @ApiModelProperty(value = "任务单号")
	private Long taskid;
	//原因分析
    @ApiModelProperty(value = "原因分析")
	private String reason;
	//解决方案
    @ApiModelProperty(value = "解决方案/验收说明")
	private String solution;
    //处理状态
    @ApiModelProperty(value = "处理状态115 通过 116 不通过")
	private Long status;
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
	//
    @ApiModelProperty(value = "处理记录ID")
	private Long dealId;
	//
    @ApiModelProperty(value = "",hidden = true)
	private Long replyType;
	//
	@ApiModelProperty(value = "完成效率")
	private Integer completeRate;
	//
    @ApiModelProperty(value = "完成质量")
	private Integer completeQuality;
	//
    @ApiModelProperty(value = "工作态度")
	private Integer workState;


	
}
