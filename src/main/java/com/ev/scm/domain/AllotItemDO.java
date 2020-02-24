package com.ev.scm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 调拨单详情表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-17 10:55:19
 */
@Data
@ApiModel(value = "调拨单详情表")
public class AllotItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增主键
    @ApiModelProperty(value = "自增主键")
	private Long id;
	//主表id
    @ApiModelProperty(value = "主表id")
	private Long allotId;
	//库位id数组
    @ApiModelProperty(value = "库位id数组")
	private String stockId;
	//调入仓库
    @ApiModelProperty(value = "调入仓库")
	private Long inFacility;
	//调入库位
    @ApiModelProperty(value = "调入库位")
	private Long inLocation;
	//调拨数量
    @ApiModelProperty(value = "调拨数量")
	private BigDecimal count;
	//产品单价
    @ApiModelProperty(value = "产品单价")
	private BigDecimal price;
	//二维码ID qrcode_id
	@ApiModelProperty(value = "二维码ID")
	private Long qrcodeId;
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
