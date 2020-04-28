package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 任务
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-18 13:26:30
 */
@Data
@ApiModel(value = "任务")
public class TaskMainDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键" )
	private Long id;
	//主题
    @ApiModelProperty(value = "主题")
	private String title;
	//任务类型
    @ApiModelProperty(value = "任务类型")
	private Long taskType;
	//任务等级
    @ApiModelProperty(value = "任务等级")
	private Long taskLevel;
	//要求完成日期
    @ApiModelProperty(value = "要求完成日期")
	private Date expireDate;
	//详细描述
    @ApiModelProperty(value = "详细描述")
	private String detail;
	//任务编号
    @ApiModelProperty(value = "任务编号",hidden = true)
	private String taskNo;
	//处理状态
    @ApiModelProperty(value = "处理状态",hidden = true)
	private Long status;
	//关联单号
    @ApiModelProperty(value = "关联单号")
	private String linkOrderId;
    //关联单号
    @ApiModelProperty(value = "关联单号类型")
	private Long linkOrderType;
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
    @ApiModelProperty(value = "",hidden = true)
	private Long providerId;


}
