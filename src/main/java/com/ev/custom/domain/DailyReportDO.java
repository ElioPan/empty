package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 日报
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-14 16:22:19
 */
@Data
@ApiModel(value = "日报")
public class DailyReportDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//今日完成工作
	@ApiModelProperty(value = "今日完成工作")
	private String comContent;
	//未完成工作
	@ApiModelProperty(value = "未完成工作")
	private String uncomContent;
	//需协调工作
	@ApiModelProperty(value = "需协调工作")
	private String needConcertContent;
	//待定事项
	@ApiModelProperty(value = "待定事项")
	private String waitContet;
	//必做事项
	@ApiModelProperty(value = "必做事项")
	private String mustContet;
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
	//日志状态
	@ApiModelProperty(value = "日志状态")
	private Long status;

}
