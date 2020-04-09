package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;


/**
 * 产品入库明细表子表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 14:36:27
 */
@Data
public class ContractBillItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	//票据号
	private String code;
	//开票数量
	private Double count;
	//开票金额
	private Double amount;
	// 源单ID
	private Long sourceId;
	// 源单单号
	private String sourceCode;

}
