package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 巡检验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-30 16:39:24
 */
@Data
@ApiModel(value = "巡检验收表")
public class PatrolCheckDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty()
	private Long id;
	//验收人
    @ApiModelProperty(value = "验收人",hidden = true)
	private Long userId;
	//收验结果
    @ApiModelProperty(value = "收验结果(115验收通过；116验收不通过)")
	private Long result;//115验收通过；116验收不通过
	//内容
    @ApiModelProperty(value = "内容")
	private String content;
	//验收时间
    @ApiModelProperty(value = "验收时间")
	private Date checkTime;
	//记录ID
    @ApiModelProperty(value = "记录ID",hidden = true)
	private Long recordId;
	//效率评分
    @ApiModelProperty(value = "效率评分")
	private Integer xlGrade;
	//质量评分
    @ApiModelProperty(value = "质量评分")
	private Integer qaGrade;
	//态度评分
    @ApiModelProperty(value = "态度评分")
	private Integer tdGrade;
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
