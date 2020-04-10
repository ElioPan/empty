package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 产品入库明细表子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 14:36:27
 */
@Data
public class InOutStockItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	// 期间
	private String period;
	// 标记（入库0，出库1）
	private Integer type;
	// 出入库日期
	private Date inOutTime;
	// 物料批次
	private String batch;
	// 单据编号
	private String code;
	// 单据类型
	private String storageTypeName;
	// 数量
	private Double count;
	// 单价
	private BigDecimal unitPrice;
	// 金额
	private Double amount;
	// 收入数量
	private Double inCount;
	// 收入单价
	private BigDecimal inUnitPrice;
	// 收入金额
	private Double inAmount;
	// 发出数量
	private Double outCount;
	// 发出单价
	private BigDecimal outUnitPrice;
	// 发出金额
	private Double outAmount;
	// 结存数量b
	private Double balanceCount;
	// 结存单价
	private BigDecimal balanceUnitPrice;
	// 结存金额
	private Double balanceAmount;

	// 排序号 0为期初 1为中间详情 2为垫底
	private int sortNo;
	// 颜色区分
	private String sign;



}
