package com.ev.scm.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 采购费用主表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-14 14:21:01
 */
@Data
@ApiModel(value = "采购费用主表")
public class PurchaseExpenseItemDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//费用代码
	@ApiModelProperty(value = "费用代码")
	private String itemCode;
	//采购费用主键
	@ApiModelProperty(value = "采购费用主键")
	private Long purchaseExpenseId;
	//费用主键
	@ApiModelProperty(value = "费用主键")
	private Long expenseId;
	//费用名称
	@ApiModelProperty(value = "费用名称")
	private String name;
	//费用类型
	@ApiModelProperty(value = "费用类型")
	private Long itemType;
	//单位
	@ApiModelProperty(value = "单位")
	private Long unit;
	//数量
	@ApiModelProperty(value = "数量")
	private BigDecimal count;
	//含税单价
	@ApiModelProperty(value = "含税单价")
	private BigDecimal taxUnitPrice;
	//不含税单价
	@ApiModelProperty(value = "不含税单价")
	private BigDecimal unitPrice;
	//税率
	@ApiModelProperty(value = "税率")
	private BigDecimal taxRate;
	//含税金额（价格合计）
	@ApiModelProperty(value = "含税金额（价格合计）")
	private BigDecimal taxAmount;
	//不含税金额
	@ApiModelProperty(value = "不含税金额")
	private BigDecimal amount;
	//税额
	@ApiModelProperty(value = "税额")
	private BigDecimal taxes;
	//发票号码
	@ApiModelProperty(value = "发票号码")
	private String invoiceNum;
	//开票日期
	@ApiModelProperty(value = "开票日期")
	private Date invoiceDate;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//制单时间
	@ApiModelProperty(value = "制单时间")
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
