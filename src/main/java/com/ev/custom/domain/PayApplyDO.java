package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 付款申请
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-26 08:59:36
 */
@Data
@ApiModel(value = "付款申请")
public class PayApplyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//付款总额
    @ApiModelProperty(value = "付款总额")
	private Double totalNumber;
	//付款方式
    @ApiModelProperty(value = "付款方式")
	private Long payMethod;
	//支付日期
    @ApiModelProperty(value = "支付日期")
	private Date payDate;
	//支付对象
    @ApiModelProperty(value = "支付对象")
	private String payUser;
	//开户行
    @ApiModelProperty(value = "开户行")
	private String openingBlank;
	//银行账号
    @ApiModelProperty(value = "银行账号")
	private String blankAccount;
	//付款事由
    @ApiModelProperty(value = "付款事由")
	private String payReason;
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
	//状态
    @ApiModelProperty(value = "状态")
	private Long status;
	//流程实例ID
    @ApiModelProperty(value = "流程实例ID")
	private String processInstanceId;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;


}
