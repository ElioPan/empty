package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 采购合同价格分析
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-31 12:54:56
 */
@Data
public class PurchaseContractVO implements Serializable {
	private static final long serialVersionUID = 1L;
	// 合同子表ID
	private Long id;
	// 合同ID
	private String purchaseContractId;
	// 合同编号
	private String contractCode;
	// 合同日期
	private String contractDate;
	// 供应商ID
	private Long supplierId;
	// 供应商名称
	private String supplierName;
	// 物料ID
	private Long materielId;
	// 物料编号
	private String materielSerialNo;
	// 物料名
	private String materielName;
	// 规格型号
	private String specification;
	// 单位
	private String unitUomName;
	// 数量
	private Double count;
	// 含税单价
	private Double taxUnitPrice;
	// 含税总额
	private Double taxAmount;

	// 最高价格
	private Double maxUnitPrice;
	// 最低价格
	private Double minUnitPrice;
	// 平均价格
	private Double avgUnitPrice;
	// 最新价格
	private Double latestUnitPrice;
}
