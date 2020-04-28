package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-22 14:08:43
 */
@Data
@ApiModel()
public class MeasurePointDO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	@ApiModelProperty(value = "主键")
	private Long id;
	// 测点ID
	@ApiModelProperty(value = "测点ID", required = true)
	private String serialNo;
	// 测点名称
	@ApiModelProperty(value = "测点名称", required = true)
	private String name;
	// 读写类型
	@ApiModelProperty(value = "读写类型", required = true)
	private Long rwType;
	// 子类型
	@ApiModelProperty(value = "子类型", hidden = true)
	private Long childType;
	// 单位
	@ApiModelProperty(value = "单位", hidden = true)
	private Long uom;
	// 使用类别
	@ApiModelProperty(value = "使用类别", required = true)
	private Long useType;
	// 是否手动输入
	@ApiModelProperty(value = "是否手动输入(0为否，1为是)", required = true)
	private Integer isManualInput;
	// 是否虚拟点
	@ApiModelProperty(value = "是否虚拟点", hidden = true)
	private Integer isVirtualPoint;
	// 表达式
	@ApiModelProperty(value = "表达式", hidden = true)
	private String expression;
	// 授权方式
	@ApiModelProperty(value = "授权方式", required = true)
	private Long impower;
	// 格式
	@ApiModelProperty(value = "格式", required = true)
	private Long format;
	// 排序号
	@ApiModelProperty(value = "排序号", hidden = true)
	private Long sortNo;
	//
	@ApiModelProperty(value = "设备ID", required = true)
	private Long deviceId;
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
