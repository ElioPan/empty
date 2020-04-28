package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 报销申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:37
 */
@Data
@ApiModel(value = "报销申请")
public class ReimApplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//收款账号
    @ApiModelProperty(value = "收款账号")
	private String accountReceivable;
	//备注
    @ApiModelProperty(value = "备注")
	private String description;
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
	//状态
    @ApiModelProperty(value = "状态")
	private Long status;
	//流程实例ID
    @ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;


}
