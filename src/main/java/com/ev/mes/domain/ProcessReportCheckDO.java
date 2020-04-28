package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序检验（主表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:15:11
 */
@Data
@ApiModel(value = "工序检验（主表）")
public class ProcessReportCheckDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//工序检验号
    @ApiModelProperty(value = "工序检验号")
	private Long id;
	//
    @ApiModelProperty()
	private String code;
	//工序报工单主键
    @ApiModelProperty(value = "工序报工单主键")
	private Long processReportId;
	//物料id
    @ApiModelProperty(value = "物料id")
	private Long materiaId;
	//供应商
    @ApiModelProperty(value = "供应商")
	private Long supplierId;
	//检验方案id
    @ApiModelProperty(value = "检验方案id")
	private String checkPlanId;
	//合格数量
    @ApiModelProperty(value = "合格数量")
	private BigDecimal conformityCount;
	//返工数量
    @ApiModelProperty(value = "返工数量")
	private BigDecimal reworkCount;
	//报废数量
    @ApiModelProperty(value = "报废数量")
	private BigDecimal scrapCount;
	//报检数量
    @ApiModelProperty(value = "报检数量")
	private BigDecimal checkCount;
	//状态
	@ApiModelProperty(value = "状态")
	private Long status;
	//创建人 
    @ApiModelProperty(value = "创建人 ")
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
