package com.ev.mes.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 投料单（变更列表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-26 10:22:50
 */
@Data
@ApiModel(value = "投料单（变更列表）")
public class ProductionFeedingAlterationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//投料单主表ID
    @ApiModelProperty(value = "投料单主表ID")
	private Long feedingId;
	//投料单类型
    @ApiModelProperty(value = "投料单类型")
	private Long type;
	//投料单号
    @ApiModelProperty(value = "投料单号")
	private String code;
	//版本号
    @ApiModelProperty(value = "版本号")
	private String version;
	//变更内容
    @ApiModelProperty(value = "变更内容")
	private String alterationContent;
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
