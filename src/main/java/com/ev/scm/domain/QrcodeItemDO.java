package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-20 14:28:20
 */
@Data
@ApiModel(value = "")
public class QrcodeItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//二维码主键ID
    @ApiModelProperty(value = "二维码主键ID")
	private Long qrcodeId;
	//单据类型
    @ApiModelProperty(value = "单据类型")
	private Long inOutType;
	//单据主键(主表主键)
    @ApiModelProperty(value = "单据主键(主表主键)")
	private Long inheadId;
	//单据主键（子表主键)
    @ApiModelProperty(value = "单据主键（子表主键)")
	private Long inbodyId;
	//
    @ApiModelProperty(value = "")
	private BigDecimal count;
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
