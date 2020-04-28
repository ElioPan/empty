package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 备品备件分类表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-21 00:28:51
 */
@Data
@ApiModel(value = "备品备件分类表")
public class SparePartDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//备件编号
    @ApiModelProperty(value = "备件编号")
	private String code;
	//备件名称
    @ApiModelProperty(value = "备件名称")
	private String name;
	//备件型号
    @ApiModelProperty(value = "备件型号")
	private String type;
	//备件类型
    @ApiModelProperty(value = "备件类型")
	private Long spartType;
	//单位
    @ApiModelProperty(value = "单位")
	private String unit;
	//参考价格
    @ApiModelProperty(value = "参考价格")
	private BigDecimal price;
	//仓库id
    @ApiModelProperty(value = "仓库id")
	private Long warehouse;
	//库位
    @ApiModelProperty(value = "库位")
	private Long locationId;
	//使用部门id
    @ApiModelProperty(value = "使用部门id")
	private Long deptId;
	//备注
    @ApiModelProperty(value = "备注")
	private String desc;
	//数量
    @ApiModelProperty(value = "数量")
	private Integer amount;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;


}
