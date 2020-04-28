package com.ev.mes.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 工序计划单
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-22 15:44:50
 */
@Data
@ApiModel(value = "工序计划单")
public class WorkingProcedurePlanDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//生产计划ID
    @ApiModelProperty(value = "生产计划ID")
	private Long productionPlanId;
	//源单类型
    @ApiModelProperty(value = "源单类型")
	private Long sourceType;
	//来源单号ID
    @ApiModelProperty(value = "来源单号ID")
	private Long sourceId;
   //来源单号
    @ApiModelProperty(value = "来源单号")
	private String sourceNo;
	//工单号
    @ApiModelProperty(value = "工单号",hidden = true)
	private String workOrderNo;
	//产品ID
    @ApiModelProperty(value = "产品ID")
	private Long materielId;
	//生产部门
    @ApiModelProperty(value = "生产部门")
	private Long proDept;
	//生产类型
    @ApiModelProperty(value = "生产类型")
	private Long type;
	//是否限额(0不限/1限)
    @ApiModelProperty(value = "是否限额(0不限/1限)")
	private Integer isQuota;
	//计划生产数量
    @ApiModelProperty(value = "计划生产数量")
	private BigDecimal count;
	//批号
    @ApiModelProperty(value = "批号")
	private String batchNo;
	//计划开始时间
    @ApiModelProperty(value = "计划开始时间")
	private Date planStartTime;
	//计划完工时间
    @ApiModelProperty(value = "计划完工时间")
	private Date planEndTime;
	//BOMID
    @ApiModelProperty(value = "BOMID")
	private Long bomId;
	//工艺路线ID
    @ApiModelProperty(value = "工艺路线ID")
	private Long tecRouteId;
	//完工数量上限
    @ApiModelProperty(value = "完工数量上限")
	private BigDecimal completionMax;
	//完工下限
    @ApiModelProperty(value = "完工下限")
	private BigDecimal completionMin;
	//是否检验
    @ApiModelProperty(value = "是否检验")
	private Integer isCheck;
	//检验方案
    @ApiModelProperty(value = "检验方案")
	private Long inspectionScheme;
	//客户ID
    @ApiModelProperty(value = "客户ID")
	private Long clientId;
    // 客户ID
    @ApiModelProperty(value = "客户名称")
    private String clientName;
	//交货期
    @ApiModelProperty(value = "交货期")
	private Date deliveryDate;
	//客户商品名称
    @ApiModelProperty(value = "客户商品名称")
	private String clientProductName;
	//客户料号
    @ApiModelProperty(value = "客户料号")
	private String clientProductNo;
	//下达时间
    @ApiModelProperty(value = "下达时间",hidden = true)
	private Date giveTime;
	//结案时间（实际完成时间）
    @ApiModelProperty(value = "结案时间（实际完成时间）",hidden = true)
	private Date actualFinishTime;
	//单据状态
    @ApiModelProperty(value = "单据状态",hidden = true)
	private Long status;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//原工序计划单号
    @ApiModelProperty(value = "原工序计划单号")
	private String originalPlanNo;
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
