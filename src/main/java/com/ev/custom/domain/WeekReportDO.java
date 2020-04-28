package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 周报
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-13 16:22:59
 */
@Data
@ApiModel(value = "周报")
public class WeekReportDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//本周完成工作
	@ApiModelProperty(value = "本周完成工作")
	private String comContent;
	//本周工作总结
	@ApiModelProperty(value = "本周工作总结")
	private String contentSum;
	//下周工作计划
	@ApiModelProperty(value = "下周工作计划")
	private String contentPlan;
	//备注
	@ApiModelProperty(value = "备注")
	private String description;
	//本周开始时间
	@ApiModelProperty(value = "本周开始时间")
	private Date startTime;
	//本周结束时间
	@ApiModelProperty(value = "本周结束时间")
	private Date endTime;
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
	//周报状态
	@ApiModelProperty(value = "周报状态")
	private Long status;


}
