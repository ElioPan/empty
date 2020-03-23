package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 工序报工单
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 14:36:27
 */
@Data
public class ProcessReportVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//报工单号
	private String code;
	//派单时间 (汇报时间)
	private Date createTime;
	//工序名
	private String processName;
	//工单明细主键
	private Long dispatchItemId;
	//创建人 （汇报人)
	private Long createBy;
	//操作工
	private Long operator;
	//操作工名
	private String operatorName;
	// 设备
	private String deviceName;
	// 产品ID
	private Long materielId;
	// 产品代码
	private String serialNo;
	// 产品名称
	private String materielName;
	// 规格型号
	private String specification;
	// 单位
	private String unitName;
	// 批号
	private String batchNo;
	//完工数量
	private BigDecimal completionCount;
	//合格数量
	private BigDecimal conformityCount;
	//返工数量
	private BigDecimal reworkCount;
	//报废数量
	private BigDecimal scrapCount;
	//工序计划单明细ID
	private Long processPlanItemId;

}
