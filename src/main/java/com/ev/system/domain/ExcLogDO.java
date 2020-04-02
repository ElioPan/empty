package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 
 * 
 * @author zd
 * @email 911435330@qq.com
 * @date 2020-04-09 14:33:09
 */
@Data
@ApiModel(value = "")
public class ExcLogDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//请求参数
    @ApiModelProperty(value = "请求参数")
	private String excRequParam;
	//异常名称
    @ApiModelProperty(value = "异常名称")
	private String excName;
	//异常消息
    @ApiModelProperty(value = "异常消息")
	private String excMessage;
	//操作人
    @ApiModelProperty(value = "操作人")
	private Long opUserId;
	//操作用户名称
    @ApiModelProperty(value = "操作用户名称")
	private String opUserName;
	//操作方法
    @ApiModelProperty(value = "操作方法")
	private String opMethod;
	//请求uri
    @ApiModelProperty(value = "请求uri")
	private String opUri;
	//机器名称
    @ApiModelProperty(value = "机器名称")
	private String opMachine;
	//
    @ApiModelProperty(value = "")
	private String opDnsIp;
	//IP地址
    @ApiModelProperty(value = "IP地址")
	private String opIp;
	//版本
    @ApiModelProperty(value = "版本")
	private String opVer;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;

}
