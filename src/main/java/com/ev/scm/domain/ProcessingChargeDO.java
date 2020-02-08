package com.ev.scm.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 加工费用主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-07 08:53:39
 */
@Data
@ApiModel(value = "加工费用主表")
public class ProcessingChargeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//客户名称
    @ApiModelProperty(value = "客户名称")
	private Long supplierId;
	//开票日期
    @ApiModelProperty(value = "开票日期")
	private Date invoiceDate;
	//收票人
    @ApiModelProperty(value = "收票人")
	private Long invoiceUser;
	//发票号码
    @ApiModelProperty(value = "发票号码")
	private String billCode;
	//票据类型
    @ApiModelProperty(value = "票据类型")
	private Long invoiceType;
	//委外入库ID
    @ApiModelProperty(value = "委外入库ID")
	private String stockInId;
	//审核状态
    @ApiModelProperty(value = "审核状态")
	private Long auditSign;
	//备注
    @ApiModelProperty(value = "备注")
	private String remarks;
	//审核人
    @ApiModelProperty(value = "审核人")
	private Long auditor;
	//审核时间
    @ApiModelProperty(value = "审核时间")
	private Date auditTime;
	//制单人
    @ApiModelProperty(value = "制单人")
	private Long createBy;
	//制单时间
    @ApiModelProperty(value = "制单时间")
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
