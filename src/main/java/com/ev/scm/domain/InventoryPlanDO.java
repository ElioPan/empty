package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 盘点主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-19 14:22:10
 */
@Data
@ApiModel(value = "盘点主表")
public class InventoryPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "主表id")
	private Long id;
	//计划盘点时间
    @ApiModelProperty(value = "计划盘点时间")
	private Date planTime;
	//实盘时间
    @ApiModelProperty(value = "实盘时间",hidden=false)
	private Date actureTime;
	//盘点编号
    @ApiModelProperty(value = "盘点编号",hidden=true)
	private String code;
	//仓库id
    @ApiModelProperty(value = "仓库id")
	private Long warehouse;
	//盘点执行状态 
    @ApiModelProperty(value = "盘点执行状态 ",hidden=true)
	private Long checkStatus;
	//盘点负责人
    @ApiModelProperty(value = "盘点负责人")
	private Long checkers;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden=true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden=true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden=true)
	private Integer delFlag;


}
