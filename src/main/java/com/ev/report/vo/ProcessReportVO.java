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
	//工单明细主键
	private Long dispatchItemId;
	//供应商
	private Long supplierId;
	//完工数量
	private BigDecimal completionCount;
	//合格数量
	private BigDecimal conformityCount;
	//返工数量
	private BigDecimal reworkCount;
	//报废数量
	private BigDecimal scrapCount;
	//状态
	private Integer status;
	//创建人 （汇报人)
	private Long createBy;
	//派单时间 (汇报时间)
	private Date createTime;
	//修改人
	private Long updateBy;
	//修改时间
	private Date updateTime;
	//删除状态
	private Integer delFlag;
	//工序计划单明细ID
	private Long processPlanItemId;

}
