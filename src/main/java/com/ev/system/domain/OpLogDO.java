package com.ev.system.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 操作日志表
 * 
 * @author zd
 * @email 911435330@qq.com
 * @date 2020-04-09 14:33:09
 */
@Data
@ApiModel(value = "操作日志表")
public class OpLogDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//功能模块
    @ApiModelProperty(value = "功能模块")
	private String opModule;
	//操作类型
    @ApiModelProperty(value = "操作类型")
	private String opType;
	//操作描述
    @ApiModelProperty(value = "操作描述")
	private String opDesc;
	//操作参数
    @ApiModelProperty(value = "操作参数")
	private String opRequParam;
	//返回内容
    @ApiModelProperty(value = "返回内容")
	private String opRespParam;
	//操作人
    @ApiModelProperty(value = "操作人")
	private Long opUserId;
	//操作人姓名
    @ApiModelProperty(value = "操作人姓名")
	private String opUserName;
	//操作方法
    @ApiModelProperty(value = "操作方法")
	private String opMethod;
	//请求URI
    @ApiModelProperty(value = "请求URI")
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
