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
public class ContractPayItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	// 已收金额
	private Double receivedAmount;
	// 未收金额
	private Double unReceivedAmount;
	// 源单ID
	private Long sourceId;

}
