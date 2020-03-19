package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 物料检验
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 12:54:56
 */
@Data
public class MaterialInspectionVO implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Long id;
	// 检验单类型(216、来料检验、217产品检验、218、发货检验)
	private Integer inspectionType;
	// 来源单号ID
	private Long sourceId;
	// 源单类型
	private Integer sourceType;
	//来源单号
	private String sourceNo;
	// 供应商ID
	private Long supplierId;
	// 客户ID
	private Long clientId;
	// 部门
	private Long deptId;
	// 检验员
	private Long inspector;
	// 物料ID
	private Integer materielId;
	// 批号
	private String batchNo;
	// 检验方案
	private Long inspectionScheme;
	// 送检数量
	private BigDecimal sendCount;
	// 检验数量
	private BigDecimal inspectionCount;
	// 合格数量
	private BigDecimal qualifiedCount;
	// 不合格数量
	private BigDecimal unqualifiedCount;
	// 工单号
	private String inspectionNo;
	// 工废数量
	private BigDecimal industrialWasteCount;
	// 料废数量
	private BigDecimal scrapWasteCount;
	// 审核人员
	private Long auditor;
	// 单据状态
	private Integer status;
	// 是否已打印二维码
	private Integer isPrintedQrcode = 0;
	// 单位二维码数量
	private BigDecimal unitCodeCount;
	// 备注
	private String remarks;
	// 创建人
	private Long createBy;
	// 创建时间
	private Date createTime;
	// 修改人
	private Long updateBy;
	// 修改时间
	private Date updateTime;
	// 删除状态
	private Integer delFlag;
}
