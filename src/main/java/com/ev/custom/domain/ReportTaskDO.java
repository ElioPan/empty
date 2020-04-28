package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 关联单据与关联任务联系表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-21 13:01:44
 */
@Data
@ApiModel(value = "关联单据与关联任务联系表")
public class ReportTaskDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//任务ID
    @ApiModelProperty(value = "任务ID")
	private Long taskId;
	//关联单据单号
    @ApiModelProperty(value = "关联单据单号")
	private String linkOrderNo;
	//关联单据类型
    @ApiModelProperty(value = "关联单据类型")
	private Long linkOrderType;
	//关联单据阶段类型
    @ApiModelProperty(value = "关联单据阶段类型")
	private Long linkStageType;
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


	public ReportTaskDO(Long taskId, String linkOrderNo, Long linkOrderType, Long linkStageType) {
		super();
		this.taskId = taskId;
		this.linkOrderNo = linkOrderNo;
		this.linkOrderType = linkOrderType;
		this.linkStageType = linkStageType;
	}

	
}
