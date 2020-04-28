package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 工序检验（主表）
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 14:36:27
 */
@Data
public class ProcessReportCheckVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//工序检验号
	private Long id;
	//
	private String code;
	//工序报工单主键
	private Long processReportId;
	//物料id
	private Long materiaId;
	//供应商
	private Long supplierId;
	//检验方案id
	private String checkPlanId;
	//合格数量
	private BigDecimal conformityCount;
	//返工数量
	private BigDecimal reworkCount;
	//报废数量
	private BigDecimal scrapCount;
	//报检数量
	private BigDecimal checkCount;
	//状态
	private Long status;
	//创建人
	private Long createBy;
	//创建时间
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
