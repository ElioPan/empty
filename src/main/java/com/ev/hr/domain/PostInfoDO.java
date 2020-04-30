package com.ev.hr.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 岗位信息
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-04-30 14:47:58
 */
@Data
@ApiModel(value = "岗位信息")
public class PostInfoDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
    @ApiModelProperty(value = "ID")
	private Long id;
	//公司id
    @ApiModelProperty(value = "公司id")
	private Long companyId;
	//部门id
    @ApiModelProperty(value = "部门id")
	private String deptId;
	//编号
    @ApiModelProperty(value = "编号")
	private String postCode;
	//岗位名称
    @ApiModelProperty(value = "岗位名称")
	private String postName;
	//岗位层级
    @ApiModelProperty(value = "岗位层级")
	private String postLevel;
	//岗位性质
    @ApiModelProperty(value = "岗位性质")
	private String postNature;
	//岗位序列
    @ApiModelProperty(value = "岗位序列")
	private String postIndex;
	//岗位系数
    @ApiModelProperty(value = "岗位系数")
	private String postCoefficient;
	//起薪等级
    @ApiModelProperty(value = "起薪等级")
	private String salaryLevel;
	//是否编制(1是0否)
    @ApiModelProperty(value = "是否编制(1是0否)")
	private Integer isAuthorized;
	//编制总人数
    @ApiModelProperty(value = "编制总人数")
	private Integer authorizedTotal;
	//编制人数
    @ApiModelProperty(value = "编制人数")
	private Integer authorizedCounts;
	//状态
    @ApiModelProperty(value = "状态")
	private Long postState;
	//任职要求
    @ApiModelProperty(value = "任职要求")
	private String jobRequirements;
	//职责
    @ApiModelProperty(value = "职责")
	private String responsibilities;
	//操作标志(0-有效，1-无效)
    @ApiModelProperty(value = "操作标志(0-有效，1-无效)")
	private String operationFlag;
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
