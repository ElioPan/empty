package com.ev.hr.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 薪资项目
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 13:10:37
 */
@Data
@ApiModel(value = "薪资项目")
public class SalaryProjectDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
    @ApiModelProperty(value = "ID")
	private Long id;
	//薪资项目（名称）
    @ApiModelProperty(value = "薪资项目（名称）")
	private String salaryItemName;
	//项目属性(字典)（固定，变动，其他）
    @ApiModelProperty(value = "项目属性(字典)（固定，变动，其他）")
	private Long attribute;
	//数据属性(0:加项；1：减项；2:代扣；3：其他)(字典)
    @ApiModelProperty(value = "数据属性(0:加项；1：减项；2:代扣；3：其他)(字典)")
	private Long dataAttribute;
	//薪资核算是否显示
    @ApiModelProperty(value = "薪资核算是否显示")
	private Integer isShowSalaryAccounting;
	//薪资核算项目排序
    @ApiModelProperty(value = "薪资核算项目排序")
	private Integer sortOfSalaryAccountingItems;
	//说明
    @ApiModelProperty(value = "说明")
	private String instruction;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间", example = "2000-01-01 00:00:01")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
	@ApiModelProperty(value = "修改时间", example = "2000-01-01 00:00:01")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

}
