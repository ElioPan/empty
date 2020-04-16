package com.ev.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 公司信息表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-26 16:02:10
 */
@Data
@ApiModel(value = "公司信息表")
public class CompanyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
    @ApiModelProperty(value = "主键ID",hidden = true)
	private Long id;
	//公司名称
    @ApiModelProperty(value = "公司名称",required = true)
	private String name;
	//省
    @ApiModelProperty(value = "省")
	private Integer province;
	//市
    @ApiModelProperty(value = "市")
	private Integer city;
	//区
    @ApiModelProperty(value = "区")
	private Integer district;
	//详细地址
    @ApiModelProperty(value = "详细地址",required = true)
	private String address;
	//联系电话
    @ApiModelProperty(value = "联系电话")
	private String telephone;
	//联系人
    @ApiModelProperty(value = "联系人",required = true)
	private String linkman;
	//手机
    @ApiModelProperty(value = "手机")
	private String mobile;
	//Email
    @ApiModelProperty(value = "Email")
	private String email;
	//传真
    @ApiModelProperty(value = "传真")
	private String zipCode;
	//传真
    @ApiModelProperty(value = "传真")
	private String fax;
	//职务
    @ApiModelProperty(value = "职务")
	private String job;
	//QQ
    @ApiModelProperty(value = "QQ")
	private String qq;
	//公司网址
    @ApiModelProperty(value = "公司网址")
	private String url;
	//公司介绍
    @ApiModelProperty(value = "公司介绍")
	private String companyIntroduction;
	//产品编号
	@ApiModelProperty(value = "产品编号")
	private String code;
	//产品版本号
	@ApiModelProperty(value = "产品版本号")
	private String version;
    //服务码
    @ApiModelProperty(value = "服务码",hidden = true)
	private String serviceCode;
    //服务结束时间
    @ApiModelProperty(value = "服务结束时间",hidden = true)
	private String serviceEndTime;
    //服务开始时间
	@ApiModelProperty(value = "服务开始时间",hidden = true)
	private String serviceStartTime;
	//创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//更改时间
    @ApiModelProperty(value = "更改时间",hidden = true)
	private Date updateTime;
	//删除标志
    @ApiModelProperty(value = "删除标志",hidden = true)
	private Integer delFlag;

}
