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
 * @date 2019-08-16 13:15:40
 */
@Data
@ApiModel(value = "8D报告表")
public class QualityReportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//报告名称
    @ApiModelProperty(value = "报告名称",required = true)
	private String reportName;
	//编制人
    @ApiModelProperty(value = "编制人",required = true)
	private Long userId;
	//严重性
    @ApiModelProperty(value = "严重性(轻微的：82，致命的：83，严重的：84)")
	private Long ponderance;
	//8D报告编号
    @ApiModelProperty(value = "8D报告编号",hidden = true)
	private String reportNo;
	//报告类型
    @ApiModelProperty(value = "报告类型(生产：85，质量：86，营销：87，客户：88，内部：89，供应商：90)")
	private Long reportType;
	//报告状态
    @ApiModelProperty(value = "报告状态(编制中：91，已完成：92)",hidden = true)
	private Long status;
	//详细描述
    @ApiModelProperty(value = "详细描述")
	private String detailContent;
	//分析主要原因
    @ApiModelProperty(value = "分析主要原因",hidden = true)
	private String analyzeReason;
	//效果分析
    @ApiModelProperty(value = "效果分析",hidden = true)
	private String resultContent;
	//
    @ApiModelProperty(value = "验收人",hidden = true)
	private Long checkId;
	//验收状态
    @ApiModelProperty(value = "验收状态",hidden = true)
	private Long checkStatus;
	//编制时间
    @ApiModelProperty(value = "编制时间",hidden = true)
	private Date createTime;
	//创建者
    @ApiModelProperty(value = "创建者",hidden = true)
	private Long createBy;
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
