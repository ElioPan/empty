package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 库存表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-22 09:10:20
 */
@Data
@ApiModel(value = "库存表")
public class StockDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//入库时间
	@ApiModelProperty(value = "入库时间")
	private Date enteringTime;
	//商品/产品id
	@ApiModelProperty(value = "商品/产品id")
	private Long materielId;
	//产品库存编号
	@ApiModelProperty(value = "产品库存编号")
	private String code;
	//生产批次
	@ApiModelProperty(value = "生产批次")
	private String batch;
	//可使用数量
	@ApiModelProperty(value = "可使用数量")
	private BigDecimal availableCount;
	//库存数量
	@ApiModelProperty(value = "库存数量")
	private BigDecimal count;
	//单价
	@ApiModelProperty(value = "单价")
	private BigDecimal unitPrice;
	//金额
	@ApiModelProperty(value = "金额")
	private BigDecimal amount;
	//来源公司
	@ApiModelProperty(value = "来源公司")
	private Long sourceCompany;
	//仓库
	@ApiModelProperty(value = "仓库")
	private Long warehouse;
	//库位
	@ApiModelProperty(value = "库位")
	private Long warehLocation;
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
