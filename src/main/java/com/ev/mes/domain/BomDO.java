package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * BOM主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-19 08:45:05
 */
@Data
@ApiModel(value = "BOM主表")
public class BomDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty()
	private Long id;
	// BOM编号
	@ApiModelProperty(value = "BOM编号", required = true)
	private String serialno;
	// BOM名称
	@ApiModelProperty(value = "BOM名称", required = true)
	private String name;
	// BOM类别
	@ApiModelProperty(value = "BOM类别（211正常生产/212返工返修）")
	private Long type;
	// BOM版本号
	@ApiModelProperty(value = "BOM版本号")
	private String version;
	// 产品ID
	@ApiModelProperty(value = "产品ID", required = true)
	private Long materielId;
	// 数量
	@ApiModelProperty(value = "数量")
	private BigDecimal count;
	// 审核人员
	@ApiModelProperty(value = "审核人员")
	private Long auditor;
	// 图号
	@ApiModelProperty(value = "图号")
	private String imageNo;
	// 备注
	@ApiModelProperty(value = "备注")
	private String remarks;
	// 审核标志：（待审核/已审核）
	@ApiModelProperty(value = "审核标志：（待审核/已审核）", hidden = true)
	private Long auditSign;
	// 启用状态
	@ApiModelProperty(value = "启用状态(是否启用 1启用，0禁用)")
	private Integer useStatus;
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
