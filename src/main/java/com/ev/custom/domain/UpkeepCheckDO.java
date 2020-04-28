package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 养保验收表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-10-28 17:33:31
 */
@Data
@ApiModel(value = "养保验收表")
public class UpkeepCheckDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty()
	private Long id;
	//验收人
	@ApiModelProperty(value = "验收人")
	private Long userId;
	//收验结果
	@ApiModelProperty(value = "收验结果")
	private Long result;
	//内容
	@ApiModelProperty(value = "内容")
	private String content;
	//验收时间
	@ApiModelProperty(value = "验收时间")
	private Date checkTime;
	//工单ID
	@ApiModelProperty(value = "工单ID")
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
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//更改时间
	@ApiModelProperty(value = "更改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;
	//结果标记（暂存/提交）
	@ApiModelProperty(value = "结果标记（暂存/提交）")
	private Long resultRemark;


}
