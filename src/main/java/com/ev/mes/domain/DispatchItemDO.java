package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工单明细
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-12-03 09:41:21
 */
@Data
@ApiModel(value = "工单明细")
public class DispatchItemDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//工单代码
	@ApiModelProperty(value = "工单代码")
	private String code;
	//工序计划子表主键
	@ApiModelProperty(value = "工序计划子表主键")
	private Long foriegnId;
	//操作工
	@ApiModelProperty(value = "操作工")
	private Long operator;
	//生产设备id
	@ApiModelProperty(value = "生产设备id")
	private Long deviceId;
	//委外供应商
	@ApiModelProperty(value = "委外供应商")
	private Long supplierId;
	//开始时间（派工单计划时间）
	@ApiModelProperty(value = "开始时间（派工单计划时间）")
	private Date startTime;
	//结束时间（派工单计划时间）
	@ApiModelProperty(value = "结束时间（派工单计划时间）")
	private Date endTime;
	//实际结束时间（工单结案时间）
	@ApiModelProperty(value = "实际结束时间（工单结案时间）")
	private Date actualEndTime;
	//实际开工时间(第一次开工时间）
	@ApiModelProperty(value = "实际开工时间(第一次开工时间）")
	private Date actualStartTime;
	//计划生产数量（派给的数量）
	@ApiModelProperty(value = "计划生产数量（派给的数量）")
	private BigDecimal planCount;
	//状态
	@ApiModelProperty(value = "状态")
	private Long status;
	//完工数量
	@ApiModelProperty(value = "完工数量")
	private BigDecimal completionCount;
	//创建人(派单人)
	@ApiModelProperty(value = "创建人(派单人)")
	private Long createBy;
	//派单时间
	@ApiModelProperty(value = "派单时间")
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
