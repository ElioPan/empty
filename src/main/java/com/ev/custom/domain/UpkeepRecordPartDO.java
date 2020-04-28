package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 保养工单与实际使用备件中间表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 10:37:18
 */
@Data
@ApiModel(value = "保养工单与实际使用备件中间表")
public class UpkeepRecordPartDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//保养计划ID
	@ApiModelProperty(value = "保养计划ID")
	private Long planId;
	//保养工单ID
	@ApiModelProperty(value = "保养工单ID")
	private Long recordId;
	//备件id
	@ApiModelProperty(value = "备件id")
	private Long partId;
	//使用备件数量
	@ApiModelProperty(value = "使用备件数量")
	private String spartAmount;
	//备件单价
	@ApiModelProperty(value = "备件单价")
	private Double spartPrice;
	//备件单位名
	@ApiModelProperty(value = "备件单位名")
	private String spartUnit;
	//总金额
	@ApiModelProperty(value = "总金额")
	private Double spartSum;
	//备注
	@ApiModelProperty(value = "备注")
	private String remark;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;

}
