package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 检验方案
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:48:58
 */
@Data
@ApiModel(value = "检验方案")
public class CheckPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//检验方案编号
    @ApiModelProperty(value = "检验方案编号")
	private String code;
	//检验方案名称
    @ApiModelProperty(value = "检验方案名称")
	private String name;
	//
    @ApiModelProperty(value = "")
	private Long auditId;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
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

}
