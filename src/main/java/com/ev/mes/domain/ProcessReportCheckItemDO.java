package com.ev.mes.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 工序检验明细（子表）
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-28 10:15:25
 */
@Data
@ApiModel(value = "工序检验明细（子表）")
public class ProcessReportCheckItemDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//工序检验单id
    @ApiModelProperty(value = "工序检验单id")
	private Long reportCheckId;
	//不良原因
    @ApiModelProperty(value = "不良原因")
	private String rejectsReason;
	//不良数量
    @ApiModelProperty(value = "不良数量")
	private BigDecimal rejectsCount;
	//处理方式
    @ApiModelProperty(value = "处理方式")
	private Long dispose;
	//检验项目id
    @ApiModelProperty(value = "检验项目id")
	private Long projectId;

	@ApiModelProperty(value = "目标检验值")
	private String normn;

	//实际检验值
    @ApiModelProperty(value = "实际检验值")
	private String actualValue;
	//创建人 
    @ApiModelProperty(value = "创建人 ")
	private Long createBy;
	//派单时间 
    @ApiModelProperty(value = "派单时间 ")
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

	@ApiModelProperty(value = "单位")
	private String unit;
	@ApiModelProperty(value = "是否必检")
	private Integer whetherCheck;
	@ApiModelProperty(value = "检验结果")
	private Long result;

}
