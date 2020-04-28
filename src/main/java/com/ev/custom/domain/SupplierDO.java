package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 供应商表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-08-15 14:08:10
 */
@Data
@ApiModel(value = "供应商表")
public class SupplierDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty()
	private Long id;
	//供应商编号
	@ApiModelProperty(value = "单位编号")
	private String code;
	//供应商名称
	@ApiModelProperty(value = "单位名称")
	private String name;
	//sper_类型
	@ApiModelProperty(value = "sper_类型")
	private Long sperType;
	//供应商类型
	@ApiModelProperty(value = "服务类型(字典)")
	private Long type;
	//电话
	@ApiModelProperty(value = "电话")
	private String phone;
	//传真
	@ApiModelProperty(value = "传真")
	private String fax;
	//地址
	@ApiModelProperty(value = "地址")
	private String address;
	//法人代表
	@ApiModelProperty(value = "法人代表")
	private String legalperson;
	//公司介绍
	@ApiModelProperty(value = "公司介绍")
	private String desc;
	//
	@ApiModelProperty()
	private Long providerId;
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

	//开户行
	@ApiModelProperty(value = "开户行")
	private String bank;
	//账号
	@ApiModelProperty(value = "账号")
	private String account;
	//备注
	@ApiModelProperty(value = "备注")
	private String mark;
	//状态
	@ApiModelProperty(value = "状态")
	private Long status;
	//审核人
	@ApiModelProperty(value = "审核人")
	private Long auditId;
	//使用状态(1是0否)
	@ApiModelProperty(value = "使用状态(1是0否)")
	private Integer useStatus;
	//税号
	@ApiModelProperty(value = "税号")
	private String taxNumber;


}
