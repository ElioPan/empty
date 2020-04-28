package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 客户联系人表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-03 11:11:48
 */
@Data
@ApiModel(value = "客户联系人表")
public class ClientLinkmanDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@ApiModelProperty(value = "")
	private Long id;
	//名称
	@ApiModelProperty(value = "名称")
	private String name;
	//客户ID
	@ApiModelProperty(value = "客户ID")
	private Long clientId;
	//电话
	@ApiModelProperty(value = "电话")
	private String phone;
	//邮箱
	@ApiModelProperty(value = "邮箱")
	private String email;
	//性别
	@ApiModelProperty(value = "性别")
	private Integer gender;
	//职位
	@ApiModelProperty(value = "职位")
	private String post;
	//备注
	@ApiModelProperty(value = "备注")
	private String desc;
	//
	@ApiModelProperty(value = "")
	private Long providerId;
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
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;

}
