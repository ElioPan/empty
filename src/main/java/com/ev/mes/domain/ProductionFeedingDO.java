package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 生产投料单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 10:08:55
 */
@Data
@ApiModel(value = "生产投料单")
public class ProductionFeedingDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//生产计划ID
    @ApiModelProperty(value = "生产计划ID")
	private Long productionPlanId;
    //委外合同明细ID
    @ApiModelProperty(value = "委外合同明细ID")
    private Long outsourceContractItemId;
	//生产类型
    @ApiModelProperty(value = "生产类型")
	private Long type;
	//生产投料工单号
    @ApiModelProperty(value = "生产投料工单号")
	private String feedingNo;
	//状态
    @ApiModelProperty(value = "状态",hidden = true)
	private Long status;
	//产品ID
    @ApiModelProperty(value = "产品ID")
	private Long materielId;
	//供应商ID
	@ApiModelProperty(value = "供应商ID")
	private Long supplierId;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long proDept;
	//计划生产数量
    @ApiModelProperty(value = "计划生产数量")
	private BigDecimal planCount;
	//是否限额(0不限/1限)
    @ApiModelProperty(value = "是否限额(0不限/1限)")
	private Integer isQuota;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;


}
