package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 工艺路线
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 10:51:00
 */
@Data
@ApiModel(value = "工艺路线")
public class CraftDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工艺代码
    @ApiModelProperty(value = "工艺代码")
	private String code;
	//工艺名称
    @ApiModelProperty(value = "工艺名称")
	private String name;
	//版本号
    @ApiModelProperty(value = "版本号")
	private String version;
	//备注
    @ApiModelProperty(value = "备注")
	private String remark;
	//审核人id
    @ApiModelProperty(value = "审核人id")
	private Long auditId;
	//启用禁用
    @ApiModelProperty(value = "启用禁用")
	private Integer useStatus;
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
