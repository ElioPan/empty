package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序报工单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:14:53
 */
@Data
@ApiModel(value = "工序报工单")
public class ProcessReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//报工单号
    @ApiModelProperty(value = "报工单号")
	private String code;
	//工单明细主键
    @ApiModelProperty(value = "工单明细主键")
	private Long dispatchItemId;
	//供应商
    @ApiModelProperty(value = "供应商")
	private Long supplierId;
	//完工数量
    @ApiModelProperty(value = "完工数量")
	private BigDecimal completionCount;
	//合格数量
    @ApiModelProperty(value = "合格数量")
	private BigDecimal conformityCount;
	//返工数量
    @ApiModelProperty(value = "返工数量")
	private BigDecimal reworkCount;
	//报废数量
    @ApiModelProperty(value = "报废数量")
	private BigDecimal scrapCount;
	//状态
    @ApiModelProperty(value = "状态")
	private Long status;
	//创建人 （汇报人)
    @ApiModelProperty(value = "创建人 （汇报人)")
	private Long createBy;
	//派单时间 (汇报时间)
    @ApiModelProperty(value = "派单时间 (汇报时间)")
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
	@ApiModelProperty(value = "备注")
	private String  remark;


}
