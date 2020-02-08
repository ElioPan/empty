package com.ev.mes.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 来源单与目标单中间表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-13 11:07:04
 */
@Data
@ApiModel(value = "来源单与目标单中间表")
public class SourceInvoicesDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//来源单ID
    @ApiModelProperty(value = "来源单ID")
	private Long sourceId;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNo;
	//来源单类型
    @ApiModelProperty(value = "来源单类型")
	private String sourceType;
	//目标单ID
    @ApiModelProperty(value = "目标单ID")
	private Long targetId;
	//目标单类型
    @ApiModelProperty(value = "目标单类型")
	private String targetType;
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
