package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料检验
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 12:54:56
 */
@Data
@ApiModel(value = "物料检验")
public class MaterialInspectionDO implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	@ApiModelProperty(value = "")
	private Long id;
	// 检验单类型
	@ApiModelProperty(value = "检验单类型(216、来料检验、217产品检验、218、发货检验)", required = true)
	private Integer inspectionType;
	// 来源单号ID
	@ApiModelProperty(value = "来源单号ID")
	private Long sourceId;
	// 源单类型
	@ApiModelProperty(value = "源单类型")
	private Integer sourceType;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNo;
	// 供应商ID
	@ApiModelProperty(value = "供应商ID(来料检验选择该字段)")
	private Long supplierId;
	// 客户ID
	@ApiModelProperty(value = "客户ID(发货检验选择该字段)")
	private Long clientId;
	// 部门
	@ApiModelProperty(value = "部门", required = true)
	private Long deptId;
	// 检验员
	@ApiModelProperty(value = "检验员", required = true)
	private Long inspector;
	// 物料ID
	@ApiModelProperty(value = "物料ID")
	private Integer materielId;
	// 批号
	@ApiModelProperty(value = "批号")
	private String batchNo;
	// 检验方案
	@ApiModelProperty(value = "检验方案")
	private Long inspectionScheme;
	// 送检数量
	@ApiModelProperty(value = "送检数量", required = true)
	private BigDecimal sendCount;
	// 检验数量
	@ApiModelProperty(value = "检验数量")
	private BigDecimal inspectionCount;
	// 合格数量
	@ApiModelProperty(value = "合格数量")
	private BigDecimal qualifiedCount;
	// 不合格数量
	@ApiModelProperty(value = "不合格数量")
	private BigDecimal unqualifiedCount;
	// 工单号
	@ApiModelProperty(value = "工单号", hidden = true)
	private String inspectionNo;
	// 工废数量
	@ApiModelProperty(value = "工废数量(产品检验选择该字段)")
	private BigDecimal industrialWasteCount;
	// 料废数量
	@ApiModelProperty(value = "料废数量(产品检验选择该字段)")
	private BigDecimal scrapWasteCount;
	// 审核人员
	@ApiModelProperty(value = "审核人员")
	private Long auditor;
	// 单据状态
	@ApiModelProperty(value = "单据状态", hidden = true)
	private Integer status;
	// 是否已打印二维码
	@ApiModelProperty(value = "是否已打印二维码", hidden = true)
	private Integer isPrintedQrcode = 0;
	// 单位二维码数量
	@ApiModelProperty(value = "单位二维码数量", hidden = true)
	private BigDecimal unitCodeCount;
	// 备注
	@ApiModelProperty(value = "备注", hidden = true)
	private String remarks;
	// 创建人
	@ApiModelProperty(value = "创建人", hidden = true)
	private Long createBy;
	// 创建时间
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Date createTime;
	// 修改人
	@ApiModelProperty(value = "修改人", hidden = true)
	private Long updateBy;
	// 修改时间
	@ApiModelProperty(value = "修改时间", hidden = true)
	private Date updateTime;
	// 删除状态
	@ApiModelProperty(value = "删除状态", hidden = true)
	private Integer delFlag;
}
