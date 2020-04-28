package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料检验详情
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-20 09:35:57
 */
@Data
@ApiModel(value = "物料检验详情")
public class MaterialInspectionDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	// 主表ID
	@ApiModelProperty(value = "主表ID")
	private Long headId;
	// 检验项目ID
	@ApiModelProperty(value = "检验项目ID")
	private Long projectId;
	// 检验项目代码
	@ApiModelProperty(value = "检验项目代码")
	private String projectCode;
	// 检验项目名称
	@ApiModelProperty(value = "检验项目名称")
	private String projectName;
	// 检验结果（0异常/1正常）
	@ApiModelProperty(value = "检验结果（0异常/1正常）")
	private Integer checkResult;
	// 检验方法
	@ApiModelProperty(value = "检验方法")
	private String projectMethod;
	// 单位
	@ApiModelProperty(value = "单位")
	private Long unit;
	// 目标值
	@ApiModelProperty(value = "目标值")
	private BigDecimal targetValue;
	// 检验值
	@ApiModelProperty(value = "检验值")
	private BigDecimal checkValue;
	// 是否必检项(0非必检/1必检）
	@ApiModelProperty(value = "是否必检项(0非必检/1必检）")
	private Integer isMustCheck;
	// 不良数量
	@ApiModelProperty(value = "不良数量")
	private BigDecimal unqualifiedCount;
	// 不良原因ID
	@ApiModelProperty(value = "不良原因ID")
	private Long reasonId;
	// 备注
	@ApiModelProperty(value = "备注")
	private String remarks;
	// 创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	// 创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	// 修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	// 修改时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	// 删除状态
	@ApiModelProperty(value = "删除状态")
	private Integer delFlag;


}
