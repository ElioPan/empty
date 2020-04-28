package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 巡检明细表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-29 14:53:37
 */
@Data
@ApiModel(value = "巡检明细表")
public class PatrolDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "",hidden = true)
	private Long id;
	//关联设备
    @ApiModelProperty(value = "关联设备")
	private Long deviceId;
	//关联项目
    @ApiModelProperty(value = "关联项目")
	private Long projectId;
	//结果
    @ApiModelProperty(value = "结果(1为正常,2为异常)")
	private Integer result;
	//任务状态
    @ApiModelProperty(value = "任务状态")
	private Long status;
    //停机时间
    @ApiModelProperty(value = "停机时间")
	private Date offTime;
	//内容
    @ApiModelProperty(value = "内容")
	private String content;
	//巡检记录ID
    @ApiModelProperty(value = "巡检记录ID")
	private Long recordId;
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
