package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 调拨单主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
@Data
@ApiModel(value = "调拨单主表")
public class AllotDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//调拨单编号
    @ApiModelProperty(value = "调拨单编号")
	private String allotCode;
	//调拨时间
    @ApiModelProperty(value = "调拨时间")
	private Date allotTime;
	//调拨人
    @ApiModelProperty(value = "调拨人")
	private Long allotUser;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
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
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

}
