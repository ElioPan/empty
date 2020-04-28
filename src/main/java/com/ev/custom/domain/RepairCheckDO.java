package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 维修验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-02 16:51:36
 */
@Data
@ApiModel(value = "维修验收表")
public class RepairCheckDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(hidden = true)
	private Long id;
	//收验结果
    @ApiModelProperty(value = "收验结果,通过115 不通过116",required = true)
	private Long result;
	//内容
    @ApiModelProperty(value = "内容",required = true)
	private String content;
	//验收人
    @ApiModelProperty(value = "验收人",hidden = true)
	private Long createBy;
	//验收时间
    @ApiModelProperty(value = "验收时间",hidden = true)
	private Date createTime;
	//事件ID
    @ApiModelProperty(value = "事件ID",hidden = true)
	private Long eventId;
    //记录ID
    @ApiModelProperty(value = "维修记录ID",hidden = true)
	private Long recordId;
	//效率评分
    @ApiModelProperty(value = "效率评分",required = true)
	private Integer xlGrade;
	//质量评分
    @ApiModelProperty(value = "质量评分",required = true)
	private Integer qaGrade;
	//态度评分
    @ApiModelProperty(value = "态度评分",required = true)
	private Integer tdGrade;
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
