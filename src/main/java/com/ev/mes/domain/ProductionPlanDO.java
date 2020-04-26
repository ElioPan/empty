package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生产计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-21 08:26:39
 */
@Data
@ApiModel(value = "生产计划单")
public class ProductionPlanDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty()
	private Long id;
	// 源单类型
	@ApiModelProperty(value = "源单类型")
	private Integer sourceType;
	// 来源单ID
	@ApiModelProperty(value = "来源单ID")
	private Long sourceId;
	//来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNo;
	// 计划单号
	@ApiModelProperty(value = "计划单号", hidden = true)
	private String planNo;
	// 产品ID
	@ApiModelProperty(value = "产品ID", required = true)
	private Integer materielId;
	// 生产部门
	@ApiModelProperty(value = "生产部门", required = true)
	private Long proDept;
	// 生产类型
	@ApiModelProperty(value = "生产类型", required = true)
	private Integer type;
	// 是否限额(0不限/1限)
	@ApiModelProperty(value = "是否限额(0不限/1限)")
	private Integer isQuota;
	// 计划生产数量
	@ApiModelProperty(value = "计划生产数量", required = true)
	private BigDecimal planCount;
	// 批号
	@ApiModelProperty(value = "批号")
	private String batchNo;
	// 计划开始时间
	@ApiModelProperty(value = "计划开始时间", required = true)
	private Date planStartTime;
	// 计划完工时间
	@ApiModelProperty(value = "计划完工时间", required = true)
	private Date planEndTime;
	// BOMID
	@ApiModelProperty(value = "BOMID", required = true)
	private Long bomId;
	// 工艺路线ID
	@ApiModelProperty(value = "工艺路线ID")
	private Long tecRouteId;
	// 完工上限
	@ApiModelProperty(value = "完工上限")
	private BigDecimal completionMax;
	// 完工下限
	@ApiModelProperty(value = "完工下限")
	private BigDecimal completionMin;
	// 是否检验(0不检验/1检验)
	@ApiModelProperty(value = "是否检验(0不检验/1检验)")
	private Integer isCheck;
	// 检验方案
	@ApiModelProperty(value = "检验方案")
	private Long inspectionScheme;
	// 客户ID
	@ApiModelProperty(value = "客户ID")
	private Long clientId;
    // 客户ID
    @ApiModelProperty(value = "客户名称")
    private String clientName;
	// 交货期
	@ApiModelProperty(value = "交货期")
	private Date deliveryDate;
	// 客户商品名称
	@ApiModelProperty(value = "客户商品名称")
	private String clientProductName;
	// 客户料号
	@ApiModelProperty(value = "客户料号")
	private String clientProductNo;
	// 下达时间
	@ApiModelProperty(value = "下达时间", hidden = true)
	private Date giveTime;
	// 结案时间（实际完成时间）
	@ApiModelProperty(value = "结案时间（实际完成时间）", hidden = true)
	private Date actualFinishTime;
	// 单据状态
	@ApiModelProperty(value = "单据状态", hidden = true)
	private Integer status;
	// 审核人员
	@ApiModelProperty(value = "审核人员", hidden = true)
	private Long auditor;
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
	// 是否用料采集
	@ApiModelProperty(value = "是否用料采集")
	private Integer isCollect;

}
