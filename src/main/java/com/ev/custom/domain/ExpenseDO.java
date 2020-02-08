package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 费用
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-01-13 12:53:38
 */
@Data
@ApiModel(value = "费用")
public class ExpenseDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//
    @ApiModelProperty(value = "")
	private String code;
	//费用类型
    @ApiModelProperty(value = "费用类型")
	private Integer type;
	//名称
    @ApiModelProperty(value = "名称")
	private String name;
	//审核人员
    @ApiModelProperty(value = "审核人员")
	private Long auditor;
	//审核标志：（待审核/已审核）
    @ApiModelProperty(value = "审核标志：（待审核/已审核）")
	private Integer auditSign;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
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

}
